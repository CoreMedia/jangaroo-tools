package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;

/**
 * Generates and packages Sencha app module.
 */
@Mojo(name = "generate-app", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class SenchaGenerateAppMojo extends AbstractSenchaMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${project.build.directory}" + SenchaUtils.APP_TARGET_DIRECTORY, readonly = true)
  private File workingDirectory;

  /**
   * The full qualified name of the application class of the Sencha app, e.g.:
   * <pre>
   * &lt;applicationClass>net.jangaroo.acme.MainApplication&lt;/applicationClass>
   * </pre>
   */
  @Parameter
  private String applicationClass;

  public String getType() {
    return Type.APP;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_APP_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-app\"");
    }
    // parameter can not just be required="true" as this would also apply for the other packaging types and mojos
    if (StringUtils.isBlank(applicationClass)) {
      throw new MojoExecutionException("\"applicationClass\" is missing. This configuration is mandatory for \"jangaroo-app\" packaging.");
    }
    createModule();
  }

  public void createModule() throws MojoExecutionException {
    // necessary?
    FileHelper.ensureDirectory(workingDirectory);

    // only generate app if senchaCfg does not exist
    if (SenchaUtils.doesSenchaAppExist(workingDirectory)) {
      getLog().info("Sencha app already exists, skip generating one");
      return;
    }

    String senchaAppName = getSenchaPackageName(project);
    SenchaUtils.generateSenchaAppFromTemplate(workingDirectory, senchaAppName, applicationClass, getToolkit(), getLog(), getSenchaLogLevel());
  }
}
