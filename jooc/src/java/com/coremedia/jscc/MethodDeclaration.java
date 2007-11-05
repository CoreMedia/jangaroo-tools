/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class MethodDeclaration extends MemberDeclaration {

  JscSymbol symFunction;
  JscSymbol lParen;

  public Parameters getParams() {
    return params;
  }

  Parameters params;
  JscSymbol rParen;

  Statement optBody;

  boolean isConstructor = false;

  private static final int defaultAllowedMethodModifers =
     MODIFIER_OVERRIDE|MODIFIER_ABSTRACT|MODIFIER_FINAL|MODIFIERS_SCOPE|MODIFIER_STATIC;

  public MethodDeclaration(JscSymbol[] modifiers, JscSymbol symFunction, Ide ide, JscSymbol lParen,
       Parameters params, JscSymbol rParen, TypeRelation optTypeRelation, Statement optBody) {
    super(modifiers, defaultAllowedMethodModifers, ide, optTypeRelation);
    this.symFunction = symFunction;
    this.lParen = lParen;
    this.params = params;
    this.rParen = rParen;
    this.optBody = optBody;
  }

  public boolean overrides() {
    return (getModifiers() & MODIFIER_OVERRIDE) != 0;
  }

  public boolean isConstructor() {
    return isConstructor;
  }

  public void analyze(AnalyzeContext context) {
    parentDeclaration = context.getCurrentClass();
    ClassDeclaration classDeclaration = getClassDeclaration();
    if (ide.getName().equals(classDeclaration.getName())) {
      isConstructor = true;
      classDeclaration.setConstructor(this);
      allowedModifiers = MODIFIERS_SCOPE;
    }
    super.analyze(context); // computes modifiers
    if (overrides() && isAbstract())
      Jscc.error(this, "overriding methods are not allowed to be declared abstract");
    if (isAbstract()) {
      if (!classDeclaration.isAbstract())
        Jscc.error(this, classDeclaration.getName() + "is not declared abstract");
      if (optBody instanceof BlockStatement)
        Jscc.error(this, "abstract method must not be implemented");
    }
    if (!isAbstract() && !(optBody instanceof BlockStatement))
      Jscc.error(this, "method must either be implemented or declared abstract");

    //TODO:check whether abstract method does not actually override

    if (params != null)
     params.analyze(context);
    if (optTypeRelation != null)
       optTypeRelation.analyze(context);
    optBody.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    ClassDeclaration classDeclaration = getClassDeclaration();
    String constructorName = out.getConstructorHelperVariableName(classDeclaration);
    String prototypeName = out.getPrototypeHelperVariableName(classDeclaration);
    String classPath = classDeclaration.getPath();
    String qualifiedClassNameAsIde = out.getQualifiedNameAsIde(classDeclaration);
    String methodName = ide.getName();
    String methodNameAsIde = out.getMethodNameAsIde(this);
    if (overrides()) {
      String savedMethodName = out.getSuperMethodName(this);
      out.writeToken(prototypeName);
      out.write('.');
      out.write(savedMethodName);
      out.write("=");
      out.writeToken(prototypeName);
      out.write('.');
      out.write(methodName);
      out.write(';');
    }
    boolean isAbstract = isAbstract();
    out.beginComment();
    writeModifiers(out);
    if (isAbstract) {
      out.writeSymbol(symFunction);
      ide.generateCode(out);
    } else {
      out.endComment();
      if (isConstructor) {
        // we emit the actual class declaration code in this method:
        /*
        _=package.class=package_class=function package_class(...) {
          ...
        }
        */
        out.write("var ");
        out.writeToken(constructorName);
        out.write('=');
        out.write(classPath);
        out.write('=');
        out.write(out.getCompatibilityName(qualifiedClassNameAsIde));
      } else {
        /* static:  classPath.method = function package_class_method(...) {...
           !static: _.method = function package_class_method(...) {...
        */
        if (isStatic()) {
          out.write(classPath);
        } else {
          out.writeToken(prototypeName);
        }
        out.write('.');
        out.write(methodName);
      }
      out.write('=');
      out.writeSymbol(symFunction);
      out.writeSymbolWhitespace(ide.ide);
      if (out.getKeepSource())
        out.writeToken(methodNameAsIde);
    }
    out.writeSymbol(lParen);
    if (params != null) params.generateCode(out);
    out.writeSymbol(rParen);
    if (optTypeRelation != null) optTypeRelation.generateCode(out);
    optBody.generateCode(out);
    if (!isAbstract())
      out.write(';');
    if (isConstructor) {
      /*
      _.constructorname = "package_class";
      _.superconstructor = package_superclass;
      _=_.prototype=new package_superclass();
      _._super_package_superclass = package_superclass;
      _.constructor = package_class;
      */
      String qualifiedSuperClassPath = classDeclaration.getSuperClassPath();
      String superConstructorNameAsIde = out.getSuperConstructorNameAsIde(classDeclaration);
      out.write(prototypeName);
      out.write(".constructorname='");
      out.write(classPath);
      out.write("';");
      out.write(prototypeName);
      out.write(".superconstructor=");
      out.write(qualifiedSuperClassPath);
      out.write(';');
      out.write(prototypeName);
      out.write('=');
      out.write(constructorName);
      out.write(".prototype=new ");
      out.write(qualifiedSuperClassPath);
      out.write("();");
      out.write(prototypeName);
      out.write('.');
      out.write(superConstructorNameAsIde);
      out.write('=');
      out.write(qualifiedSuperClassPath);
      out.write(';');
      out.write(prototypeName);
      out.write(".constructor=");
      out.write(classPath);
      out.write(';');
    }
    if (isAbstract())
      out.endComment();
  }

}
