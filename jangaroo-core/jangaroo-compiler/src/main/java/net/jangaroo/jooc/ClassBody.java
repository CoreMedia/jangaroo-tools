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

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class ClassBody extends NodeImplBase {
  JooSymbol lBrace;

  public List<AstNode> getDeclararations() {
    return declararations;
  }

  List<AstNode> declararations;
  JooSymbol rBrace;

  public ClassBody(JooSymbol lBrace, List<AstNode> declararations, JooSymbol rBrace) {
    this.lBrace = lBrace;
    this.declararations = declararations;
    this.rBrace = rBrace;
  }

  @Override
  public void scope(final Scope scope) {
    for (AstNode node : declararations) {
      node.scope(scope);
    }
  }

  @Override
  protected void generateAsApiCode(final JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    generateCode(declararations, out);
    out.writeSymbol(rBrace);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(lBrace);
    generateCode(declararations, out);
    out.writeSymbolWhitespace(rBrace);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    declararations = analyze(this, declararations, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return lBrace;
  }

}
