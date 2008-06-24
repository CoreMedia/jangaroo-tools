/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andreas Gawecki
 */
public class JsWriter extends FilterWriter {

  JsStringLiteralWriter stringLiteralWriter;
  boolean enableAssertions = false;
  boolean keepLines = false;
  boolean keepSource = false;
  boolean inComment = false;
  int nOpenBeginComments = 0;
  char lastChar = ' ';

  public JsWriter(Writer target) {
    super(target);
    stringLiteralWriter = new JsStringLiteralWriter(target, false);
  }

  public boolean getKeepSource() {
    return keepSource;
  }

  public void setKeepSource(boolean keepSource) {
    this.keepSource = keepSource;
  }

  public boolean getKeepLines() {
    return keepLines;
  }

  public void setKeepLines(boolean keepLines) {
    this.keepLines = keepLines;
  }

  public boolean getEnableAssertions() {
    return enableAssertions;
  }

  public void setEnableAssertions(boolean enableAssertions) {
    this.enableAssertions = enableAssertions;
  }

  public Writer getTarget() {
    return out;
  }

  public void writeInt(int value) throws IOException {
    if (shouldWrite())
      write(String.valueOf(value));
  }

  public void writeString(String value) throws IOException {
    if (shouldWrite()) {
      if (value == null)
        write("null");
      else {
        stringLiteralWriter.beginString();
        stringLiteralWriter.write(value);
        stringLiteralWriter.endString();
      }
    }
  }

  public void writeDate(Date value) throws IOException {
    if (shouldWrite())
      writeString("new Date(" + value.getTime() + ")");
  }

  public void writeDate(Calendar value) throws IOException {
    writeDate(value.getTime());
  }

  public void writeObject(Object o) throws IOException {
    if (shouldWrite()) {
      if (o instanceof String)
        writeString((String) o);
      else if (o instanceof Integer)
        writeInt(((Integer) o).intValue());
      else if (o instanceof Date)
        writeDate((Date) o);
      else if (o instanceof Calendar)
        writeDate((Calendar) o);
      else if (o instanceof Object[])
        writeArray((Object[]) o);
      else {
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
        if (i > 0)
          write(',');
        writeObject(items[i]);
      }
      write("]");
    }
  }

  public void beginComment() throws IOException {
    nOpenBeginComments++;
  }

  private final boolean shouldWrite() throws IOException {
    boolean result = keepSource || nOpenBeginComments == 0;
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

  public void beginCommentWriteSymbol(JscSymbol symbol) throws IOException {
    beginComment();
    writeSymbol(symbol);
  }

  public void writeSymbolWhitespace(JscSymbol symbol) throws IOException {
    String ws = symbol.getWhitespace();
    if (keepSource)
      write(ws);
    else if (keepLines)
      writeLines(ws);
  }

  protected void writeLines(String s) throws IOException {
    writeLines(s, 0, s.length());
  }

  protected void writeLines(String s, int off, int len) throws IOException {
     int pos = off;
     while ((pos = s.indexOf('\n', pos)+1) > 0 && pos < off+len+1)
       write('\n');
  }

  public void writeToken(String token) throws IOException {
    if (shouldWrite()) {
      String text = token;
      char firstSymbolChar = text.charAt(0);
      if ((Character.isLetterOrDigit(lastChar) && Character.isLetterOrDigit(firstSymbolChar)) ||
            (lastChar == firstSymbolChar && "=><!&|+-*/&|^%".indexOf(lastChar) >= 0) ||
            (firstSymbolChar == '=' && "=><!&|+-*/&|^%".indexOf(lastChar) >= 0))
        write(' ');
      write(text);
    }
  }

  public void writeSymbol(JscSymbol symbol) throws IOException {
    writeSymbolWhitespace(symbol);
    writeSymbolToken(symbol);
  }

  public void writeSymbolToken(JscSymbol symbol) throws IOException {
    writeToken(symbol.getText());
  }

  public void write(int c) throws IOException {
    if ((keepLines && c == '\n') || shouldWrite()) {
      if (lastChar == '*' && c == '/')
        super.write(' ');
      super.write(c);
      lastChar = (char)c;
    }
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    if (len > 0) {
      if (shouldWrite()) {
        if (inComment) {
          for (int i = 0; i < len; i++) {
            char c = cbuf[off+i];
            write(c);
          }
        } else
          super.write(cbuf, off, len);
        lastChar = cbuf[off+len-1];
      }
      else if (keepLines) {
        for (int i = 0; i < len; i++) {
          char c = cbuf[off+i];
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
           char c = str.charAt(off+i);
            write(c);
          }
        } else
          super.write(str, off, len);
        lastChar = str.charAt(off+len-1);
      } else if (keepLines)
        writeLines(str, off, len);
    }
  }

  private static String qualifiedNameToIde(String[] qn) {
    StringBuffer result = new StringBuffer(20);
    for (int i = 0; i < qn.length; i++) {
      if (i > 0)
        result.append('$');
      result.append(qn[i]);
    }
    return result.toString();
  }

  public String getCompatibilityName(String name) {
    return name.replace('$', '_');
  }

  public String getQualifiedNameAsIde(IdeDeclaration ideDeclaration) {
    return qualifiedNameToIde(ideDeclaration.getQualifiedName());
  }

  public String getMethodNameAsIde(MethodDeclaration methodDeclaration) {
    ClassDeclaration classDeclaration = methodDeclaration.getClassDeclaration();
    String classNameAsIde = getQualifiedNameAsIde(classDeclaration);
    return classNameAsIde + "$" + methodDeclaration.getName();
  }

  public String getFunctionNameAsIde(FunctionExpr functionExpr) {
    ClassDeclaration classDeclaration = functionExpr.getClassDeclaration();
    String classNameAsIde = getQualifiedNameAsIde(classDeclaration);
    JscSymbol sym = functionExpr.getSymbol();
    return classNameAsIde + "$" + sym.getLine() + "_" + sym.getColumn();
  }

  public String getSuperClassNameAsIde(ClassDeclaration classDeclaration) {
    Type type = classDeclaration.getSuperClassType();
    //TODO: define ApplyType
    //TODO: scope class declarations, implement getSuperClassDeclaration()
    IdeType ideType = (IdeType) type;
    return qualifiedNameToIde(ideType.getIde().getQualifiedName());
  }

  public String getSuperMethodName(MethodDeclaration methodDeclaration) {
    return getSuperMethodName(methodDeclaration.getClassDeclaration(), methodDeclaration.getName());
  }

  public String getSuperMethodName(ClassDeclaration classDeclaration, String methodName) {
    return getSuperMethodName(getSuperClassNameAsIde(classDeclaration), methodName);
  }

  public String getSuperConstructorNameAsIde(ClassDeclaration classDeclaration) {
    MethodDeclaration constructorDeclaration = classDeclaration.getConstructorDeclaration();
    String superClassName= getSuperClassNameAsIde(constructorDeclaration.getClassDeclaration());
    return "super$" + superClassName;
  }

  //TODO:make private once we scope classes and super calls:
  private String getSuperMethodName(String className, String methodName) {
    return "super$" + className + "$" + methodName;
  }

  public String getPrototypeHelperVariableName(ClassDeclaration classDeclaration) {
    return "$";
  }
  public String getConstructorHelperVariableName(ClassDeclaration classDeclaration) {
    return "$";
  }

  public void close() throws IOException {
    shouldWrite(); // will close comments
    Debug.assertTrue(nOpenBeginComments == 0, ""+ nOpenBeginComments + " endComment() missing");
    super.close();
  }
}
