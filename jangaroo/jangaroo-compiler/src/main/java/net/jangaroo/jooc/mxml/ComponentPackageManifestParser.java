package net.jangaroo.jooc.mxml;

import net.jangaroo.jooc.util.PreserveLineNumberHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class ComponentPackageManifestParser {

  private String namespace;

  public ComponentPackageManifestParser(String namespace) {
    this.namespace = namespace;
  }

  public ComponentPackageModel parse(InputStream manifestInputStream) throws IOException {
    ComponentPackageModel componentPackageModel = new ComponentPackageModel(namespace);
    try {
      Document document = buildDom(manifestInputStream);
      NodeList componentNodes = document.getElementsByTagName("component");
      for (int i = 0; i < componentNodes.getLength(); i++) {
        Node componentNode = componentNodes.item(i);
        NamedNodeMap attributes = componentNode.getAttributes();
        String componentId = attributes.getNamedItem("id").getNodeValue();
        String componentClass = attributes.getNamedItem("class").getNodeValue();
        componentPackageModel.addElementToClassNameMapping(componentId, componentClass);
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return componentPackageModel;
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
