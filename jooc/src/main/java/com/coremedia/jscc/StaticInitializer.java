/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class StaticInitializer extends Declaration {

  BlockStatement block;

  public StaticInitializer(BlockStatement block) {
    super(new JscSymbol[]{ }, MODIFIER_STATIC);
    this.block = block;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    out.write("\"static\", function()");
    block.generateCode(out);
    out.write(",");
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    block.analyze(context);
  }

  public JscSymbol getSymbol() {
    return symModifiers[0];
  }
}
