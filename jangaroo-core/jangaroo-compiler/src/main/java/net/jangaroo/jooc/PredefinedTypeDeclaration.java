/*
 * Copyright 2010 CoreMedia AG
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
public class PredefinedTypeDeclaration extends IdeDeclaration {

  //todo define well-known types as final consts here

  public PredefinedTypeDeclaration(final String name) {
    super(new Ide(new JooSymbol(name)));
  }

  @Override
  protected int getAllowedModifiers() {
    return MODIFIER_PUBLIC;
  }

  @Override
  protected void generateJsCode(final JsWriter out) throws IOException {
    throw new UnsupportedOperationException("there should be no code generation for predefined types");
  }

}
