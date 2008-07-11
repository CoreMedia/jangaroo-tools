/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
interface Node {

  JooSymbol getSymbol();
  void generateCode(JsWriter out) throws IOException;
  void analyze(AnalyzeContext context);

}
