package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;

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
   * @parameter expression="${project.artifactId}.xsd"
   */
  protected String xsd;
  /**
   * The folder where the XSD Schema for this component suite will be generated
   *
   * @parameter expression="${project.build.directory}/generated-resources"
   */
  protected File generatedResourcesDirectory;


  @Override
  protected void executeExmlc(Exmlc exmlc) {
    if (!generatedResourcesDirectory.exists()) {
      getLog().info("generating resources into: " + generatedResourcesDirectory.getPath());
      getLog().debug("created " + generatedResourcesDirectory.mkdirs());
    }

    //generate the XSD for that
    File xsdFile = new File(generatedResourcesDirectory, xsd);
    exmlc.generateXsd(xsdFile);
    projectHelper.attachArtifact(project, "xsd", xsdFile);
    getLog().info("Xsd '" + xsdFile + "' generated.");
  }


}
