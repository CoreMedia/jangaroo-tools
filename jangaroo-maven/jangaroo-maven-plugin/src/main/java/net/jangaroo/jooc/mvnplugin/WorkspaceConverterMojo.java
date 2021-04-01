package net.jangaroo.jooc.mvnplugin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.jangaroo.jooc.config.SearchAndReplace;
import net.jangaroo.jooc.mvnplugin.converter.AdditionalPackageJsonEntries;
import net.jangaroo.jooc.mvnplugin.converter.IdeaProjectIml;
import net.jangaroo.jooc.mvnplugin.converter.JangarooConfig;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import net.jangaroo.jooc.mvnplugin.converter.PackageJson;
import net.jangaroo.jooc.mvnplugin.converter.PackageJsonPrettyPrinter;
import net.jangaroo.jooc.mvnplugin.converter.RootPackageJson;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
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
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mojo(name = "convert-workspace",
        defaultPhase = LifecyclePhase.INSTALL,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class WorkspaceConverterMojo extends AbstractMojo {
  private static final Logger logger = LoggerFactory.getLogger(WorkspaceConverterMojo.class);
  private static final Object lock = new Object();

  // --- CONVERTER CONFIGURATION --- //

  @Parameter(property = "convertedWorkspaceTarget", required = true)
  private String convertedWorkspaceTarget;

  @Parameter
  private List<SearchAndReplaceConfiguration> npmPackageNameReplacers = new ArrayList<>();

  @Parameter
  private List<SearchAndReplaceConfiguration> npmPackageFolderNameReplacers = new ArrayList<>();

  @Parameter
  private boolean useTypesVersions = false;

  // --- ACTUAL JANGAROO MAVEN CONFIGURATION --- //

  @Parameter
  private String packageType = "code";

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
  private String extNamespace = "";

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
  private ObjectMapper objectMapper;
  private RootPackageJson rootPackageJson;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    PackageJsonPrettyPrinter prettyPrinter = new PackageJsonPrettyPrinter();

    objectMapper = SenchaUtils.getObjectMapper()
            .setDefaultPrettyPrinter(prettyPrinter)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    rootPackageJson = new RootPackageJson(objectMapper, convertedWorkspaceTarget);

    resolvedNpmPackageNameReplacers = npmPackageNameReplacers.stream()
            .map(config -> new SearchAndReplace(Pattern.compile(config.getSearch()), config.getReplace()))
            .collect(Collectors.toList());
    resolvedNpmPackageFolderNameReplacers = npmPackageFolderNameReplacers.stream()
            .map(config -> new SearchAndReplace(Pattern.compile(config.getSearch()), config.getReplace()))
            .collect(Collectors.toList());

    List<Package> packageRegistry = new ArrayList<>();

    packageRegistry.add(new Package("@coremedia/sencha-ext-charts", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext-classic", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext-classic-theme-triton", "7.2.0"));
    packageRegistry.add(new Package("@jangaroo/joo", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/jangaroo-net", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/jooflash-core", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/jooflexframework", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/joounit", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/ext-ts", "1.0.0"));
    packageRegistry.add(new Package("@jangaroo/ckeditor4", "1.0.0"));

    Map<String, MavenModule> moduleMappings = loadMavenModule(project.getFile().getPath().replace("pom.xml", ""));
    Optional<Package> optionalPackage = getOrCreatePackage(packageRegistry, findPackageNameByReference(String.format("%s:%s", project.getGroupId(), project.getArtifactId()), moduleMappings), null, moduleMappings);
    try {
      synchronized (lock) {
        rootPackageJson.readPackageJson();
        moduleMappings.entrySet().stream()
                .map(entry -> {
                  if (ModuleType.IGNORE == entry.getValue().getModuleType()) {
                    return null;
                  } else if (ModuleType.AGGREGATOR == entry.getValue().getModuleType()) {
                    return null;
                  } else {
                    return getPackageFolderName(entry.getKey());
                  }
                })
                .filter(Objects::nonNull)
                .forEach(rootPackageJson::addWorkspace);

        rootPackageJson.writePackageJson();

        Map<String, Object> lernaJson = new LinkedHashMap<>();
        lernaJson.put("useWorkspaces", true);
        lernaJson.put("version", "0.0.0");
        Map<String, Object> lernaCommandMap = new LinkedHashMap<>();
        Map<String, Object> lernaRunMap = new LinkedHashMap<>();
        lernaRunMap.put("stream", true);
        lernaCommandMap.put("run", lernaRunMap);
        lernaJson.put("command", lernaCommandMap);

        FileUtils.write(new File(convertedWorkspaceTarget + "/lerna.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(lernaJson).concat("\n"));
      }

      List<String> excludePaths = new ArrayList<>();
      if (!optionalPackage.isPresent()) {
        logger.warn("Package was null");
        return;
      }
      Package aPackage = optionalPackage.get();

      MavenModule mavenModule = moduleMappings.get(aPackage.getName());
      if (mavenModule != null && !ModuleType.IGNORE.equals(mavenModule.getModuleType())) {
        String packageFolderName = getPackageFolderName(aPackage.getName());
        String targetPackageDir = convertedWorkspaceTarget + "/" + packageFolderName;
        logger.info(String.format("Generating npm workspace for module %s to directory %s", mavenModule.getData().getArtifactId(), new File(targetPackageDir).getCanonicalPath()));
        String targetPackageJson = targetPackageDir + "/package.json";
        excludePaths.add(targetPackageDir + "/dist");

        JangarooConfig jangarooConfig = new JangarooConfig();
        AdditionalPackageJsonEntries additionalJsonEntries = new AdditionalPackageJsonEntries();
        if (mavenModule.getModuleType() == ModuleType.SWC) {
          jangarooConfig.setType(packageType);
          jangarooConfig.setExtName(String.format("%s__%s", mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId()));

          jangarooConfig.setExtNamespace(extNamespace);
          jangarooConfig.setExtSassNamespace(extSassNamespace);

          if (theme != null) {
            String themePackageName = findPackageNameByReference(theme, moduleMappings);
            jangarooConfig.setTheme(themePackageName);
          }
          jangarooConfig.setAdditionalCssIncludeInBundle(additionalCssIncludeInBundle);
          jangarooConfig.setAdditionalCssNonBundle(additionalCssNonBundle);
          jangarooConfig.setAdditionalJsIncludeInBundle(additionalJsIncludeInBundle);
          jangarooConfig.setAdditionalJsNonBundle(additionalJsNonBundle);
          jangarooConfig.setGlobalResourcesMap(globalResourcesMap);
          if (testSuite != null) {
            jangarooConfig.setTestSuite(testSuite);
            if (!extNamespace.isEmpty()) {
              String extNamespaceWithTrailingDot = extNamespace.concat(".");
              if (!jangarooConfig.getTestSuite().startsWith(extNamespaceWithTrailingDot)) {
                logger.error(String.format("Invalid testSuite configuration \"jangarooConfig.testSuite\". When Using extNamespace \"%s\" the test suite cannot exist.", extNamespace));
                return;
              }
              jangarooConfig.setTestSuite(jangarooConfig.getTestSuite().replace(extNamespaceWithTrailingDot, ""));
            }
          }
          if (jooUnitTestExecutionTimeout != null) {
            setCommandMapEntry(jangarooConfig, "joounit", "testExecutionTimeout", jooUnitTestExecutionTimeout);
          }
          if (new File(mavenModule.getDirectory().getPath() + "/package.json").exists()) {
            jangarooConfig.setSencha(objectMapper.readValue(FileUtils.readFileToString(new File(mavenModule.getDirectory().getPath() + "/package.json")), Map.class));
          }
          Map<String, String> testDependencies = new TreeMap<>();
          Map<String, String> testScripts = new LinkedHashMap<>();
          if (jangarooConfig.getTestSuite() != null) {
            excludePaths.add(targetPackageDir + "/build");
            testDependencies.put("@jangaroo/joounit", "1.0.0");
            testDependencies.put("@coremedia/sencha-ext", "7.2.0");
            testDependencies.put("@coremedia/sencha-ext-classic", "7.2.0");
            testDependencies.put("@coremedia/sencha-ext-classic-locale", "7.2.0");

            testScripts.put("test", "jangaroo joounit");
          }
          if (mavenModule.getData().getOrganization() != null) {
            additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
          }
          additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
          Map<String, String> dependencies = new TreeMap<>();
          dependencies.put("@jangaroo/joo", "1.0.0");
          additionalJsonEntries.setDependencies(dependencies);
          Map<String, String> devDependencies = new TreeMap<>();
          devDependencies.put("@jangaroo/core", "1.0.0");
          devDependencies.put("@jangaroo/build", "1.0.0");
          devDependencies.put("@jangaroo/publish", "1.0.0");
          devDependencies.putAll(testDependencies);
          devDependencies.put("rimraf", "^3.0.2");
          additionalJsonEntries.setDevDependencies(devDependencies);
          Map<String, String> scripts = new LinkedHashMap<>();
          scripts.put("clean", "rimraf ./dist && rimraf ./build");
          scripts.put("build", "jangaroo build");
          scripts.put("watch", "jangaroo watch");
          scripts.put("publish", "jangaroo publish dist");
          scripts.putAll(testScripts);
          additionalJsonEntries.setScripts(scripts);
          if (useTypesVersions) {
            List<String> typesPaths = new ArrayList<>();
            typesPaths.add("./src/*");
            Map<String, List> allMapping = new HashMap<>();
            allMapping.put("*", typesPaths);
            Map<String, Object> typesVersions = new HashMap<>();
            typesVersions.put("*", allMapping);
            additionalJsonEntries.setTypesVersions(typesVersions);
          }

          List<String> ignoreFromSrcMain = new ArrayList<>();
          ignoreFromSrcMain.add("package.json");
          copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "packages",
                  String.format("%s__%s", mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId())).toString(),
                  "src", ignoreFromSrcMain, targetPackageDir
          );
          if (Paths.get(targetPackageDir, "src", "index.d.ts").toFile().exists()) {
            if (useTypesVersions) {
              // although this is the default value, explicitly add this entry
              additionalJsonEntries.setTypes("index.d.ts");
              additionalJsonEntries.addPublishOverride("types", "index.d.ts");
            } else {
              additionalJsonEntries.setTypes("src/index.d.ts");
              additionalJsonEntries.addPublishOverride("types", "src/index.d.ts");
            }
          }
        } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APP) {
          excludePaths.add(targetPackageDir + "/build");
          jangarooConfig.setType("app");
          setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");
          jangarooConfig.setExtNamespace(extNamespace);
          jangarooConfig.setExtSassNamespace(extSassNamespace);
          if (theme != null) {
            String themePackageName = findPackageNameByReference(theme, moduleMappings);
            jangarooConfig.setTheme(themePackageName);
          }
          jangarooConfig.setApplicationClass(applicationClass);
          jangarooConfig.setAdditionalLocales(additionalLocales);
          jangarooConfig.setAdditionalCssIncludeInBundle(additionalCssIncludeInBundle);
          jangarooConfig.setAdditionalCssNonBundle(additionalCssNonBundle);
          jangarooConfig.setAdditionalJsIncludeInBundle(additionalJsIncludeInBundle);
          jangarooConfig.setAdditionalJsNonBundle(additionalJsNonBundle);
          if (new File(mavenModule.getDirectory().getPath() + "/app.json").exists()) {
            jangarooConfig.setSencha(objectMapper.readValue(FileUtils.readFileToString(new File(mavenModule.getDirectory().getPath() + "/app.json")), Map.class));
          }
          if (mavenModule.getData().getOrganization() != null) {
            additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
          }
          additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
          Map<String, String> dependencies = new TreeMap<>();
          dependencies.put("@coremedia/sencha-ext", "7.2.0");
          dependencies.put("@coremedia/sencha-ext-classic", "7.2.0");
          dependencies.put("@coremedia/sencha-ext-classic-locale", "7.2.0");
          dependencies.put("@jangaroo/joo", "1.0.0");
          additionalJsonEntries.setDependencies(dependencies);
          Map<String, String> devDependencies = new TreeMap<>();
          devDependencies.put("@jangaroo/core", "1.0.0");
          devDependencies.put("@jangaroo/build", "1.0.0");
          devDependencies.put("@jangaroo/run", "1.0.0");
          devDependencies.put("rimraf", "^3.0.2");
          additionalJsonEntries.setDevDependencies(devDependencies);
          Map<String, String> scripts = new LinkedHashMap<>();
          scripts.put("clean", "rimraf ./dist && rimraf ./build");
          scripts.put("build", "jangaroo build");
          scripts.put("watch", "jangaroo watch");
          scripts.put("start", "jangaroo run");
          additionalJsonEntries.setScripts(scripts);
          if (useTypesVersions) {
            List<String> typesPaths = new ArrayList<>();
            typesPaths.add("./src/*");
            Map<String, List> allMapping = new LinkedHashMap<>();
            allMapping.put("*", typesPaths);
            Map<String, Object> typesVersions = new LinkedHashMap<>();
            typesVersions.put("*", allMapping);
            additionalJsonEntries.setTypesVersions(typesVersions);
          }
          List<String> ignoreFromSrcMain = new ArrayList<>();
          ignoreFromSrcMain.add("app.json");
          copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "app").toString(),
                  "app", ignoreFromSrcMain, targetPackageDir
          );
        } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APP_OVERLAY) {
          excludePaths.add(targetPackageDir + "/build");
          jangarooConfig.setType("app-overlay");
          setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");
          if (mavenModule.getData().getOrganization() != null) {
            additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
          }
          additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
          Map<String, String> devDependencies = new TreeMap<>();
          devDependencies.put("@jangaroo/core", "1.0.0");
          devDependencies.put("@jangaroo/build", "1.0.0");
          devDependencies.put("@jangaroo/run", "1.0.0");
          devDependencies.put("rimraf", "^3.0.2");
          additionalJsonEntries.setDevDependencies(devDependencies);
          Map<String, String> scripts = new LinkedHashMap<>();
          scripts.put("clean", "rimraf ./dist");
          scripts.put("build", "jangaroo build");
          scripts.put("watch", "jangaroo watch");
          scripts.put("start", "jangaroo run");
          additionalJsonEntries.setScripts(scripts);
        } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APPS) {
          excludePaths.add(targetPackageDir + "/build");
          jangarooConfig.setType("apps");
          setCommandMapEntry(jangarooConfig, "run", "proxyPathSpec", "/rest/");
          if (rootApp != null && !rootApp.isEmpty()) {
            String[] splitName = rootApp.split(":");
            if (splitName.length == 2) {
              jangarooConfig.setRootApp(calculateMavenName(splitName[0], splitName[1]));
            }
          }
          if (mavenModule.getData().getOrganization() != null) {
            additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
          }
          additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
          Map<String, String> devDependencies = new TreeMap<>();
          devDependencies.put("@jangaroo/core", "1.0.0");
          devDependencies.put("@jangaroo/build", "1.0.0");
          devDependencies.put("@jangaroo/run", "1.0.0");
          devDependencies.put("rimraf", "^3.0.2");
          additionalJsonEntries.setDevDependencies(devDependencies);
          Map<String, String> scripts = new LinkedHashMap<>();
          scripts.put("clean", "rimraf ./dist");
          scripts.put("build", "jangaroo build");
          scripts.put("watch", "jangaroo watch");
          scripts.put("start", "jangaroo run");
          additionalJsonEntries.setScripts(scripts);
        } else {
          return;
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
                jangarooConfig.addAppManifest(locale, objectMapper.readValue(new File(appManifestPath), Map.class));
              } catch (IOException ioException) {
                logger.error("error while reading manifest file: " + appManifestPath);
              }
            } else {
              logger.error("Could not detect locale for manifest file: " + appManifestPath);
            }
          }
        }

        synchronized (lock) {
          String projectName = new File(convertedWorkspaceTarget).getName();
          File ideaConfigFolder = Paths.get(convertedWorkspaceTarget, ".idea").toFile();
          File modulesXmlPath = Paths.get(ideaConfigFolder.getPath(), "modules.xml").toFile();
          File projectImlPath = Paths.get(ideaConfigFolder.getPath(), projectName + ".iml").toFile();
          //todo: Some security config necessary?

          if (!ideaConfigFolder.exists()) {
            String modulesXml = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project version=\"4\">\n" +
                    "  <component name=\"ProjectModuleManager\">\n" +
                    "    <modules>\n" +
                    "      <module fileurl=\"file://$PROJECT_DIR$/.idea/%s.iml\" filepath=\"$PROJECT_DIR$/%s\" />\n" +
                    "    </modules>\n" +
                    "  </component>\n" +
                    "</project>\n" +
                    "", projectName, Paths.get(convertedWorkspaceTarget).relativize(projectImlPath.toPath()).toString());
            FileUtils.writeStringToFile(modulesXmlPath, modulesXml);
          }
          IdeaProjectIml ideaProjectIml = new IdeaProjectIml(convertedWorkspaceTarget, projectImlPath);
          ideaProjectIml.writeProjectIml(excludePaths);

          File gitignoreFile = Paths.get(convertedWorkspaceTarget, ".gitignore").toFile();
          if (!gitignoreFile.exists()) {
            StringJoiner stringJoiner = new StringJoiner("\n");
            stringJoiner.add("# NodeJS");
            stringJoiner.add("node_modules/");
            stringJoiner.add("");
            stringJoiner.add("# Jangaroo Build");
            stringJoiner.add("dist/");
            stringJoiner.add("build/");
            stringJoiner.add("");
            stringJoiner.add("# IntellIJ IDEA");
            stringJoiner.add(Paths.get(convertedWorkspaceTarget).relativize(ideaConfigFolder.toPath()).toString() + "/*");
            stringJoiner.add("!" + Paths.get(convertedWorkspaceTarget).relativize(modulesXmlPath.toPath()).toString());
            stringJoiner.add("!" + Paths.get(convertedWorkspaceTarget).relativize(projectImlPath.toPath()).toString());
            stringJoiner.add("");
            String gitignore = stringJoiner.toString();
            FileUtils.writeStringToFile(gitignoreFile, gitignore);
          }
        }

        String jangarooConfigDocument = "/** @type { import('@jangaroo/core').IJangarooConfig } */\nmodule.exports = ".concat(convertJangarooConfig(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jangarooConfig).concat(";\n")));
        FileUtils.writeStringToFile(Paths.get(targetPackageDir, "jangaroo.config.js").toFile(), jangarooConfigDocument);
        if (jangarooConfig.getTheme() != null && !jangarooConfig.getTheme().isEmpty()) {
          Optional<Package> optionalThemeDependency = packageRegistry.stream()
                  .filter(somePackage -> somePackage.matches(jangarooConfig.getTheme(), null))
                  .findFirst();
          optionalThemeDependency.ifPresent(value -> additionalJsonEntries.getDependencies().put(value.getName(), value.getVersion()));
        }


        PackageJson packageJson = new PackageJson(additionalJsonEntries);
        packageJson.setName(aPackage.getName());
        packageJson.setVersion(aPackage.getVersion());
        packageJson.setLicense("MIT");
        packageJson.setPrivat(true);
        aPackage.getDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getVersion)).forEach(packageJson::addDependency);
        aPackage.getDevDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getVersion)).forEach(packageJson::addDevDependency);
        Map<String, String> sortedDependencies = new TreeMap<>();
        packageJson.getDependencies().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sortedDependencies.put(entry.getKey(), entry.getValue()));
        packageJson.setDependencies(sortedDependencies);
        Map<String, String> sortedDevDependencies = new TreeMap<>();
        packageJson.getDevDependencies().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sortedDevDependencies.put(entry.getKey(), entry.getValue()));
        packageJson.setDevDependencies(sortedDevDependencies);
        FileUtils.write(new File(targetPackageJson), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getPackageFolderName(String packageName) {
    String packageFolderName = packageName;
    for (SearchAndReplace searchAndReplace : resolvedNpmPackageFolderNameReplacers) {
      Matcher matcher = searchAndReplace.search.matcher(packageFolderName);
      if (matcher.matches()) {
        packageFolderName = matcher.replaceAll(searchAndReplace.replace);
        break;
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

  private void copyCodeFromMaven(String baseDirectory, String generatedExtModuleDirectory, String srcFolderName, List<String> ignoreFromSrcMainSencha, String targetPackageDir) throws IOException {
    List<String> fullIgnoreFromSrcMainSencha = new ArrayList<>(ignoreFromSrcMainSencha);
    fullIgnoreFromSrcMainSencha.add("sass/var");
    fullIgnoreFromSrcMainSencha.add("sass/src");
    for (String dir : Arrays.asList(srcFolderName, "sass/var", "sass/src")) {
      Path sourceDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, dir);
      if (sourceDirPath.toFile().exists() && sourceDirPath.toFile().isDirectory()) {
        Path targetDirPath = Paths.get(targetPackageDir, "sencha", dir);
        FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> pathname.isDirectory() || !pathname.getName().endsWith(".ts"));
      }

      sourceDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, srcFolderName);
      if (sourceDirPath.toFile().exists() && sourceDirPath.toFile().isDirectory()) {
        Path targetDirPath = Paths.get(targetPackageDir, srcFolderName);
        FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> pathname.isDirectory() || pathname.getName().endsWith(".ts"));
      }

      Path jooUnitSourcePath = Paths.get(baseDirectory, "target", "test-classes", srcFolderName);
      if (jooUnitSourcePath.toFile().exists() && jooUnitSourcePath.toFile().isDirectory()) {
        Path jooUnitTargetDirPath = Paths.get(targetPackageDir, "joounit");
        FileUtils.copyDirectory(jooUnitSourcePath.toFile(), jooUnitTargetDirPath.toFile(), pathname -> pathname.isDirectory() || pathname.getName().endsWith(".ts"));
      }
      Path srcMainSenchaPath = Paths.get(baseDirectory, "src", "main", "sencha");
      Path targetSenchaPath = Paths.get(targetPackageDir, "sencha");
      if (srcMainSenchaPath.toFile().exists() && srcMainSenchaPath.toFile().isDirectory()) {
        FileUtils.copyDirectory(srcMainSenchaPath.toFile(), targetSenchaPath.toFile(),
                pathname -> acceptFile(pathname, srcMainSenchaPath, fullIgnoreFromSrcMainSencha));
      }
    }
  }

  private boolean acceptFile(File file, Path srcMainSenchaPath, List<String> ignoreFromSrcMainSencha) {
    List<Path> collect = ignoreFromSrcMainSencha.stream()
            .map(string -> Paths.get(srcMainSenchaPath.toString(), string))
            .collect(Collectors.toList());
    return collect.stream()
            .noneMatch(path -> file.getPath().contains(path.toString()));
  }


  private Optional<Package> getOrCreateDependencyPackage(String name, Dependency dependency) {
    Optional<Artifact> optionalArtifact = project.getArtifacts().stream()
            .filter(artifact -> artifact.getGroupId().equals(dependency.getGroupId()) && artifact.getArtifactId().equals(dependency.getArtifactId()))
            .findFirst();
    if (!optionalArtifact.isPresent()) {
      return Optional.empty();
    }
    ModuleType moduleType = MavenModule.calculateModuleType(optionalArtifact.get().getArtifactHandler().getPackaging());
    if (moduleType == ModuleType.IGNORE) {
      return Optional.empty();
    }
    Model model = new Model();
    model.setGroupId(optionalArtifact.get().getGroupId());
    model.setArtifactId(optionalArtifact.get().getArtifactId());
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
    return Optional.of(handlePackageDependencies(name, new MavenModule("", model)));
  }

  private boolean inDependencyTrail(Dependency dependency, List<String> dependencyTrail) {
    return dependencyTrail.stream()
            .map(dependencyPart -> dependencyPart.split(":"))
            .filter(dependencySplit -> dependency.getGroupId().equals(dependencySplit[0]))
            .anyMatch(dependencySplit -> dependency.getArtifactId().equals(dependencySplit[1]));
  }

  private Optional<Package> getOrCreatePackage(List<Package> packageRegistry, String packageName, String packageVersion, Map<String, MavenModule> moduleMappings) {
    Optional<Package> matchingPackage = packageRegistry.stream()
            .filter(aPackage -> aPackage.matches(packageName, packageVersion))
            .findFirst();
    if (matchingPackage.isPresent()) {
      return matchingPackage;
    } else {
      MavenModule module = moduleMappings.get(packageName);
      if (module == null) {
        logger.error("could not find module {}", packageName);
        return Optional.empty();
      }
      if (module.getModuleType() == ModuleType.IGNORE) {
        return Optional.empty();
      }
      Package newPackage = handlePackageDependencies(packageName, module);
      packageRegistry.add(newPackage);
      return Optional.of(newPackage);
    }
  }

  private Package handlePackageDependencies(String packageName, MavenModule mavenModule) {
    String newPackageVersion = isValidVersion(mavenModule.getVersion()) ? mavenModule.getVersion() : "1.0.0-SNAPSHOT";
    List<Package> newDependencies = new ArrayList<>();
    List<Package> newDevDependencies = new ArrayList<>();
    List<Dependency> dependencies;
    dependencies = mavenModule.getData().getDependencies().stream()
            .filter(dependency -> !"test".equals(dependency.getScope()))
            .filter(dependency -> !ignoreDependency(dependency))
            .map(dependency -> {
              if ("${project.groupId}".equals(dependency.getGroupId())) {
                dependency.setGroupId(mavenModule.getData().getGroupId());
              }
              if ("${project.version}".equals(dependency.getVersion())) {
                dependency.setVersion(mavenModule.getVersion());
              }
              return dependency;
            })
            .collect(Collectors.toList());

    List<Dependency> testDependencies = mavenModule.getData().getDependencies().stream()
            .filter(dependency -> "test".equals(dependency.getScope()))
            .filter(dependency -> !ignoreDependency(dependency))
            .map(dependency -> {
              if ("$(project.groupid)".equals(dependency.getGroupId())) {
                dependency.setGroupId(mavenModule.getData().getGroupId());
              }
              if ("${project.version}".equals(dependency.getVersion())) {
                dependency.setVersion(mavenModule.getVersion());
              }
              return dependency;
            })
            .collect(Collectors.toList());

    for (Dependency dependency : dependencies) {
      Package createdPackage = new Package(mapJangarooName(dependency.getGroupId(), dependency.getArtifactId()), isValidVersion(dependency.getVersion()) ? dependency.getVersion() : "1.0.0-SNAPSHOT");
      if (isJangarooDependency(dependency)) {
        createdPackage = new Package(mapJangarooName(dependency.getGroupId(), dependency.getArtifactId()), "1.0.0");
      }
      if (Arrays.asList("swc", "jar").contains(dependency.getType())) {
        newDependencies.add(createdPackage);
      } else {
        createdPackage = getOrCreateDependencyPackage(mapJangarooName(dependency.getGroupId(), dependency.getArtifactId()), dependency).orElse(null);
        if (createdPackage != null) {
          newDependencies.addAll(createdPackage.getDependencies());
        }
      }
    }
    for (Dependency dependency : testDependencies) {
      Package createdPackage = new Package(mapJangarooName(dependency.getGroupId(), dependency.getArtifactId()), isValidVersion(dependency.getVersion()) ? dependency.getVersion() : "1.0.0-SNAPSHOT");
      if (isJangarooDependency(dependency)) {
        createdPackage = new Package(mapJangarooName(dependency.getGroupId(), dependency.getArtifactId()), "1.0.0");
      }
      if (Arrays.asList("swc", "jar").contains(dependency.getType())) {
        newDevDependencies.add(createdPackage);
      } else {
        newDevDependencies.addAll(createdPackage.getDependencies());
      }
    }
    return new Package(packageName, newPackageVersion, newDependencies, newDevDependencies);
  }

  private String mapJangarooName(String groupId, String artifactId) {
    if (Objects.equals(artifactId, "ext")) {
      return "@coremedia/sencha-ext";
    }
    if (Objects.equals(artifactId, "ext-classic")) {
      return "@coremedia/sencha-ext-classic";
    }
    if (Objects.equals(artifactId, "charts")) {
      return "@coremedia/sencha-ext-charts";
    }
    if (artifactId.startsWith("theme-")) {
      return "@coremedia/sencha-ext-classic-" + artifactId;
    }
    if (groupId != null && groupId.startsWith("net.jangaroo")) {
      if (Objects.equals(artifactId, "jangaroo-net")) {
        return "@jangaroo/jangaroo-net";
      }
      if (Objects.equals(artifactId, "jangaroo-runtime")) {
        return "@jangaroo/joo";
      }
      if (Objects.equals(artifactId, "jooflash-core")) {
        return "@jangaroo/jooflash-core";
      }
      if (Objects.equals(artifactId, "jooflexframework")) {
        return "@jangaroo/jooflexframework";
      }
      if (Objects.equals(artifactId, "joounit")) {
        return "@jangaroo/joounit";
      }
      if (Objects.equals(artifactId, "ext-as")) {
        return "@jangaroo/ext-ts";
      }
      if (Objects.equals(artifactId, "ckeditor4")) {
        return "@jangaroo/ckeditor4";
      }
    }
    return calculateMavenName(groupId, artifactId);
  }


  private boolean isJangarooDependency(Dependency dependency) {
    return Arrays.asList("net.jangaroo__jangaroo-browser",
            "net.jangaroo__ext-as",
            "net.jangaroo__jangaroo-net",
            "net.jangaroo__jangaroo-runtime",
            "net.jangaroo__jooflash-core",
            "net.jangaroo__jooflexframework",
            "net.jangaroo__joounit",
            "net.jangaroo__ckeditor4")
            .contains(String.format("%s__%s", dependency.getGroupId(), dependency.getArtifactId()));
  }

  private boolean ignoreDependency(Dependency dependency) {
    return "net.jangaroo__jangaroo-browser".contains(String.format("%s__%s", dependency.getGroupId(), dependency.getArtifactId()));
  }

  public String findPackageNameByReference(String reference, Map<String, MavenModule> moduleMappings) {
    Optional<String> packageName;
    if (reference == null) {
      return null;
    }
    String[] splitName = reference.split(":");
    if (splitName.length == 2 && splitName[0] != null && splitName[1] != null) {
      packageName = Optional.of(calculateMavenName(splitName[0], splitName[1]));
    } else {
      if (reference.startsWith("theme-")) {
        return "@coremedia/sencha-ext-classic-theme-" + reference.substring("theme-".length());
      }
      packageName = moduleMappings.entrySet().stream()
              .map(moduleEntry -> {
                if (ModuleType.IGNORE.equals(moduleEntry.getValue().getModuleType())) {
                  return new AbstractMap.SimpleEntry<>(moduleEntry.getKey(), moduleEntry.getValue().getData().getName());
                } else {
                  return new AbstractMap.SimpleEntry<>(moduleEntry.getKey(), calculateMavenName(moduleEntry.getValue().getData()));
                }
              })
              .filter(entry -> reference.equals(entry.getValue()))
              .map(Map.Entry::getKey)
              .findFirst();
    }
    if (!packageName.isPresent()) {
      logger.error(String.format("Could not resolve reference %s. No suitable module was found.", reference));
      return null;
    }
    for (SearchAndReplace searchAndReplace : resolvedNpmPackageNameReplacers) {
      Matcher matcher = searchAndReplace.search.matcher(packageName.get());
      if (matcher.matches()) {
        return matcher.replaceAll(searchAndReplace.replace);
      }
    }
    return packageName.get();
  }

  private Map<String, MavenModule> loadMavenModule(String modulePath) {
    Map<String, MavenModule> modules = new TreeMap<>();
    modules.put(calculateMavenName(project.getModel()), new MavenModule(modulePath, project.getModel()));
    return modules;
  }

  private String calculateMavenName(Model model) {
    return calculateMavenName(model.getGroupId(), model.getArtifactId());
  }

  private String calculateMavenName(String groupId, String artifactId) {
    String mavenName;
    if ("com.coremedia.sencha".equals(groupId) && "ext-js-pkg".equals(artifactId) ||
            "net.jangaroo.com.sencha".equals(groupId) && "ext-js-pkg-gpl".equals(artifactId)) {
      mavenName = "ext";
    } else {
      mavenName = groupId + "__" + artifactId;
    }
    for (SearchAndReplace searchAndReplace : resolvedNpmPackageNameReplacers) {
      Matcher matcher = searchAndReplace.search.matcher(mavenName);
      if (matcher.matches()) {
        return matcher.replaceAll(searchAndReplace.replace);
      }
    }
    return mavenName;
  }

  private String convertJangarooConfig(String jangarooConfig) {
    return jangarooConfig
            .replaceAll("\"([a-zA-Z_$][0-9a-zA-Z_$]*)\":", "$1:")
            .replace("}", "},")
            .replace("]", "],")
            .replace("\"\n", "\",\n")
            .replace(",,", ",")
            .replace(",;", ";");
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

  private void setCommandMapEntry(JangarooConfig jangarooConfig, String commandName, String entryName, Object entryValue) {
    Map<String, Map<String, Object>> commandsByName = jangarooConfig.getCommand();
    if (commandsByName == null) {
      commandsByName = new LinkedHashMap<>();
      jangarooConfig.setCommand(commandsByName);
    }
    Map<String, Object> command = commandsByName.computeIfAbsent(commandName, k -> new LinkedHashMap<>());
    command.put(entryName, entryValue);
  }
}
