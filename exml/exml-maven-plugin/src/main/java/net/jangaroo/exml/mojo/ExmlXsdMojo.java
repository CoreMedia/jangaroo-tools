package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ValidationMode;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Mojo that generates the Xsd files for the module. Needs the same information as the exml mojo
 *
 * @goal exml-xsd
 * @phase generate-resources
 * @requiresDependencyResolution
 * @threadSafe
 */
public class ExmlXsdMojo extends ExmlMojo {

  /**
   * @component
   */
  private MavenProjectHelper projectHelper;

  public ExmlXsdMojo() {
    super();
  }

  @Override
  protected List<File> getSourcePath() {
    List<File> sourcePath = new ArrayList<File>(super.getSourcePath());
    sourcePath.add(getGeneratedSourcesDirectory());
    return Collections.unmodifiableList(sourcePath);
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
