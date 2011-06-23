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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.Debug;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Parameter extends IdeDeclaration {

  private JooSymbol optSymConstOrRest;
  private TypeRelation optTypeRelation;
  private Initializer optInitializer;

  public Parameter(JooSymbol optSymConst, Ide ide, TypeRelation optTypeRelation, Initializer optInitializer) {
    super(ide);
    this.setOptSymConstOrRest(optSymConst);
    this.setOptTypeRelation(optTypeRelation);
    this.setOptInitializer(optInitializer);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitParameter(this);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getOptTypeRelation() != null) {
      getOptTypeRelation().scope(scope);
    }
    if (getOptInitializer() != null) {
      getOptInitializer().scope(scope);
    }
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (getOptTypeRelation() !=null) {
      getOptTypeRelation().analyze(this, context);
      if (isRest() && !"Array".equals(getOptTypeRelation().getType().getSymbol().getText())) {
        //todo replace that condition with real Array definition lookup
        throw Jooc.error(getOptTypeRelation().getSymbol(), "Rest parameter must have Array type.");
      }
    }
    if (getOptInitializer() !=null) {
      getOptInitializer().analyze(this, context);
    }
  }

  public boolean isRest() {
    return getOptSymConstOrRest() !=null && getOptSymConstOrRest().sym== sym.REST;
  }

  public void generateAsApiCode(JsWriter out) throws IOException {
    if (getOptSymConstOrRest() != null) {
      out.writeSymbol(getOptSymConstOrRest());
    }
    getIde().generateCode(out, true);
    if (getOptTypeRelation() !=null)
      getOptTypeRelation().generateCode(out, true);
    if (getOptInitializer() != null) {
      getOptInitializer().generateCode(out, true);
    }
  }

  public void generateJsCode(JsWriter out) throws IOException {
    Debug.assertTrue(getModifiers() == 0, "Parameters must not have any modifiers");
    boolean isRest = isRest();
    if (getOptSymConstOrRest() != null) {
      out.beginCommentWriteSymbol(getOptSymConstOrRest());
      if (isRest) {
        getIde().generateCode(out, false);
      }
      out.endComment();
    }
    if (!isRest) {
      getIde().generateCode(out, false);
    }
    if (getOptTypeRelation() !=null)
      getOptTypeRelation().generateCode(out, false);
    // in the method signature, comment out initializer code.
    if (getOptInitializer() != null) {
      out.beginComment();
      getOptInitializer().generateCode(out, false);
      out.endComment();
    }
  }

  public boolean hasInitializer() {
    return getOptInitializer() !=null &&
      // ignore initializers that assign undefined. Parameters are already undefined if not present.
      (!(getOptInitializer().getValue() instanceof IdeExpr) ||
        !((IdeExpr) getOptInitializer().getValue()).getIde().getName().equals("undefined"));
  }

  void generateBodyInitializerCode(JsWriter out, boolean generateApi) throws IOException {
    out.writeToken(getName());
    getOptInitializer().generateCode(out, generateApi);
    out.write(";");
  }

  void generateRestParamCode(JsWriter out, int paramIndex) throws IOException {
    String paramName = getName();
    if (paramName != null && !(paramName.equals("arguments") && paramIndex==0)) {
      out.write("var " + paramName + "=Array.prototype.slice.call(arguments" + (paramIndex == 0 ? "" : "," + paramIndex) + ");");
    }
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return getOptTypeRelation() == null ? null : getOptTypeRelation().getType().resolveDeclaration();
  }


  public JooSymbol getOptSymConstOrRest() {
    return optSymConstOrRest;
  }

  public void setOptSymConstOrRest(JooSymbol optSymConstOrRest) {
    this.optSymConstOrRest = optSymConstOrRest;
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public void setOptTypeRelation(TypeRelation optTypeRelation) {
    this.optTypeRelation = optTypeRelation;
  }

  public Initializer getOptInitializer() {
    return optInitializer;
  }

  public void setOptInitializer(Initializer optInitializer) {
    this.optInitializer = optInitializer;
  }
}
