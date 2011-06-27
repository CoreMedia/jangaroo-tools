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

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Parameters extends CommaSeparatedList<Parameter> {

  public Parameters getTail() {
    return (Parameters) super.getTail();
  }

  public Parameters(Parameter param, JooSymbol symComma, Parameters tail) {
    super(param, symComma, tail);
  }

  public Parameters(Parameter param) {
    this(param, null, null);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitParameters(this);
  }
}
