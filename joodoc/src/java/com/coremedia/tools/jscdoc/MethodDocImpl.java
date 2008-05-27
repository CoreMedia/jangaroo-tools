package com.coremedia.tools.jscdoc;

import com.sun.javadoc.Type;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ThrowsTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Doc;
import com.sun.javadoc.TypeVariable;
import com.coremedia.jscc.Parameters;
import com.coremedia.jscc.MethodDeclaration;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 11:23:33
 * To change this template use File | Settings | File Templates.
 */
public class MethodDocImpl extends MemberDocImpl implements MethodDoc, ConstructorDoc  {

  ArrayList paramList = new ArrayList();

  public MethodDocImpl(MethodDeclaration declaration) {
    super (declaration);

    Parameters params = getMethodDeclaration().getParams();
    while (params!=null) {
      paramList.add(DocMap.getDoc(params.getHead()));
      params=params.getTail();
    }
  }

  private MethodDeclaration getMethodDeclaration() {
    return (MethodDeclaration)getMemberDeclaration();
  }

  public Type returnType() {
    return getType();
  }

  public boolean isAbstract() {
    return getMethodDeclaration().isAbstract();
  }

  public Type overriddenType() {
    ClassDoc oc = overriddenClass();
    return oc==null ? null : new TypeImpl(null, oc);
  }

  public boolean overrides(MethodDoc doc) {
    return doc.equals(overriddenMethod());
  }

  public ClassDoc overriddenClass() {
    MethodDoc methodDoc = overriddenMethod();
    return methodDoc!=null ? methodDoc.containingClass() : null;
  }

  public MethodDoc overriddenMethod() {
    ClassDoc superClass = containingClass().superclass();
    while (superClass!=null) {
      MethodDoc superMethodDoc = getSuperMethodDoc(superClass);
      if (superMethodDoc!=null)
        return superMethodDoc;
      superClass = superClass.superclass();
    }
    return null;
  }

  private MethodDoc getSuperMethodDoc(ClassDoc classDoc) {
    Doc doc = DocMap.getDocByQualifiedName(classDoc.qualifiedName()+"#"+name(),false);
    return doc instanceof MethodDoc ? (MethodDoc)doc : null;
  }
  
  public boolean isNative() {
    return false;
  }

  public boolean isSynchronized() {
    return false;
  }

  public ClassDoc[] thrownExceptions() {
    return new ClassDoc[0];
  }

  public ParamTag[] paramTags() {
    return (ParamTag[])typedTags("param",ParamTag.class);
  }

  public Parameter[] parameters() {
    return (Parameter[]) paramList.toArray(new Parameter[0]);
  }

  public ThrowsTag[] throwsTags() {
    return new ThrowsTag[0];
  }

  public String flatSignature() {
    // used on the "index" page:
    return "()";
  }

  public String signature() {
    // used to define anchor (a name="...")
    // since we have no overloading, can be empty.
    return "";
  }

  public boolean isConstructor() {
    return getMethodDeclaration().isConstructor();
  }


  public boolean isMethod() {
    return !isConstructor();
  }

  public Type[] thrownExceptionTypes() {
    return new Type[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isVarArgs() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ParamTag[] typeParamTags() {
    return new ParamTag[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public TypeVariable[] typeParameters() {
    return new TypeVariable[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

}
