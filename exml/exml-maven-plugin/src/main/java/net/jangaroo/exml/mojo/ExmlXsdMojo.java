package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;

/**
 * A Mojo that generates the Xsd files for the module. Needs the same information as the exml mojo
 *
 * @goal exml-xsd
 * @phase generate-resources
 * @requiresDependencyResolution
 */
public class ExmlXsdMojo extends ExmlMojo {

  /**
   * The XSD Schema that will be generated for this component suite
   *
   * @parameter default-value="${project.artifactId}.xsd"
   */
  private String xsd;
  /**
   * The folder where the XSD Schema for this component suite will be generated
   *
   * @parameter default-value="${project.build.directory}/generated-resources"
   */
  private File generatedResourcesDirectory;

  /**
   * @component
   */
  private MavenProjectHelper projectHelper;

  @Override
  protected void executeExmlc(Exmlc exmlc) {
    if (!generatedResourcesDirectory.exists()) {
      getLog().info("generating resources into: " + generatedResourcesDirectory.getPath());
      getLog().debug("created " + generatedResourcesDirectory.mkdirs());
    }

    //generate the XSD for that
    File xsdFile = new File(generatedResourcesDirectory, xsd);
    exmlc.generateXsd(xsdFile);
    projectHelper.attachArtifact(getProject(), "xsd", xsdFile);
    getLog().info("Xsd '" + xsdFile + "' generated.");
  }


}
