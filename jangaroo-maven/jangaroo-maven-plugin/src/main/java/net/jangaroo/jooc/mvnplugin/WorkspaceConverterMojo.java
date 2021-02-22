package net.jangaroo.jooc.mvnplugin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jangaroo.jooc.mvnplugin.converter.AdditionalPackageJsonEntries;
import net.jangaroo.jooc.mvnplugin.converter.GlobalLibraryConfiguration;
import net.jangaroo.jooc.mvnplugin.converter.JangarooConfig;
import net.jangaroo.jooc.mvnplugin.converter.JangarooMavenPluginConfiguration;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import net.jangaroo.jooc.mvnplugin.converter.PackageJson;
import net.jangaroo.jooc.mvnplugin.converter.RootPackageJson;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.tools.ant.taskdefs.Pack;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mojo(name = "convert-workspace", //convert-workspace
        defaultPhase = LifecyclePhase.INSTALL,
        threadSafe = false) // check for threadsafety and make it threadsafe
public class WorkspaceConverterMojo extends AbstractJangarooMojo {
  private static final Logger logger = LoggerFactory.getLogger(WorkspaceConverterMojo.class);

  @Parameter(property = "studio.npm.maven.root")
  private String studioNpmMavenRoot = "/home/fwellers/dev/cms/apps/studio-client";

  @Parameter(property = "studio.npm.target")
  private String studioNpmTarget = "/home/fwellers/dev/jangaroo-tools/jangaroo-maven/jangaroo-maven-plugin/created_workspace";

  //todo: remove
  @Parameter(property = "sudio.app.package.name")
  private String moduleName = "com.coremedia.blueprint__studio-resources";

  //todo: remove
  @Parameter(property = "activeProfiles", defaultValue = "${session.request.activeProfiles}")
  protected List<String> activeProfiles;

  @Parameter(property = "studio.npm.clean-build")
  private boolean cleanBuild = false;

  private ObjectMapper objectMapper = SenchaUtils.getObjectMapper();

  private RootPackageJson rootPackageJson = new RootPackageJson(objectMapper, studioNpmTarget);

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
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

