package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageHelper;
import org.apache.maven.plugin.AbstractMojo;
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
public class SenchaPackageMojo extends AbstractMojo {

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  /**
   * The sencha configuration to use.
   */
  @Parameter(property = "senchaConfiguration")
  private MavenSenchaConfiguration senchaConfiguration;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!senchaConfiguration.isSkipBuild()) {

      senchaConfiguration.setType(project.getPackaging()); // packaging is validated through lifecycle component mapping
      senchaConfiguration.setProjectBuildDir(project.getBuild().getDirectory());

      SenchaHelper senchaHelper = new SenchaPackageHelper(project, senchaConfiguration, getLog());
      // for now:
      senchaHelper.createModule();
      senchaHelper.prepareModule();
      File pkg = senchaHelper.packageModule();

      helper.attachArtifact(project, Type.PACKAGE_EXTENSION, pkg);
    }

  }

}
