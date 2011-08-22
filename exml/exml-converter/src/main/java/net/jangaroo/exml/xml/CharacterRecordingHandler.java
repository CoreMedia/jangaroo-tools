package net.jangaroo.exml.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A ContentHandler that records characters (CDATA). Recording can be started and is stopped when
 * the characters are retrieved.
 */
public class CharacterRecordingHandler extends DefaultHandler {//stores all characters
  private StringBuffer characterStack;

  protected void startRecordingCharacters() {
    //create new character Stack that will store all following CDATA sections
    characterStack = new StringBuffer();
  }

  public void ignorableWhitespace(char ch[], int start, int length)
      throws SAXException {
    if (characterStack != null) {
      String cdata = new String(ch, start, length);
      characterStack.append(cdata);
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (characterStack != null) {
      String cdata = new String(ch, start, length);
      characterStack.append(cdata);
    }
  }

  protected String popRecordedCharacters() {
    if (characterStack == null) {
      return null;
    }
    String characters = characterStack.toString();
    characterStack = null;
    return characters;
  }
}
