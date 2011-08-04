package net.jangaroo.exml.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * SAX Handler that fills a DOM document with the XML nodes and
 * ads line numbers to the element user data
 */
public class PreserveLineNumberHandler extends DefaultHandler {
  public final static String LINE_NUMBER_KEY_NAME = "lineNumber";

  private final Stack<Element> elementStack = new Stack<Element>();
  private final StringBuilder textBuffer = new StringBuilder();

  private Document doc;
  private Locator locator;

  public PreserveLineNumberHandler(Document doc) {
    this.doc = doc;
  }

  @Override
  public void setDocumentLocator(final Locator locator) {
    this.locator = locator; // Save the locator, so that it can be used later for line tracking when traversing nodes.
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
      throws SAXException {
    addTextIfNeeded();
    final Element el = doc.createElementNS(uri, qName);
    for (int i = 0; i < attributes.getLength(); i++) {
      el.setAttributeNS(attributes.getURI(i), attributes.getQName(i), attributes.getValue(i));
    }
    el.setUserData(LINE_NUMBER_KEY_NAME, String.valueOf(this.locator.getLineNumber()), null);
    elementStack.push(el);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) {
    addTextIfNeeded();
    final Element closedEl = elementStack.pop();
    if (elementStack.isEmpty()) { // Is this the root element?
      doc.appendChild(closedEl);
    } else {
      final Element parentEl = elementStack.peek();
      parentEl.appendChild(closedEl);
    }
  }

  @Override
  public void characters(final char ch[], final int start, final int length) throws SAXException {
    textBuffer.append(ch, start, length);
  }

  // Outputs text accumulated under the current node
  private void addTextIfNeeded() {
    if (textBuffer.length() > 0) {
      final Element el = elementStack.peek();
      final Node textNode = doc.createTextNode(textBuffer.toString());
      el.appendChild(textNode);
      textBuffer.delete(0, textBuffer.length());
    }
  }
}
