/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.file;

import net.jangaroo.extxml.ComponentClass;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentType;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class ExmlComponentSrcFileScanner {
  private ExmlComponentSrcFileScanner() {
    //hide the constructor
  }

  public static void scan(ComponentSuite componentSuite, File srcFile, ComponentType type) throws IOException {
    //parse EXML files
    ComponentClass clazz = new ComponentClass(srcFile);
    clazz.setSuite(componentSuite);
    String className = FileUtils.removeExtension(srcFile.getName());
    String packageName = FileUtils.dirname(clazz.getRelativeSrcFilePath().substring(1)).replaceAll("[\\\\/]", ".");
    String fullName;
    if (packageName != null && !"".equals(packageName)) {
      fullName = packageName + "." + className;
    } else {
      fullName = className;
    }
    clazz.setFullClassName(fullName);
    clazz.setXtype(fullName);
    clazz.setType(ComponentType.EXML);
    componentSuite.addComponentClass(clazz);

  }
}
