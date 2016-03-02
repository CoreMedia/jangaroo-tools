package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaApplicationConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class SenchaAppHelper extends AbstractSenchaHelper {

  private final Configurer[] appConfigurers;
  private final String senchaPath;

  public SenchaAppHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    String buildDirectory = project.getBuild().getDirectory();
    this.senchaPath = buildDirectory + File.separator + getSenchaModuleName();

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project, senchaConfiguration);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(project, senchaConfiguration);
    PathConfigurer pathConfigurer = new PathConfigurer(project, senchaConfiguration);

    this.appConfigurers = new Configurer[] {
            DefaultSenchaApplicationConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
  }

  @Override
  public void createModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File workingDirectory = new File(senchaPath);

      if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
        throw new MojoExecutionException("could not create working directory");
      }

      File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_CONFIG);
      // make sure senchaCfg does not exist
      if (senchaCfg.exists()) {
        if (!senchaCfg.delete()) {
          throw new MojoExecutionException("could not delete " + SenchaUtils.SENCHA_APP_CONFIG + " for app");
        }
      }

      String themePackageName = SenchaUtils.getSenchaPackageNameForTheme(getSenchaConfiguration().getTheme(), getProject());
      String line = "sencha generate app"
              + " -ext"
              + " -" + getSenchaConfiguration().getToolkit()
              + " --theme-name=\"" + themePackageName + "\""
              + " --path=\"\""
              + " " + getSenchaModuleName();
      CommandLine cmdLine = CommandLine.parse(line);
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(workingDirectory);
      executor.setExitValue(0);
      try {
        executor.execute(cmdLine);
      } catch (IOException e) {
        throw new MojoExecutionException("could not execute sencha cmd to generate app", e);
      }

      // sencha.cfg should be recreated
      // for normal packages skip generating css and slices
      if (senchaCfg.exists()) {
        PrintWriter pw = null;
        try {
          FileWriter fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);

          pw = new PrintWriter(fw);
          // If true will cause problems with class pre- and postprocessors we use
          pw.println("app.output.js.optimize.defines=false");
        } catch (IOException e) {
          throw new MojoExecutionException("could disable derive and minifying in sencha config of app");
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
  public void prepareModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File senchaDirectory = new File(senchaPath);

      if (!senchaDirectory.exists()) {
        getLog().info("generating sencha into: " + senchaDirectory.getPath());
        getLog().debug("created " + senchaDirectory.mkdirs());
      }

      copyFiles(senchaPath);

      File workingDirectory = new File(senchaPath);

      writeAppJson(workingDirectory);
    }
  }

  @Override
  public void packageModule(JarArchiver archiver) throws MojoExecutionException {
    // TODO
  }

  @Override
  public void deleteModule() throws MojoExecutionException {
    // TODO
  }

  private void writeAppJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> appConfig = getAppConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, appConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_APP_FILENAME, e);
    }

  }

  private void buildSenchaApp(File senchaAppDirectory) throws MojoExecutionException {
    String line = "sencha build app";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(senchaAppDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to build app", e);
    }
  }

  private Map<String, Object> getAppConfig() throws MojoExecutionException {
    return getConfig(appConfigurers);
  }
}
