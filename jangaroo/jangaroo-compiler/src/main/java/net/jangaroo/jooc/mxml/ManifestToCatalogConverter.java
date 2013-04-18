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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ManifestToCatalogConverter {

  private InputStream manifestInputStream;
  private OutputStream catalogOutputStream;
  private String namespace;

  public ManifestToCatalogConverter(InputStream manifestInputStream, OutputStream catalogOutputStream, String namespace) {
    this.manifestInputStream = manifestInputStream;
    this.catalogOutputStream = catalogOutputStream;
    this.namespace = namespace;
  }

  private Map<String, String> buildComponentMap() {
    Map<String, String> componentMap = new HashMap<String, String>();
    try {
      Document document = buildDom(manifestInputStream);
      NodeList componentNodes = document.getElementsByTagName("component");
      for (int i = 0; i < componentNodes.getLength(); i++) {
        Node componentNode = componentNodes.item(i);
        NamedNodeMap attributes = componentNode.getAttributes();
        String componentId = attributes.getNamedItem("id").getNodeValue();
        String componentClass = attributes.getNamedItem("class").getNodeValue();
        componentMap.put(componentId, componentClass);
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return componentMap;
  }

  public void generateCatalog() {
    Map<String, String> componentMap = buildComponentMap();

    try {
      XMLStreamWriter catalogStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(catalogOutputStream, "utf-8"));
      catalogStreamWriter.writeStartDocument("utf-8", "1.0");

      catalogStreamWriter.writeStartElement("swc");
      catalogStreamWriter.writeAttribute("xmlns", "http://www.adobe.com/flash/swccatalog/9");

      catalogStreamWriter.writeStartElement("versions");

      catalogStreamWriter.writeEmptyElement("swc");
      catalogStreamWriter.writeAttribute("version", "1.2");

      catalogStreamWriter.writeEmptyElement("flex");
      catalogStreamWriter.writeAttribute("version", "4.6.0");
      catalogStreamWriter.writeAttribute("build", "23201");
      catalogStreamWriter.writeAttribute("minimumSupportedVersion", "4.0.0");

      catalogStreamWriter.writeEndElement(); //versions

      catalogStreamWriter.writeStartElement("features");

      catalogStreamWriter.writeEmptyElement("feature-components");

      catalogStreamWriter.writeEndElement(); //features

      catalogStreamWriter.writeStartElement("components");

      for (String componentKey : componentMap.keySet()) {
        catalogStreamWriter.writeEmptyElement("component");
        String componentClass = componentMap.get(componentKey);
        int lastDotIndex = componentClass.lastIndexOf(".");
        componentClass = componentClass.substring(0, lastDotIndex) + ":" + componentClass.substring(lastDotIndex + 1);
        catalogStreamWriter.writeAttribute("className", componentClass);
        catalogStreamWriter.writeAttribute("name", componentKey);
        catalogStreamWriter.writeAttribute("uri", namespace);
      }

      catalogStreamWriter.writeEndElement(); // components

      catalogStreamWriter.writeEndElement(); // swc

      catalogStreamWriter.writeEndDocument();

      catalogStreamWriter.close();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
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
