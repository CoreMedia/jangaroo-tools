package net.jangaroo.jooc.util;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import java.util.Stack;

/**
 * SAX Handler that fills a DOM document with the XML nodes and
 * ads line numbers to the element user data
 */
public class PreserveLineNumberHandler extends DefaultHandler2 {
  private static final String LINE_NUMBER_KEY_NAME = "lineNumber";
  private static final String COLUMN_NUMBER_KEY_NAME = "columnNumber";

  private final Stack<Node> elementStack = new Stack<Node>();
  private final StringBuilder textBuffer = new StringBuilder();

  private Document doc;
  private Locator locator;

  public PreserveLineNumberHandler(Document doc) {
    this.doc = doc;
    elementStack.push(doc);
  }

  public static int getLineNumber(Node node) {
    String lineStr = (String) node.getUserData(PreserveLineNumberHandler.LINE_NUMBER_KEY_NAME);
    return lineStr == null ? -1 : Integer.parseInt(lineStr);
  }

  public static int getColumnNumber(Node node) {
    String lineStr = (String) node.getUserData(PreserveLineNumberHandler.COLUMN_NUMBER_KEY_NAME);
    return lineStr == null ? -1 : Integer.parseInt(lineStr);
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
    el.setUserData(COLUMN_NUMBER_KEY_NAME, String.valueOf(this.locator.getColumnNumber()), null);
    elementStack.push(el);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) {
    addTextIfNeeded();
    final Node closedEl = elementStack.pop();
    elementStack.peek().appendChild(closedEl);
  }

  @Override
  public void characters(final char ch[], final int start, final int length) throws SAXException {
    textBuffer.append(ch, start, length);
  }

  @Override
  public void comment(char[] ch, int start, int length) throws SAXException {
    super.comment(ch, start, length);
    Comment comment = doc.createComment(new String(ch, start, length));
    final Node parentEl = elementStack.peek();
    parentEl.appendChild(comment);
  }

  // Outputs text accumulated under the current node
  private void addTextIfNeeded() {
    if (textBuffer.length() > 0) {
      final Node textNode = doc.createTextNode(textBuffer.toString());
      elementStack.peek().appendChild(textNode);
      textBuffer.delete(0, textBuffer.length());
    }
  }
}
