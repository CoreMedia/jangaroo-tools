package net.jangaroo.jooc.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CompilerConfigParser {

  private JoocConfiguration joocConfiguration;

  public CompilerConfigParser(JoocConfiguration joocConfiguration) {
    this.joocConfiguration = joocConfiguration;
  }

  public void parse(InputStream compilerConfig) {
    try {
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      SAXParser parser = saxFactory.newSAXParser();
      CompilerConfigSaxHandler handler = new CompilerConfigSaxHandler();
      parser.parse(compilerConfig, handler);
      joocConfiguration.addNamespaces(handler.getNamespaces());
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class CompilerConfigSaxHandler extends DefaultHandler {
    private List<NamespaceConfiguration> namespaces = new ArrayList<NamespaceConfiguration>();
    private String lastElement;
    private NamespaceConfiguration namespace;

    @Override
    public void startElement(String elementNamespace, String localName, String qName, Attributes attributes) throws SAXException {
      lastElement = localName;
      if ("namespace".equals(localName)) {
        namespace = new NamespaceConfiguration();
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (namespace != null) {
        String value = new String(ch, start, length);
        if ("uri".equals(lastElement)) {
          namespace.setUri(value);
        } else if ("manifest".equals(lastElement)) {
          namespace.setManifest(value);
        }
      }
    }

    @Override
    public void endElement(String elementNamespace, String localName, String qName) throws SAXException {
      lastElement = null;
      if ("namespace".equals(localName)) {
        // TODO: report errors if uri or manifest has not been set!
        namespaces.add(namespace);
        namespace = null;
      }
    }

    public List<NamespaceConfiguration> getNamespaces() {
      return namespaces;
    }
  }
}
