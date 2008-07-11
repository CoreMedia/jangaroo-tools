package net.jangaroo.joodoc;

import net.jangaroo.jooc.IdeType;
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

  net.jangaroo.jooc.IdeType type;
  boolean primitive = true;
  ClassDoc classDoc;

  public TypeImpl(IdeType type) {
    this.type = type;
  }

  public TypeImpl(IdeType type, ClassDoc classDoc) {
    this(type);
    this.classDoc = classDoc;
    primitive = false;
  }

  public ClassDoc asClassDoc() {
    if (classDoc==null) {
      initClassDoc();
      if (classDoc==null) {
        throw new IllegalStateException("TypeImpl#asClassDoc() called for type "+qualifiedTypeName()+", but no ClassDoc found.");
      }
    }
    return classDoc;
  }

  private void initClassDoc() {
    ClassDoc[] classes = DocMap.classes;
    String qTN = this.qualifiedTypeName();
    for (int i = 0; i < classes.length; i++) {
      ClassDoc aClass = classes[i];
      if (aClass.qualifiedTypeName().equals(qTN)) {
        classDoc = aClass;
        primitive = false;
        break;
      }
    }
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

  public String simpleTypeName() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isPrimitive() {
    if (classDoc==null) {
      initClassDoc();
    }
    return primitive;
  }

  public ParameterizedType asParameterizedType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public TypeVariable asTypeVariable() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public WildcardType asWildcardType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AnnotationTypeDoc asAnnotationTypeDoc() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}