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

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class MethodDeclaration extends MemberDeclaration {

  JooSymbol symFunction;
  JooSymbol lParen;

  public Parameters getParams() {
    return params;
  }

  Parameters params;
  JooSymbol rParen;

  Statement optBody;

  boolean isConstructor = false;
  boolean containsSuperConstructorCall = false;

  private static final int defaultAllowedMethodModifers =
     MODIFIER_OVERRIDE|MODIFIER_ABSTRACT|MODIFIER_FINAL|MODIFIERS_SCOPE|MODIFIER_STATIC|MODIFIER_NATIVE;

  public MethodDeclaration(JooSymbol[] modifiers, JooSymbol symFunction, Ide ide, JooSymbol lParen,
       Parameters params, JooSymbol rParen, TypeRelation optTypeRelation, Statement optBody) {
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

  @Override
  public boolean isMethod() {
    return true;
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

  public boolean isAbstract() {
    return classDeclaration!=null && classDeclaration.isInterface() || super.isAbstract();
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    parentDeclaration = classDeclaration = context.getCurrentClass();
    if (classDeclaration!=null && ide.getName().equals(classDeclaration.getName())) {
      isConstructor = true;
      classDeclaration.setConstructor(this);
      allowedModifiers = MODIFIERS_SCOPE | MODIFIER_NATIVE;
    }
    super.analyze(parentNode, context); // computes modifiers
    if (overrides() && isAbstract())
      Jooc.error(this, "overriding methods are not allowed to be declared abstract");
    if (isAbstract()) {
      if (classDeclaration==null) {
        Jooc.error(this, "package-scoped function "+getName()+" must not be abstract.");
      }
      if (!classDeclaration.isAbstract())
        Jooc.error(this, classDeclaration.getName() + "is not declared abstract");
      if (optBody instanceof BlockStatement)
        Jooc.error(this, "abstract method must not be implemented");
    }
    if (isNative() && optBody instanceof BlockStatement)
      Jooc.error(this, "native method must not be implemented");

    if (!isAbstract() && !isNative() && !(optBody instanceof BlockStatement))
      Jooc.error(this, "method must either be implemented or declared abstract or native");

    //TODO:check whether abstract method does not actually override

    context.enterScope(this);
    if (params != null)
      params.analyze(this, context);
    if (context.getScope().getIdeDeclaration("arguments")==null) {
      context.getScope().declareIde("arguments", this); // is always defined inside a function!
    }
    if (optTypeRelation != null)
      optTypeRelation.analyze(this, context);
    optBody.analyze(this, context);
    context.leaveScope(this);

    if (containsSuperConstructorCall()) {
      // must be contained at top level
      BlockStatement block = (BlockStatement) optBody;
      block.checkSuperConstructorCall();
    }
    return this;
  }

  public void generateCode(JsWriter out) throws IOException {
    boolean isAbstract = isAbstract();
    boolean isConstructor = isConstructor();
    if (isAbstract) {
      out.beginComment();
      writeModifiers(out);
      out.writeSymbol(symFunction);
      ide.generateCode(out);
    } else {
      out.beginString();
      writeModifiers(out);
      String methodName = ide.getName();
      if (classDeclaration!=null && !isConstructor && !isStatic() && classDeclaration.isBoundMethod(methodName)) {
        // TODO: move this into an annotation!
        out.writeToken("bound");
      }
      out.writeSymbol(symFunction);
      ide.generateCode(out);
      out.endString();
      if (isNative()) {
        out.beginComment();
      } else {
        out.write(",");
        out.writeToken("function");
        if (out.getKeepSource()) {
          if (isConstructor) {
            // do not name the constructor initializer function like the class, or it will be called
            // instead of the constructor function generated by the runtime! So we prefix it with a "$".
            // The name is for debugging purposes only, anyway.
            out.writeToken("$"+methodName);
          } else if (ide instanceof AccessorIde) {
            out.writeToken(((AccessorIde)ide).getFunctionName());
          } else {
            out.writeToken(methodName);
          }
        }
      }
    }
    out.writeSymbol(lParen);
    if (params != null) {
      params.generateCode(out);
      if (optBody instanceof BlockStatement) {
        // inject into body for generating initilizers later:
        ((BlockStatement)optBody).addBlockStartCodeGenerator(params.getParameterInitializerCodeGenerator());
      }
    }
    out.writeSymbol(rParen);
    if (optTypeRelation != null) optTypeRelation.generateCode(out);
    if (isConstructor() && !containsSuperConstructorCall() && optBody instanceof BlockStatement) {
      ((BlockStatement)optBody).addBlockStartCodeGenerator(new CodeGenerator() {
        public void generateCode(JsWriter out) throws IOException {
          out.writeToken("this[$super]();");
        }
      });
    }
    if (optBody!=null)
      optBody.generateCode(out);
    if (isAbstract() || isNative()) {
      out.endComment();
    }
    out.write(',');
  }

}
