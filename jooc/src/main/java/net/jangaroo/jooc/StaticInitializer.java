/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class StaticInitializer extends Declaration {

  BlockStatement block;

  public StaticInitializer(BlockStatement block) {
    super(new JooSymbol[]{ }, MODIFIER_STATIC);
    this.block = block;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    out.write("function()");
    block.generateCode(out);
    out.write(",");
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    block.analyze(context);
  }

  public JooSymbol getSymbol() {
    return symModifiers[0];
  }
}
