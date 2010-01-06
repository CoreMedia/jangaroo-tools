/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import freemarker.template.TemplateException;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;

import java.io.File;
import java.io.IOException;

public final class Properties {
  private Properties() {

  }

  public static void main(String[] args) throws IOException, TemplateException {

    LocalizationSuite suite = new LocalizationSuite(new File(args[0]), new File(args[1]));

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    scanner.scan();

    PropertyClassGenerator generator = new PropertyClassGenerator(suite);
    generator.generate();
  }
}
