package com.coremedia.tools.jscdoc;

import com.coremedia.jscc.NodeImplBase;
import com.coremedia.jscc.TypeRelation;
import com.coremedia.jscc.sym;
import com.sun.javadoc.Type;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.AnnotationDesc;

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

  public AnnotationDesc[] annotations() {
    return new AnnotationDesc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

/*   *//**
094:             * Get the annotations of this parameter.
095:             * Return an empty array if there are none.
096:             *//*
097:            public AnnotationDesc[] annotations() {
098:                AnnotationDesc res[] = new AnnotationDesc[sym
099:                        .getAnnotationMirrors().length()];
100:                int i = 0;
101:                for (Attribute.Compound a : sym.getAnnotationMirrors()) {
102:                    res[i++] = new AnnotationDescImpl(env, a);
103:                }
104:                return res;
105:            }*//*
  */
}
