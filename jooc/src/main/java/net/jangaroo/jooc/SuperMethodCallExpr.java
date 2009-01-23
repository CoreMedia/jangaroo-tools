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
class SuperMethodCallExpr extends ApplyExpr {

  protected JooSymbol symSuper;
  protected Ide methodIde;

  protected ClassDeclaration classDeclaration;

  public SuperMethodCallExpr(JooSymbol symSuper, JooSymbol symDot, Ide ide, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    //TODO: define super ide
    super(new DotExpr(new IdeExpr(new Ide(symSuper)), symDot, ide),lParen,args,rParen);
    this.methodIde = ide;
    this.symSuper = symSuper;
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSuper);
    //TODO:report error on super calls within closures
    out.writeToken("this[$");
    out.write(methodIde.getName());
    out.writeToken("]");
  }

  public void analyze(AnalyzeContext context) {
    //TODO:check whether called method is overridden
    //TODO:analyze super
    this.classDeclaration = context.getCurrentClass();
    if (args != null)
      args.analyze(context);
  }

}
