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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class VariableDeclaration extends TypedIdeDeclaration {

  private JooSymbol optSymConstOrVar;
  private Initializer optInitializer;
  private VariableDeclaration optNextVariableDeclaration;
  private JooSymbol optSymSemicolon;
  private VariableDeclaration previousVariableDeclaration;

  public VariableDeclaration(JooSymbol[] modifiers,
                             JooSymbol optSymConstOrVar,
                             Ide ide,
                             TypeRelation optTypeRelation,
                             Initializer optInitializer,
                             VariableDeclaration optNextVariableDeclaration,
                             JooSymbol optSymSemicolon  ) {
    // inherit modifiers of first declaration to those following this declaration
    super(modifiers, ide, optTypeRelation);
    this.setOptSymConstOrVar(optSymConstOrVar);
    this.setOptInitializer(optInitializer);
    this.setOptNextVariableDeclaration(optNextVariableDeclaration);
    this.setOptSymSemicolon(optSymSemicolon);
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.previousVariableDeclaration = this;
      if (optSymSemicolon != null) {
        optNextVariableDeclaration.setInheritedModifiers(modifiers);
      }
    }
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitVariableDeclaration(this);
  }

  protected int getAllowedModifiers() {
    return MODIFIERS_SCOPE | MODIFIER_STATIC;
  }

  public VariableDeclaration(JooSymbol symConstOrVar,
                             Ide ide,
                             TypeRelation optTypeRelation,
                             Initializer optInitializer,
                             VariableDeclaration optNextVariableDeclaration,
                             JooSymbol optSymSemicolon) {
    this(new JooSymbol[0], symConstOrVar, ide, optTypeRelation, optInitializer, optNextVariableDeclaration, optSymSemicolon);
  }

  public VariableDeclaration(JooSymbol symConstOrVar,
                             Ide ide,
                             TypeRelation optTypeRelation,
                             Initializer optInitializer,
                             VariableDeclaration optNextVariableDeclaration) {
    this(symConstOrVar, ide, optTypeRelation, optInitializer, optNextVariableDeclaration, null);
  }

  public VariableDeclaration(JooSymbol symConstOrVar,
                             Ide ide,
                             TypeRelation optTypeRelation,
                             Initializer optInitializer) {
    this(symConstOrVar, ide, optTypeRelation, optInitializer, null);
  }

  public VariableDeclaration(JooSymbol symConstOrVar,
                             Ide ide,
                             TypeRelation optTypeRelation) {
    this(symConstOrVar, ide, optTypeRelation, null, null, null);
  }

  @Override
  protected void setInheritedModifiers(final JooSymbol[] modifiers) {
    super.setInheritedModifiers(modifiers);
    if (getOptNextVariableDeclaration() != null) {
      getOptNextVariableDeclaration().setInheritedModifiers(modifiers);
    }
  }

  @Override
  public void setClassMember(boolean classMember) {
    super.setClassMember(classMember);
    if (getOptNextVariableDeclaration() != null) {
      getOptNextVariableDeclaration().setClassMember(classMember);
    }
  }

  @Override
  public boolean isField() {
    return isClassMember();
  }

  public boolean isCompileTimeConstant() {
    return isConst() && (getOptInitializer() == null || getOptInitializer().getValue().isCompileTimeConstant());
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (getOptInitializer() == null && isConst()) {
      throw Jooc.error(getOptSymConstOrVar(), "constant must be initialized");
    }
    if (getOptInitializer() != null) {
      getOptInitializer().analyze(this, context);
    }
    if (getOptNextVariableDeclaration() != null) {
      getOptNextVariableDeclaration().analyze(this, context);
    }
    if (isClassMember() && !isStatic() && getOptInitializer() != null && !getOptInitializer().getValue().isCompileTimeConstant()) {
      getClassDeclaration().addFieldWithInitializer(this);
    }
  }

  protected void generateStartCode(JsWriter out) throws IOException {
    if (isClassMember()) {
      generateFieldStartCode(out);
    } else {
      generateVarStartCode(out);
    }
  }

  protected void generateVarStartCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    if (getOptSymConstOrVar() != null) {
      if (isConst()) {
        out.beginCommentWriteSymbol(getOptSymConstOrVar());
        out.endComment();
        out.writeToken("var");
      } else {
        out.writeSymbol(getOptSymConstOrVar());
      }
    }
  }

  protected void generateFieldStartCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    if (getOptSymConstOrVar() !=null)
      out.writeSymbol(getOptSymConstOrVar());
    out.endString();
    out.write(",{");
  }

  private static final Map<String,String> DEFAULT_VALUE_BY_TYPE = new HashMap<String,String>(10);
  static {
    DEFAULT_VALUE_BY_TYPE.put("Boolean", "false");
    DEFAULT_VALUE_BY_TYPE.put("int", "0");
    DEFAULT_VALUE_BY_TYPE.put("Number", "NaN");
    DEFAULT_VALUE_BY_TYPE.put("uint", "0");
    DEFAULT_VALUE_BY_TYPE.put("*", "undefined");
  }

  protected void generateFieldInitializerCode(JsWriter out) throws IOException {
    if (getOptInitializer() != null) {
      out.writeSymbolWhitespace(getOptInitializer().getSymEq());
      out.write(':');
      boolean mustEvaluateAtRuntime = !getOptInitializer().getValue().isCompileTimeConstant();
      if (mustEvaluateAtRuntime) {
        out.writeToken("function(){return(");
      }
      getOptInitializer().getValue().generateJsCode(out);
      if (mustEvaluateAtRuntime) {
        out.writeToken(");}");
      }
    } else {
      TypeRelation typeRelation = this.getOptTypeRelation();
      String emptyValue = getDefaultValue(typeRelation);
      out.write(":" + emptyValue);
    }
  }

  public static String getDefaultValue(TypeRelation typeRelation) {
    String typeName = typeRelation == null ? "*" : typeRelation.getType().getSymbol().getText();
    String emptyValue = DEFAULT_VALUE_BY_TYPE.get(typeName);
    if (emptyValue == null) {
      emptyValue = "null";
    }
    return emptyValue;
  }

  protected void generateFieldEndCode(JsWriter out) throws IOException {
    if (!hasPreviousVariableDeclaration()) {
      out.write('}');
      Debug.assertTrue(getOptSymSemicolon() != null, "optSymSemicolon != null");
      out.writeSymbolWhitespace(getOptSymSemicolon());
      out.writeToken(",");
    }
  }

  protected void generateVarEndCode(JsWriter out) throws IOException {
    if (getOptSymSemicolon() != null) {
      out.writeSymbol(getOptSymSemicolon());
    }
  }

  protected void generateEndCode(JsWriter out) throws IOException {
    if (isClassMember()) {
      generateFieldEndCode(out);
    } else {
      generateVarEndCode(out);
    }
  }

  protected void generateInitializerCode(JsWriter out) throws IOException {
    if (isClassMember()) {
      generateFieldInitializerCode(out);
    } else {
      generateVarInitializerCode(out);
    }
  }

  private void generateVarInitializerCode(JsWriter out) throws IOException {
    if (getOptInitializer() != null) {
      getOptInitializer().generateJsCode(out);
    }
  }

  public void generateInitCode(JsWriter out, boolean endWithSemicolon) throws IOException {
    String accessCode = "this." + getName() + (isPrivate() ? "$" + getClassDeclaration().getInheritanceLevel() : "");
    out.write(accessCode + "=" + accessCode + "()");
    if (endWithSemicolon) {
      out.write(";");
    }
  }

  boolean allowDuplicates(Scope scope) {
    // todo It is "worst practice" to redeclare local variables in AS3, make this configurable:
    return !isClassMember();
  }

  public void generateJsCode(JsWriter out) throws IOException {
    if (hasPreviousVariableDeclaration()) {
      Debug.assertTrue(getOptSymConstOrVar() != null && getOptSymConstOrVar().sym == sym.COMMA, "Additional variable declarations must start with a COMMA.");
      out.writeSymbol(getOptSymConstOrVar());
    } else {
      generateStartCode(out);
    }
    getIde().generateJsCode(out);
    if (getOptTypeRelation() != null) {
      getOptTypeRelation().generateJsCode(out);
    }
    generateInitializerCode(out);
    if (getOptNextVariableDeclaration() != null) {
      getOptNextVariableDeclaration().generateJsCode(out);
    }
    generateEndCode(out);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getOptInitializer() != null) {
      getOptInitializer().scope(scope);
    }
    if (getOptNextVariableDeclaration() != null) {
      getOptNextVariableDeclaration().scope(scope);
    }
  }

  public void generateAsApiCode(JsWriter out) throws IOException {
    if (!isPrivate()) {
      writeModifiers(out);
      out.writeSymbol(getOptSymConstOrVar());
      getIde().generateAsApiCode(out);
      if (getOptTypeRelation() != null) {
        getOptTypeRelation().generateAsApiCode(out);
      }
      if (getOptInitializer() != null) {
        getOptInitializer().generateAsApiCode(out);
      }
      if (getOptNextVariableDeclaration() != null) {
        getOptNextVariableDeclaration().generateAsApiCode(out);
      }
      if (getOptSymSemicolon() != null) {
        out.writeSymbol(getOptSymSemicolon());
      }
    }
  }

  protected boolean hasPreviousVariableDeclaration() {
    return previousVariableDeclaration != null;
  }

  protected VariableDeclaration getPreviousVariableDeclaration() {
    return previousVariableDeclaration;
  }

  protected VariableDeclaration getFirstVariableDeclaration() {
    VariableDeclaration firstVariableDeclaration = this;
    while (firstVariableDeclaration.hasPreviousVariableDeclaration()) {
      firstVariableDeclaration = firstVariableDeclaration.getPreviousVariableDeclaration();
    }
    return firstVariableDeclaration;
  }

  @Override
  protected int getModifiers() {
    return hasPreviousVariableDeclaration()
        ? getFirstVariableDeclaration().getModifiers()
        : super.getModifiers();
  }

  public boolean isConst() {
    VariableDeclaration firstVariableDeclaration = getFirstVariableDeclaration();
    return firstVariableDeclaration.getOptSymConstOrVar() != null && firstVariableDeclaration.getOptSymConstOrVar().sym == sym.CONST;
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return getOptTypeRelation() == null ? null : getOptTypeRelation().getType().resolveDeclaration();
  }

  public JooSymbol getOptSymConstOrVar() {
    return optSymConstOrVar;
  }

  public void setOptSymConstOrVar(JooSymbol optSymConstOrVar) {
    this.optSymConstOrVar = optSymConstOrVar;
  }

  public Initializer getOptInitializer() {
    return optInitializer;
  }

  public void setOptInitializer(Initializer optInitializer) {
    this.optInitializer = optInitializer;
  }

  public VariableDeclaration getOptNextVariableDeclaration() {
    return optNextVariableDeclaration;
  }

  public void setOptNextVariableDeclaration(VariableDeclaration optNextVariableDeclaration) {
    this.optNextVariableDeclaration = optNextVariableDeclaration;
  }

  public JooSymbol getOptSymSemicolon() {
    return optSymSemicolon;
  }

  public void setOptSymSemicolon(JooSymbol optSymSemicolon) {
    this.optSymSemicolon = optSymSemicolon;
  }
}
