package com.coremedia.jscc;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: agawecki
 * Date: 17.09.2003
 * Time: 17:19:19
 * To change this template use Options | File Templates.
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
