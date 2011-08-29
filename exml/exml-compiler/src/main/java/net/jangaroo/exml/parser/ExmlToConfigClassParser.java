package net.jangaroo.exml.parser;

import net.jangaroo.exml.ExmlcException;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.utils.CompilerUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExmlToConfigClassParser {
  private ExmlConfiguration config;

  public ExmlToConfigClassParser(ExmlConfiguration config) {
    this.config = config;
  }

  public ConfigClass parseExmlToConfigClass(File source) throws IOException {
    String fullQualifiedName = CompilerUtils.qNameFromFile(config.findSourceDir(source), source);
    ConfigClass configClass = new ConfigClass();

    String componentPackageName = CompilerUtils.packageName(fullQualifiedName);
    String componentClassName = ExmlModel.createComponentClassName(CompilerUtils.className(fullQualifiedName));

    configClass.setComponentClassName(componentPackageName.equals("") ? componentClassName : componentPackageName + "." + componentClassName);
    
    configClass.setPackageName(config.getConfigClassPackage());
    configClass.setName(ConfigClass.createConfigClassName(CompilerUtils.removeExtension(source.getName())));
    // Only components are encoded in EXML.
    configClass.setType(ConfigClassType.XTYPE);

    //read exml data and write it into the config class
    ExmlMetadataHandler metadataHandler = new ExmlMetadataHandler(configClass);
    parseFileWithHandler(source, metadataHandler);
    return configClass;
  }

  private static void parseFileWithHandler(File source, ContentHandler handler) {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(source);
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      xr.parse(new org.xml.sax.InputSource(inputStream));
    } catch (ExmlcException e) {
      // Simply pass our own exceptions.
      e.setSource(source);
      throw e;
    } catch (Exception e) {
      throw new ExmlcException("could not parse EXML file", source, e);
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

}
