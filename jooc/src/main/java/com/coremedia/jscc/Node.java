/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
interface Node {

  JscSymbol getSymbol();
  void generateCode(JsWriter out) throws IOException;
  void analyze(AnalyzeContext context);

}
