package net.jangaroo.properties.compiler;/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import freemarker.template.TemplateException;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.FileSet;

import java.io.File;
import java.io.IOException;

public final class PropertiesCompiler {
  private PropertiesCompiler() {

  }

  public static void main(String[] args) throws IOException, TemplateException {

    FileSet properties = new FileSet();
    properties.setDirectory(new File(args[0]).getAbsolutePath());
    properties.addInclude("**/*.properties");

    LocalizationSuite suite = new LocalizationSuite(properties, new File(args[1]));

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    scanner.scan();

    PropertyClassGenerator generator = new PropertyClassGenerator(suite);
    generator.generate();
  }
}
