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

