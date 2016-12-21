package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.model.ConfigClass;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExmlToConfigClassParser {

  public ConfigClass parseExmlToConfigClass(File source) throws IOException {
    ConfigClass configClass = new ConfigClass();
    //read exml data and write it into the config class
    ExmlMetadataHandler metadataHandler = new ExmlMetadataHandler(configClass);
    parseFileWithHandler(source, metadataHandler);
    return configClass;
  }

  public static void parseFileWithHandler(File source, ContentHandler handler) {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(source);
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      if (handler instanceof LexicalHandler) {
        xr.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
      }
      xr.parse(new org.xml.sax.InputSource(inputStream));
    } catch (ExmlcException e) {
      // Simply pass our own exceptions.
      e.setFile(source);
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
