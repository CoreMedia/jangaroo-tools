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

/**
 * @author Andreas Gawecki
 */
public abstract class NodeImplBase implements Node {

  Node parentNode;

  void generateCode(Collection<? extends Node> nodes, JsWriter out) throws IOException {
    for (Node node : nodes) {
      node.generateCode(out);
    }
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    this.parentNode = parentNode;
    return this;
  }

  public void analyze(Node parent, Collection<? extends Node> nodes, AnalyzeContext context) {
    for (Node node : nodes) {
      node.analyze(parent, context);
    }
  }

}
