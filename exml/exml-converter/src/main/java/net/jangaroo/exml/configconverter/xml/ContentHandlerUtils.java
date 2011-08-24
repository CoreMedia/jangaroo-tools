/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.configconverter.xml;

import net.jangaroo.exml.configconverter.model.ComponentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class ContentHandlerUtils {

  private static final Logger log = LoggerFactory.getLogger(ContentHandlerUtils.class);

  private ContentHandlerUtils() {
    
  }
  public static boolean parseExmlWithHandler(ComponentClass cc, ContentHandler handler) {
    FileInputStream inputStream = null;
    try {
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      inputStream = new FileInputStream(cc.getSrcFile());
      xr.parse(new InputSource(inputStream));
      return true;
    } catch (FileNotFoundException e) {
      log.error("Exception while parsing", e);
    } catch (IOException e) {
      log.error("Exception while parsing", e);
    } catch (SAXParseException e) {
      log.error(e.getMessage(), e.getLineNumber(), e.getColumnNumber());
    } catch (SAXException e) {
      log.error("Exception while parsing", e);
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
