package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.jar.JarArchiver;

/**
 * Packages Sencha app overlay module.
 */
@Mojo(name = "package-plugin",
        defaultPhase = LifecyclePhase.PACKAGE,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class PackagePluginMojo extends PreparePackageAppOverlayMojo {

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private JarArchiver archiver;

  @Override
  public void execute() throws MojoExecutionException {
    if (!Type.JANGAROO_SWC_PACKAGING.equals(project.getPackaging())) {
      throw new MojoExecutionException("This goal only supports projects with packaging type \"swc\"");
    }
    packageAppOverlay(true);
    FileHelper.createPluginZip(session, archiver, artifactHandlerManager);
  }
}
