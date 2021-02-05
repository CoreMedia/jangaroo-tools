package net.jangaroo.jooc.mvnplugin;

import com.google.gson.Gson;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.Module;
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

@Mojo(name = "workspaceConverter",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = false) // check for threadsafety and make it threadsafe
public class WorkspaceConverterMojo extends AbstractMojo {
  private static final Logger logger = LoggerFactory.getLogger(WorkspaceConverterMojo.class);

  @Parameter(property = "studio.npm.maven.root")
  private String studioNpmMavenRoot = "/home/fwellers/dev/cms/apps/studio-client";


  @Parameter(property = "studio.npm.target")
  private String studioNpmTarget = "../created_stuff";

  @Parameter(property = "sudio.app.package.name")
  private String appPackageName = "";

  @Parameter(property = "activeProfiles", defaultValue = "${session.request.activeProfiles}")
  protected List<String> activeProfiles;

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
    getOrCreatePackage(packageRegistry, appPackageName, null, moduleMappings);
    System.out.println("test");
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
        default:
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

  private boolean isProfileActive(String profileId) {
    return true;
    // todo: use this
    //return activeProfiles.contains(profileId);
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

  private boolean isValidVersion(String version) {
    //todo: implement this
    return true;
  }
}
