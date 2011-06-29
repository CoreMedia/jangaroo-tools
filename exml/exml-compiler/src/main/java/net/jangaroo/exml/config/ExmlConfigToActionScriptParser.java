package net.jangaroo.exml.config;

import net.jangaroo.exml.config.model.ConfigClass;
import net.jangaroo.exml.config.xml.ExmlMetadataHandler;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.utils.ContentHandlerUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class ExmlConfigToActionScriptParser {


  public static File parseExmlFile(File rootFolder, File outputFolder, File source, String typeNamespace) {
    File result = new File(outputFolder, typeNamespace.replaceAll(".", File.separator) + FilenameUtils.getBaseName(source.getName()) + ".as");

    String fullQualifiedName = computeComponentFullQualifiedName(rootFolder, source);
    ConfigClass configClass = new ConfigClass(new FileInputSource(source));
    configClass.setComponentName(fullQualifiedName);
    configClass.setPackageName(typeNamespace);
    configClass.setName(FilenameUtils.getBaseName(result.getName()));

    //read exml data and write it into the config class
    ExmlMetadataHandler metadataHandler = new ExmlMetadataHandler(configClass);
    ContentHandlerUtils.parseFileWithHandler(source, metadataHandler);

    //generate the new config class ActionScript file
    ExmlConfigClassGenerator.generateClass(configClass, result);

    return result;
  }

  public static String computeComponentFullQualifiedName(File rootFolder, File sourceFile) {
    int rootDirPathLength = rootFolder.getPath().length()+1;
    String subpath = FilenameUtils.removeExtension(sourceFile.getPath().substring(rootDirPathLength));
    return subpath.replaceAll(File.separator,".");
  }
}
