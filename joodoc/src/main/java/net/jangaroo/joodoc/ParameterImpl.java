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

import net.jangaroo.jooc.TypeRelation;
import net.jangaroo.jooc.sym;
import com.sun.javadoc.Type;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.AnnotationDesc;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 17:16:15
 * To change this template use File | Settings | File Templates.
 */
public class ParameterImpl implements Parameter{
  net.jangaroo.jooc.Parameter param;
  public ParameterImpl(net.jangaroo.jooc.Parameter param) {
    this.param=param;
  }

  public Type type() {
    TypeRelation optTypeRelation = param.getOptTypeRelation();
    return optTypeRelation==null ? TypeImpl.ANY
      : (Type)DocMap.getDoc(optTypeRelation.getType());
  }

  public String name() {
    return param.getSymbol().getText();
  }

  public String typeName() {
    String typeName = type().typeName();
    return typeName;
  }

  public AnnotationDesc[] annotations() {
    return new AnnotationDesc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

/*   *//**
094:             * Get the annotations of this parameter.
095:             * Return an empty array if there are none.
096:             *//*
097:            public AnnotationDesc[] annotations() {
098:                AnnotationDesc res[] = new AnnotationDesc[sym
099:                        .getAnnotationMirrors().length()];
100:                int i = 0;
101:                for (Attribute.Compound a : sym.getAnnotationMirrors()) {
102:                    res[i++] = new AnnotationDescImpl(env, a);
103:                }
104:                return res;
105:            }*//*
  */
}