    Map<String, MavenModule> moduleMappings = loadMavenModule(getProject().getFile().getPath().replace("pom.xml", ""));
    Optional<Package> optionalPackage = getOrCreatePackage(packageRegistry, findPackageNameByReference(String.format("%s:%s", getProject().getGroupId(), getProject().getArtifactId()), moduleMappings), null, moduleMappings);
    // todo: handle clean build
    try {
      rootPackageJson.readPackageJson();
      moduleMappings.entrySet().stream()
              .map(entry -> {
                if (ModuleType.IGNORE.equals(entry.getValue().getModuleType())) {
                  return null;
                } else {
                  return "packages/" + entry.getKey();
                }
              })
              .filter(Objects::nonNull)
              .forEach(rootPackageJson::addWorkspace);

      rootPackageJson.writePackageJson();

      Map<String, Object> lernaJson = new HashMap<>();
      lernaJson.put("npmClient", "yarn");
      lernaJson.put("useWorkspaces", true);
      lernaJson.put("version", "0.0.0");
      Map<String, Object> lernaCommandMap = new HashMap<>();
      Map<String, Object> lernaRunMap = new HashMap<>();
      lernaRunMap.put("stream", true);
      lernaCommandMap.put("run", lernaRunMap);
      lernaJson.put("command", lernaCommandMap);

      FileUtils.write(new File(studioNpmTarget + "/lerna.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(lernaJson));

      if (!optionalPackage.isPresent()) {
        // todo: handle this properly
        logger.error("Package was null");
        return;
      }
      Package aPackage = optionalPackage.get();

       MavenModule mavenModule = moduleMappings.get(aPackage.getName());
        if (mavenModule != null && !ModuleType.IGNORE.equals(mavenModule.getModuleType())) {
          String targetPackageDir = studioNpmTarget + "/packages/" + aPackage.getName();
          String targetPackageJson = targetPackageDir + "/package.json";

          JangarooConfig jangarooConfig = new JangarooConfig();
          AdditionalPackageJsonEntries additionalJsonEntries = new AdditionalPackageJsonEntries();
          JangarooMavenPluginConfiguration jangarooMavenPluginConfiguration = new JangarooMavenPluginConfiguration(mavenModule.getData());
          if (mavenModule.getModuleType() == ModuleType.SWC) {
            jangarooConfig.setType(jangarooMavenPluginConfiguration.getPackageType());
            jangarooConfig.setExtName(calculateMavenName(mavenModule.getData()));
            jangarooConfig.setExtNamespace(jangarooMavenPluginConfiguration.getExtNamespace());
            if (jangarooMavenPluginConfiguration.getTheme() != null) {
              jangarooConfig.setTheme(findPackageNameByReference(jangarooMavenPluginConfiguration.getTheme(), moduleMappings));
            }
            GlobalLibraryConfiguration globalLibraryConfiguration = new GlobalLibraryConfiguration(mavenModule.getData());
            jangarooConfig.setGlobalLibraries(globalLibraryConfiguration.getGlobalLibraries());
            jangarooConfig.setAdditionalCssIncludeInBundle(jangarooMavenPluginConfiguration.getAdditionalJsIncludeInBundle());
            jangarooConfig.setAdditionalCssNonBundle(jangarooMavenPluginConfiguration.getAdditionalCssNonBundle());
            // todo: is this correct?
            jangarooConfig.setAdditionalJsIncludeInBundle(
                    jangarooMavenPluginConfiguration.getAdditionalJsIncludeInBundle().stream()
                            .filter(jsPath -> !globalLibraryConfiguration.getAdditionalJsPaths().contains(jsPath))
                            .collect(Collectors.toList()));
            jangarooConfig.setTestSuite(jangarooMavenPluginConfiguration.getTestSuite());
            if (new File(mavenModule.getDirectory().getPath() + "/package.json").exists()) {
              jangarooConfig.setSencha(objectMapper.readValue(FileUtils.readFileToString(new File(mavenModule.getDirectory().getPath() + "/package.json")), Map.class));
            }
            Map<String, String> testDependencies = new HashMap<>();
            Map<String, String> testScripts = new HashMap<>();
            if (jangarooConfig.getTestSuite() != null && !match("glob:/" + targetPackageDir + "/joounit/**/*.ts", targetPackageDir + "/joounit").isEmpty()) {
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
            Map<String, String> dependencies = new HashMap<>();
            dependencies.put("@jangaroo/joo", "1.0.0");
            dependencies.putAll(globalLibraryConfiguration.getDependencies());
            additionalJsonEntries.setDependencies(dependencies);
            Map<String, String> devDependencies = new HashMap<>();
            devDependencies.put("@jangaroo/core", "^1.0.0");
            devDependencies.put("@jangaroo/build", "^1.0.0");
            devDependencies.put("@jangaroo/publish", "^1.0.0");
            devDependencies.putAll(testDependencies);
            devDependencies.put("rimraf", "^3.0.2");
            additionalJsonEntries.setDevDependencies(devDependencies);
            Map<String, String> scripts = new HashMap<>();
            scripts.put("clean", "rimraf ./dist && rimraf ./build");
            scripts.put("build", "jangaroo build");
            scripts.put("publish", "jangaroo-publish dist");
            List<String> typesPaths = new ArrayList<>();
            typesPaths.add("./src/*");
            Map<String, List> allMapping = new HashMap<>();
            allMapping.put("*", typesPaths);
            Map<String, Object> typesVersions = new HashMap<>();
            typesVersions.put("*", allMapping);
            additionalJsonEntries.setTypesVersions(typesVersions);

            List<String> ignoreFromSrcMain = new ArrayList<>();
            ignoreFromSrcMain.add("package.json");
            copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "packages",
                    String.format("%s__%s", mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId())).toString(),
                    "src", ignoreFromSrcMain, targetPackageDir
            );
          } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APP) {
            jangarooConfig.setType("app");
            Map<String, Object> commandMap = new HashMap<>();
            Map<String, String> runMap = new HashMap<>();
            runMap.put("proxyPathSpec", "/rest/");
            commandMap.put("run", runMap);
            jangarooConfig.setCommand(commandMap);
            jangarooConfig.setExtNamespace(jangarooMavenPluginConfiguration.getExtNamespace());
            jangarooConfig.setTheme(findPackageNameByReference(jangarooMavenPluginConfiguration.getTheme(), moduleMappings));
            jangarooConfig.setApplicationClass(jangarooMavenPluginConfiguration.getApplicationClass());
            jangarooConfig.setAdditionalLocales(jangarooMavenPluginConfiguration.getAdditionalLocales());
            GlobalLibraryConfiguration globalLibraryConfiguration = new GlobalLibraryConfiguration(mavenModule.getData());
            jangarooConfig.setGlobalLibraries(globalLibraryConfiguration.getGlobalLibraries());
            jangarooConfig.setAdditionalCssIncludeInBundle(jangarooMavenPluginConfiguration.getAdditionalCssIncludeInBundle());
            jangarooConfig.setAdditionalCssNonBundle(jangarooMavenPluginConfiguration.getAdditionalCssNonBundle());
            // todo: is this correct?
            jangarooConfig.setAdditionalJsIncludeInBundle(
                    jangarooMavenPluginConfiguration.getAdditionalJsIncludeInBundle().stream()
                            .filter(jsPath -> !globalLibraryConfiguration.getAdditionalJsPaths().contains(jsPath))
                            .collect(Collectors.toList()));
            jangarooConfig.setAdditionalJsNonBundle(
                    jangarooMavenPluginConfiguration.getAdditionalJsNonBundle().stream()
                            .filter(jsPath -> !globalLibraryConfiguration.getAdditionalJsPaths().contains(jsPath))
                            .collect(Collectors.toList()));
            if (new File(mavenModule.getDirectory().getPath() + "/app.json").exists()) {
              jangarooConfig.setSencha(objectMapper.readValue(FileUtils.readFileToString(new File(mavenModule.getDirectory().getPath() + "/app.json")), Map.class));
            }
            if (mavenModule.getData().getOrganization() != null) {
              additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
            }
            additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
            Map<String, String> dependencies = new HashMap<>();
            dependencies.put("@coremedia/sencha-ext", "7.2.0");
            dependencies.put("@coremedia/sencha-ext-classic", "7.2.0");
            dependencies.put("@coremedia/sencha-ext-classic-locale", "7.2.0");
            dependencies.put("@jangaroo/joo", "1.0.0");
            additionalJsonEntries.setDependencies(dependencies);
            Map<String, String> devDependencies = new HashMap<>();
            devDependencies.put("@jangaroo/core", "^1.0.0");
            devDependencies.put("@jangaroo/build", "^1.0.0");
            devDependencies.put("@jangaroo/run", "^1.0.0");
            devDependencies.put("rimraf", "^3.0.2");
            additionalJsonEntries.setDevDependencies(devDependencies);
            List<String> typesPaths = new ArrayList<>();
            typesPaths.add("./app/*");
            Map<String, List> allMapping = new HashMap<>();
            allMapping.put("*", typesPaths);
            Map<String, Object> typesVersions = new HashMap<>();
            typesVersions.put("*", allMapping);
            additionalJsonEntries.setTypesVersions(typesVersions);
            List<String> ignoreFromSrcMain = new ArrayList<>();
            ignoreFromSrcMain.add("app.json");
            copyCodeFromMaven(mavenModule.getDirectory().getPath(), Paths.get("target", "app",
                    String.format("{}__{}", mavenModule.getData().getGroupId(), mavenModule.getData().getArtifactId())).toString(),
                    "app", ignoreFromSrcMain, targetPackageDir
            );
          } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APP_OVERLAY) {
            jangarooConfig.setType("app-overlay");
            Map<String, Object> commandMap = new HashMap<>();
            Map<String, String> runMap = new HashMap<>();
            runMap.put("proxyPathSpec", "/rest/");
            commandMap.put("run", runMap);
            jangarooConfig.setCommand(commandMap);
            if (mavenModule.getData().getOrganization() != null) {
              additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
            }
            additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
            Map<String, String> devDependencies = new HashMap<>();
            devDependencies.put("@jangaroo/core", "^1.0.0");
            devDependencies.put("@jangaroo/build", "^1.0.0");
            devDependencies.put("@jangaroo/run", "^1.0.0");
            devDependencies.put("rimraf", "^3.0.2");
            additionalJsonEntries.setDevDependencies(devDependencies);
            Map<String, String> scripts = new HashMap<>();
            scripts.put("clean", "rimraf ./dist");
            scripts.put("build", "jangaroo build");
            scripts.put("start", "jangaroo run");
            additionalJsonEntries.setScripts(scripts);
          } else if (mavenModule.getModuleType() == ModuleType.JANGAROO_APPS) {
            jangarooConfig.setType("apps");
            Map<String, Object> commandMap = new HashMap<>();
            Map<String, String> runMap = new HashMap<>();
            runMap.put("proxyPathSpec", "/rest/");
            commandMap.put("run", runMap);
            jangarooConfig.setCommand(commandMap);
            if (jangarooMavenPluginConfiguration.getRootApp() != null && !jangarooMavenPluginConfiguration.getRootApp().isEmpty()) {
              jangarooConfig.setRootApp(findPackageNameByReference(jangarooMavenPluginConfiguration.getRootApp(), moduleMappings));
            }
            if (mavenModule.getData().getOrganization() != null) {
              additionalJsonEntries.setAuthor(mavenModule.getData().getOrganization().getName());
            }
            additionalJsonEntries.setDescription(mavenModule.getData().getDescription());
            Map<String, String> devDependencies = new HashMap<>();
            devDependencies.put("@jangaroo/core", "^1.0.0");
            devDependencies.put("@jangaroo/build", "^1.0.0");
            devDependencies.put("@jangaroo/run", "^1.0.0");
            devDependencies.put("rimraf", "^3.0.2");
            additionalJsonEntries.setDevDependencies(devDependencies);
            Map<String, String> scripts = new HashMap<>();
            scripts.put("clean", "rimraf ./dist");
            scripts.put("build", "jangaroo build");
            scripts.put("start", "jangaroo run");
            additionalJsonEntries.setScripts(scripts);
          }

          //todo: handle manifest paths
          objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
          String jangarooConfigDocument = "/** @type { import('@jangaroo/core').IJangarooConfig } */\nmodule.exports = ".concat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jangarooConfig));
          objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
          FileUtils.writeStringToFile(Paths.get(targetPackageDir, "jangaroo.config.js").toFile(), jangarooConfigDocument);
          if (jangarooConfig.getTheme() != null && !jangarooConfig.getTheme().isEmpty()) {
            Optional<Package> optionalThemeDependency = packageRegistry.stream()
                    .filter(somePackage -> somePackage.matches(jangarooConfig.getTheme(), null))
                    .findFirst();
            if (optionalThemeDependency.isPresent()) {
              additionalJsonEntries.getDependencies().put(optionalThemeDependency.get().getName(), optionalThemeDependency.get().getVersion());
            }
          }
          if (new File(targetPackageJson).exists()) {
            PackageJson packageJson = objectMapper.readValue(FileUtils.readFileToString(new File(targetPackageJson)), PackageJson.class);
            packageJson.getDependencies().forEach(additionalJsonEntries::addDependency);
            packageJson.getDevDependencies().forEach(additionalJsonEntries::addDevDependency);
            packageJson.getScripts().forEach(additionalJsonEntries::addScript);
            packageJson.getTypesVersions().forEach(additionalJsonEntries::addTypesVersion);
          }
          PackageJson packageJson = new PackageJson(additionalJsonEntries);
          packageJson.setName(aPackage.getName());
          packageJson.setVersion(aPackage.getVersion());
          packageJson.setLicense("MIT");
          packageJson.setPrivat(true);
          aPackage.getDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getVersion)).forEach(packageJson::addDependency);
          aPackage.getDevDependencies().stream().collect(Collectors.toMap(Package::getName, Package::getVersion)).forEach(packageJson::addDevDependency);
          FileUtils.write(new File(targetPackageJson), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson));
        }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void copyCodeFromMaven(String baseDirectory, String generatedExtModuleDirectory, String srcFolderName, List<String> ignoreFromSrcMainSencha, String targetPackageDir) throws IOException {
    // todo: deleted locale, is that ok?
    for (String dir : Arrays.asList(srcFolderName)) {
      Path sourceDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, dir);
      if (sourceDirPath.toFile().exists() && sourceDirPath.toFile().isDirectory()) {
        Path targetDirPath = Paths.get(targetPackageDir, "sencha", dir);
        FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> pathname.isDirectory() && pathname.getName().contains(".js"));
      }

      sourceDirPath = Paths.get(baseDirectory, generatedExtModuleDirectory, srcFolderName);
      if (sourceDirPath.toFile().exists() && sourceDirPath.toFile().isDirectory()) {
        Path targetDirPath = Paths.get(targetPackageDir, srcFolderName);
        FileUtils.copyDirectory(sourceDirPath.toFile(), targetDirPath.toFile(), pathname -> !pathname.getName().contains(".js"));
      }

      Path jooUnitSourcePath = Paths.get(baseDirectory, "target", "test-classes", srcFolderName);
      if (jooUnitSourcePath.toFile().exists() && jooUnitSourcePath.toFile().isDirectory()) {
        Path jooUnitTargetDirPath = Paths.get(targetPackageDir, "joounit");
        FileUtils.copyDirectory(jooUnitSourcePath.toFile(), jooUnitTargetDirPath.toFile(), pathname -> !pathname.getName().contains(".js"));
      }
      Path srcMainSenchaPath = Paths.get(baseDirectory, "src", "main", "sencha");
      Path targetSenchaPath = Paths.get(targetPackageDir, "sencha");
      if (srcMainSenchaPath.toFile().exists() && srcMainSenchaPath.toFile().isDirectory()) {
        FileUtils.copyDirectory(srcMainSenchaPath.toFile(), targetSenchaPath.toFile(),
                pathname -> ignoreFromSrcMainSencha.stream()
                        .map(ignore -> Paths.get(srcMainSenchaPath.toString(), ignore).toString())
                        .anyMatch(ignore -> !ignore.equals(pathname.getPath()))
        );
      }
    }
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

  private String getRootPackageJson(List<String> workspaces) throws JsonProcessingException {
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootPackageJson.readPackageJson());
  }

  private Optional<Package> getOrCreatePackage(List<Package> packageRegistry, String packageName, String packageVersion, Map<String, MavenModule> moduleMappings) {
    Optional<Package> matchingPackage = packageRegistry.stream()
            .filter(aPackage -> aPackage.matches(packageName, packageVersion))
            .findFirst();
    if (matchingPackage.isPresent()) {
      return matchingPackage;
    } else {
      String newPackageVersion = null;
      List<Package> newDependencies = new ArrayList<>();
      List<Package> newDevDependencies = new ArrayList<>();

      MavenModule module = moduleMappings.get(packageName);
      if (module == null) {
        logger.error("could not find module {}", packageName);
        return Optional.empty();
      }
      if (module.getModuleType() == ModuleType.IGNORE) {
        return Optional.empty();
      } else {
        newPackageVersion = isValidVersion(module.getVersion()) ? module.getVersion() : "1.0.0";
        List<Dependency> dependencies = module.getData().getDependencies().stream()
                .filter(dependency -> !"test".equals(dependency.getScope()))
                .map(dependency -> {
                  if ("${project.groupId}".equals(dependency.getGroupId())) {
                    dependency.setGroupId(module.getData().getGroupId());
                  }
                  if ("${project.version}".equals(dependency.getVersion())) {
                    dependency.setVersion(module.getVersion());
                  }
                  return dependency;
                })
                .collect(Collectors.toList());

        //dependencies
        //        .forEach(dependency -> addToDependencies(dependency, newDependencies, moduleMappings, packageRegistry));

        List<Dependency> testDependencies = module.getData().getDependencies().stream()
                .filter(dependency -> "test".equals(dependency.getScope()))
                .map(dependency -> {
                  if ("$(project.groupid)".equals(dependency.getGroupId())) {
                    dependency.setGroupId(module.getData().getGroupId());
                  }
                  if ("${project.version}".equals(dependency.getVersion())) {
                    dependency.setVersion(module.getVersion());
                  }
                  return dependency;
                })
                .collect(Collectors.toList());

        //testDependencies
        //        .forEach(dependency -> addToDependencies(dependency, newDevDependencies, moduleMappings, packageRegistry));

        for (Dependency dependency : dependencies) {
          Package createdPackage = new Package(calculateMavenName(dependency), isValidVersion(dependency.getVersion()) ? dependency.getVersion() : "1.0.0");
            if (Arrays.asList("swc", "jar").contains(dependency.getType())) {
              newDependencies.add(createdPackage);
            } else {
              newDependencies.addAll(createdPackage.getDependencies());
            }
        }
        for (Dependency dependency : testDependencies) {
          Package createdPackage = new Package(calculateMavenName(dependency), isValidVersion(dependency.getVersion()) ? dependency.getVersion() : "1.0.0");
            if (Arrays.asList("swc", "jar").contains(dependency.getType())) {
              newDevDependencies.add(createdPackage);
            } else {
              newDevDependencies.addAll(createdPackage.getDependencies());
            }
        }
      }
      Package newPackage = new Package(packageName, newPackageVersion, newDependencies, newDevDependencies);
      packageRegistry.add(newPackage);
      return Optional.of(newPackage);
    }
  }


  private Package getDependency(Dependency dependency) {
    return new Package(calculateMavenName(dependency), isValidVersion(dependency.getVersion()) ? dependency.getVersion() : "1.0.0");
  }

  private void copyStaticPackages() {
  }

  public String findPackageNameByReference(String reference, Map<String, MavenModule> moduleMappings) {
    Optional<String> packageName;
    if (reference == null) {
      System.out.println("sadasd");
    }
    String[] splitName = reference.split(":");
    if (splitName.length == 2 && splitName[0] != null && splitName[1] != null) {
      packageName = moduleMappings.entrySet().stream()
              .filter(moduleEntry -> hasCorrectModuleType(moduleEntry.getValue().getModuleType()))
              .filter(moduleEntry -> splitName[0].equals(((MavenModule) moduleEntry.getValue()).getData().getGroupId()))
              .filter(moduleEntry -> splitName[1].equals(((MavenModule) moduleEntry.getValue()).getData().getArtifactId()))
              .map(Map.Entry::getKey)
              .findFirst();
    } else {
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
      logger.error("Could not resolve reference " + reference + ". No suitable module was found.");
      return null;
    }
    return packageName.get();
  }

  private boolean hasCorrectModuleType(ModuleType moduleType) {
    List<ModuleType> validModuleTypes = Arrays.asList(ModuleType.SWC, ModuleType.JANGAROO_APP, ModuleType.JANGAROO_APP_OVERLAY, ModuleType.JANGAROO_APPS);
    return validModuleTypes.contains(moduleType);
  }


  private Map<String, MavenModule> loadMavenModule(String modulePath) {
    Map<String, MavenModule> modules = new HashMap<>();
    modules.put(calculateMavenName(getProject().getModel()), new MavenModule(modulePath, getProject().getModel()));
    return modules;
  }

  /*
  private Map<String, MavenModule> loadMavenModules(String basePath) {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Map<String, MavenModule> modules = new HashMap<>();
    try {
      Model model = reader.read(new FileReader(basePath + "/pom.xml"));

      if (model.getGroupId() == null) {
        model.setGroupId(model.getParent().getGroupId());
      }
      if (model.getVersion() == null) {
        model.setVersion(model.getParent().getVersion());
      }
      List<String> childModules = model.getModules();
      model.getProfiles().stream()
              .filter(profile -> isProfileActive(profile.getId()))
              .flatMap(profile -> profile.getModules().stream())
              .forEach(childModules::add);
      if (!childModules.isEmpty()) {
        for (String moduleName : childModules) {
          modules.putAll(loadMavenModules(basePath + "/" + moduleName));
        }
      }
      modules.put(calculateMavenName(model), new MavenModule(basePath, model));
    } catch (IOException | XmlPullParserException e) {
      logger.debug(String.format("pom does not exist in directory %s", basePath));
    }
    return modules;
  }
   */

  /*
  private boolean isProfileActive(String profileId) {
    return true;
    // todo: use this
    //return activeProfiles.contains(profileId);
  }
   */

  private String calculateMavenName(Model model) {
    if ("com.coremedia.sencha".equals(model.getGroupId()) && "ext-js-pkg".equals(model.getArtifactId()) ||
    "net.jangaroo.com.sencha".equals(model.getGroupId()) && "ext-js-pkg-gpl".equals(model.getArtifactId())) {
      return "ext";
    } else {
      String groupId = model.getGroupId();
      if (groupId == null) {
        groupId = model.getParent().getGroupId();
      }
      return groupId + "__" + model.getArtifactId();
    }
  }

  private String calculateMavenName(Dependency dependency) {
    if ("com.coremedia.sencha".equals(dependency.getGroupId()) && "ext-js-pkg".equals(dependency.getArtifactId())) {
      return "ext";
    } else {
      return dependency.getGroupId() + "__" + dependency.getArtifactId();
    }

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
}
