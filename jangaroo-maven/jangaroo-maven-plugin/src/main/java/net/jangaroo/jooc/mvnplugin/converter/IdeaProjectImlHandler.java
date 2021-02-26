package net.jangaroo.jooc.mvnplugin.converter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class IdeaProjectImlHandler extends DefaultHandler {
  private List<String> excludePaths;

  @Override
  public void startDocument() throws SAXException {
    this.excludePaths = new ArrayList<>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("excludeFolder")) {
      excludePaths.add(attributes.getValue("url").replace("file://$MODULE_DIR$/", ""));
    }
    super.startElement(uri, localName, qName, attributes);
  }

  public List<String> getExcludePaths() {
    return excludePaths;
  }
}
