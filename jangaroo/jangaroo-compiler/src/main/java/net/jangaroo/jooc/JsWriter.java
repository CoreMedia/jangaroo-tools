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

import com.google.debugging.sourcemap.FilePosition;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocOptions;
import net.jangaroo.jooc.util.PositionTrackingWriter;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public final class JsWriter extends FilterWriter {

  private JsStringLiteralWriter stringLiteralWriter;
  private JoocOptions options;
  private boolean commentStartWritten = false;
  private int nOpenBeginComments = 0;
  private char lastChar = ' ';
  private boolean inString = false;
  private int nOpenStrings = 0;
  private boolean suppressWhitespace = false;
  private List<SymbolToOutputFilePosition> sourceMappings = new ArrayList<SymbolToOutputFilePosition>();

  public JsWriter(Writer target) {
    super(new PositionTrackingWriter(target));
    stringLiteralWriter = new JsStringLiteralWriter(out, false);
  }

  public void setOptions(JoocOptions options) {
    this.options = options;
  }

  public JoocOptions getOptions() {
    return options;
  }

  public boolean getKeepSource() {
    return options.getDebugMode() != null && DebugMode.SOURCE.equals(options.getDebugMode());
  }

  public boolean getKeepLines() {
    return options.getDebugMode() != null && DebugMode.LINES.equals(options.getDebugMode());
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
      if (nOpenBeginComments > 0 && !commentStartWritten) {
        out.write("/*");
        lastChar = '*';
        commentStartWritten = true;
      } else if (nOpenBeginComments == 0 && commentStartWritten) {
        out.write("*/");
        lastChar = '/';
        commentStartWritten = false;
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

  public void setSuppressWhitespace(boolean suppressWhitespace) {
    this.suppressWhitespace = suppressWhitespace;
  }

  public void writeSymbolWhitespace(JooSymbol symbol) throws IOException {
    if (suppressWhitespace) {
      return;
    }
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
    while (true) {
      pos = s.indexOf('\n', pos) + 1;
      if (pos <= 0 || pos >= off + len + 1) {
        break;
      }
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

  private FilePosition getCurrentOutputFilePosition() {
    return new PrettyPrintFilePosition(
      ((PositionTrackingWriter)out).getLine() - 1,
      ((PositionTrackingWriter)out).getColumn() - 1);
  }

  public void writeSymbol(JooSymbol symbol, boolean withWhitespace) throws IOException {
    if (withWhitespace) {
      writeSymbolWhitespace(symbol);
    }
    writeSymbolToken(symbol);
  }


  public void writeSymbolToken(JooSymbol symbol) throws IOException {
    FilePosition outputStartPosition = getCurrentOutputFilePosition();
    writeToken(symbol.getText());
    if (!isWritingComment() && !inString) {
      FilePosition outputFileEndPosition = getCurrentOutputFilePosition();
      SymbolToOutputFilePosition symbolToOutputFilePosition =
        new SymbolToOutputFilePosition(symbol, outputStartPosition, outputFileEndPosition);
      sourceMappings.add(symbolToOutputFilePosition);
    }
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
        if (commentStartWritten) {
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
        if (commentStartWritten) {
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
    StringBuilder result = new StringBuilder(20);
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
    close("");
  }

  public void close(String suffix) throws IOException {
    shouldWrite(); // will close comments
    Debug.assertTrue(nOpenBeginComments == 0, "" + nOpenBeginComments + " endComment() missing");
    write(suffix);
    super.close();
  }

  public List<SymbolToOutputFilePosition> getSourceMappings() {
    return Collections.unmodifiableList(sourceMappings);
  }

  public boolean isWritingComment() {
    return nOpenBeginComments > 0;
  }

  private static class PrettyPrintFilePosition extends FilePosition {
    public PrettyPrintFilePosition(int line, int column) {
      super(line, column);
    }

    @Override
    public String toString() {
      return (getLine() + 1) + ":" + (getColumn() + 1);
    }
  }

  public static class SymbolToOutputFilePosition {
    private JooSymbol symbol;
    private FilePosition outputFileStartPosition;
    private FilePosition outputFileEndPosition;

    SymbolToOutputFilePosition(JooSymbol symbol, FilePosition outputFileStartPosition, FilePosition outputFileEndPosition) {
      this.symbol = symbol;
      this.outputFileStartPosition = outputFileStartPosition;
      this.outputFileEndPosition = outputFileEndPosition;
    }

    public JooSymbol getSymbol() {
      return symbol;
    }

    public FilePosition getSourceFilePosition() {
      return new PrettyPrintFilePosition(symbol.getLine() - 1, symbol.getColumn() - 1);
    }

    public FilePosition getOutputFileStartPosition() {
      return outputFileStartPosition;
    }

    public FilePosition getOutputFileEndPosition() {
      return outputFileEndPosition;
    }

    @Override
    public String toString() {
      return symbol.getFileName() + ":" + getSourceFilePosition() + " to " + outputFileStartPosition + " -> " + outputFileEndPosition;
    }
  }
}
