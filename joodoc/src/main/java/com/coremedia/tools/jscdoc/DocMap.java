package com.coremedia.tools.jscdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import net.jangaroo.jooc.*;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 14:02:25
 * To change this template use File | Settings | File Templates.
 */
public class DocMap {
  static HashMap  map = new HashMap();
  
  public static Object getKey(NodeImplBase node) {
    if (node instanceof MemberDeclaration) {
      if (node instanceof Parameter)
        return node; // Parameters are cheap, use node itself as key
      MemberDeclaration memberDeclaration = ((MemberDeclaration)node);
      return getKey(memberDeclaration.getClassDeclaration())
             +"#"+memberDeclaration.getName();
    }
    String[] qualifiedNameArcs;
    if (node instanceof IdeDeclaration) {
      qualifiedNameArcs = ((IdeDeclaration)node).getQualifiedName();
    } else if (node instanceof IdeType) {
      qualifiedNameArcs = ((IdeType)node).getIde().getQualifiedName();
    } else {
      throw new IllegalArgumentException("Don't know how to compute a key for "+node);
    }
    return Util.getQualifiedName(qualifiedNameArcs);
  }

  public static Object getDoc(NodeImplBase node) {
    if (node==null) throw new NullPointerException("node is null");
    Object key = getKey(node);
    Object value = map.get(key);
    if (value==null) {
      //System.out.println("getDoc('"+key+"')");
      value = getNewInstance(node);
      map.put(key,value);
    }
    return value;
  }

  public static Doc getDocByQualifiedName(String qualName) {
    return getDocByQualifiedName(qualName,true);
  }

  public static Doc getDocByQualifiedName(String qualName, boolean warn) {
    Object value = map.get(qualName);
    if (!(value instanceof Doc)) // might be Type or Parameter or ...
      value = null;
    if (warn && value==null) {
      System.err.println("Warning: qualified name '"+qualName+"' not found.");
    }
    return (Doc)value;
  }

  static ClassDoc[] classes;

  public static void setClasses(ClassDoc[] classes) {
    DocMap.classes=classes;
  }

  public static ClassDoc[] getClasses() {
    return classes;
  }

  private static Object getNewInstance(NodeImplBase node) {
    if (node instanceof MethodDeclaration) return new MethodDocImpl((MethodDeclaration)node);
    if (node instanceof ClassDeclaration)  return new ClassDocImpl((ClassDeclaration)node);
    if (node instanceof Parameter)  return new ParameterImpl((Parameter)node);
    if (node instanceof IdeType)  return new TypeImpl((IdeType)node);
    if (node instanceof FieldDeclaration)  return new FieldDocImpl((FieldDeclaration)node);
    if (node instanceof PackageDeclaration)  { return new PackageDocImpl((PackageDeclaration)node); }
    if (true)throw new NoClassDefFoundError("No class for node="+node);
    return null;
  }
}
