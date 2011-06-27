package net.jangaroo.exml.config;

import net.jangaroo.exml.config.model.ConfigClass;
import net.jangaroo.exml.config.xml.ExmlMetadataHandler;
import net.jangaroo.utils.ContentHandlerUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class ExmlConfigToActionScriptParser {


  public static File parseExmlFile(File rootFolder, File outputFolder, File source, String typeNamespace) {
    File result = new File(outputFolder, typeNamespace.replaceAll(".", File.separator) + FilenameUtils.getBaseName(source.getName()) + ".as");

    ConfigClass configClass = new ConfigClass(source, result, rootFolder);
    configClass.setPackageName(typeNamespace);

    ExmlMetadataHandler metadataHandler = new ExmlMetadataHandler(configClass);
    ContentHandlerUtils.parseFileWithHandler(source, metadataHandler);
    ExmlConfigClassGenerator.generateClass(configClass);

    return result;
  }
}
