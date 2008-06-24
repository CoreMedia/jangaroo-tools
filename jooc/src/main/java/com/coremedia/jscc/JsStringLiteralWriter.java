/*

  Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.

*/

package com.coremedia.jscc;

import java.io.Writer;


/**
 * @author Andreas Gawecki
 */
class JsStringLiteralWriter extends SubstitutingWriter {

  protected boolean insideScriptTag;

  public JsStringLiteralWriter(Writer out) {
    this(out, true);
  }

  public JsStringLiteralWriter(Writer out, boolean insideScriptTag) {
    super(out);
    this.insideScriptTag = insideScriptTag;
  }

  protected String substitute(char ch) {
    if (ch == '"')
      return "\\\"";
    if (ch == '\n')
      return "\\n";
    if (ch == '/' && insideScriptTag)
      // this prevents literals containing </script> from terminating the script tag
      return "\\/";
    if (ch == '\\')
      return "\\\\";
    if ((int) ch < 16)
      return "\\x0" + Integer.toHexString((int) ch);
    if ((int) ch < 32)
      return "\\x" + Integer.toHexString((int) ch);
    return null;
  }

  public void beginString() throws java.io.IOException {
    out.write('"');
  }

  public void endString() throws java.io.IOException {
    out.write('"');
  }
}

