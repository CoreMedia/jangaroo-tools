/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
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
  boolean containsSuperConstructorCall = false;

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

  public boolean containsSuperConstructorCall() {
    return containsSuperConstructorCall;
  }

  public void setContainsSuperConstructorCall(boolean containsSuperConstructorCallStatement) {
    this.containsSuperConstructorCall = containsSuperConstructorCallStatement;
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
    if (isPublic() && isStatic()) {
      classDeclaration.registerPublicStaticMethod(ide);
    }

    //TODO:check whether abstract method does not actually override

    context.enterScope(this);
    if (params != null)
      params.analyze(context);
    if (optTypeRelation != null)
      optTypeRelation.analyze(context);
    optBody.analyze(context);
    context.leaveScope(this);

    if (containsSuperConstructorCall()) {
      // must be contained at top level
      BlockStatement block = (BlockStatement) optBody;
      block.checkSuperConstructorCall();
    }
  }

  public void generateCode(JsWriter out) throws IOException {
    boolean isAbstract = isAbstract();
    boolean isConstructor = isConstructor();
    String methodName = ide.getName();
    if (isConstructor) {
      methodName = "_" + methodName;
    }
    boolean khtmlCompatMode = false; // TODO: use compiler flag for KHTML-compatibility mode!
    if (isAbstract) {
      out.beginComment();
      writeModifiers(out);
      out.writeSymbol(symFunction);
      ide.generateCode(out);
    } else {
      writeRuntimeModifiers(out);
      if (khtmlCompatMode) {
        out.write("{");
        out.write(methodName);
        out.writeSymbolWhitespace(ide.ide);
        out.write(":(");
      }
      out.writeSymbol(symFunction);
      out.write(" ");
      out.write(methodName);
      out.writeSymbolWhitespace(ide.ide);
    }
    out.writeSymbol(lParen);
    if (params != null) params.generateCode(out);
    out.writeSymbol(rParen);
    if (optTypeRelation != null) optTypeRelation.generateCode(out);
    if (isConstructor() && !containsSuperConstructorCall()) {
      ((BlockStatement)optBody).generateCodeWithSuperCall(out);
    } else {
      optBody.generateCode(out);
    }
    if (isAbstract()) {
      out.endComment();
    } else {
      if (khtmlCompatMode) {
        out.write(")}");
      }
      out.write(',');
    }
  }

}
