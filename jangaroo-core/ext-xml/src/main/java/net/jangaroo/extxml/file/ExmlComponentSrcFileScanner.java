/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.file;

import net.jangaroo.extxml.ComponentClass;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentType;
import net.jangaroo.extxml.Log;
import net.jangaroo.extxml.ExmlToComponentMetadataHandler;
import org.codehaus.plexus.util.FileUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ExmlComponentSrcFileScanner {

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
    ExmlToComponentMetadataHandler metadataHandler= new ExmlToComponentMetadataHandler(componentSuite);
    if (parseExmlWithHandler(clazz, metadataHandler)) {
      clazz.setSuperClassName(metadataHandler.getSuperClassName());
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

  public static boolean parseExmlWithHandler(ComponentClass cc, ContentHandler handler) {
    FileInputStream inputStream = null;
    Log.getErrorHandler().setCurrentFile(cc.getSrcFile());
    try {
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      inputStream = new FileInputStream(cc.getSrcFile());
      xr.parse(new InputSource(inputStream));
      return true;
    } catch (FileNotFoundException e) {
      Log.getErrorHandler().error("Exception while parsing", e);
    } catch (IOException e) {
      Log.getErrorHandler().error("Exception while parsing", e);
    } catch (SAXParseException e) {
      Log.getErrorHandler().error(e.getMessage(), e.getLineNumber(), e.getColumnNumber());
    } catch (SAXException e) {
      Log.getErrorHandler().error("Exception while parsing", e);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        //never happened
      }
    }
    return false;
  }
}
