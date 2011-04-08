/*
 * Copyright 2011 CoreMedia AG
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
public class ClassBody extends AbstractBlock {


  public ClassBody(JooSymbol lBrace, List<Directive> directives, JooSymbol rBrace) {
    super(rBrace, directives, lBrace);
    for (Directive directive :directives) {
      directive.setClassMember(true);
    }
  }

  public void scope(Scope staticScope, Scope instanceScope) {
    for (Directive directive : directives) {
      directive.scope(directive.isStatic() ? staticScope : instanceScope);
    }
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    for (Directive directive : directives) {
      directive.generateAsApiCode(out);
    }
    out.writeSymbol(rBrace);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(lBrace);
    boolean inStaticInitializerBlock = false;
    for (Directive directive : directives) {
      final boolean isStaticInitializer = !(directive instanceof Declaration); //todo handle directives which are not statements
      if (isStaticInitializer) {
        inStaticInitializerBlock = beginStaticInitializer(out, inStaticInitializerBlock);
      } else {
        inStaticInitializerBlock = endStaticInitializer(out, inStaticInitializerBlock);
      }
      directive.generateJsCode(out);
    }
    endStaticInitializer(out, inStaticInitializerBlock);
    out.writeSymbolWhitespace(rBrace);
  }

  private boolean beginStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (!inStaticInitializerBlock) {
      out.writeToken("function(){");
    }
    return true;
  }

  private boolean endStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (inStaticInitializerBlock) {
      out.writeToken("},");
    }
    return false;
  }

}
