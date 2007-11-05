/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

class ThisExpr extends IdeExpr {

  public ThisExpr(JscSymbol symThis) {
    super(new Ide(symThis));
  }
  
}
