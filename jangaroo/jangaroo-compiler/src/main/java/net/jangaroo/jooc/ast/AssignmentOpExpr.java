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
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.sym;

import java.io.IOException;


/**
 * @author Andreas Gawecki
 */
public class AssignmentOpExpr extends BinaryOpExpr {
  public AssignmentOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitAssignmentOpExpr(this);
  }

  @Override
  public void generateJsCode(JsWriter out) throws IOException {
    if (getOp().sym == sym.ANDANDEQ || getOp().sym == sym.OROREQ) {
      getArg1().generateJsCode(out);
      out.writeSymbolWhitespace(getOp());
      out.writeToken("=");
      // TODO: refactor for a simpler way to switch of white-space temporarily:
      JoocConfiguration options = (JoocConfiguration)out.getOptions();
      boolean debug = options.isDebug();
      boolean debugLines = options.isDebugLines();
      options.setDebug(false);
      options.setDebugLines(false);
      getArg1().generateJsCode(out);
      options.setDebug(debug);
      options.setDebugLines(debugLines);
      out.writeToken(getOp().sym == sym.ANDANDEQ ? "&&" : "||");
      out.writeToken("(");
      getArg2().generateCode(out, false);
      out.writeToken(")");
    } else {
      super.generateJsCode(out);
    }
  }
}
