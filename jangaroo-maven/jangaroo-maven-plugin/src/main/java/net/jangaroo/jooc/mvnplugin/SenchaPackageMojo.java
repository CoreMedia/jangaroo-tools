package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import javax.inject.Inject;
import java.io.File;

/**
 * Generates and packages Sencha package modules of type "test" and "code"
 */
@Mojo(name = "package-pkg", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyCollection = ResolutionScope.COMPILE, threadSafe = true )
public class SenchaPackageMojo extends AbstractSenchaMojo implements SenchaPackageConfiguration {

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  /**
   * Skips the build process of the Sencha package which results in a separate <em>pkg</em>
   * artifact. The <em>pkg</em> artifact is required if any other non-local Maven module
   * depends on this project.
   * <p />
   * Enabling this option speeds up the build process.
   *
   * @since 4.0
   */
  @Parameter(property = "skipPkg", defaultValue = "false")
  private boolean skipPkg;

  /**
   * Defines the packageType of the Sencha package that will be generated. Possible values are "code" (default) and "theme".
   *
   * @since 4.0
   */
  @Parameter(defaultValue = Type.CODE)
  private String packageType;

  /**
   * Defines if the resources of this package will be shared between different profiles of an application production
   * build.
   *
   * Due to restrictions in Sencha CMD only usable if {@link #packageType} is set to {@link Type#CODE}
   *
   * @since 4.0
   */
  @Parameter(defaultValue = "false")
  private boolean shareResources;

  /**
   * Defines if the resources of this package will isolated in an application production build.
   *
   * Due to restrictions in Sencha CMD only usable if {@link #packageType} is set to {@link Type#THEME}
   *
   * @since 4.0
   */
  @Parameter(defaultValue = "false")
  private boolean isolateResources;


  @Override
  public String getType() {
    if (Type.CODE.equals(packageType) || Type.THEME.equals(packageType)) {
      return packageType;
    }
    getLog().error(String.format("%s is not a valid packaging packageType. Using \"code\" instead.", packageType));
    packageType = Type.CODE;
    return packageType;
  }

  @Override
  public boolean isIsolateResources() {
    return isolateResources;
  }

  @Override
  public boolean isShareResources() {
    return shareResources;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!Type.JANGAROO_PKG_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"jangaroo-pkg\"");
    }

    SenchaPackageHelper senchaHelper = new SenchaPackageHelper(project, this, getLog());
    // for now:
    senchaHelper.createModule();
    senchaHelper.prepareModule();

    if (!skipPkg) {
      File pkg = senchaHelper.packageModule();
      helper.attachArtifact(project, Type.PACKAGE_EXTENSION, pkg);
    }

  }

}
