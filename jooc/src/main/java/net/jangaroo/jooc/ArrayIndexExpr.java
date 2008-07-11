/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;


/**
 * @author Andreas Gawecki
 */
class ArrayIndexExpr extends ApplyExpr {

  public ArrayIndexExpr(Expr fun, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    super(fun,lParen,args,rParen);
  }

}
