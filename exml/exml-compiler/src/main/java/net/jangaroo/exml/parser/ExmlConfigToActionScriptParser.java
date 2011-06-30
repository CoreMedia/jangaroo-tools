package net.jangaroo.exml.parser;

import net.jangaroo.exml.generation.ExmlConfigClassGenerator;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.xml.ExmlMetadataHandler;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExmlConfigToActionScriptParser {


  public static ConfigClass generateConfigClass(File source, File sourceRootDir, File outputRootDir, String configClassPackage) {
    String fullQualifiedName = computeComponentFullQualifiedName(sourceRootDir, source);
    ConfigClass configClass = new ConfigClass();
    configClass.setComponentName(fullQualifiedName);
    configClass.setPackageName(configClassPackage);
    configClass.setName(FilenameUtils.getBaseName(source.getName()));

    //read exml data and write it into the config class
    ExmlMetadataHandler metadataHandler = new ExmlMetadataHandler(configClass);
    parseFileWithHandler(source, metadataHandler);

    File targetPackageFolder = new File(outputRootDir, configClassPackage.replaceAll("\\.", File.separator));
    if(!targetPackageFolder.exists()) {
      targetPackageFolder.mkdirs();
    }

    File targetFile = new File(targetPackageFolder, FilenameUtils.getBaseName(source.getName()) + ".as");

    //only recreate file if result file is older than the source file
    if(!targetFile.exists() || targetFile.lastModified() < source.lastModified()) {
      //generate the new config class ActionScript file
      ExmlConfigClassGenerator.generateClass(configClass, targetFile);
    }

    return configClass;
  }

  private static void parseFileWithHandler(File source, ContentHandler handler) {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(source);
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      xr.parse(new org.xml.sax.InputSource(inputStream));
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        //never happened
      }
    }
  }

  public static String computeComponentFullQualifiedName(File rootFolder, File sourceFile) {
    int rootDirPathLength = rootFolder.getPath().length()+1;
    String subpath = FilenameUtils.removeExtension(sourceFile.getPath().substring(rootDirPathLength));
    return subpath.replaceAll("\\" + File.separator, "\\.");
  }
}
