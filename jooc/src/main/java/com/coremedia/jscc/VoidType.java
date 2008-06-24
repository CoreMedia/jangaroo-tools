/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
class VoidType extends IdeType {
  
  public VoidType(JscSymbol symVoid) {
    super(new Ide(symVoid));
  }
}
