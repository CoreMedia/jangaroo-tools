package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "generate-app", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class SenchaGenerateAppMojo extends AbstractSenchaMojo {

  private static final String SENCHA_APP_CLASS_PATH = "/app";

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Override
  public String getType() {
    return Type.APP;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app\"");
    }
    createModule();
  }

  public void createModule() throws MojoExecutionException {
    File workingDirectory = new File(project.getBuild().getDirectory() + SenchaUtils.APP_TARGET_DIRECTORY);

    if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
      throw new MojoExecutionException("Could not create working directory.");
    }

    File senchaCfg = new File(workingDirectory, SenchaUtils.SENCHA_APP_CONFIG);
    // only generate app if senchaCfg does not exist
    if (senchaCfg.exists()) {
      return;
    }

    String senchaAppName = getSenchaPackageName(project.getGroupId(), project.getArtifactId());
    String arguments = "generate app"
            + " -ext"
            + " -" + getToolkit()
            + " --path=\"\""
            + " " + senchaAppName;
    getLog().info("Generating Sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog());
    senchaCmdExecutor.execute();

    // remove example resources from application's classpath directory
    File appDir = new File(workingDirectory.getAbsolutePath() + SENCHA_APP_CLASS_PATH);
    try {
      FileUtils.cleanDirectory(appDir);
    } catch (IOException e) {
      throw new MojoExecutionException("Could not delete example app directory: " + appDir, e);
    }

    // sencha.cfg should have been recreated.
    if (!senchaCfg.exists()) {
      throw new MojoExecutionException("Could not find sencha.cfg of app");
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(senchaCfg.getAbsoluteFile(), true))) {
      // For apps, skip generating slices.
      pw.println("skip.slice=1");
      // If true will cause problems with class pre- and post-processors we use.
      pw.println("app.output.js.optimize.defines=false");
      // If 0.99 (default), some deprecated API will not be available in production build:
      pw.println("build.options.minVersion=0");
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write configuration to " + senchaCfg);
    }
  }

}
