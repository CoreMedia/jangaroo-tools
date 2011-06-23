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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class VariableDeclaration extends TypedIdeDeclaration {

  JooSymbol optSymConstOrVar;
  Initializer optInitializer;
  VariableDeclaration optNextVariableDeclaration;
  JooSymbol optSymSemicolon;
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
    this.optSymConstOrVar = optSymConstOrVar;
    this.optInitializer = optInitializer;
    this.optNextVariableDeclaration = optNextVariableDeclaration;
    this.optSymSemicolon = optSymSemicolon;
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
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.setInheritedModifiers(modifiers);
    }
  }

  @Override
  public void setClassMember(boolean classMember) {
    super.setClassMember(classMember);
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.setClassMember(classMember);
    }
  }

  @Override
  public boolean isField() {
    return isClassMember();
  }

  public boolean isCompileTimeConstant() {
    return isConst() && (optInitializer == null || optInitializer.value.isCompileTimeConstant());
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (optInitializer == null && isConst()) {
      throw Jooc.error(optSymConstOrVar, "constant must be initialized");
    }
    if (optInitializer != null) {
      optInitializer.analyze(this, context);
    }
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.analyze(this, context);
    }
    if (isClassMember() && !isStatic() && optInitializer != null && !optInitializer.value.isCompileTimeConstant()) {
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
    if (optSymConstOrVar != null) {
      if (isConst()) {
        out.beginCommentWriteSymbol(optSymConstOrVar);
        out.endComment();
        out.writeToken("var");
      } else {
        out.writeSymbol(optSymConstOrVar);
      }
    }
  }

  protected void generateFieldStartCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    if (optSymConstOrVar!=null)
      out.writeSymbol(optSymConstOrVar);
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
    if (optInitializer != null) {
      out.writeSymbolWhitespace(optInitializer.symEq);
      out.write(':');
      boolean mustEvaluateAtRuntime = !optInitializer.value.isCompileTimeConstant();
      if (mustEvaluateAtRuntime) {
        out.writeToken("function(){return(");
      }
      optInitializer.value.generateCode(out);
      if (mustEvaluateAtRuntime) {
        out.writeToken(");}");
      }
    } else {
      TypeRelation typeRelation = this.optTypeRelation;
      String emptyValue = getDefaultValue(typeRelation);
      out.write(":" + emptyValue);
    }
  }

  static String getDefaultValue(TypeRelation typeRelation) {
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
      Debug.assertTrue(optSymSemicolon != null, "optSymSemicolon != null");
      out.writeSymbolWhitespace(optSymSemicolon);
      out.writeToken(",");
    }
  }

  protected void generateVarEndCode(JsWriter out) throws IOException {
    if (optSymSemicolon != null) {
      out.writeSymbol(optSymSemicolon);
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
    if (optInitializer != null) {
      optInitializer.generateCode(out);
    }
  }

  public void generateInitCode(JsWriter out, boolean endWithSemicolon) throws IOException {
    String accessCode = "this." + getName() + (isPrivate() ? "$" + classDeclaration.getInheritanceLevel() : "");
    out.write(accessCode + "=" + accessCode + "()");
    if (endWithSemicolon) {
      out.write(";");
    }
  }

  boolean allowDuplicates(Scope scope) {
    // todo It is "worst practice" to redeclare local variables in AS3, make this configurable:
    return !isClassMember();
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    if (hasPreviousVariableDeclaration()) {
      Debug.assertTrue(optSymConstOrVar != null && optSymConstOrVar.sym == sym.COMMA, "Additional variable declarations must start with a COMMA.");
      out.writeSymbol(optSymConstOrVar);
    } else {
      generateStartCode(out);
    }
    ide.generateCode(out);
    if (optTypeRelation != null) {
      optTypeRelation.generateCode(out);
    }
    generateInitializerCode(out);
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.generateCode(out);
    }
    generateEndCode(out);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (optInitializer != null) {
      optInitializer.scope(scope);
    }
    if (optNextVariableDeclaration != null) {
      optNextVariableDeclaration.scope(scope);
    }
  }

  protected void generateAsApiCode(JsWriter out) throws IOException {
    if (!isPrivate()) {
      writeModifiers(out);
      out.writeSymbol(optSymConstOrVar);
      ide.generateCode(out);
      if (optTypeRelation != null) {
        optTypeRelation.generateCode(out);
      }
      if (optInitializer != null) {
        optInitializer.generateAsApiCode(out);
      }
      if (optNextVariableDeclaration != null) {
        optNextVariableDeclaration.generateCode(out);
      }
      if (optSymSemicolon != null) {
        out.writeSymbol(optSymSemicolon);
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
    return firstVariableDeclaration.optSymConstOrVar != null && firstVariableDeclaration.optSymConstOrVar.sym == sym.CONST;
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return optTypeRelation == null ? null : optTypeRelation.getType().resolveDeclaration();
  }
}
