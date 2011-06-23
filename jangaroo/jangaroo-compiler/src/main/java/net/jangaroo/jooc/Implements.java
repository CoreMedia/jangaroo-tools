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

/**
 * @author Andreas Gawecki
 */
class Implements extends NodeImplBase {
  JooSymbol symImplements;
  CommaSeparatedList<Ide> superTypes;

  public Implements(JooSymbol symImplements, CommaSeparatedList<Ide> superTypes) {
    this.symImplements = symImplements;
    this.superTypes = superTypes;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitImplements(this);
  }

  @Override
  public void scope(final Scope scope) {
    superTypes.scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    superTypes.analyze(this, context);
    CommaSeparatedList<Ide> superTypes = this.superTypes;
    while (superTypes != null) {
      Ide superType = superTypes.head;
      superType.addExternalUsage();
      superTypes = superTypes.tail;
    }
  }

  protected void generateJsCode(JsWriter out) throws IOException {
     out.writeSymbol(symImplements);
     generateImplements(superTypes, out);
  }

  private void generateImplements(CommaSeparatedList<Ide> superTypes, JsWriter out) throws IOException {
    superTypes.head.generateCodeAsExpr(out);
    if (superTypes.symComma != null) {
      out.writeSymbol(superTypes.symComma);
      generateImplements(superTypes.tail, out);
    }
  }

  public JooSymbol getSymbol() {
      return symImplements;
  }

}
