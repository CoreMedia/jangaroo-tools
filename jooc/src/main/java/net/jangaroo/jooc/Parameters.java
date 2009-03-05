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
public class Parameters extends NodeImplBase {

  Parameter param;
  JooSymbol symComma;

  public Parameter getHead() {
    return param;
  }

  public Parameters getTail() {
    return tail;
  }

  Parameters tail;

  public Parameters(Parameter param, JooSymbol symComma, Parameters tail) {
    this.param = param;
    this.symComma = symComma;
    this.tail = tail;
  }

  public Parameters(Parameter param) {
    this(param, null, null);
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    param.analyze(this, context);
    if (tail != null)
      tail.analyze(this, context);
    return this;
  }

  public void generateCode(JsWriter out) throws IOException {
    param.generateCode(out);
    if (symComma != null) {
      if (!tail.param.isRest()) {
        out.writeSymbol(symComma);
        tail.generateCode(out);
      } else {
        out.beginCommentWriteSymbol(symComma);
        tail.generateCode(out);
        out.endComment();
      }
    }
  }

  public CodeGenerator getParameterInitializerCodeGenerator() {
    return new CodeGenerator() {
      public void generateCode(JsWriter out) throws IOException {
        // first pass: generate conditionals and count parameters.
        int cnt = 0;
        StringBuilder code = new StringBuilder();
        for (Parameters parameters = Parameters.this; parameters!=null; parameters = parameters.tail) {
          Parameter param = parameters.param;
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
        for (Parameters parameters = Parameters.this; parameters!=null; parameters = parameters.tail) {
          Parameter param = parameters.param;
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

  public JooSymbol getSymbol() {
    return param.getSymbol();
  }

  public String getRestParamName() {
    if (param.isRest()) {
      return param.getName();
    }
    return tail==null ? null : tail.getRestParamName();
  }

  public int getOtherParamCount() {
    if (param.isRest()) {
      return 0;
    }
    int tailOtherParamCount = tail==null ? -1 : tail.getOtherParamCount();
    return tailOtherParamCount==-1 ? tailOtherParamCount : 1+tailOtherParamCount;
  }
}
