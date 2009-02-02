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
public class Parameter extends AbstractVariableDeclaration {

  public Parameter(JooSymbol optSymConst, Ide ide, TypeRelation typeRelation, Initializer optInitializer) {
    super(new JooSymbol[0], 0, optSymConst, ide, typeRelation, optInitializer, null);
  }

  public boolean isRest() {
    return optSymConstOrVar!=null && optSymConstOrVar.sym==sym.REST;
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    Debug.assertTrue(getModifiers() == 0, "Parameters must not have any modifiers");
    boolean isRest = isRest();
    if (optSymConstOrVar != null) {
      out.beginCommentWriteSymbol(optSymConstOrVar);
      if (isRest) {
        ide.generateCode(out);
      }
      out.endComment();
    }
    if (!isRest) {
      ide.generateCode(out);
    }
  }

  @Override
  protected void generateInitializerCode(JsWriter out) throws IOException {
    // in the method signature, as comment only:
    out.beginComment();
    super.generateInitializerCode(out);
    out.endComment();
  }

  void generateBodyInitializerCode(JsWriter out) throws IOException {
    if (optInitializer!=null) {
      out.writeToken(getName());
      optInitializer.generateCode(out);
      out.write(";");
    }
  }

  void generateRestParamCode(JsWriter out, int paramIndex) throws IOException {
    String paramName = getName();
    if (paramName != null && !(paramName.equals("arguments") && paramIndex==0)) {
      out.write("var "+paramName+"=");
      if (paramIndex==0) {
        out.write("arguments;");
      } else {
        out.write("Array.prototype.slice.call(arguments,"+paramIndex+");");
      }
    }
  }

}
