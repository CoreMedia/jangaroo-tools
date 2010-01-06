/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.xml;

import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.log.Log;
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

  private ContentHandlerUtils() {
    
  }
  public static boolean parseExmlWithHandler(ComponentClass cc, ContentHandler handler) {
    FileInputStream inputStream = null;
    Log.setCurrentFile(cc.getSrcFile());
    try {
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(handler);
      inputStream = new FileInputStream(cc.getSrcFile());
      xr.parse(new InputSource(inputStream));
      return true;
    } catch (FileNotFoundException e) {
      Log.e("Exception while parsing", e);
    } catch (IOException e) {
      Log.e("Exception while parsing", e);
    } catch (SAXParseException e) {
      Log.e(e.getMessage(), e.getLineNumber(), e.getColumnNumber());
    } catch (SAXException e) {
      Log.e("Exception while parsing", e);
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
