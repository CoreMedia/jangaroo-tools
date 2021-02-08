package net.jangaroo.jooc.mvnplugin;

import com.google.gson.Gson;
import net.jangaroo.jooc.mvnplugin.converter.MavenModule;
import net.jangaroo.jooc.mvnplugin.converter.Module;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import org.apache.maven.model.Dependency;
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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


  @Parameter(property = "studio.npm.target")
  private String studioNpmTarget = "../created_stuff";

  @Parameter(property = "sudio.app.package.name")
  private String appPackageName = "com.coremedia.blueprint__studio-resources";

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

    Map<String, MavenModule> moduleMappings = loadMavenModules(studioNpmMavenRoot);
    getOrCreatePackage(packageRegistry, findPackageNameByReference(appPackageName, moduleMappings), null, moduleMappings);
    System.out.println("test");
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
      switch (module.getModuleType()) {
        case IGNORE:
          return Optional.empty();
        default:
          newPackageVersion = isValidVersion(module.getVersion()) ? module.getVersion() : "1.0.0";
          module.getData().getDependencies().stream()
                  .filter(dependency -> !"test".equals(dependency.getScope()))
                  .forEach(dependency -> addToDependencies(dependency, newDependencies, moduleMappings, packageRegistry));
          module.getData().getDependencies().stream()
                  .filter(dependency -> "test".equals(dependency.getScope()))
                  .forEach(dependency -> addToDependencies(dependency, newDevDependencies, moduleMappings, packageRegistry));
      }
      Package newPackage = new Package(packageName, newPackageVersion, newDependencies, newDevDependencies);
      packageRegistry.add(newPackage);
      return Optional.of(newPackage);
    }
  }

  private void addToDependencies(Dependency dependency, List<Package> dependencies, Map<String, MavenModule> moduleMappings, List<Package> packageRegistry) {
    String internalPackageName = findPackageNameByReference(calculateMavenName(dependency), moduleMappings);
    Optional<Package> optionalPackage = getOrCreatePackage(packageRegistry, internalPackageName, dependency.getVersion(), moduleMappings);
    if (optionalPackage.isPresent()) {
      if (Arrays.asList("swc", "jar").contains(dependency.getType())) {
        dependencies.add(optionalPackage.get());
      } else {
        dependencies.addAll(optionalPackage.get().getDependencies());
      }
    }
  }

  private void copyStaticPackages() {
  }

  public String findPackageNameByReference(String reference, Map<String, MavenModule> moduleMappings) {
    Optional<String> packageName;
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
      logger.error("Could not resolve reference ${ref}. No suitable module was found.");
      return null;
    }
    return packageName.get();
  }

  private boolean hasCorrectModuleType(ModuleType moduleType) {
    List<ModuleType> validModuleTypes = Arrays.asList(ModuleType.SWC, ModuleType.JANGAROO_APP, ModuleType.JANGAROO_APP_OVERLAY, ModuleType.JANGAROO_APPS);
    return validModuleTypes.contains(moduleType);
  }


  private Map<String, MavenModule> loadMavenModules(String basePath) {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Map<String, MavenModule> modules = new HashMap<>();
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

  private String calculateMavenName(Dependency dependency) {
    if ("com.coremedia.sencha".equals(dependency.getGroupId()) && "ext-js-pkg".equals(dependency.getArtifactId())) {
      return "ext";
    } else {
      return dependency.getGroupId() + "__" + dependency.getArtifactId();
    }

  }


  private boolean isValidVersion(String version) {
    //todo: implement this
    return true;
  }
}
