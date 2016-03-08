package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "sencha-package", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaPackageMojo extends JangarooMojo {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    SenchaHelper senchaHelper = new SenchaModuleHelper(getProject(), getSenchaConfiguration(), getLog());
    // for now:
    senchaHelper.createModule();
    senchaHelper.prepareModule();
    senchaHelper.packageModule(null);
  }

}
