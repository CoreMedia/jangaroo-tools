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

import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public abstract class Expr extends NodeImplBase {

  private ExpressionType type;
  private AS3Type coerceTo;

  public ExpressionType getType() {
    return type;
  }

  public AS3Type getAS3Type() {
    return type == null ? null : type.getAS3Type();
  }

  public void setType(final ExpressionType type) {//TODO compute type in more subclasses during analyze()
    this.type = type;
  }

  CompilationUnit applyCoercionIfNeeded(ExpressionType targetType) {
    if (targetType != null
            && !isCompileTimeConstant() // Assume devs know what they're doing when using compile-time constants:
            && targetType.needsCoercion(getType())) {
      coerceTo = targetType.getAS3Type();
      return targetType.getDeclaration().getCompilationUnit();
    }
    return null;
  }

  public AS3Type getCoerceTo() {
    return coerceTo;
  }

  public boolean isRuntimeConstant() {
    return false;
  }

  public boolean isCompileTimeConstant() {
    return false;
  }

  /**
   * Whether this expression denotes a stand-alone constant, that is, a constant
   * that can be derived without looking at other classes.
   *
   * @return whether this expression denotes a stand-alone constant
   */
  public boolean isStandAloneConstant() {
    return isRuntimeConstant();
  }

}
