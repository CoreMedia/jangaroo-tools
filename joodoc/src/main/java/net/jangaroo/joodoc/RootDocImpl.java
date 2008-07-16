/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packfields(3) packimports(7) deadcode fieldsfirst splitstr(64) nonlb lnc radix(10) lradix(10) 
// Source File Name:   RootDocImpl.java

package net.jangaroo.joodoc;

import com.sun.javadoc.*;
import com.sun.tools.javac.util.List;
import net.jangaroo.jooc.CompilationUnit;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

// Referenced classes of package com.sun.tools.javadoc:
//            DocImpl, ClassDocImpl, DocEnv, Messager, 
//            PackageDocImpl, SourcePositionImpl

public class RootDocImpl implements RootDoc {
  Context context;
  ArrayList list;
  ClassDoc[] classes;
  PackageDoc[] packages;
  private List options;


  public RootDocImpl(Context context, List optionList) {
    this.options = optionList;
    this.context=context;
    this.list=context.getList();
    classes=new ClassDoc[list.size()];
    Set packageSet = new HashSet(); 
    for (int i = 0; i < list.size(); i++) {
      CompilationUnit unit = (CompilationUnit) list.get(i);
      classes[i]=(ClassDoc) DocMap.getDoc(unit.getClassDeclaration());
      packageSet.add((PackageDoc)DocMap.getDoc(unit.getPackageDeclaration()));
    }
    packages=(PackageDoc[])new ArrayList(packageSet).toArray(new PackageDoc[packageSet.size()]);
    DocMap.setClasses(classes);
  }

  public ClassDoc[] classes() {

    return classes;
  }

  public ClassDoc[] specifiedClasses() {

    return classes;
  }

  public PackageDoc[] specifiedPackages() {
    return packages;
  }

  public String[][] options() {
    return (String[][])options.toArray(new String[options.length()][]);
  }

  public ClassDoc classNamed(String s) {
    for (int i = 0; i < classes.length; i++) {
      ClassDoc aClass= classes[i];
      if (aClass.name().equals(s)) return aClass;
    }
    return null;

  }

  public PackageDoc packageNamed(String s) {
    for (int i = 0; i < packages.length; i++) {
      PackageDoc aPackage = packages[i];
      if (aPackage.name().equals(s)) return aPackage;
    }
    return null;

  }

  public boolean isClass() {
    return false;
  }

  public boolean isConstructor() {
    return false;
  }

  public boolean isError() {
    return false;
  }

  public boolean isException() {
    return false;
  }

  public boolean isField() {
    return false;
  }

  public boolean isIncluded() {
    return false;
  }

  public boolean isInterface() {
    return false;
  }

  public boolean isMethod() {
    return false;
  }

  public boolean isOrdinaryClass() {
    return false;
  }

  public SeeTag[] seeTags() {
    return new SeeTag[0];
  }

  public SourcePosition position() {
    return null;
  }

  public Tag[] firstSentenceTags() {
    return new Tag[0];
  }

  public Tag[] inlineTags() {
    return new Tag[0];
  }

  public Tag[] tags() {
    return new Tag[0];
  }

  public int compareTo(Object o) {
    return 0;
  }

  public String commentText() {
    return null;
  }

  public String getRawCommentText() {
    return null;
  }

  public String name() {
    return null;
  }

  public void setRawCommentText(String s) {

  }

  public Tag[] tags(String s) {
    return new Tag[0];
  }

  public void printError(String s) {

  }

  public void printNotice(String s) {

  }

  public void printWarning(String s) {

  }

  public void printError(SourcePosition sourcePosition, String s) {

  }

  public void printNotice(SourcePosition sourcePosition, String s) {

  }

  public void printWarning(SourcePosition sourcePosition, String s) {

  }

  public boolean isEnum() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isEnumConstant() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isAnnotationTypeElement() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isAnnotationType() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
