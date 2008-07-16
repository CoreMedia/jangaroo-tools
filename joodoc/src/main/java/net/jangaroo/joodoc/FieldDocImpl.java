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

package net.jangaroo.joodoc;

import com.sun.javadoc.*;
import com.sun.javadoc.Type;
import net.jangaroo.jooc.*;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 11:18:10
 * To change this template use File | Settings | File Templates.
 */
public class FieldDocImpl extends MemberDocImpl implements FieldDoc {

  public FieldDocImpl(FieldDeclaration decl) {
    super (decl);
  }

  public boolean isTransient() {
    return false;
  }

  public boolean isVolatile() {
    return false;
  }

  public SerialFieldTag[] serialFieldTags() {
    return new SerialFieldTag[0];
  }

  public Type type() {
    return getType();
  }

  public Object constantValue() {
    return null;
  }

  public String constantValueExpression() {
    return null;
  }

  public boolean isField() {
    return true;
  }

  

}
