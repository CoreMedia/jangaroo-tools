package net.jangaroo.jooc.mxml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

public class CatalogGenerator {

  private MxmlComponentRegistry mxmlComponentRegistry;

  public CatalogGenerator(MxmlComponentRegistry mxmlComponentRegistry) {
    this.mxmlComponentRegistry = mxmlComponentRegistry;
  }

  public void generateCatalog(File catalogFile) throws IOException {
    try {
      generateCatalog(new OutputStreamWriter(new FileOutputStream(catalogFile), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e); // should not happen for "UTF-8"!
    }
  }

  public void generateCatalog(Writer catalogWriter) throws IOException {
    if (mxmlComponentRegistry.getComponentPackageModels().isEmpty()) {
      // do not create an empty catalog.xml!
      return;
    }
    try {
      XMLStreamWriter catalogStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(catalogWriter);
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

      for (ComponentPackageModel componentPackageModel : mxmlComponentRegistry.getComponentPackageModels()) {
        for (Map.Entry<String, String> componentMapping : componentPackageModel.entrySet()) {
          catalogStreamWriter.writeEmptyElement("component");
          String componentClass = componentMapping.getValue();
          int lastDotIndex = componentClass.lastIndexOf(".");
          componentClass = componentClass.substring(0, lastDotIndex) + ":" + componentClass.substring(lastDotIndex + 1);
          catalogStreamWriter.writeAttribute("uri", componentPackageModel.getNamespace());
          catalogStreamWriter.writeAttribute("name", componentMapping.getKey());
          catalogStreamWriter.writeAttribute("className", componentClass);
        }
      }

      catalogStreamWriter.writeEndElement(); // components

      catalogStreamWriter.writeEndElement(); // swc

      catalogStreamWriter.writeEndDocument();

      catalogStreamWriter.close();
    } catch (XMLStreamException e) {
      throw new IOException("While generating catalog.xml:", e);
    }
  }

}
