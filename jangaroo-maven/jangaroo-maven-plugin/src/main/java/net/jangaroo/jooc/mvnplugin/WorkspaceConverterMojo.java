package net.jangaroo.jooc.mvnplugin;

import com.google.gson.Gson;
import net.jangaroo.jooc.mvnplugin.converter.Module;
import net.jangaroo.jooc.mvnplugin.converter.ModuleType;
import net.jangaroo.jooc.mvnplugin.converter.Package;
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

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    List<Package> packageRegistry = new ArrayList<>();

    packageRegistry.add(new Package("@coremedia/sencha-ext-charts", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext-classic", "7.2.0"));
    packageRegistry.add(new Package("@coremedia/sencha-ext-classic-theme-triton", "7.2.0"));
    // this module will be ignored
    packageRegistry.add(new Package("@coremedia/com.coremedia.sencha__ext-js-pkg", "7.2.0"));


    Map<String, Module> modules = loadMavenModules(studioNpmMavenRoot);
    modules.putAll(loadExtModules(studioNpmRemotePackages));
    modules.forEach((key, value) -> {
      System.out.println("module " + key + " found!");
      /*
      try {
        FileUtils.copyDirectory(new File(value.getDirectory().getPath() + "/target/packages"), new File(studioNpmTarget));
      } catch (IOException e) {
        e.printStackTrace();
      }
       */
    });
  }

  private void getOrCreatePackge(List<Package> packageRegistry) {

  }

  private void copyStaticPackages() {
  }

  private Map<String, Module> loadMavenModules(String basePath) {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Map<String, Module> modules = new HashMap<>();
    try {
      Model model = reader.read(new FileReader(basePath + "/pom.xml"));
      List<String> childModules = model.getModules();
      if (!childModules.isEmpty()) {
        for (String moduleName : childModules) {
          modules.putAll(loadMavenModules(basePath + "/" + moduleName));
        }
      } else {
        modules.put(model.getGroupId()+ "__" + model.getArtifactId(), new Module(basePath, model));
      }
    } catch (IOException | XmlPullParserException e) {
      logger.debug(String.format("pom does not exist in directory %s", basePath));
    }
    return modules;
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
                    //todo: properly implement this
                    String additionalExtPkgFile = filePath.replace("/package.json", subdirectory.concat("/package.json"));
                    Optional<PackageJsonData> additionalJsonData = readPackageJson(additionalExtPkgFile);
                    additionalJsonData.ifPresent(packageJsonData -> additionalPackages.put(calculatePackageNameFromExtModuleName(packageJsonData.getName()),
                            new Module(ModuleType.IGNORE, new File(additionalExtPkgFile), packageJsonData.getSencha())));
                  });
        }
        moduleMappings.put(calculatePackageNameFromExtModuleName(packageJson.get().getName()),
                new Module(moduleType, new File(filePath), null));
      }
    }
    return moduleMappings;
  }

  public Optional<PackageJsonData> readPackageJson(String filePath) {
    PackageJsonData packageJsonData = null;
    try (FileReader fileReader = new FileReader(filePath)) {
      Gson gson = new Gson();
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


  private class PackageJsonData {
    private String name;
    private Object sencha;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Object getSencha() {
      return sencha;
    }

    public void setSencha(Object sencha) {
      this.sencha = sencha;
    }
  }
}
