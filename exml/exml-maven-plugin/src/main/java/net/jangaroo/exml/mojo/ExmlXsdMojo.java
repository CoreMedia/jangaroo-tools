package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ValidationMode;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;

/**
 * A Mojo that generates the Xsd files for the module. Needs the same information as the exml mojo
 */
@Mojo(name = "exml-xsd",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class ExmlXsdMojo extends ExmlCompileMojo {

  @Component
  private MavenProjectHelper projectHelper;

  public ExmlXsdMojo() {
    super();
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    useAllSources(); // as this goal is executed *after* the EXML compiler, there are no stale sources anymore!
    super.execute();
  }

  @Override
  protected void executeExmlc(Exmlc exmlc) {
    if (exmlc.getConfig().getValidationMode() != ValidationMode.OFF) {
      getLog().info("validating " + exmlc.getConfig().getSourceFiles().size() + " EXML files...");
    }

    //generate the XSD for that
    File xsdFile = exmlc.generateXsd();
    projectHelper.attachArtifact(getProject(), "xsd", xsdFile);
    getLog().info("xsd-file '" + xsdFile + "' generated.");

    // add target/generated-resources to project's resources so XSDs are always packaged:
    Resource generatedResources = new Resource();
    generatedResources.setDirectory(getGeneratedResourcesDirectory().getPath());
    getProject().addResource(generatedResources);
    getLog().info("added project resource '" + generatedResources + ".");
  }
}
