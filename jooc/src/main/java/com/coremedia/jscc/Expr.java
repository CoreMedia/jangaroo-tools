/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
abstract class Expr extends NodeImplBase {

  boolean isCompileTimeConstant() {
    return false;
  }

}
