/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class ThisExpr extends IdeExpr {

  public ThisExpr(JooSymbol symThis) {
    super(new Ide(symThis));
  }
  
}
