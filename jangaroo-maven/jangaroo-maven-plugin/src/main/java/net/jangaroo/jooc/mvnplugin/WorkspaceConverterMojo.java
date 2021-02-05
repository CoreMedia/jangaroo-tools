package net.jangaroo.jooc.mvnplugin;

import com.google.gson.Gson;
import net.jangaroo.jooc.mvnplugin.converter.ExtModule;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.Module;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import net.jangaroo.jooc.mvnplugin.converter.PackageJsonData;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "workspaceConverter",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = false) // check for threadsafety and make it threadsafe
public class WorkspaceConverterMojo extends AbstractMojo {
  private static final Logger logger = LoggerFactory.getLogger(WorkspaceConverterMojo.class);

  @Parameter(property = "studio.npm.maven.root")
  private String studioNpmMavenRoot = "/home/fwellers/dev/cms/apps/studio-client";


  @Parameter(property = "studio.npm.remote.packages")
  private String studioNpmRemotePackages = "/home/fwellers/dev/cms/.remote-packages";

  @Parameter(property = "studio.npm.target")
  private String studioNpmTarget = "../created_stuff";

  @Parameter(property = "sudio.app.package.name")
  private String appPackageName = "";

  private Gson gson = new Gson();

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

    Map<String, Module> moduleMappings = loadMavenModules(studioNpmMavenRoot);
    moduleMappings.putAll(loadExtModules(studioNpmRemotePackages));
    getOrCreatePackage(packageRegistry, appPackageName, null, moduleMappings);
  }

  private Optional<Package> getOrCreatePackage(List<Package> packageRegistry, String packageName, String packageVersion, Map<String, Module> moduleMappings) {
    Optional<Package> matchingPackage = packageRegistry.stream()
            .filter(aPackage -> aPackage.matches(packageName, packageVersion))
            .findFirst();
    if (matchingPackage.isPresent()) {
      return matchingPackage;
    } else {
      String newPackageName = null;
      String newPackageVersion = null;
      List<Package> newDependencies = new ArrayList<>();
      List<Package> newDevDependencies = new ArrayList<>();

      Module module = moduleMappings.get(packageName);
      if (module == null) {
        logger.error("could not find module {}", packageName);
        return Optional.empty();
      }
      switch (module.getModuleType()) {
        case IGNORE:
          return Optional.empty();
        case EXT_PKG:
          newPackageVersion = calculatePackageVersionFromExtModuleVersion(module.getVersion());

      }
      return Optional.of(new Package(newPackageName, newPackageVersion, newDependencies, newDevDependencies));
    }
  }

  private void copyStaticPackages() {
  }

  public String fundPackageNameByReference(String reference, Map<String, Map> moduleMappings) {
    String packageName = null;
    String[] split = reference.split(":");
    if (split.length == 2 & split[0] != null && split[1] != null) {

    } else {

    }
    return packageName;
  }


  private Map<String, Module> loadMavenModules(String basePath) {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Map<String, Module> modules = new HashMap<>();
    try {
      Model model = reader.read(new FileReader(basePath + "/pom.xml"));
      List<String> childModules = model.getModules();
      model.getProfiles().stream()
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

  private String calculateMavenName(Model model) {
    if ("com.coremedia.sencha".equals(model.getGroupId()) && "ext-js-pkg".equals(model.getArtifactId())) {
      return "ext";
    } else {
      String groupId = model.getGroupId();
      if (groupId == null) {
        groupId = model.getParent().getGroupId();
      }
      return groupId + "__" + model.getArtifactId();
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

  private Map<String, Module> loadExtModules(String basePath) {
    List<String> filePaths = match("glob:**package.json", basePath);
    Map<String, Module> moduleMappings = new HashMap<>();
    for (String filePath : filePaths) {
      ModuleType moduleType = ModuleType.EXT_PKG;
      Optional<PackageJsonData> packageJson = readPackageJson(filePath);
      if (packageJson.isPresent()) {
        if (ignorePackage(packageJson.get().getName())) {
          moduleType = ModuleType.IGNORE;
        }
        Map<String, Module> additionalPackages = new HashMap<>();
        if ("ext".equals(packageJson.get().getName())) {
          Stream.of("classic/classic", "classic/theme-triton", "packages/charts")
                  .forEach(subdirectory -> {
                    String additionalExtPkgFile = filePath.replace("/package.json", subdirectory.concat("/package.json"));
                    Optional<PackageJsonData> additionalJsonData = readPackageJson(additionalExtPkgFile);
                    additionalJsonData.ifPresent(packageJsonData -> additionalPackages.put(calculatePackageNameFromExtModuleName(packageJsonData.getName()),
                            new ExtModule(ModuleType.IGNORE, new File(additionalExtPkgFile), (PackageJsonData) packageJsonData.getSencha())));
                  });
        }
        moduleMappings.put(calculatePackageNameFromExtModuleName(packageJson.get().getName()),
                new ExtModule(moduleType, new File(filePath), null));
      }
    }
    return moduleMappings;
  }

  public Optional<PackageJsonData> readPackageJson(String filePath) {
    PackageJsonData packageJsonData = null;
    try (FileReader fileReader = new FileReader(filePath)) {
      packageJsonData = gson.fromJson(fileReader, PackageJsonData.class);
    } catch (IOException e) {
      logger.debug(String.format("package.json oes not exist in %s", filePath));
    }
    return Optional.ofNullable(packageJsonData);
  }

  private boolean ignorePackage(String packageName) {
    String[] ingoreNames = {"net.jangaroo__jangaroo-browser",
            "net.jangaroo__ext-as",
            "net.jangaroo__jangaroo-net",
            "net.jangaroo__jangaroo-runtime",
            "net.jangaroo__jooflash-core",
            "net.jangaroo__jooflexframework",
            "net.jangaroo__joounit"};
    return Arrays.asList(ingoreNames).contains(packageName);
  }

  private String calculatePackageNameFromExtModuleName(String name) {
    if ("ext".equals(name)) {
      return "@coremedia/sencha-ext";
    }
    if ("ext-classic".equals(name)) {
      return "@coremedia/sencha-ext-classic";
    }
    if ("charts".equals(name)) {
      return "@coremedia/sencha-ext-charts";
    }
    if (name.startsWith("theme-")) {
      return "@coremedia/sencha-ext-classic-" + name;
    }
    if (name.startsWith("net.jangaroo__")) {
      String suffix = name.replace("net.jangaroo__", "");
      if ("jangaroo-net".equals(suffix)) {
        return "@jangaroo/jangaroo-net";
      }
      if ("jangaroo-runtime".equals(suffix)) {
        return "@jangaroo/joo";
      }
      if ("jooflash-core".equals(suffix)) {
        return "@jangaroo/jooflash-core";
      }
      if ("jooflexframework".equals(suffix)) {
        return "@jangaroo/jooflexframework";
      }
      if ("joounit".equals(suffix)) {
        return "@jangaroo/joounit";
      }
      if ("ext-as".equals(suffix)) {
        return "@jangaroo/ext-ts";
      }
    }
    return name;
  }

  private String calculatePackageVersionFromExtModuleVersion(String version) {
    return isValidVersion(version) ? version : "7.2.0";
  }

  private boolean isValidVersion(String version) {
    //todo: implement this
    return true;
  }
}
