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

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 12:08:33
 * To change this template use File | Settings | File Templates.
 */
public class Context {
  ArrayList/*CompilationUnit*/ list;

  public Context(ArrayList/*CompilationUnit*/ list) {
    this.list = list;
  }

  public ArrayList getList() {
    return list;
  }
}
