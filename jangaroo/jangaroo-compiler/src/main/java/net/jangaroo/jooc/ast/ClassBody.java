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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

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

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitClassBody(this);
  }

  public void scope(Scope staticScope, Scope instanceScope) {
    for (Directive directive : getDirectives()) {
      directive.scope(directive.isStatic() ? staticScope : instanceScope);
    }
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(getLBrace());
    for (Directive directive : getDirectives()) {
      directive.generateAsApiCode(out);
    }
    out.writeSymbol(getRBrace());
  }

  public void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(getLBrace());
    boolean inStaticInitializerBlock = false;
    for (Directive directive : getDirectives()) {
      final boolean isStaticInitializer = directive instanceof Statement && !(directive instanceof Declaration);
      if (isStaticInitializer) {
        inStaticInitializerBlock = beginStaticInitializer(out, inStaticInitializerBlock);
      } else {
        inStaticInitializerBlock = endStaticInitializer(out, inStaticInitializerBlock);
      }
      directive.generateJsCode(out);
    }
    endStaticInitializer(out, inStaticInitializerBlock);
    out.writeSymbolWhitespace(getRBrace());
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
