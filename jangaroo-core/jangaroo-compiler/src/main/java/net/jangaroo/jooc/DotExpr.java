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
 * @author Frank Wienberg
 */
class DotExpr extends PostfixOpExpr {

  Ide ide;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(symDot, expr);
    this.ide = ide;
  }

  public Ide getIde() {
    return ide;
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    ide.scope(scope);
  }

  @Override
  public void analyze(final AstNode parentNode, final AnalyzeContext context) {
    super.analyze(parentNode, context);
    IdeDeclaration qualiferType = arg.getType();
    if (qualiferType != null) {
      IdeDeclaration memberDeclaration = arg.getType().resolvePropertyDeclaration(ide.getName());
      if (memberDeclaration != null && memberDeclaration.isStatic()) {
        throw Jooc.error(ide.ide, "static member used in dynamic context");
      }
      setType(memberDeclaration);
    }

  }

  @Override
  protected void generateJsCode(final JsWriter out) throws IOException {
    arg.generateCode(out);
    Ide.writeMemberAccess(Ide.resolveMember(arg.getType(), ide), op, ide, true, out);
  }

}
