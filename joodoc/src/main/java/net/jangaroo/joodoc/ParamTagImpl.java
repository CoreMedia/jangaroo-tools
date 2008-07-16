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

import com.sun.javadoc.Doc;
import com.sun.javadoc.ParamTag;

public class ParamTagImpl extends TagImpl implements ParamTag {

  private int splitPos;

  public ParamTagImpl(Doc doc, String text) {
    super(doc, "@param", "@param", text);
    splitPos = Util.getIdentifierLength(text);
    //System.out.println("**************\n"+this+"****************\n\n");
  }

  protected String getInlineText() {
    return parameterComment();
  }

  public String parameterComment() {
    return text.substring(splitPos).trim();
  }

  public String parameterName() {
    return text.substring(0,splitPos).trim();
  }

  public String toString() {
    return "@param '"+parameterName()+"': '"+parameterComment()+"'";
  }

  public boolean isTypeParameter() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
