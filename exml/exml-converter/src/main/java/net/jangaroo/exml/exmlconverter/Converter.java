package net.jangaroo.exml.exmlconverter;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Convert an EXML specification provided as a character stream.
 * This class is mainly provided as a starting point for tests,
 * which are much easier to write if they don't have to deal with files.
 */
public abstract class Converter {
  private static Map<String,String> CLASS_TO_CLASS = readProperties("class.properties");
  private static Map<String,String> NAMESPACE_TO_NAMESPACE = readProperties("namespace.properties");

  private static HashMap<String, String> readProperties(String resourceName) {
    Properties properties = new Properties();
    InputStream resourceAsStream = null;
    try {
      resourceAsStream = Converter.class.getResourceAsStream(resourceName);
      properties.load(resourceAsStream);
    } catch (IOException e) {
      throw new IllegalStateException(resourceName + " must be packaged with the Converter class");
    } finally {
      if(resourceAsStream != null) {
        try {
          resourceAsStream.close();
        } catch (IOException e) {
          //will never hapen
        }
      }
    }
    //noinspection unchecked
    return new HashMap(properties); // NOSONAR I know that this is a map from strings to strings.
  }

  private static final int NOTHING = -1;

  private BufferedReader reader;
  private int lookAhead = NOTHING;

  private BufferedWriter writer;

  protected abstract Writer createWriter() throws UnsupportedEncodingException, FileNotFoundException;

  protected abstract Reader createReader() throws UnsupportedEncodingException, FileNotFoundException;

  public void execute() throws IOException, ParseException {
    reader = new BufferedReader(createReader());
    writer = new BufferedWriter(createWriter());

    try {
      copyStream();
    } finally {
      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(writer);
    }
  }

  // EXML conversion logic

  private String translateElementName(String qName) {
    int colonPos = qName.indexOf(':');
    String namespace = qName.substring(0, colonPos + 1); // include colon, if present
    String localName = qName.substring(colonPos + 1); // exclude colon
    String translated = translateLocalName(localName);
    return namespace + translated;
  }
  private String translateLocalName(String localName) {
    String translated = CLASS_TO_CLASS.get(localName);
    if (translated != null) {
      return translated;
    }
    if (localName.length() < 2) {
      return localName.toLowerCase(Locale.ROOT);
    }
    return localName.substring(0, 1).toLowerCase(Locale.ROOT) + localName.substring(1);
  }

  // basic I/O

  protected int read() throws IOException {
    if (lookAhead != Converter.NOTHING) {
      int result = lookAhead;
      lookAhead = Converter.NOTHING;
      return result;
    } else {
      return reader.read();
    }
  }

  private void unread(char c) {
    if (lookAhead != NOTHING) {
      throw new IllegalStateException("multiple unread characters are not supported");
    }
    lookAhead = c;
  }

  protected void write(char c) throws IOException {
    writer.write(c);
  }

  protected void write(String s) throws IOException {
    writer.write(s);
  }

  // basic parsing utilities

  private void copyForced(char expected) throws IOException, ParseException {
    int read = read();
    if (read == -1) {
      throw new ParseException("The file ended while expecting a '" + expected + "' character.");
    }
    char c = (char) read;
    if (c != expected) {
      throw new ParseException("Expected character '" + expected + "', but found character '" + c + "'.");
    }
    write(c);
  }

  private void copyForced(String expected) throws IOException, ParseException {
    for (int i = 0; i < expected.length(); i++) {
      copyForced(expected.charAt(i));
    }
  }

  private void copyAny(String expected) throws IOException, ParseException {
    while (true) {
      int read = read();
      if (read == -1) {
        return;
      }
      char c = (char) read;
      if (!expected.contains(String.valueOf(c))) {
        unread(c);
        return;
      }
      write(c);
    }
  }

  private void copyUntil(String expected, String messageOnEof) throws IOException, ParseException {
    int found = 0;
    while (true) {
      int read = read();
      if (read == -1) {
        throw new ParseException(messageOnEof);
      }
      char c = (char) read;
      write(c);
      if (c == expected.charAt(found)) {
        found++;
        if (found == expected.length()) {
          return;
        }
      } else {
        found = 0;
      }
    }
  }

  // XML parsing and processing

