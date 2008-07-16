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
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Andreas Gawecki
 */
public abstract class NodeImplBase implements Node {

  void generateCode(Collection/*<Node>*/ nodes, JsWriter out) throws IOException {
    Iterator iter = nodes.iterator();
    while (iter.hasNext()) {
      NodeImplBase node = (NodeImplBase) iter.next();
      node.generateCode(out);
    }
  }

  public void analyze(AnalyzeContext context) {
    // default is to do nothing
  }

  public void analyze(Collection/*<Node>*/ nodes, AnalyzeContext context) {
    Iterator iter = nodes.iterator();
    while (iter.hasNext()) {
      NodeImplBase node = (NodeImplBase) iter.next();
      node.analyze(context);
    }
  }

}
