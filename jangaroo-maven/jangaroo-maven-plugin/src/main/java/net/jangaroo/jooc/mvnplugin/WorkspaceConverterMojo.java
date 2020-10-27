package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.converter.Module;
import net.jangaroo.jooc.mvnplugin.converter.Package;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "compile",
        defaultPhase = LifecyclePhase.COMPILE,
        threadSafe = false) // check for threadsafety and make it threadsafe
public class WorkspaceConverterMojo extends AbstractMojo {

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


    List<Module> modules = loadMavenModules(studioNpmMavenRoot);
    modules.addAll(loadExtModules(studioNpmRemotePackages));
    modules.forEach(module -> {
      try {
        FileUtils.copyDirectory(new File(module.getDirectory().getPath()+"/target/packages"), new File(studioNpmTarget));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    System.out.println("test");
  }

  private List<Module> loadMavenModules(String basePath) {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    List<Module> modules = new ArrayList<>();
    try {
      Model model = reader.read(new FileReader(basePath + "/pom.xml"));
      List<String> childModules = model.getModules();
      if (childModules.size() > 0) {
        for (String moduleName : childModules) {
          modules.addAll(loadMavenModules(basePath + "/" + moduleName));
        }
      } else {
        modules.add(new Module(basePath, model));
      }
    } catch (IOException | XmlPullParserException e) {
      e.printStackTrace();
    }
    return modules;
  }

  private List<Module> loadExtModules(String basePath) {

    return new ArrayList<>();
  }
}
