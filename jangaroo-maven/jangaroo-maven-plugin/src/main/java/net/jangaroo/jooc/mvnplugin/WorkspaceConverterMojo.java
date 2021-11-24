package net.jangaroo.jooc.mvnplugin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.config.SearchAndReplace;
import net.jangaroo.jooc.mvnplugin.converter.AdditionalPackageJsonEntries;
import net.jangaroo.jooc.mvnplugin.converter.JangarooConfig;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import net.jangaroo.jooc.mvnplugin.converter.PackageJson;
import net.jangaroo.jooc.mvnplugin.converter.PackageJsonPrettyPrinter;
import net.jangaroo.jooc.mvnplugin.converter.WorkspaceRoot;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.ConversionUtils;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import static java.util.AbstractMap.Entry;
import static java.util.AbstractMap.SimpleEntry;
import static net.jangaroo.jooc.mvnplugin.Type.JANGAROO_APP_PACKAGING;
import static net.jangaroo.jooc.mvnplugin.Type.JANGAROO_PACKAGING_TYPES;
import static net.jangaroo.jooc.mvnplugin.Type.JANGAROO_SWC_PACKAGING;

@Mojo(name = "convert-workspace",
        defaultPhase = LifecyclePhase.INSTALL,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.TEST)
public class WorkspaceConverterMojo extends AbstractMojo {

  private static final Pattern EXTENSION_POINT_PATTERN = Pattern.compile("^(.+)-extension-dependencies$");

  private static final Logger logger = LoggerFactory.getLogger(WorkspaceConverterMojo.class);

  // --- CONVERTER CONFIGURATION --- //

  @Parameter(property = "convertedWorkspaceTarget", required = true)
  private String convertedWorkspaceTarget;

  @Parameter(property = "npmPackageNameReplacers")
  private List<SearchAndReplaceConfiguration> npmPackageNameReplacers = new ArrayList<>();

  @Parameter(property = "npmPackageFolderNameReplacers")
  private List<SearchAndReplaceConfiguration> npmPackageFolderNameReplacers = new ArrayList<>();

  @Parameter(property = "npmPackageVersionReplacers")
  private List<SearchAndReplaceConfiguration> npmPackageVersionReplacers = new ArrayList<>();

  @Parameter(property = "npmDependencyVersionOverride")
  private List<SearchAndReplaceConfiguration> npmDependencyOverrides = new ArrayList<>();

  @Parameter(property = "projectExtensionWorkspacePath")
  private File projectExtensionWorkspacePath;

  @Parameter(property = "relativeProjectExtensionsPath", defaultValue = "modules/extensions")
  private String relativeProjectExtensionsPath;

  @Parameter(property = "relativeProjectExtensionPointsPath", defaultValue = "modules/extension-config")
  private String relativeProjectExtensionPointsPath;

  @Parameter(property = "relativeNpmProjectExtensionWorkspacePath", defaultValue="")
  private String relativeNpmProjectExtensionWorkspacePath;

  @Parameter(property = "jangarooNpmVersion", defaultValue = "1.0.0")
  private String jangarooNpmVersion;

  @Parameter(property = "extJsVersion", defaultValue = "1.0.0")
  private String extJsVersion;

  // --- ACTUAL JANGAROO MAVEN CONFIGURATION --- //

  @Parameter
  private String packageType;

  @Parameter
  private String theme;

  @Parameter
  private String applicationClass;

  @Parameter
  private String rootApp;

  @Parameter
  private String testSuite;

  @Parameter
  private Integer jooUnitTestExecutionTimeout;

  @Parameter
  private String extNamespace;

  @Parameter(property = "extNamespaceRequired")
  private boolean extNamespaceRequired;

  @Parameter
  private String extSassNamespace;

  @Parameter
  private List<String> additionalLocales;

  @Parameter
  private List<String> additionalCssNonBundle;

  @Parameter
  private List<String> additionalJsNonBundle;

  @Parameter
  private List<String> additionalCssIncludeInBundle;

  @Parameter
  private List<String> additionalJsIncludeInBundle;

  @Parameter
  private Map<String, String> globalResourcesMap;

  private List<SearchAndReplace> resolvedNpmPackageNameReplacers;
  private List<SearchAndReplace> resolvedNpmPackageFolderNameReplacers;
  private List<SearchAndReplace> resolvedNpmPackageVersionReplacers;
  private List<SearchAndReplace> resolvedNpmDependencyOverrides;
  private ObjectMapper jsonObjectMapper;
  private ObjectMapper yamlObjectMapper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  private Map<String, Package> packagesByOriginalName;

