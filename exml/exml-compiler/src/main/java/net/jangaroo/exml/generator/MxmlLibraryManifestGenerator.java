package net.jangaroo.exml.generator;

import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

public class MxmlLibraryManifestGenerator {
  private final ConfigClassRegistry configClassRegistry;

  public MxmlLibraryManifestGenerator(ConfigClassRegistry configClassRegistry) {
    this.configClassRegistry = configClassRegistry;
  }

  public File createManifestFile() throws FileNotFoundException, UnsupportedEncodingException {
    // create catalog.xml component library:
    File outputFile = new File(configClassRegistry.getConfig().getOutputDirectory(), "manifest.xml");
    System.out.printf("Creating manifest file %s...%n", outputFile.getPath());
    PrintStream out = new PrintStream(new FileOutputStream(outputFile), true, net.jangaroo.exml.api.Exmlc.OUTPUT_CHARSET);

    Collection<ConfigClass> sourceConfigClasses = configClassRegistry.getSourceConfigClasses();
    out.println("<?xml version=\"1.0\"?>");
    out.println("<componentPackage>");
    for (ConfigClass configClass : sourceConfigClasses) {
      out.printf("  <component class=\"%s\"/>%n", configClass.getFullName());
      if (configClass.getType().getType() == null) {
        out.printf("  <component %s/>%n", String.format("class=\"%s\"", configClass.getComponentClassName()));
      }
    }
    out.println("</componentPackage>");
    out.close();
    return outputFile;
  }
}
