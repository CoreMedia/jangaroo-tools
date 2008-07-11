/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
abstract class Expr extends NodeImplBase {

  boolean isCompileTimeConstant() {
    return false;
  }

}
