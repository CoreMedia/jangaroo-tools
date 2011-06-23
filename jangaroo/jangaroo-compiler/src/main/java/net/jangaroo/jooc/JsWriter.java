/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.config.JoocOptions;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andreas Gawecki
 */
public final class JsWriter extends FilterWriter {

  private JsStringLiteralWriter stringLiteralWriter;
  private JoocOptions options;
  private boolean inComment = false;
  private int nOpenBeginComments = 0;
  private char lastChar = ' ';
  private boolean inString = false;
  private int nOpenStrings = 0;

  public JsWriter(Writer target) {
    super(target);
    stringLiteralWriter = new JsStringLiteralWriter(target, false);
  }

  public void setOptions(JoocOptions options) {
    this.options = options;
  }

  public JoocOptions getOptions() {
    return options;
  }

  public boolean getKeepSource() {
    return options.isDebug() && options.isDebugSource();
  }

  public boolean getKeepLines() {
    return options.isDebug() && options.isDebugLines();
  }

  public boolean getEnableAssertions() {
    return options.isEnableAssertions();
  }

  public void writeInt(int value) throws IOException {
    if (shouldWrite()) {
      write(String.valueOf(value));
    }
  }

  public void writeString(String value) throws IOException {
    if (shouldWrite()) {
      if (value == null) {
        write("null");
      } else {
        stringLiteralWriter.beginString();
        stringLiteralWriter.write(value);
        stringLiteralWriter.endString();
      }
    }
  }

  public void writeDate(Date value) throws IOException {
    if (shouldWrite()) {
      writeString("new Date(" + value.getTime() + ")");
    }
  }

  public void writeDate(Calendar value) throws IOException {
    writeDate(value.getTime());
  }

  public void writeObject(Object o) throws IOException {
    if (shouldWrite()) {
      if (o instanceof String) {
        writeString((String) o);
      } else if (o instanceof Integer) {
        writeInt((Integer) o);
      } else if (o instanceof Date) {
        writeDate((Date) o);
      } else if (o instanceof Calendar) {
        writeDate((Calendar) o);
      } else if (o instanceof Object[]) {
        writeArray((Object[]) o);
      } else {
        throw new IOException(this.getClass().getName() + ": cannot write object: " + o.getClass().getName());
      }
    }
  }

  /*
  public hox.text.xml.XHtmlUnparser getXHtmlLiteralUnparser() {
    return new hox.text.xml.XHtmlUnparser(stringLiteralWriter) {
      public void startDocument() throws SAXException {
        try {
          stringLiteralWriter.beginString();
          super.startDocument();
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }

      public void endDocument() throws SAXException {
        try {
          super.endDocument();
          stringLiteralWriter.endString();
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
    };
  }
  */

  public void writeArray(Object[] items) throws IOException {
    if (shouldWrite()) {
      write("[");
      int n = items.length;
      for (int i = 0; i < n; i++) {
        if (i > 0) {
          write(',');
        }
        writeObject(items[i]);
      }
      write("]");
    }
  }

  public void beginComment() throws IOException {
    nOpenBeginComments++;
  }

  private boolean shouldWrite() throws IOException {
    boolean result = getKeepSource() || nOpenBeginComments == 0;
    if (result) {
      if (nOpenBeginComments > 0 && !inComment) {
        out.write("/*");
        lastChar = '*';
        inComment = true;
      } else if (nOpenBeginComments == 0 && inComment) {
        out.write("*/");
        lastChar = '/';
        inComment = false;
      }
    }
    return result;
  }

  public void endComment() throws IOException {
    Debug.assertTrue(nOpenBeginComments > 0, "missing beginComment() for endComment()");
    nOpenBeginComments--;
  }

  public void beginCommentWriteSymbol(JooSymbol symbol) throws IOException {
    beginComment();
    writeSymbol(symbol);
  }

  public void beginString() throws IOException {
    nOpenStrings++;
  }

  private void checkOpenString() throws IOException {
    if (nOpenStrings > 0 && !inString) {
      out.write('"');
      lastChar = '"';
      inString = true;
    }
  }

  private boolean checkCloseString() throws IOException {
    if (inString) {
      out.write('"');
      inString = false;
      return true;
    }
    return false;
  }

  public void endString() throws IOException {
    Debug.assertTrue(nOpenStrings > 0, "missing beginString() for endString()");
    nOpenStrings--;
    if (nOpenStrings == 0) {
      checkCloseString();
    }
  }

  private void writeLinesInsideString(String ws) throws IOException {
    String[] lines = ws.split("\n", -1);
    for (int i = 0; i < lines.length - 1; i++) {
      String line = lines[i];
      if (line.length() > 1) {
        checkOpenString();
        write(line.substring(0, line.length() - 1));
        write("\\n");
      }
      if (checkCloseString()) {
        write("+");
      }
      write("\n");
    }
    String line = lines[lines.length - 1];
    if (line.length() > 0) {
      checkOpenString();
      write(line);
    }
  }

