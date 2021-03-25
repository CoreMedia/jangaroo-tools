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

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;
import java.util.List;

/**
 * @author Frank Wienberg
 */
public class NamespaceDeclaration extends IdeDeclaration {

  private Initializer optInitializer;
  private JooSymbol symNamespace;
  private JooSymbol optSymSemicolon;

  public NamespaceDeclaration(AnnotationsAndModifiers am,
                              JooSymbol symNamespace,
                              Ide ide,
                              Initializer optInitializer,
                              JooSymbol optSymSemicolon) {
    super(am, ide);
    assert SyntacticKeywords.NAMESPACE.equals(symNamespace.getText());
    this.symNamespace = symNamespace;
    this.optInitializer = optInitializer;
    this.optSymSemicolon = optSymSemicolon;
  }

  public NamespaceDeclaration(AnnotationsAndModifiers am,
                              Initializer optInitializer,
                              JooSymbol optSymSemicolon) {
    this(new AnnotationsAndModifiers(am.getAnnotations(), am.getModifiers().subList(0, am.getModifiers().size() - 2)),
            am.getModifiers().get(am.getModifiers().size() - 2),
            new Ide(am.getModifiers().get(am.getModifiers().size() - 1)),
            optInitializer,
            optSymSemicolon);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optInitializer);
  }

  @Override
  public JooSymbol getDeclarationSymbol() {
    return getSymNamespace();
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitNamespaceDeclaration(this);
  }

  public Initializer getOptInitializer() {
    return optInitializer;
  }

  public JooSymbol getSymNamespace() {
    return symNamespace;
  }

  public JooSymbol getOptSymSemicolon() {
    return optSymSemicolon;
  }

  @Override
  protected int getAllowedModifiers() {
    return MODIFIER_PUBLIC | MODIFIER_INTERNAL;
  }
}
