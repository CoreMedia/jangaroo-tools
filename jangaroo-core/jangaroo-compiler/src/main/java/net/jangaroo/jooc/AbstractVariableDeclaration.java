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
abstract class AbstractVariableDeclaration extends TypedIdeDeclaration {

  JooSymbol optSymConstOrVar;
  Initializer optInitializer;
  AbstractVariableDeclaration optNextVariableDeclaration;

  JooSymbol optSymSemicolon;

  protected AbstractVariableDeclaration(JooSymbol[] modifiers, int allowedModifiers, JooSymbol optSymConstOrVar, Ide ide,
                                        TypeRelation optTypeRelation, Initializer optInitializer, AbstractVariableDeclaration optNextVariableDeclaration, JooSymbol optSymSemicolon
  ) {
    super(modifiers, allowedModifiers, ide, optTypeRelation);
    this.optSymConstOrVar = optSymConstOrVar;
    this.optInitializer = optInitializer;
    this.optNextVariableDeclaration = optNextVariableDeclaration;
    this.optSymSemicolon = optSymSemicolon;
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

  public void generateCode(JsWriter out) throws IOException {
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

  protected abstract void generateStartCode(JsWriter out) throws IOException;

  protected void generateInitializerCode(JsWriter out) throws IOException {
    if (optInitializer != null) {
      optInitializer.generateCode(out);
    }
  }

  protected void generateEndCode(JsWriter out) throws IOException {
    if (optSymSemicolon != null) {
      out.writeSymbol(optSymSemicolon);
    }
  }

  protected boolean hasPreviousVariableDeclaration() {
    return parentNode instanceof AbstractVariableDeclaration;
  }

  protected AbstractVariableDeclaration getPreviousVariableDeclaration() {
    return (AbstractVariableDeclaration) parentNode;
  }

  protected AbstractVariableDeclaration getFirstVariableDeclaration() {
    AbstractVariableDeclaration firstVariableDeclaration = this;
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
    AbstractVariableDeclaration firstVariableDeclaration = getFirstVariableDeclaration();
    return firstVariableDeclaration.optSymConstOrVar != null && firstVariableDeclaration.optSymConstOrVar.sym == sym.CONST;
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
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
    return this;
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return optTypeRelation == null ? null : optTypeRelation.getType().resolveDeclaration();
  }
}