  public void writeSymbolWhitespace(JooSymbol symbol) throws IOException {
    String ws = symbol.getWhitespace();
    if (getKeepSource()) {
      if (inString) {
        writeLinesInsideString(ws);
      } else {
        write(ws);
      }
    } else if (getKeepLines()) {
      writeLines(ws);
    } else {
      writeLine(ws);
    }
  }

  private void writeLine(String ws) throws IOException {
    if (ws.indexOf('\n') != -1) {
      writeNewline();
    }
  }

  protected void writeLines(String s) throws IOException {
    writeLines(s, 0, s.length());
  }

  protected void writeLines(String s, int off, int len) throws IOException {
    int pos = off;
    while ((pos = s.indexOf('\n', pos) + 1) > 0 && pos < off + len + 1) {
      writeNewline();
    }
  }

  private void writeNewline() throws IOException {
    if (inString) {
      write("\\n");
      checkCloseString();
      write('+');
    }
    write('\n');
  }

  public void writeToken(String token) throws IOException {
    if (shouldWrite()) {
      char firstSymbolChar = token.charAt(0);
      if ((isIdeChar(lastChar) && isIdeChar(firstSymbolChar)) ||
          (lastChar == firstSymbolChar && "=><!&|+-*/&|^%".indexOf(lastChar) >= 0) ||
          (firstSymbolChar == '=' && "=><!&|+-*/&|^%".indexOf(lastChar) >= 0)) {
        write(' ');
      }
      checkOpenString();
      write(token);
    }
  }

  private boolean isIdeChar(final char ch) {
    return ch == '$' || ch == '_' || Character.isLetterOrDigit(ch);
  }

  public void writeSymbol(JooSymbol symbol) throws IOException {
    writeSymbol(symbol, true);
  }

  public void writeSymbol(JooSymbol symbol, boolean withWhitespace) throws IOException {
    if (withWhitespace) {
      writeSymbolWhitespace(symbol);
    }
    writeSymbolToken(symbol);
  }

  /**
   * Variant of writeSymbol() to use if you want to transform the symbol text with a prefix and/or postfix string
   *
   * @param symbol  the symbol to write
   * @param prefix  a (possibly empty) string to write before the symbol token string
   * @param postfix a (possibly empty) string to write after the symbol token string
   * @throws java.io.IOException if an IO error occurs
   */
  public void writeSymbol(JooSymbol symbol, String prefix, String postfix) throws IOException {
    writeSymbolWhitespace(symbol);
    writeToken(prefix + symbol.getText() + postfix);
  }

  public void writeSymbolToken(JooSymbol symbol) throws IOException {
    writeToken(symbol.getText());
  }

  public void write(int c) throws IOException {
    if ((getKeepLines() && c == '\n') || shouldWrite()) {
      if (lastChar == '*' && c == '/') {
        super.write(' ');
      }
      super.write(c);
      lastChar = (char) c;
    }
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    if (len > 0) {
      if (shouldWrite()) {
        if (inComment) {
          for (int i = 0; i < len; i++) {
            char c = cbuf[off + i];
            write(c);
          }
        } else {
          super.write(cbuf, off, len);
        }
        lastChar = cbuf[off + len - 1];
      } else if (getKeepLines()) {
        for (int i = 0; i < len; i++) {
          char c = cbuf[off + i];
          if (c == '\n') {
            super.write(c);
            lastChar = c;
          }
        }
      }
    }
  }

  public void write(String str, int off, int len) throws IOException {
    if (len > 0) {
      if (shouldWrite()) {
        if (inComment) {
          for (int i = 0; i < len; i++) {
            char c = str.charAt(off + i);
            write(c);
          }
        } else {
          super.write(str, off, len);
        }
        lastChar = str.charAt(off + len - 1);
      } else if (getKeepLines()) {
        writeLines(str, off, len);
      }
    }
  }

  private static String qualifiedNameToIde(String[] qn) {
    StringBuffer result = new StringBuffer(20);
    for (int i = 0; i < qn.length; i++) {
      if (i > 0) {
        result.append('$');
      }
      result.append(qn[i]);
    }
    return result.toString();
  }

  public String getQualifiedNameAsIde(IdeDeclaration ideDeclaration) {
    return qualifiedNameToIde(ideDeclaration.getQualifiedName());
  }

  public void close() throws IOException {
    shouldWrite(); // will close comments
    Debug.assertTrue(nOpenBeginComments == 0, "" + nOpenBeginComments + " endComment() missing");
    super.close();
  }
}
