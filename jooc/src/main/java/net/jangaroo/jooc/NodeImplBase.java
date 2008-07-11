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
