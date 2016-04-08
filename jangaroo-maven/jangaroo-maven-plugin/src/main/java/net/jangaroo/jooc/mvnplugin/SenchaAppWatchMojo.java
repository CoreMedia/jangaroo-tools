package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Start "sencha app watch" for a <em>jangaroo-app</em> project.
 */
@Mojo(name = "sencha-app-watch", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaAppWatchMojo extends AbstractSenchaMojo{

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY)
  private File appTargetDir;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
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

    String arguments = "app watch";
    getLog().info("Starting \"sencha app watch\"");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(appTargetDir, arguments, getLog());
    senchaCmdExecutor.execute();

  }
}
