/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

abstract class Expr extends NodeImplBase {

  boolean isCompileTimeConstant() {
    return false;
  }

}
