/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.mojo;

import freemarker.template.TemplateException;
import net.jangaroo.extxml.*;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 *
 * @goal extxml
 * @phase generate-sources
 */
public class ExtXmlMojo extends AbstractMojo {

   /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${basedir}/src/main/joo"
   */
  private File sourceDirectory;

  /**
   * The namespace of the component suite
   *
   * @parameter expression="${project.artifactId}"
   */
  private String namespace;

  /**
   * Output directory for all ActionScript3 files generated out of exml components
   *
   * @parameter expression="${project.build.directory}/generated-sources/joo"
   */
  private File generatedSourcesDirectory;

  /**
   * The XSD Schema that will be generated for this component suite
   *
   * @parameter expression="${project.artifactId}.xsd"
   */
  private String xsd;

  /**
   * The XSD Schema that will be generated for this component suite
   *
   * @parameter expression="${project.build.directory}/generated-resources"
   */
  private File generatedResourcesDirectory;

  /**
   * The XSD Schema that will be generated for this component suite
   *
   * @parameter
   */
  private File[] importedXsds;

  public void execute() throws MojoExecutionException, MojoFailureException {

    if (!generatedSourcesDirectory.exists()) {
      getLog().info("generating sources into: " + generatedSourcesDirectory.getPath());
      getLog().debug("created " + generatedSourcesDirectory.mkdirs());
    }
    if (!generatedResourcesDirectory.exists()) {
      getLog().info("generating resources into: " + generatedResourcesDirectory.getPath());
      getLog().debug("created " + generatedResourcesDirectory.mkdirs());
    }


    ComponentSuite suite = new ComponentSuite(namespace, sourceDirectory, generatedSourcesDirectory);

    try {
      suite.addImportedComponentSuite(XsdScanner.getExt3ComponentSuite());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    if (importedXsds != null) {
      for (int i = 0; i < importedXsds.length; i++) {
        InputStream in = null;
        try {
          in = new FileInputStream(importedXsds[i]);
          suite.addImportedComponentSuite(XsdScanner.scan(in));
        } catch (IOException e) {
          e.printStackTrace();
        } catch (SAXException e) {
          e.printStackTrace();
        } catch (ParserConfigurationException e) {
          e.printStackTrace();
        } finally {
          try {
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }


    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    try {
      fileScanner.scan();
    } catch (IOException e) {
      throw new MojoExecutionException("Error while file scanning", e);
    }

    //Generate JSON out of the xml compontents, complete the data in those ComponentClasses
    JooClassGenerator generator = null;
    try {
      generator = new JooClassGenerator(suite);
    } catch (SaxonApiException e) {
      throw new MojoExecutionException("Error while preparing joo class generator", e);
    }
    try {
      generator.generateClasses();
    } catch (SaxonApiException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    } catch (SAXException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    } catch (XPathExpressionException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    } catch (TemplateException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("Error while generating classes", e);
    }
    //generate the XSD for that
    Writer out = null;
    try {
      //generate the XSD for that
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(generatedResourcesDirectory, xsd)), "UTF-8"));
      new XsdGenerator(suite).generateXsd(out);

    } catch (IOException e) {
      throw new MojoExecutionException("Error while generating XML schema", e);
    } catch (TemplateException e) {
      throw new MojoExecutionException("Error while generating XML schema", e);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    project.addCompileSourceRoot(generatedSourcesDirectory.getPath());
    
  }
}
