/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.configconverter.file;

import net.jangaroo.exml.configconverter.model.ComponentClass;
import net.jangaroo.exml.configconverter.model.ComponentSuite;
import net.jangaroo.exml.configconverter.model.ComponentType;
import net.jangaroo.exml.configconverter.xml.ContentHandlerUtils;
import net.jangaroo.exml.configconverter.xml.ExmlToComponentMetadataHandler;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

public final class ExmlComponentSrcFileScanner {

  public static final String EXML_NAMESPACE_URI = "http://net.jangaroo.com/extxml/0.1";

  private ExmlComponentSrcFileScanner() {
    //hide the constructor
  }

  public static void scan(ComponentSuite componentSuite, File srcFile, ComponentType type) throws IOException {
    assert ComponentType.EXML.equals(type);
    // extract meta data from file name:
    ComponentClass clazz = new ComponentClass(srcFile);
    clazz.setSuite(componentSuite);
    String fullName = getComponentClassName(componentSuite, srcFile);
    clazz.setFullClassName(fullName);
    clazz.setXtype(fullName);
    clazz.setType(ComponentType.EXML);
    //parse EXML file for meta data
    ExmlToComponentMetadataHandler metadataHandler= new ExmlToComponentMetadataHandler();
    if (ContentHandlerUtils.parseExmlWithHandler(clazz, metadataHandler)) {
      clazz.setSuperClassLocalName(metadataHandler.getSuperClassLocalName());
      clazz.setSuperClassNamespaceUri(metadataHandler.getSuperClassUri());
      clazz.setDescription(metadataHandler.getComponentDescription());
      clazz.setCfgs(metadataHandler.getCfgs());
    }
    componentSuite.addComponentClass(clazz);
  }

  public static String getComponentClassName(ComponentSuite componentSuite, File srcFile) {
    String className = FileUtils.removeExtension(srcFile.getName());
    String packageName = FileUtils.dirname(ComponentClass.relativeSrcFilePath(componentSuite, srcFile).substring(1)).replaceAll("[\\\\/]", ".");
    String fullName;
    if (packageName != null && !"".equals(packageName)) {
      fullName = packageName + "." + className;
    } else {
      fullName = className;
    }
    return fullName;
  }


}
