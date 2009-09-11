/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.mojo;

import freemarker.template.TemplateException;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ErrorHandler;
import net.jangaroo.extxml.JooClassGenerator;
import net.jangaroo.extxml.SrcFileScanner;
import net.jangaroo.extxml.XsdGenerator;
import net.jangaroo.extxml.XsdScanner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
      for (File importedXsd : importedXsds) {
        InputStream in = null;
        try {
          in = new FileInputStream(importedXsd);
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
    MavenErrorHandler errorHandler = new MavenErrorHandler();

    JooClassGenerator generator = new JooClassGenerator(suite, errorHandler);
    generator.generateClasses();

    if(!errorHandler.exceptions.isEmpty()) {
     for (Map.Entry<String, Exception> entry: errorHandler.exceptions.entrySet()) {
      throw new MojoExecutionException(entry.getKey(), entry.getValue());
     }
    }

    if(!errorHandler.errors.isEmpty()) {
      for (String msg : errorHandler.errors) {
        throw new MojoFailureException(msg);
      }
    }

    if(!errorHandler.warnings.isEmpty()) {
      for (String msg : errorHandler.warnings) {
        getLog().warn(msg);
      }
    }

    //generate the XSD for that
    if(!suite.getComponentClasses().isEmpty()) {
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
    }

    project.addCompileSourceRoot(generatedSourcesDirectory.getPath());
  }

  class MavenErrorHandler implements ErrorHandler {
    ArrayList<String> errors = new ArrayList<String>();
    ArrayList<String> warnings = new ArrayList<String>();
    HashMap<String, Exception> exceptions = new HashMap<String, Exception>();
    File currentFile;

    public void setCurrentFile(File file) {
      this.currentFile = file;
    }

    public void error(String message, int lineNumber, int columnNumber) {
      errors.add(String.format("ERROR in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
    }

    public void error(String message, Exception exception) {
      exceptions.put(message, exception);
    }

    public void error(String message) {
      errors.add(message);
    }

    public void warning(String message) {
      warnings.add(message);
    }

    public void warning(String message, int lineNumber, int columnNumber) {
      warnings.add(String.format("WARNING in %s, line %s, column %s: %s", currentFile, lineNumber, columnNumber, message));
    }
  }
}
