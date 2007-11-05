package com.coremedia.tools.jscdoc;

import com.coremedia.jscc.IdeType;
import com.sun.javadoc.*;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 16:38:53
 * To change this template use File | Settings | File Templates.
 */
public class TypeImpl implements Type {

  public static final Type ANY = new TypeImpl(null);

  com.coremedia.jscc.IdeType type;

  public TypeImpl(IdeType type) {
    this.type = type;
  }

  public ClassDoc asClassDoc() {
    return null;
  }

  public String dimension() {
    return "";
  }

  public String qualifiedTypeName() {
    if (type!= null) {
      String[] qual=type.getIde().getQualifiedName();
      return Util.getQualifiedName(qual);
    }
    return "Object";
  }

  public String typeName() {
    if (type!=null)
      return type.getIde().getName();
    else return "Object";
  }

}