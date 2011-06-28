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
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitClassBody(this);
  }

  public void scope(Scope staticScope, Scope instanceScope) {
    for (Directive directive : getDirectives()) {
      directive.scope(directive.isStatic() ? staticScope : instanceScope);
    }
  }
}
