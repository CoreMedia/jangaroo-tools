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
import net.jangaroo.jooc.DeclarationScope;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public abstract class NodeImplBase implements AstNode {

  public void generateCode(Collection<? extends AstNode> nodes, JsWriter out, boolean generateApi) throws IOException {
    for (AstNode node : nodes) {
      node.generateCode(out, generateApi);
    }
  }

  @Override
  public void generateCode(final JsWriter out, boolean generateApi) throws IOException {
    if (generateApi) {
      generateAsApiCode(out);
    } else {
      generateJsCode(out);
    }
  }

  public abstract void generateJsCode(final JsWriter out) throws IOException;

  /**
   * Default implementation generates same code as JS. Overwritten where this must differ.
   */
  public void generateAsApiCode(final JsWriter out) throws IOException {
    generateJsCode(out);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
  }

  public <N extends AstNode> void scope(List<N> nodes, Scope scope) {
    for (AstNode node : nodes) {
      node.scope(scope);
    }
  }

  public <N extends AstNode> void analyze(AstNode parent, List<N> nodes, AnalyzeContext context) {
    for (AstNode node : nodes) {
      node.analyze(parent, context);
    }
  }

  public interface Scoped {
    void run(Scope scope);
  }

  public void withNewDeclarationScope(final AstNode definingNode, final Scope scope, final Scoped scoped) {
    scoped.run(new DeclarationScope(definingNode, scope));
  }

  public void withNewLabelScope(final Statement statement, final Scope scope, final Scoped scoped) {
    scoped.run(new LabelScope(statement, scope));
  }

}
