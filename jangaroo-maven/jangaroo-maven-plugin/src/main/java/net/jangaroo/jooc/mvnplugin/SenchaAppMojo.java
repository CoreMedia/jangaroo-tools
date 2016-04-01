package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaAppHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.FileSet;
import org.codehaus.plexus.archiver.util.DefaultFileSet;

import javax.inject.Inject;
import java.io.File;


@Mojo(name = "sencha-app", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class SenchaAppMojo extends AbstractSenchaMojo {

  @Inject
  private MavenProjectHelper helper;

  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = Type.JAR_EXTENSION)
  private Archiver jarArchiver;

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Override
  public String getType() {
    return Type.APP;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    SenchaHelper senchaHelper = new SenchaAppHelper(project, this, getLog());
    // for now:
    senchaHelper.createModule();
    senchaHelper.prepareModule();
    File productionDirectory = senchaHelper.packageModule();


    try {
      FileSet appFiles = new DefaultFileSet(productionDirectory).prefixed("META-INF/resources/");
      jarArchiver.addFileSet(appFiles);

    } catch (ArchiverException e) {
      throw new MojoExecutionException("could not add app production directory to jar", e);
    }

  }

}