  @Override
  public void execute() throws MojoFailureException, MojoExecutionException {

    MavenModule mavenModule = new MavenModule(project.getFile().getPath().replace("pom.xml", ""), project.getModel(), project.getArtifact());

    ModuleType moduleType = MavenModule.calculateModuleType(project.getPackaging());
    if (ModuleType.IGNORE.equals(moduleType)) {
      logger.info("Skipping conversion of current Maven project as packaging cannot be handled.");
      return;
    }
    if (ModuleType.AGGREGATOR.equals(moduleType) && getProjectExtensionPoint(mavenModule) == null) {
      logger.info("Skipping conversion of current Maven project as it is an aggregator. Dependencies of aggregators are moved to every package depending on the former aggregator as long as they are not marked as extension points.");
      return;
    }

    if (extNamespace == null) {
      if (extNamespaceRequired
              && Arrays.asList(Type.JANGAROO_PKG_PACKAGING, JANGAROO_SWC_PACKAGING, Type.JANGAROO_APP_PACKAGING).contains(project.getPackaging())) {
        throw new MojoExecutionException("Flag 'extNamespaceRequired' was enabled but no 'extNamespace' was provided.");
      }
      extNamespace = "";
    }

    if (".".equals(extNamespace)) {
      extNamespace = "";
    }

    if (".".equals(extSassNamespace)) {
      extSassNamespace = "";
    }

    if (relativeNpmProjectExtensionWorkspacePath == null || ".".equals(relativeNpmProjectExtensionWorkspacePath)) {
      relativeNpmProjectExtensionWorkspacePath = "";
    }

    PackageJsonPrettyPrinter prettyPrinter = new PackageJsonPrettyPrinter();

    jsonObjectMapper = new ObjectMapper()
            .setDefaultPrettyPrinter(prettyPrinter)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    yamlObjectMapper = new ObjectMapper(new YAMLFactory()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
    );


    resolvedNpmPackageNameReplacers = ConversionUtils.getSearchAndReplace(npmPackageNameReplacers);;
    resolvedNpmPackageFolderNameReplacers = ConversionUtils.getSearchAndReplace(npmPackageFolderNameReplacers);
    resolvedNpmPackageVersionReplacers = ConversionUtils.getSearchAndReplace(npmPackageVersionReplacers);
    resolvedNpmDependencyOverrides = ConversionUtils.getSearchAndReplace(npmDependencyOverrides);

    packagesByOriginalName = Stream.of(
            new SimpleEntry<>("@coremedia/sencha-ext-charts", extJsVersion),
            new SimpleEntry<>("@coremedia/sencha-ext", extJsVersion),
            new SimpleEntry<>("@coremedia/sencha-ext-classic", extJsVersion),
            new SimpleEntry<>("@coremedia/sencha-ext-classic-locale", extJsVersion),
            new SimpleEntry<>("@coremedia/sencha-ext-classic-theme-triton", extJsVersion),
            new SimpleEntry<>("@jangaroo/core", jangarooNpmVersion),
            new SimpleEntry<>("@jangaroo/build", jangarooNpmVersion),
            new SimpleEntry<>("@jangaroo/joounit", jangarooNpmVersion),
            new SimpleEntry<>("@jangaroo/run", jangarooNpmVersion),
            new SimpleEntry<>("@jangaroo/publish", jangarooNpmVersion),
            new SimpleEntry<>("@jangaroo/runtime", jangarooNpmVersion),
            new SimpleEntry<>("rimraf", "^3.0.2"),
            new SimpleEntry<>("eslint", "7.27.0")
    ).collect(Collectors.toMap(
            SimpleEntry::getKey,
            item -> {
              Entry<String, String> overriddenPackageNameAndDependencyVersion = getOverriddenPackageNameAndDependencyVersion(item);
              return new Package(overriddenPackageNameAndDependencyVersion.getKey(), item.getValue(), overriddenPackageNameAndDependencyVersion.getValue());
            }
    ));

    Package aPackage = readPackageFromMavenModule(mavenModule);

    if (!new File(convertedWorkspaceTarget).exists()) {
      new File(convertedWorkspaceTarget).mkdirs();
    }

    String projectExtensionFor = getProjectExtensionFor(mavenModule);
    String projectExtensionPoint = getProjectExtensionPoint(mavenModule);
    String relativePackageFolderName = getPackageFolderName(aPackage.getName(), mavenModule);

    final JangarooConfig jangarooConfig = new JangarooConfig();
    AdditionalPackageJsonEntries additionalJsonEntries = new AdditionalPackageJsonEntries();

    String targetPackageDir = convertedWorkspaceTarget + "/" + relativePackageFolderName;
    logger.info(String.format("Generating npm workspace for module %s to directory %s", mavenModule.getData().getArtifactId(), new File(targetPackageDir).getPath()));
    String targetPackageJson = targetPackageDir + "/package.json";

    if (moduleType == ModuleType.SWC) {
      jangarooConfig.setType("code");
      addSenchaEntries(jangarooConfig);

      if (theme != null) {
        Package dependencyPackage = getDependencyPackageByRef(theme);
        if (dependencyPackage == null) {
          getLog().warn(String.format("Could not find theme dependency for %s which is configured in the jangaroo-maven-plugin configuration.", theme));
        } else {
          jangarooConfig.setTheme(dependencyPackage.getName());
        }
      }

      if (aPackage.getClassMapping().containsKey((!extNamespace.equals("") ? extNamespace + "." : "") + "init")) {
        addAutoLoadEntry(jangarooConfig, "./src/init");
      }
      if (globalResourcesMap != null && globalResourcesMap.size() > 0) {
        addAutoLoadEntry(jangarooConfig, "./src/packageConfig");
      }
      if (testSuite != null) {
        String testSuiteImport;
        File testSourceDir = Paths.get(project.getBasedir().getPath(), "target", "test-classes", "src").toFile();
        Map<String, String> testClassMapping = SenchaUtils.getClassMapping(testSourceDir, extNamespace, testSourceDir);
        if (testClassMapping.containsKey(testSuite)) {
          testSuiteImport =  "./joounit/" + testClassMapping.get(testSuite);
        } else {
          if (aPackage.getClassMapping().containsKey(testSuite)) {
            testSuiteImport = "./src/" + aPackage.getClassMapping().get(testSuite);
          } else {
            testSuiteImport = findClassImportInDependencies(aPackage, testSuite, true);
          }
        }

        if (testSuiteImport != null) {
          setCommandMapEntry(jangarooConfig, "joounit", "testSuite", testSuiteImport.replaceFirst("[.]ts$", ""));
        }

        // 1) testExecutionTimeout does only matter if there is a testSuite to execute
        // 2) do not duplicate default value
        if (jooUnitTestExecutionTimeout != null && jooUnitTestExecutionTimeout != 30000) {
          setCommandMapEntry(jangarooConfig, "joounit", "testExecutionTimeout", jooUnitTestExecutionTimeout);
        }
      }
      File packageJsonPath = new File(mavenModule.getDirectory().getPath() + "/package.json");
      if (packageJsonPath.exists()) {
        try {
          //noinspection unchecked
          jangarooConfig.addToSencha(jsonObjectMapper.readValue(FileUtils.readFileToString(packageJsonPath), Map.class));
        } catch (IOException e) {
          throw new MojoFailureException(e.getMessage(), e.getCause());
        }
      }

      Map<String, String> dependencies = new TreeMap<>();
      addManagedDependency(dependencies, "@jangaroo/runtime");
      if (jangarooConfig.getTheme() != null && !jangarooConfig.getTheme().isEmpty()) {
        addManagedDependency(dependencies, jangarooConfig.getTheme());
      }
      additionalJsonEntries.setDependencies(dependencies);

      Map<String, String> devDependencies = new TreeMap<>();
      addManagedDependency(devDependencies, "@jangaroo/core");
      addManagedDependency(devDependencies, "@jangaroo/build");
      addManagedDependency(devDependencies, "@jangaroo/publish");
      addManagedDependency(devDependencies, "rimraf");
      if (getCommandMapEntry(jangarooConfig, "joounit", "testSuite") != null) {
        addManagedDependency(devDependencies, "@jangaroo/joounit");
        addManagedDependency(devDependencies, "@coremedia/sencha-ext");
        addManagedDependency(devDependencies, "@coremedia/sencha-ext-classic");
        addManagedDependency(devDependencies, "@coremedia/sencha-ext-classic-locale");
      }
      additionalJsonEntries.setDevDependencies(devDependencies);

      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("clean", "rimraf ./dist && rimraf ./build");
      scripts.put("build", "jangaroo build");
      scripts.put("watch", "jangaroo watch");
      scripts.put("publish", "jangaroo publish");
      if (getCommandMapEntry(jangarooConfig, "joounit", "testSuite") != null) {
        scripts.put("test", "jangaroo joounit");
      }
      additionalJsonEntries.setScripts(scripts);

      additionalJsonEntries.addPublishConfig("directory", "dist");

      Map<String, Object> exports = new TreeMap<>();
      exports.put("./*", ImmutableMap.of(
              "types", "./src/*.ts",
              "default", "./dist/src/*.js"
      ));
      additionalJsonEntries.setExports(exports);

      Map<String, Object> publishConfigExports = new HashMap<>();
      publishConfigExports.put("./*", ImmutableMap.of(
              "types", "./src/*.d.ts",
              "default", "./src/*.js"
      ));
      additionalJsonEntries.addPublishConfig("exports", publishConfigExports);

      List<String> ignoreFromSencha = new ArrayList<>();
      ignoreFromSencha.add("package.json");
      CopyFromMavenResult copyFromMavenResult = null;
      try {
        copyFromMavenResult = copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "packages",
                String.format("%s__%s", mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId())).toString(),
                ignoreFromSencha, targetPackageDir
        );
      } catch (IOException e) {
        throw new MojoFailureException(e.getMessage(), e.getCause());
      }
      if (copyFromMavenResult.hasSourceTsFiles || copyFromMavenResult.hasJooUnitTsFiles) {
        setCommandMapEntry(jangarooConfig, "build", "ignoreTypeErrors", true);
        addManagedDependency(devDependencies, "eslint");
        List<String> eslintPatterns = new ArrayList<>();
        if (copyFromMavenResult.hasSourceTsFiles) {
          eslintPatterns.add("\"src/**/*.ts\"");
        }
        if (copyFromMavenResult.hasJooUnitTsFiles) {
          eslintPatterns.add("\"joounit/**/*.ts\"");
        }
        scripts.put("lint", "eslint --fix " + String.join(" ", eslintPatterns));
      }
      if (Paths.get(targetPackageDir, "src", "index.d.ts").toFile().exists()) {
        additionalJsonEntries.setTypes("./src/index.d.ts");
        additionalJsonEntries.addPublishConfig("types", "./src/index.d.ts");
        additionalJsonEntries.getExports().put(".", ImmutableMap.of(
                "types", "./src/index.d.ts"
        ));
        //noinspection unchecked
        ((Map<String, Object>) additionalJsonEntries.getPublishConfig().get("exports")).put(
                ".", ImmutableMap.of(
                        "types", "./src/index.d.ts"
                )
        );
      }
      if (projectExtensionFor != null) {
        Map<String, Object> coremedia = new LinkedHashMap<>();
        coremedia.put("projectExtensionFor", renameLegacyProjectExtensionPoint(projectExtensionFor));
        additionalJsonEntries.setCoremedia(coremedia);
      }
    } else if (moduleType == ModuleType.JANGAROO_APP) {
      jangarooConfig.setType("app");
      setCommandMapEntry(jangarooConfig, "run", "proxyTargetUri", "http://localhost:41080");
      setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");
      addSenchaEntries(jangarooConfig);
      if (theme != null) {
        Package dependencyPackage = getDependencyPackageByRef(theme);
        if (dependencyPackage == null) {
          getLog().warn(String.format("Could not find theme dependency for %s which is configured in the jangaroo-maven-plugin configuration.", theme));
        } else {
          jangarooConfig.setTheme(dependencyPackage.getName());
        }
      }
      if (applicationClass != null) {
        String applicationClassImport;
        if (aPackage.getClassMapping().containsKey(applicationClass)) {
          applicationClassImport = "./src/" + aPackage.getClassMapping().get(applicationClass);
        } else {
          applicationClassImport = findClassImportInDependencies(aPackage, applicationClass, false);
        }
        if (applicationClassImport != null) {
          jangarooConfig.setApplicationClass(applicationClassImport.replaceFirst("[.]ts$", ""));
        }
      }
      jangarooConfig.setAdditionalLocales(additionalLocales);
      File appJsonPath = new File(mavenModule.getDirectory().getPath() + "/app.json");
      if (appJsonPath.exists()) {
        try {
          //noinspection unchecked
          jangarooConfig.addToSencha(jsonObjectMapper.readValue(FileUtils.readFileToString(appJsonPath), Map.class));
        } catch (IOException e) {
          throw new MojoFailureException(e.getMessage(), e.getCause());
        }
      }

      Map<String, String> dependencies = new TreeMap<>();
      addManagedDependency(dependencies, "@coremedia/sencha-ext");
      addManagedDependency(dependencies, "@coremedia/sencha-ext-classic");
      addManagedDependency(dependencies, "@coremedia/sencha-ext-classic-locale");
      addManagedDependency(dependencies, "@jangaroo/runtime");
      if (jangarooConfig.getTheme() != null && !jangarooConfig.getTheme().isEmpty()) {
        addManagedDependency(dependencies, jangarooConfig.getTheme());
      }
      additionalJsonEntries.setDependencies(dependencies);

      Map<String, String> devDependencies = new TreeMap<>();
      addManagedDependency(devDependencies, "@jangaroo/core");
      addManagedDependency(devDependencies, "@jangaroo/build");
      addManagedDependency(devDependencies, "@jangaroo/publish");
      addManagedDependency(devDependencies, "@jangaroo/run");
      addManagedDependency(devDependencies, "rimraf");
      additionalJsonEntries.setDevDependencies(devDependencies);

      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("clean", "rimraf ./dist && rimraf ./build");
      scripts.put("build", "jangaroo build");
      scripts.put("watch", "jangaroo watch");
      scripts.put("start", "jangaroo run");
      scripts.put("publish", "jangaroo publish");
      additionalJsonEntries.setScripts(scripts);

      additionalJsonEntries.addPublishConfig("directory", "build");

      Map<String, Object> exports = new TreeMap<>();
      exports.put("./*", ImmutableMap.of(
              "types", "./src/*.ts",
              "default", "./build/src/*.js"
      ));
      additionalJsonEntries.setExports(exports);

      Map<String, Object> publishConfigExports = new HashMap<>();
      publishConfigExports.put("./*", ImmutableMap.of(
              "types", "./src/*.d.ts",
              "default", "./src/*.js"
      ));
      additionalJsonEntries.addPublishConfig("exports", publishConfigExports);

      List<String> ignoreFromSencha = new ArrayList<>();
      ignoreFromSencha.add("app.json");
      CopyFromMavenResult copyFromMavenResult = null;
      try {
        copyFromMavenResult = copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "app").toString(),
                ignoreFromSencha, targetPackageDir
        );
      } catch (IOException e) {
        throw new MojoFailureException(e.getMessage(), e.getCause());
      }
      if (copyFromMavenResult.hasSourceTsFiles) {
        setCommandMapEntry(jangarooConfig, "build", "ignoreTypeErrors", true);
        addManagedDependency(devDependencies, "eslint");
        scripts.put("lint", "eslint --fix \"src/**/*.ts\"");
      }
    } else if (moduleType == ModuleType.JANGAROO_APP_OVERLAY) {
      jangarooConfig.setType("app-overlay");
      setCommandMapEntry(jangarooConfig, "run", "proxyTargetUri", "http://localhost:41080");
      setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");

      Map<String, String> devDependencies = new TreeMap<>();
      addManagedDependency(devDependencies, "@jangaroo/core");
      addManagedDependency(devDependencies, "@jangaroo/build");
      addManagedDependency(devDependencies, "@jangaroo/publish");
      addManagedDependency(devDependencies, "@jangaroo/run");
      addManagedDependency(devDependencies, "rimraf");
      additionalJsonEntries.setDevDependencies(devDependencies);
      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("clean", "rimraf ./dist");
      scripts.put("build", "jangaroo build");
      scripts.put("watch", "jangaroo watch");
      scripts.put("start", "jangaroo run");
      scripts.put("publish", "jangaroo publish");
      additionalJsonEntries.setScripts(scripts);

      additionalJsonEntries.addPublishConfig("directory", "dist");
    } else if (moduleType == ModuleType.JANGAROO_APPS) {
      jangarooConfig.setType("apps");
      setCommandMapEntry(jangarooConfig, "run", "proxyTargetUri", "http://localhost:41080");
      setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");
      if (rootApp != null) {
        Package dependencyPackage = getDependencyPackageByRef(rootApp);
        if (dependencyPackage == null) {
          getLog().warn(String.format("Could not find rootApp dependency for %s which is configured in the jangaroo-maven-plugin configuration.", theme));
        } else {
          jangarooConfig.addAppPath(dependencyPackage.getName(), "");
        }
      }

      Map<String, String> devDependencies = new TreeMap<>();
      addManagedDependency(devDependencies, "@jangaroo/core");
      addManagedDependency(devDependencies, "@jangaroo/build");
      addManagedDependency(devDependencies, "@jangaroo/publish");
      addManagedDependency(devDependencies, "@jangaroo/run");
      addManagedDependency(devDependencies, "rimraf");
      additionalJsonEntries.setDevDependencies(devDependencies);
      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("clean", "rimraf ./dist");
      scripts.put("build", "jangaroo build");
      scripts.put("watch", "jangaroo watch");
      scripts.put("start", "jangaroo run");
      scripts.put("package", "jangaroo package");
      scripts.put("publish", "jangaroo publish");
      additionalJsonEntries.setScripts(scripts);

      additionalJsonEntries.addPublishConfig("directory", "dist");
    } else if (moduleType == ModuleType.AGGREGATOR) {
      jangarooConfig.setType("code");
      Map<String, String> devDependencies = new TreeMap<>();
      addManagedDependency(devDependencies, "@jangaroo/core");
      addManagedDependency(devDependencies, "@jangaroo/build");
      addManagedDependency(devDependencies, "@jangaroo/publish");
      addManagedDependency(devDependencies, "rimraf");
      additionalJsonEntries.setDevDependencies(devDependencies);
      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("clean", "rimraf ./dist && rimraf ./build");
      scripts.put("build", "jangaroo build");
      scripts.put("watch", "jangaroo watch");
      scripts.put("publish", "jangaroo publish");
      additionalJsonEntries.setScripts(scripts);

      additionalJsonEntries.addPublishConfig("directory", "dist");

      if (projectExtensionPoint != null) {
        Map<String, Object> coremedia = new LinkedHashMap<>();
        coremedia.put("projectExtensionPoint", renameLegacyProjectExtensionPoint(projectExtensionPoint));
        additionalJsonEntries.setCoremedia(coremedia);
      }
    } else {
      throw new MojoExecutionException("Unknown maven module type: " + moduleType);
    }

    List<String> appManifestPaths = match("glob:/**/app-manifest-fragment*.json", mavenModule.getDirectory().getPath());
    if (!appManifestPaths.isEmpty()) {
      for (String appManifestPath : appManifestPaths) {
        String fileName = new File(appManifestPath).getName();
        if ("app-manifest-fragment.json".equals(fileName)) {
          fileName = "app-manifest-fragment-en.json";
        }
        Matcher matcher = Pattern.compile("app-manifest-fragment-([^.]+).json").matcher(fileName);
        if (matcher.find()) {
          String locale = matcher.group(1);
          try {
            jangarooConfig.addAppManifest(locale, jsonObjectMapper.readValue(new File(appManifestPath), Map.class));
          } catch (IOException ioException) {
            logger.error("error while reading manifest file: " + appManifestPath);
          }
        } else {
          logger.error("Could not detect locale for manifest file: " + appManifestPath);
        }
      }
    }

    try {
      String jangarooConfigDocument = "const { jangarooConfig } = require(\"@jangaroo/core\");\n\nmodule.exports = jangarooConfig(".concat(convertJangarooConfig(jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jangarooConfig).concat(");\n")));
      FileUtils.writeStringToFile(Paths.get(targetPackageDir, "jangaroo.config.js").toFile(), jangarooConfigDocument);
    } catch (IOException e) {
      throw new MojoFailureException(e.getMessage(), e.getCause());
    }


    PackageJson packageJson = new PackageJson(additionalJsonEntries);
    packageJson.setName(aPackage.getName());
    packageJson.setVersion(aPackage.getVersion());
    if (mavenModule.getData().getOrganization() != null) {
      packageJson.setAuthor(mavenModule.getData().getOrganization().getName());
    }
    packageJson.setPrivat(true);
    aPackage.getDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getDependencyVersion)).forEach(packageJson::addDependency);
    aPackage.getDevDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getDependencyVersion)).forEach(packageJson::addDevDependency);
    Map<String, String> sortedDependencies = new TreeMap<>();
    if (packageJson.getDependencies() != null) {
      packageJson.getDependencies().entrySet().stream()
              .sorted(Map.Entry.comparingByKey())
              .forEach(entry -> sortedDependencies.put(entry.getKey(), entry.getValue()));
    }
    packageJson.setDependencies(sortedDependencies);
    Map<String, String> sortedDevDependencies = new TreeMap<>();
    if (packageJson.getDevDependencies() != null) {
      packageJson.getDevDependencies().entrySet().stream()
              .sorted(Map.Entry.comparingByKey())
              .forEach(entry -> sortedDevDependencies.put(entry.getKey(), entry.getValue()));
    }
    packageJson.setDevDependencies(sortedDevDependencies);
    try {
      FileUtils.write(new File(targetPackageJson), jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n"));
    } catch (IOException e) {
      throw new MojoFailureException(e.getMessage(), e.getCause());
    }

    // Use this since for the synchronized keyword to properly work, we need one shared variable for all the threads.
    // A static field doesn`t suffice since maven used internally different classloaders, therefore not the same static
    // field is used. The session request is constant throughout the whole run an can therefore be used as lock for the
    // synchronized block.
    synchronized (session.getRequest()) {
      WorkspaceRoot workspaceRoot = new WorkspaceRoot(jsonObjectMapper, yamlObjectMapper, convertedWorkspaceTarget);
      try {
        workspaceRoot.writePackageJson(projectExtensionFor != null || projectExtensionPoint != null
                ? Collections.singletonList(relativeNpmProjectExtensionWorkspacePath)
                : Collections.emptyList()
        );
        workspaceRoot.writeWorkspacePackages(Collections.singletonList(relativePackageFolderName));
      } catch (IOException e) {
        throw new MojoFailureException(e.getMessage(), e.getCause());
      }
    }

    try {
      loadAndCopyResource("eslintrc.js", ".eslintrc.js");
      loadAndCopyResource("gitignore", ".gitignore");
    } catch (IOException e) {
      throw new MojoFailureException(e.getMessage(), e.getCause());
    }
  }

  private String getProjectExtensionPoint(MavenModule mavenModule) {
    Matcher extensionPointMatcher = EXTENSION_POINT_PATTERN.matcher(mavenModule.getData().getArtifactId());
    String projectExtensionPoint = extensionPointMatcher.matches() ? extensionPointMatcher.group(1) : null;
    if (projectExtensionWorkspacePath == null && projectExtensionPoint != null) {
      logger.warn("Maven project is marked as extension point but projectExtensionWorkspacePath is missing. Ignoring marker...");
      return null;
    }
    return projectExtensionPoint;
  }

  private String getProjectExtensionFor(MavenModule mavenModule) {
    String projectExtensionFor = mavenModule.getData().getProperties().getProperty("coremedia.project.extension.for");
    if (projectExtensionWorkspacePath == null && projectExtensionFor != null) {
      logger.warn("Maven project is marked as extension but projectExtensionWorkspacePath is missing. Ignoring marker...");
      return null;
    }
    return projectExtensionFor;
  }

  private String renameLegacyProjectExtensionPoint(String projectExtensionPointName) {
    if ("studio".equals(projectExtensionPointName) || "studio-client".equals(projectExtensionPointName)) {
      return "studio-client.main-static";
    } else if ("studio-dynamic".equals(projectExtensionPointName) || "studio-client-dynamic".equals(projectExtensionPointName)) {
      return "studio-client.main";
    }
    return projectExtensionPointName;
  }

  private boolean isProjectExtensionPointDependency(Dependency dependency) {
    boolean result = "pom".equals(dependency.getType()) && EXTENSION_POINT_PATTERN.matcher(dependency.getArtifactId()).matches();
    if (projectExtensionWorkspacePath == null && result) {
      logger.warn(String.format("Dependency %s is marked as project extension point but configuration 'projectExtensionWorkspacePath' is missing. Ignoring marker...", dependency));
      return false;
    }
    return result;
  }

  private Path getRelativePathBelowRoot(Path rootPath, Path absolutePathBelowRoot) {
    Path relativePath = rootPath.relativize(absolutePathBelowRoot).normalize();
    int depth = relativePath.getNameCount();
    if (depth >= 1 && !"..".equals(relativePath.getName(0).toString())) {
      return relativePath;
    }
    return null;
  }

  private void loadAndCopyResource(String resourceName, String fileName) throws IOException {
    Path filePath = Paths.get(convertedWorkspaceTarget, fileName);
    try (InputStream fileResource = getClass().getResourceAsStream("/net/jangaroo/jooc/mvnplugin/" + resourceName)) {
      if (fileResource != null) {
        try {
          Files.copy(fileResource, filePath);
        } catch (FileAlreadyExistsException e) {
          // do nothing since this means the file already exists and there is no need to create it.
        }
      }
    }
  }

  private String getPackageFolderName(String packageName, MavenModule mavenModule) throws MojoExecutionException {
    if (projectExtensionWorkspacePath != null) {
      // project extensions
      Path projectExtensionsPath = projectExtensionWorkspacePath.toPath().resolve(relativeProjectExtensionsPath);
      Path relativeProjectExtensionPath = getRelativePathBelowRoot(projectExtensionsPath, mavenModule.getDirectory().toPath());
      // extensions must be in project extensions path
      if (relativeProjectExtensionPath == null && getProjectExtensionFor(mavenModule) != null) {
        throw new MojoExecutionException("Maven project is marked as project extension but is not below the given project extensions path: " + projectExtensionsPath);
      }
      // if project is in extensions path it will also be in the extensions path in NPM
      if (relativeProjectExtensionPath != null) {
        Path relativeNpmProjectExtensionsPath = Paths.get(relativeNpmProjectExtensionWorkspacePath, "extensions");

        Path packageFolderName;
        if (relativeProjectExtensionPath.getNameCount() > 1) {
          packageFolderName = relativeNpmProjectExtensionsPath
                  // keep parent folder as nesting of folders make a difference when enabling/disabling extensions
                  .resolve(relativeProjectExtensionPath.subpath(0, relativeProjectExtensionPath.getNameCount() - 1))
                  // but replace last segment with the generated name of the package
                  .resolve(packageName);
        } else {
          // cannot use the generated packageName as foldername here as the name of the extension would be changed
          packageFolderName = relativeNpmProjectExtensionsPath.resolve(relativeProjectExtensionPath);
        }

        packageFolderName = Paths.get(getReplacedPackageFolderName(packageFolderName.toString()));

        // after replacers have run the path still has to be valid
        if (getRelativePathBelowRoot(relativeNpmProjectExtensionsPath, packageFolderName) == null) {
          throw new MojoExecutionException(String.format("NPM package folder name has been replaced with %s. This path is not below the given project extensions path %s. The extensions workspace path can be configured using 'relativeNpmProjectExtensionWorkspacePath'", packageFolderName, projectExtensionsPath));
        }
        return packageFolderName.toString();
      }

      // project extension points
      Path projectExtensionPointsPath = projectExtensionWorkspacePath.toPath().resolve(relativeProjectExtensionPointsPath);
      Path relativeProjectExtensionPointPath = getRelativePathBelowRoot(projectExtensionPointsPath, mavenModule.getDirectory().toPath());
      // extension points must be in project extension points path
      if (relativeProjectExtensionPointPath == null && getProjectExtensionPoint(mavenModule) != null) {
        throw new MojoExecutionException("Maven project is marked as project extension point but is not below the given project extension points path: " + projectExtensionPointsPath);
      }
      // if project is in extensions path it will also be in the extensions path in NPM
      if (relativeProjectExtensionPointPath != null) {
        Path relativeNpmProjectExtensionPointsPath = Paths.get(relativeNpmProjectExtensionWorkspacePath, "extension-config");
        Path packageFolderName = relativeNpmProjectExtensionPointsPath.resolve(packageName);
        packageFolderName = Paths.get(getReplacedPackageFolderName(packageFolderName.toString()));
        // after replacers have run the path still has to be valid
        if (getRelativePathBelowRoot(relativeNpmProjectExtensionPointsPath, packageFolderName) == null) {
          throw new MojoExecutionException(String.format("NPM package folder name has been replaced with %s. This path is not below the given project extension points path %s. The extensions workspace path can be configured using 'relativeNpmProjectExtensionWorkspacePath'.", packageFolderName, projectExtensionPointsPath));
        }
        return packageFolderName.toString();
      }
    }

    // fallback to normal package name replacement
    return getReplacedPackageFolderName(packageName);
  }

  private String getReplacedPackageFolderName(String packageFolderName) {
    for (SearchAndReplace searchAndReplace : resolvedNpmPackageFolderNameReplacers) {
      Matcher matcher = searchAndReplace.search.matcher(packageFolderName);
      if (matcher.matches()) {
        return matcher.replaceAll(searchAndReplace.replace);
      }
    }
    return packageFolderName;
  }

  private List<String> match(String glob, String location) {
    final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
            glob);
    List<String> matchingFilePaths = new ArrayList<>();

    try {
      Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
          if (pathMatcher.matches(path)) {
            matchingFilePaths.add(path.toString());
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return matchingFilePaths;
  }

  private CopyFromMavenResult copyCodeFromMaven(String baseDirectory, String
          generatedExtModuleDirectory, List<String> ignoreFromSencha, String targetPackageDir) throws IOException {
    AtomicBoolean hasSourceTsFiles = new AtomicBoolean(false);
    AtomicBoolean hasJooUnitTsFiles = new AtomicBoolean(false);

    Path generatedSrc = Paths.get(baseDirectory, "target", "generated-sources", "joo");
    List<Path> generatedTsFiles = match("glob:/**/*.{as,mxml}", generatedSrc.toString()).stream()
            .map(filePathStr -> filePathStr.replaceAll("[.](as|mxml)$", ".ts"))
            .map(Paths::get)
            .map(generatedSrc::relativize)
            .filter(filePath -> {
              String filePathStr = filePath.toString();
              if (!filePathStr.endsWith("_properties.ts")) {
                return true;
              }
              String propertiesFile = filePathStr.replaceAll("_properties[.]ts$", ".properties");
              return !Paths.get(baseDirectory, "src", "main", "joo", propertiesFile).toFile().exists();
            })
            .map(filePath -> {
              if (!extNamespace.isEmpty()) {
                String slashedExtNamespaceWithTrailingSlash = extNamespace.concat(".").replaceAll("[.]", File.separator);
                return Paths.get(filePath.toString().replace(slashedExtNamespaceWithTrailingSlash, ""));
              }
              return filePath;
            }).collect(Collectors.toList());
    final String srcFolderName = "src";
    final String generatedFolderName = "generated";
    Path sourceDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, srcFolderName);
    if (sourceDirPath.toFile().exists() && sourceDirPath.toFile().isDirectory()) {
      Path targetDirPath = Paths.get(targetPackageDir, "sencha", srcFolderName);
      FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> pathname.isDirectory() || !pathname.getName().endsWith(".ts"));
      targetDirPath = Paths.get(targetPackageDir, srcFolderName);
      FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> {
        boolean isTsFile = pathname.getName().endsWith(".ts");
        if (isTsFile) {
          hasSourceTsFiles.set(true);
        }
        return pathname.isDirectory() || isTsFile && !generatedTsFiles.contains(sourceDirPath.relativize(pathname.toPath()));
      });
      if (generatedTsFiles.size() > 0) {
        Path generatedTargetDirpath = Paths.get(targetPackageDir, generatedFolderName, srcFolderName);
        FileUtils.copyDirectory(sourceDirPath.toFile(), generatedTargetDirpath.toFile(), pathname -> {
          boolean isTsFile = pathname.getName().endsWith(".ts");
          return pathname.isDirectory() || isTsFile && generatedTsFiles.contains(sourceDirPath.relativize(pathname.toPath()));
        });
      }
    }

    Path jooUnitSourcePath = Paths.get(baseDirectory, "target", "test-classes", srcFolderName);
    if (jooUnitSourcePath.toFile().exists() && jooUnitSourcePath.toFile().isDirectory()) {
      Path jooUnitTargetDirPath = Paths.get(targetPackageDir, "joounit");
      FileUtils.copyDirectory(jooUnitSourcePath.toFile(), jooUnitTargetDirPath.toFile(), pathname -> {
        boolean isTsFile = pathname.isFile() && pathname.getName().endsWith(".ts");
        if (isTsFile) {
          hasJooUnitTsFiles.set(true);
        }
        return pathname.isDirectory() || isTsFile;
      });
    }

    List<String> fullIgnoreFromSrcMainSencha = new ArrayList<>(ignoreFromSencha);
    for (String dir : Arrays.asList("sass/var", "sass/src")) {
      fullIgnoreFromSrcMainSencha.add(dir);
      Path sassDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, dir);
      if (sassDirPath.toFile().exists() && sassDirPath.toFile().isDirectory()) {
        Path targetDirPath = Paths.get(targetPackageDir, "sencha", dir);
        FileUtils.copyDirectory(sassDirPath.toFile(), targetDirPath.toFile());
      }
    }
    Path senchaSrc = Paths.get(baseDirectory, "src", "main", "sencha");
    if (senchaSrc.toFile().exists() && senchaSrc.toFile().isDirectory()) {
      FileUtils.copyDirectory(senchaSrc.toFile(), Paths.get(targetPackageDir, "sencha").toFile(),
              pathname -> acceptFile(pathname, senchaSrc, fullIgnoreFromSrcMainSencha));
    }
    Path generatedSenchaSrc = Paths.get(baseDirectory, "target", "generated-sencha");
    if (generatedSenchaSrc.toFile().exists() && generatedSenchaSrc.toFile().isDirectory()) {
      FileUtils.copyDirectory(generatedSenchaSrc.toFile(), Paths.get(targetPackageDir, generatedFolderName, "sencha").toFile(),
              pathname -> acceptFile(pathname, generatedSenchaSrc, fullIgnoreFromSrcMainSencha));
    }
    return new CopyFromMavenResult(hasSourceTsFiles.get(), hasJooUnitTsFiles.get());
  }

  private boolean acceptFile(File file, Path srcMainSenchaPath, List<String> ignoreFromSrcMainSencha) {
    List<Path> collect = ignoreFromSrcMainSencha.stream()
            .map(string -> Paths.get(srcMainSenchaPath.toString(), string))
            .collect(Collectors.toList());
    return collect.stream()
            .noneMatch(path -> file.getPath().contains(path.toString()));
  }

  private Optional<Artifact> getDependencyArtifact(Dependency dependency) {
    return project.getArtifacts().stream()
            .filter(artifact -> artifact.getGroupId().equals(dependency.getGroupId()) && artifact.getArtifactId().equals(dependency.getArtifactId()))
            .findFirst();
  }

  private Package getDependencyPackageByRef(String ref) {
    Dependency dependency = SenchaUtils.getDependencyByRef(project, ref);
    if (dependency != null) {
      Optional<Package> dependencyPackage = getOrCreateDependencyPackage(dependency);
      if (dependencyPackage.isPresent()) {
        return dependencyPackage.get();
      }
    }
    if ("ext-classic".equals(ref)) {
      return packagesByOriginalName.get("@coremedia/sencha-ext-classic");
    }
    if ("charts".equals(ref)) {
      return packagesByOriginalName.get("@coremedia/sencha-ext-charts");
    }
    if (ref != null && ref.startsWith("theme-")) {
      return packagesByOriginalName.get("@coremedia/sencha-ext-classic-" + ref);
    }
    return null;
  }

  private Entry<String, String> getOverriddenPackageNameAndDependencyVersion(Entry<String, String> packageNameAndDependencyVersion) {
    String dependencyStr = packageNameAndDependencyVersion.getKey() + ":" + packageNameAndDependencyVersion.getValue();
    for (SearchAndReplace searchAndReplace : resolvedNpmDependencyOverrides) {
      Matcher matcher = searchAndReplace.search.matcher(dependencyStr);
      if (matcher.matches()) {
        String dependencyStrReplacement = matcher.replaceAll(searchAndReplace.replace);
        String[] parts = dependencyStrReplacement.split(":");
        if (parts.length == 2) {
          String version = parts[1];
          // allow maven version here / try to convert to npm version
          Pattern DEPENDENCY_VERSION_PATTERN = Pattern.compile("^([^~]?)(.*)$");
          Matcher dependencyVersionMatcher = DEPENDENCY_VERSION_PATTERN.matcher(version);
          if (dependencyVersionMatcher.matches()) {
            version = dependencyVersionMatcher.group(1) + ConversionUtils.normalizeNpmPackageVersion(dependencyVersionMatcher.group(2));
          }
          return new SimpleEntry<>(parts[0], version);
        }
        // always break if replacement was invalid
        logger.warn(String.format("Ignoring invalid replacement for dependency: %s => %s", dependencyStr, dependencyStrReplacement));
        break;
      }
    }
    return packageNameAndDependencyVersion;
  }

  private ConversionUtils.NpmPackageMetadata getNpmPackageMetadata(Artifact artifact) {
    String artifactType = artifact.getType();
    if (artifact.getFile() == null || (!JANGAROO_PACKAGING_TYPES.contains(artifactType) && !"jar".equals(artifactType))) {
      return null;
    }
    Manifest manifest;
    try {
      manifest = new JarFile(artifact.getFile()).getManifest();
    } catch (IOException e) {
      getLog().warn(String.format("Artifact %s could not be read!", artifact));
      return null;
    }
    if (manifest == null) {
      return null;
    }

    Map<String, String> entries = new HashMap<>();
    for (Map.Entry<Object, Object> mainAttribute : manifest.getMainAttributes().entrySet()) {
      entries.put(mainAttribute.getKey().toString(), mainAttribute.getValue().toString());
    }
    return ConversionUtils.getNpmPackageMetadataFromManifestEntries(entries);
  }

  private Optional<Package> getOrCreateDependencyPackage(Dependency dependency) {
    Optional<Artifact> optionalArtifact = getDependencyArtifact(dependency);
    if (!optionalArtifact.isPresent()) {
      return Optional.empty();
    }
    /*ModuleType moduleType = MavenModule.calculateModuleType(optionalArtifact.get().getArtifactHandler().getPackaging());
    if (moduleType == ModuleType.IGNORE) {
      return Optional.empty();
    }*/
    Model model = new Model();
    model.setGroupId(optionalArtifact.get().getGroupId());
    model.setArtifactId(optionalArtifact.get().getArtifactId());
    model.setVersion(optionalArtifact.get().getVersion());
    model.setPackaging(optionalArtifact.get().getArtifactHandler().getPackaging());
    model.setDependencies(
            project.getArtifacts().stream()
                    .filter(artifact -> inDependencyTrail(dependency, artifact.getDependencyTrail()))
                    .filter(artifact -> !artifact.getGroupId().equals(dependency.getGroupId()) || !artifact.getArtifactId().equals(dependency.getArtifactId()))
                    .map(artifact -> {
                      Dependency localDependency = new Dependency();
                      localDependency.setArtifactId(artifact.getArtifactId());
                      localDependency.setGroupId(artifact.getGroupId());
                      localDependency.setScope(artifact.getScope());
                      localDependency.setType(artifact.getType());
                      localDependency.setVersion(artifact.getVersion());
                      return localDependency;
                    })
                    .collect(Collectors.toList())
    );
    return Optional.of(readPackageFromMavenModule(new MavenModule("", model, optionalArtifact.get())));
  }

  private boolean inDependencyTrail(Dependency dependency, List<String> dependencyTrail) {
    return dependencyTrail.stream()
            .map(dependencyPart -> dependencyPart.split(":"))
            .filter(dependencySplit -> dependency.getGroupId().equals(dependencySplit[0]))
            .anyMatch(dependencySplit -> dependency.getArtifactId().equals(dependencySplit[1]));
  }

  private Package readPackageFromMavenModule(MavenModule mavenModule) {
    Artifact artifact = mavenModule.getArtifact();
    ConversionUtils.NpmPackageMetadata npmPackageMetadata = getNpmPackageMetadata(artifact);
    String originalPackageName = npmPackageMetadata != null ? npmPackageMetadata.name : ConversionUtils.getNpmPackageName(mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId(), resolvedNpmPackageNameReplacers);
    if (packagesByOriginalName.containsKey(originalPackageName)) {
      return packagesByOriginalName.get(originalPackageName);
    }
    String packageVersion = npmPackageMetadata != null ? npmPackageMetadata.version : ConversionUtils.getNpmPackageVersion(mavenModule.getData().getVersion(), resolvedNpmPackageVersionReplacers);

    Entry<String, String> overriddenPackageNameAndDependency = getOverriddenPackageNameAndDependencyVersion(new SimpleEntry<>(originalPackageName, packageVersion));
    String packageName = overriddenPackageNameAndDependency.getKey();
    String dependencyVersion = overriddenPackageNameAndDependency.getValue();

    List<Package> packageDependencies = new ArrayList<>();
    List<Package> packageDevDependencies = new ArrayList<>();
    List<Dependency> dependencies;
    dependencies = mavenModule.getData().getDependencies().stream()
            .filter(dependency -> !"test".equals(dependency.getScope()))
            .filter(dependency -> !ignoreDependency(dependency))
            .peek(dependency -> {
              if ("${project.groupId}".equals(dependency.getGroupId())) {
                dependency.setGroupId(mavenModule.getData().getGroupId());
              }
              if ("${project.version}".equals(dependency.getVersion())) {
                dependency.setVersion(mavenModule.getVersion());
              }
            })
            .collect(Collectors.toList());

    List<Dependency> testDependencies = mavenModule.getData().getDependencies().stream()
            .filter(dependency -> "test".equals(dependency.getScope()))
            .filter(dependency -> !ignoreDependency(dependency))
            .peek(dependency -> {
              if ("$(project.groupid)".equals(dependency.getGroupId())) {
                dependency.setGroupId(mavenModule.getData().getGroupId());
              }
              if ("${project.version}".equals(dependency.getVersion())) {
                dependency.setVersion(mavenModule.getVersion());
              }
            })
            .collect(Collectors.toList());

    for (Dependency dependency : dependencies) {
      getOrCreateDependencyPackage(dependency).ifPresent(
              dependencyPackage -> {
                if (Arrays.asList("swc", "jar").contains(dependency.getType())
                        || isProjectExtensionPointDependency(dependency)) {
                  packageDependencies.add(dependencyPackage);
                } else {
                  packageDependencies.addAll(dependencyPackage.getDependencies());
                }
              }
      );
    }
    for (Dependency dependency : testDependencies) {
      getOrCreateDependencyPackage(dependency).ifPresent(
              dependencyPackage -> {
                if (Arrays.asList("swc", "jar").contains(dependency.getType())
                        || isProjectExtensionPointDependency(dependency)) {
                  packageDevDependencies.add(dependencyPackage);
                } else {
                  packageDevDependencies.addAll(dependencyPackage.getDependencies());
                }
              }
      );
    }
    Map<String, String> classMapping = readClassMapping(artifact);

    Package aPackage = new Package(packageName, packageVersion, dependencyVersion, packageDependencies, packageDevDependencies, classMapping);
    packagesByOriginalName.put(originalPackageName, aPackage);
    return aPackage;
  }

  private boolean ignoreDependency(Dependency dependency) {
    return "net.jangaroo__jangaroo-browser".contains(String.format("%s__%s", dependency.getGroupId(), dependency.getArtifactId()));
  }

  private String convertJangarooConfig(String jangarooConfig) {
    return jangarooConfig
            .replaceAll("\"([a-zA-Z_$][0-9a-zA-Z_$]*)\":", "$1:")
            .replace("}", "},")
            .replace("]", "],")
            .replace("\"\n", "\",\n")
            .replace(",,", ",")
            .replace(",);", ");");
  }

  private boolean isValidVersion(String version) {
    if (version == null) {
      return false;
    }
    int length = version.length();
    if (length == 0 || version.split("\\.").length < 3) {
      return false;
    }
    return true;
  }

  private void addManagedDependency(Map<String, String> dependencies, String name) {
    Package aPackage = packagesByOriginalName.get(name);
    if (aPackage != null) {
      dependencies.put(aPackage.getName(), aPackage.getDependencyVersion());
    } else {
      logger.warn("Could not find package with name " + name);
    }
  }

  private static void addAutoLoadEntry(JangarooConfig jangarooConfig, String item) {
    if (jangarooConfig.getAutoLoad() == null) {
      jangarooConfig.setAutoLoad(Lists.newArrayList(item));
    } else {
      jangarooConfig.getAutoLoad().add(item);
    }
  }

  private static Object getCommandMapEntry(JangarooConfig jangarooConfig, String commandName, String entryName) {
    Map<String, Map<String, Object>> commandsByName = jangarooConfig.getCommand();
    if (commandsByName == null) {
      return null;
    }
    if (!commandsByName.containsKey(commandName)) {
      return null;
    }
    Map<String, Object> command = commandsByName.get(commandName);
    return command.getOrDefault(entryName, null);
  }

  private static void setCommandMapEntry(JangarooConfig jangarooConfig, String commandName, String entryName, Object
          entryValue) {
    Map<String, Map<String, Object>> commandsByName = jangarooConfig.getCommand();
    if (commandsByName == null) {
      commandsByName = new TreeMap<>();
      jangarooConfig.setCommand(commandsByName);
    }
    Map<String, Object> command = commandsByName.computeIfAbsent(commandName, k -> new LinkedHashMap<>());
    command.put(entryName, entryValue);
  }

  private static void addSenchaEntry(JangarooConfig jangarooConfig, String entryName, Object entryValue) {
    jangarooConfig.addToSencha(ImmutableMap.of(
            entryName, entryValue
    ));
  }

  private void addSenchaEntries(JangarooConfig jangarooConfig) {
    addSenchaEntry(jangarooConfig, "name", SenchaUtils.getSenchaPackageName(project));
    if (extNamespace != null) {
      addSenchaEntry(jangarooConfig, "namespace", extNamespace);
    }
    if (packageType != null) {
      addSenchaEntry(jangarooConfig, "type", packageType);
    }
    if (extSassNamespace != null) {
      addSenchaEntry(jangarooConfig, "sass", ImmutableMap.of("namespace", extSassNamespace));
    }
    if (additionalCssIncludeInBundle != null) {
      for (String css : additionalCssIncludeInBundle) {
        addSenchaEntry(jangarooConfig, "css", ImmutableList.of(ImmutableMap.of("path", css)));
      }
    }
    if (additionalCssNonBundle != null) {
      for (String css : additionalCssNonBundle) {
        addSenchaEntry(jangarooConfig, "css", ImmutableList.of(ImmutableMap.of("path", css)));
      }
    }
    if (additionalJsIncludeInBundle != null) {
      for (String js : additionalJsIncludeInBundle) {
        addSenchaEntry(jangarooConfig, "js", ImmutableList.of(ImmutableMap.of("path", js)));
      }
    }
    if (additionalJsNonBundle != null) {
      for (String js : additionalJsNonBundle) {
        addSenchaEntry(jangarooConfig, "js", ImmutableList.of(ImmutableMap.of("path", js)));
      }
    }
  }

  private Map<String, String> readClassMapping(Artifact artifact) {
    String artifactType = artifact.getType();
    boolean isCode = JANGAROO_SWC_PACKAGING.equals(artifactType);
    boolean isApp = JANGAROO_APP_PACKAGING.equals(artifactType);
    if (artifact.getFile() != null && (isCode || isApp)) {
      try {
        JarFile jarFile = new JarFile(artifact.getFile());
        ZipEntry classMappingEntry =  isCode
                ? jarFile.getEntry("META-INF/pkg/classMapping.json")
                : jarFile.getEntry("META-INF/resources/classMapping.json");
        if (classMappingEntry != null) {
          //noinspection unchecked
          Map<String, String> classMapping = jsonObjectMapper.readValue(jarFile.getInputStream(classMappingEntry), Map.class);
          if (!classMapping.isEmpty()) {
            return classMapping;
          }
        }
        // fall back to inventory (Rename annotations are not considered here)
        String inventoryFileName = SenchaUtils.getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId()) + ".json";
        ZipEntry inventoryEntry =  isCode
                ? jarFile.getEntry("META-INF/pkg/" + inventoryFileName)
                : jarFile.getEntry("META-INF/resources/" + inventoryFileName);
        if (inventoryEntry != null) {
          ZipEntry jsonEntry = jarFile.getEntry(isCode ? "META-INF/pkg/package.json" : "META-INF/resources/app.json");
          if (jsonEntry != null) {
            @SuppressWarnings("unchecked") Map<String, String> json = (Map<String, String>) jsonObjectMapper.readValue(jarFile.getInputStream(jsonEntry), Map.class);
            String namespace = json.getOrDefault("namespace", "");
            final int namespaceLengthToRemove = namespace.isEmpty() ? 0 : namespace.length() + 1;
            @SuppressWarnings("unchecked") List<String> inventoryList = jsonObjectMapper.readValue(jarFile.getInputStream(inventoryEntry), List.class);
            //noinspection ConstantConditions will not happen
            return inventoryList.stream().collect(Collectors.toMap(Function.identity(), fqn -> CompilerUtils.fileNameFromQName(fqn.substring(namespaceLengthToRemove), '/', ".ts")));
          }
        }
      } catch (IOException e) {
        getLog().warn(String.format("Class Mapping could not be read from artifact %s!", artifact), e);
      }
    }
    return new HashMap<>();
  }

  private String findClassImportInDependencies(Package pkg, String className, boolean includeDevDependencies) {
    List<Package> allDependencies = new ArrayList<>(pkg.getDependencies());
    if (includeDevDependencies) {
      allDependencies.addAll(pkg.getDependencies());
    }
    // prefer direct dependencies
    for (Package dependency : allDependencies) {
      if (dependency.getClassMapping().containsKey(className)) {
        return dependency.getName() + "/" + dependency.getClassMapping().get(className);
      }
    }
    getLog().warn("Could not resolve ExtJS class: " + className);
    return null;
  }

  private static class CopyFromMavenResult {
    public final boolean hasSourceTsFiles;
    public final boolean hasJooUnitTsFiles;

    public CopyFromMavenResult(boolean hasSourceTsFiles, boolean hasJooUnitTsFiles) {
      this.hasSourceTsFiles = hasSourceTsFiles;
      this.hasJooUnitTsFiles = hasJooUnitTsFiles;
    }
  }
}
