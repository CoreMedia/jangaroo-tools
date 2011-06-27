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
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class TryStatement extends KeywordStatement {

  private BlockStatement block;
  private List<Catch> catches;
  private JooSymbol symFinally;
  private BlockStatement finallyBlock;

  public TryStatement(JooSymbol symTry, BlockStatement block, List<Catch> catches) {
    this(symTry, block, catches, null, null);
  }

  public TryStatement(JooSymbol symTry, BlockStatement block, List<Catch> catches, JooSymbol symFinally, BlockStatement finallyBlock) {
    super(symTry);
    this.setBlock(block);
    this.setCatches(catches);
    this.setSymFinally(symFinally);
    this.setFinallyBlock(finallyBlock);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitTryStatement(this);
  }

    @Override
  public void scope(final Scope scope) {
    getBlock().scope(scope);
    scope(getCatches(), scope);
    if (getFinallyBlock() != null)
      getFinallyBlock().scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getBlock().analyze(this, context);
    analyze(this, getCatches(), context);
    if (getFinallyBlock() != null)
      getFinallyBlock().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    throw new UnsupportedOperationException();
  }

  public BlockStatement getBlock() {
    return block;
  }

  public void setBlock(BlockStatement block) {
    this.block = block;
  }

  public List<Catch> getCatches() {
    return catches;
  }

  public void setCatches(List<Catch> catches) {
    this.catches = catches;
  }

  public JooSymbol getSymFinally() {
    return symFinally;
  }

  public void setSymFinally(JooSymbol symFinally) {
    this.symFinally = symFinally;
  }

  public BlockStatement getFinallyBlock() {
    return finallyBlock;
  }

  public void setFinallyBlock(BlockStatement finallyBlock) {
    this.finallyBlock = finallyBlock;
  }
}
