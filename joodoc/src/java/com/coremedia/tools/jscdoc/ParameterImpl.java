package com.coremedia.tools.jscdoc;

import com.coremedia.jscc.NodeImplBase;
import com.coremedia.jscc.TypeRelation;
import com.sun.javadoc.Type;
import com.sun.javadoc.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 17:16:15
 * To change this template use File | Settings | File Templates.
 */
public class ParameterImpl implements Parameter{
  com.coremedia.jscc.Parameter param;
  public ParameterImpl(com.coremedia.jscc.Parameter param) {
    this.param=param;
  }

  public Type type() {
    TypeRelation optTypeRelation = param.getOptTypeRelation();
    return optTypeRelation==null ? TypeImpl.ANY
      : (Type)DocMap.getDoc(optTypeRelation.getType());
  }

  public String name() {
    return param.getSymbol().getText();
  }

  public String typeName() {
    String typeName = type().typeName();
    return typeName;
  }
}
