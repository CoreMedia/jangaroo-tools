package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.AppConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaWorkspaceConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PackagesConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class SenchaWorkspaceHelper extends AbstractSenchaHelper {

  private final Configurer[] workspaceConfigurers;
  private final String senchaWorkspacePath;

  public SenchaWorkspaceHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    this.senchaWorkspacePath = getProject().getBasedir().getAbsolutePath();

    PackagesConfigurer packagesConfigurer = new PackagesConfigurer(project, senchaConfiguration);
    AppConfigurer appConfigurer = new AppConfigurer(project);

    this.workspaceConfigurers = new Configurer[]{
            DefaultSenchaWorkspaceConfigurer.getInstance(),
            packagesConfigurer,
            appConfigurer
    };
  }

  @Override
  public void createModule() throws MojoExecutionException {
    File workingDirectory;
    try {
      workingDirectory = getProject().getBasedir().getCanonicalFile();
    } catch (IOException e) {
      throw new MojoExecutionException("could not determine project base directory", e);
    }

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("could not create working directory");
    }

    if (null == SenchaUtils.findClosestSenchaWorkspaceDir(workingDirectory.getParentFile())) {

      File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_WORKSPACE_CONFIG);
      // make sure senchaCfg does not exist
      if (senchaCfg.exists()) {
        if (!senchaCfg.delete()) {
          throw new MojoExecutionException("could not delete " + SenchaUtils.SENCHA_WORKSPACE_CONFIG + " for workspace");
        }
      }

        getLog().info("generating sencha workspace module");
        SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, "generate workspace .", getLog());
        senchaCmdExecutor.execute();

      // sencha.cfg should be recreated
      // for normal packages skip generating css and slices
      if (senchaCfg.exists()) {
        PrintWriter pw = null;
        try {
          FileWriter fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);
          pw = new PrintWriter(fw);
          pw.println("ext.dir=" + SenchaUtils.generateAbsolutePathUsingPlaceholder(Type.WORKSPACE, getSenchaConfiguration().getExtFrameworkDir()));
        } catch (IOException e) {
          throw new MojoExecutionException("could not append ext framework dir to sencha config of workspace");
        } finally {
          if (null != pw) {
            pw.close();
          }
        }
      } else {
        throw new MojoExecutionException("could not find sencha.cfg of workspace");
      }
    } else {
      getLog().info("Skipping generate workspace because there already is a workspace in the directory hierarchy");
    }
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    File workingDirectory;
    workingDirectory = new File(senchaWorkspacePath);

    if (!workingDirectory.exists()) {
      getLog().info("generating sencha package into: " + workingDirectory.getPath());
      getLog().debug("created " + workingDirectory.mkdirs());
    }

    if (null == SenchaUtils.findClosestSenchaWorkspaceDir(workingDirectory.getParentFile())) {
      writeWorkspaceJson(workingDirectory);
    } else {
      getLog().info("Skipping preparation of workspace because there already is a workspace in the directory hierarchy");
    }
  }

  @Override
  public File packageModule() throws MojoExecutionException {
    // nothing to do
    return null;
  }


  private void writeWorkspaceJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> workspaceConfig = getWorkspaceConfig();

    File fWorkspaceJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_WORKSPACE_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fWorkspaceJson, workspaceConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_WORKSPACE_FILENAME, e);
    }
  }

  private Map<String, Object> getWorkspaceConfig() throws MojoExecutionException {
    return getConfig(workspaceConfigurers);
  }
}
