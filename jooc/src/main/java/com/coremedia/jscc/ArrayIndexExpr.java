/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;


/**
 * @author Andreas Gawecki
 */
class ArrayIndexExpr extends ApplyExpr {

  public ArrayIndexExpr(Expr fun, JscSymbol lParen, Arguments args, JscSymbol rParen) {
    super(fun,lParen,args,rParen);
  }

}
