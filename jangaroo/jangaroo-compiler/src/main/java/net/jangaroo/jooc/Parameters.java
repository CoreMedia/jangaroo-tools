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
public class Parameters extends CommaSeparatedList<Parameter> {

  public Parameters getTail() {
    return (Parameters)tail;
  }

  public Parameters(Parameter param, JooSymbol symComma, Parameters tail) {
    super(param, symComma, tail);
  }

  public Parameters(Parameter param) {
    this(param, null, null);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitParameters(this);
  }

  protected void generateTailCode(JsWriter out) throws IOException {
    if (out.isWriteActionScriptApi() || !tail.head.isRest()) {
      super.generateTailCode(out);
    } else {
      out.beginCommentWriteSymbol(symComma);
      tail.generateCode(out);
      out.endComment();
    }
  }

  public CodeGenerator getParameterInitializerCodeGenerator() {
    return new CodeGenerator() {
      public void generateCode(JsWriter out) throws IOException {
        // first pass: generate conditionals and count parameters.
        int cnt = 0;
        StringBuilder code = new StringBuilder();
        for (Parameters parameters = Parameters.this; parameters!=null; parameters = parameters.getTail()) {
          Parameter param = parameters.head;
          if (param.isRest()) {
            break;
          }
          if (param.hasInitializer()) {
            code.insert(0,"if(arguments.length<"+(cnt+1)+"){");
          }
          ++cnt;
        }
        out.write(code.toString());
        // second pass: generate initializers and rest param code.
        for (Parameters parameters = Parameters.this; parameters!=null; parameters = parameters.getTail()) {
          Parameter param = parameters.head;
          if (param.isRest()) {
            param.generateRestParamCode(out, cnt);
            break;
          }
          if (param.hasInitializer()) {
            param.generateBodyInitializerCode(out);
            out.write("}");
          }
        }
      }
    };
  }

  public String getRestParamName() {
    if (head.isRest()) {
      return head.getName();
    }
    return tail==null ? null : getTail().getRestParamName();
  }

  public int getOtherParamCount() {
    if (head.isRest()) {
      return 0;
    }
    int tailOtherParamCount = tail==null ? -1 : getTail().getOtherParamCount();
    return tailOtherParamCount==-1 ? tailOtherParamCount : 1+tailOtherParamCount;
  }
}
