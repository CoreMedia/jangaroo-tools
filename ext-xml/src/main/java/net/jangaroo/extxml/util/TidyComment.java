package net.jangaroo.extxml.util;

import org.w3c.tidy.Tidy;
import org.w3c.dom.Document;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * A helper class to convert HTML-style comments into well-formed XHTML.
 */
public final class TidyComment {
  private static final Tidy tidy;

  private TidyComment() {
    
  }

  static {
    tidy = new Tidy();
    tidy.setDropEmptyParas(true);
    tidy.setDropFontTags(true);
    tidy.setFixComments(true);
    tidy.setHideEndTags(false);
    tidy.setIndentAttributes(true);
    tidy.setMakeClean(true);
    tidy.setQuiet(true);
    tidy.setQuoteAmpersand(true);
    tidy.setShowWarnings(false);
    tidy.setXHTML(true);
    tidy.setXmlOut(true);
    tidy.setXmlSpace(false);
    tidy.setXmlPi(false);
  }

  public static String tidy(String dirtyHtml) {
    String wrappedHtml = "<html><body>"+dirtyHtml+"</body></html>";
    StringWriter result = new StringWriter();
    try {
      Document document = tidy.parseDOM(new ByteArrayInputStream(wrappedHtml.getBytes("ISO-8859-1")), null);
      DOMSource domSource = new DOMSource(document.getDocumentElement());
      Transformer serializer = TransformerFactory.newInstance().newTransformer();
      serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
      serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
      serializer.transform(domSource, new StreamResult(result));
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      // should not happen for ISO-8859-1:
      throw new RuntimeException(e);
    }
    String xml = result.toString();
    if (xml.indexOf("<body/>")!=-1) {
      return "";
    }
    int bodyStart = xml.indexOf("<body");
    int bodyEnd = xml.indexOf("</body>");
    if (bodyStart==-1 || bodyEnd==-1) {
      // should not happen:
      throw new RuntimeException("No body element found in "+xml);
    }
    return xml.substring(xml.indexOf('>',bodyStart)+1,bodyEnd);
  }
}
