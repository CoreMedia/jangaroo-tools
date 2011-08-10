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
   * @component
   */
  private MavenProjectHelper projectHelper;

  @Override
  protected void executeExmlc(Exmlc exmlc) {
    //generate the XSD for that
    File xsdFile = exmlc.generateXsd();
    projectHelper.attachArtifact(getProject(), "xsd", xsdFile);
    getLog().info("xsd-file '" + xsdFile + "' generated.");
  }
}
