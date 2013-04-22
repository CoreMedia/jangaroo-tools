package net.jangaroo.jooc.mxml;

import net.jangaroo.jooc.util.PreserveLineNumberHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class CatalogComponentsParser {

  private MxmlComponentRegistry mxmlComponentRegistry;

  public CatalogComponentsParser(MxmlComponentRegistry mxmlComponentRegistry) {
    this.mxmlComponentRegistry = mxmlComponentRegistry;
  }

  public void parse(InputStream catalogInputStream) {
    try {
      Document document = buildDom(catalogInputStream);
      NodeList componentNodes = document.getElementsByTagName("component");
      for (int i = 0; i < componentNodes.getLength(); i++) {
        Node componentNode = componentNodes.item(i);
        if (componentNode.getNodeType() == Node.ELEMENT_NODE) {
          Element componentElement = (Element) componentNode;
          String componentClass = componentElement.getAttribute("className");
          mxmlComponentRegistry.registerElement(componentElement.getAttribute("uri"),
                  componentElement.getAttribute("name"),
                  componentClass.replace(':', '.'));
        }
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Document buildDom(InputStream inputStream) throws SAXException, IOException {
    SAXParser parser;
    final Document doc;
    try {
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      parser = saxFactory.newSAXParser();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      doc = factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }
}
