package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import javax.inject.Inject;
import java.io.File;

@Mojo(name = "sencha-package", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaPackageMojo extends AbstractSenchaMojo {

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * Skip the build process of the sencha module.
   *
   * Only use this for local development to speed up the build process of the maven app.
   * For deployment the build process is required otherwise remote packages will have no contents.
   */
  @Parameter(defaultValue = "false")
  private boolean skipRemotePackaging;

  /**
   * @see SenchaConfiguration#getType()
   */
  @Parameter(defaultValue = Type.CODE)
  private String type;

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skipRemotePackaging) {

      SenchaHelper senchaHelper = new SenchaPackageHelper(project, this, getLog());
      // for now:
      senchaHelper.createModule();
      senchaHelper.prepareModule();
      File pkg = senchaHelper.packageModule();

      helper.attachArtifact(project, Type.PACKAGE_EXTENSION, pkg);
    }

  }

}
