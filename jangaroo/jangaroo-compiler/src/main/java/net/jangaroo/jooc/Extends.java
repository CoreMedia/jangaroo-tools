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
public class Extends extends NodeImplBase {

  JooSymbol symExtends;
  Ide superClass;

  public Extends(JooSymbol extds, Ide superClass) {
    this.symExtends = extds;
    this.superClass = superClass;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitExtends(this);
  }

  @Override
  public void scope(final Scope scope) {
    superClass.scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (!(superClass.getDeclaration() instanceof ClassDeclaration)) {
      throw new Jooc.CompilerError(superClass.getSymbol(), "identifier in extends clause must denote a class");
    }
    superClass.analyze(this, context);
    superClass.addExternalUsage();
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(symExtends);
    superClass.generateCodeAsExpr(out);
  }

  public JooSymbol getSymbol() {
    return symExtends;
  }

}
