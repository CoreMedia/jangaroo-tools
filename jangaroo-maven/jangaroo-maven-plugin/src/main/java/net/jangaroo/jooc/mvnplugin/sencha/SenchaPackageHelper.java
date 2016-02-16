package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaPackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class SenchaPackageHelper extends AbstractSenchaHelper {

  private final Configurer[] packageConfigurers;
  private final String senchaPath;
  private final String senchaPackagePath;

  public SenchaPackageHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    String buildDirectory = project.getBuild().getDirectory();
    this.senchaPath = buildDirectory + File.separator + SenchaUtils.SENCHA_BASE_PATH;

    this.senchaPackagePath = senchaPath + File.separator + "packages" + File.separator + getSenchaModuleName();

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(senchaConfiguration);
    PathConfigurer pathConfigurer = new PathConfigurer(senchaConfiguration);

    this.packageConfigurers = new Configurer[] {
            DefaultSenchaPackageConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File senchaPackageDirectory = new File(senchaPackagePath);

      if (!senchaPackageDirectory.exists()) {
        getLog().info("generating sencha package into: " + senchaPackageDirectory.getPath());
        getLog().debug("created " + senchaPackageDirectory.mkdirs());
      }

      copyFilesFromJoo(senchaPackagePath);

      File workingDirectory = new File(senchaPackagePath);

      writePackageJson(workingDirectory);
    }
  }

  @Override
  public void generateModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File workingDirectory = new File(senchaPackagePath);

      String pathToWorkingDirectory = getPathToWorkingDir(workingDirectory);

      File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_DIRECTORYNAME + "/package/sencha.cfg");
      // make sure senchaCfg does not exist
      if (senchaCfg.exists()) {
        if (!senchaCfg.delete()) {
          throw new MojoExecutionException("could not delete sencha.cfg for package");
        }
      }

      String line = "sencha generate package"
              + " --name=\"" + getSenchaModuleName() + "\""
              + " --namespace=\"\""
              + " --type=\"code\""
              + " " + pathToWorkingDirectory;
      CommandLine cmdLine = CommandLine.parse(line);
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(workingDirectory);
      executor.setExitValue(0);
      try {
        executor.execute(cmdLine);
      } catch (IOException e) {
        throw new MojoExecutionException("could not execute sencha cmd to generate package", e);
      }

      // sencha.cfg should be recreated
      // for normal packages skip generating css and slices
      if (senchaCfg.exists()) {
        PrintWriter pw = null;
        try {
          FileWriter fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);
          pw = new PrintWriter(fw);
          pw.println("skip.sass=1");
          pw.println("skip.slice=1");
        } catch (IOException e) {
          throw new MojoExecutionException("could not append skip.sass and skip.slice to sencha config of package");
        } finally {
          if (null != pw) {
            pw.close();
          }
        }
      } else {
        throw new MojoExecutionException("could not find sencha.cfg of package");
      }
    }
  }

  @Override
  public void packageModule(JarArchiver archiver) throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File senchaPackageDirectory = new File(senchaPackagePath);

      if (!senchaPackageDirectory.exists()) {
        throw new MojoExecutionException("sencha package directory does not exist: " + senchaPackageDirectory.getPath());
      }

      buildSenchaPackage(senchaPackageDirectory); // move to packageSenchaModule

      File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(getProject().getFile().getParentFile());

      if (null == workspaceDir) {
        throw new MojoExecutionException("could not find sencha workspace directory");
      }

      // Read workspace.json
      String workspaceOutputPath = workspaceDir.getAbsolutePath() + File.separator + getSenchaConfiguration().getBuildDir();
      try {
        @SuppressWarnings("unchecked") Map<String, Object> workspaceConfig = (Map<String, Object>) SenchaUtils.getObjectMapper().readValue(new File(workspaceDir.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_WORKSPACE_FILENAME), Map.class);

        // check if custom workspace dir has been set
        Object build = workspaceConfig.get(PathConfigurer.BUILD);
        if (build instanceof Map) {
          build = ((Map) build).get(PathConfigurer.DIR);
        }
        if (build instanceof String) {
          workspaceOutputPath = ((String) build).replace(SenchaUtils.PLACEHOLDERS.get(SenchaConfiguration.Type.WORKSPACE), workspaceDir.getAbsolutePath());
        }
      } catch (IOException e) {
        throw new MojoExecutionException("could not read " + SenchaUtils.SENCHA_WORKSPACE_FILENAME, e);
      }

      File pkg = new File(workspaceOutputPath + File.separator + getSenchaModuleName() + File.separator + getSenchaModuleName() + ".pkg");
      if (!pkg.exists()) {
        throw new MojoExecutionException("could not find pkg for sencha package " + getSenchaModuleName());
      }
      try {
        archiver.addFile(pkg, pkg.getName());
      } catch (ArchiverException e) {
        throw new MojoExecutionException("could not add pkg file to maven artifact", e);
      }
    }
  }

  private void writePackageJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> packageConfig = getPackageConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, packageConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_PACKAGE_FILENAME, e);
    }

  }

  private void buildSenchaPackage(File senchaPackageDirectory) throws MojoExecutionException {
    String line = "sencha package build";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(senchaPackageDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to build package", e);
    }
  }

  private Map<String, Object> getPackageConfig() throws MojoExecutionException {
    return getConfig(packageConfigurers);
  }
}
