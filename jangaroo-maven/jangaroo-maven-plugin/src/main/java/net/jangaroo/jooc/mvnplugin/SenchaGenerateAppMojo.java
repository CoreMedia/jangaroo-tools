package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

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
    File senchaCfg = new File(workingDirectory, SenchaUtils.SENCHA_APP_CONFIG);

    String senchaAppName = getSenchaPackageName(project);
    String arguments = "generate app"
            + " -ext"
            + " -" + getToolkit()
            + " --template " + SenchaUtils.getSenchaPackageName(SenchaUtils.SENCHA_APP_TEMPLATE_GROUP_ID, SenchaUtils.SENCHA_APP_TEMPLATE_ARTIFACT_ID) + "/tpl"
            + " --path=\"\""
            + " --refresh=false"
            + " -DappName=" + senchaAppName
            + " -DapplicationClass=" + applicationClass
            + " " + senchaAppName;
    getLog().info("Generating Sencha app module");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, arguments, getLog(), getSenchaLogLevel());
    senchaCmdExecutor.execute();

    // sencha.cfg should have been recreated.
    if (!senchaCfg.exists()) {
      throw new MojoExecutionException("Could not find sencha.cfg of app");
    }

    // For apps, skip generating slices.
    // app.output.js.optimize.defines - If true will cause problems with class pre- and post-processors we use.
    // build.options.minVersion - If 0.99 (default), some deprecated API will not be available in production build:
    FileHelper.addToConfigFile(senchaCfg, ImmutableList.of("skip.slice=1", "app.output.js.optimize.defines=false", "build.options.minVersion=0"));
  }
}
