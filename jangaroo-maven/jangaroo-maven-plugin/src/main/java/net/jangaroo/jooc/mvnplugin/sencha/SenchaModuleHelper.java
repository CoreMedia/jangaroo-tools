package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;

public class SenchaModuleHelper implements SenchaHelper {

  private SenchaHelper delegate;

  public SenchaModuleHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    if (SenchaConfiguration.Type.WORKSPACE.equals(senchaConfiguration.getType())) {
      delegate = new SenchaWorkspaceHelper(project, senchaConfiguration, log);
    }
    if (SenchaConfiguration.Type.CODE.equals(senchaConfiguration.getType())
            || SenchaConfiguration.Type.THEME.equals(senchaConfiguration.getType())) {
      delegate = new SenchaPackageHelper(project, senchaConfiguration, log);
    }
    if (SenchaConfiguration.Type.APP.equals(senchaConfiguration.getType())) {
      delegate = new SenchaAppHelper(project, senchaConfiguration, log);
    }
  }

  @Override
  public void deleteModule() throws MojoExecutionException {
    delegate.deleteModule();
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    delegate.prepareModule();
  }

  @Override
  public void generateModule() throws MojoExecutionException {
    delegate.generateModule();
  }

  @Override
  public void packageModule(JarArchiver archiver) throws MojoExecutionException {
    delegate.packageModule(archiver);
  }
}