  protected void copyStream() throws IOException, ParseException {
    while (true) {
      int read = read();
      if (read == -1) {
        return;
      }
      char c = (char) read;
      write(c);
      switch (c) {
        case '&':
          copyEntity();
          break;
        case '<':
          copyAngleBracketThingy();
          break;
      }
    }
  }

  private void copyEntity() throws ParseException, IOException {
    while (true) {
      int read = read();
      if (read == -1) {
        throw new ParseException("The file ended while parsing an entity.");
      }
      char c = (char) read;
      write(c);
      if (c == ';') {
        break;
      }
    }
  }

  private void copyAngleBracketThingy() throws IOException, ParseException {
    int read = read();
    if (read == -1) {
      throw new ParseException("The file ended while parsing an element.");
    }
    char c = (char) read;
    switch (c) {
      case '!':
        write(c);
        copyCommentOrCDATA();
        break;
      case '?':
        write(c);
        copyProcessingInstruction();
        break;
      case '/':
        write(c);
        copyClosingTag();
        break;
      default:
        unread(c);
        copyOpeningTag();
        break;
    }
  }

  private void copyOpeningTag() throws IOException, ParseException {
    String elementName = parseElementName();
    writer.write(translateElementName(elementName));
    while (true) {
      int read = read();
      if (read == -1) {
        throw new ParseException("The file ended while parsing an opening tag.");
      }
      char c = (char) read;
      switch (c) {
        case '>':
          write(c);
          return;
        case ' ':
        case '\t':
        case '\r':
        case '\n':
        case '/':
          write(c);
          break;
        default:
          unread(c);
          copyAttribute();
      }
    }
  }

  private void copyAttribute() throws IOException, ParseException {
    copyUntil("=", "The file ended while seeking to the equal sign of an attribute.");
    copyAny(" \t\r\f");
    int read = read();
    if (read == -1) {
      throw new ParseException("The file ended while expecting a quotation mark of an attribute value.");
    }
    char c = (char) read;
    if (c != '"' && c != '\'') {
      throw new ParseException("A quotation mark was expected, but the character '" + c + "' was found.");
    }
    String attributeValue = readAttributeValue(c);
    if (NAMESPACE_TO_NAMESPACE.containsKey(attributeValue)) {
      attributeValue = NAMESPACE_TO_NAMESPACE.get(attributeValue);
    }
    write(c);
    write(attributeValue);
    write(c);
  }

  private String readAttributeValue(char delimiter) throws IOException, ParseException {
    StringBuilder builder = new StringBuilder();
    while (true) {
      int read = read();
      if (read == -1) {
        throw new ParseException("The file ended while parsing an attribute value.");
      }
      char c = (char) read;
      if (c == delimiter) {
        break;
      }
      builder.append(c);
    }
    return builder.toString();
  }

  private void copyClosingTag() throws IOException, ParseException {
    String elementName = parseElementName();
    writer.write(translateElementName(elementName));
    copyUntil(">", "The file ended while parsing a closing tag.");
  }

  private String parseElementName() throws IOException, ParseException {
    StringBuilder builder = new StringBuilder();
    while (true) {
      int read = read();
      if (read == -1) {
        throw new ParseException("The file ended while parsing a tag name.");
      }
      char c = (char) read;
      if (!Character.isLetterOrDigit(c) && c != ':' && c != '_') {
        unread(c);
        break;
      }
      builder.append(c);
    }
    return builder.toString();
  }

  private void copyCommentOrCDATA() throws IOException, ParseException {
    int read = read();
    if (read == -1) {
      throw new ParseException("The file ended while parsing a comment or CDATA section.");
    }
    char c = (char) read;
    write(c);
    if (c == '-') {
      copyComment();
    } else if (c == '[') {
      copyCDATA();
    } else {
      // A DOCTYPE definition or a similarly unlikely object. Just transfer this to the output.
      copyUntil(">", "A '<' character was not matched with a '>' character before the end of the file.");
    }
  }

  private void copyComment() throws IOException, ParseException {
    copyForced('-');
    copyUntil("-->", "The file ended while parsing a comment.");
  }

  private void copyCDATA() throws IOException, ParseException {
    copyForced("CDATA[");
    copyUntil("]]>", "The file ended while parsing a CDATA section.");
  }

  private void copyProcessingInstruction() throws IOException, ParseException {
    copyUntil("?>", "The file ended while parsing a processing instruction.");
  }
}
