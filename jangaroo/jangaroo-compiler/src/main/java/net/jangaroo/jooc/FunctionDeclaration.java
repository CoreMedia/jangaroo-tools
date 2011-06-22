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
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class FunctionDeclaration extends TypedIdeDeclaration {

  private FunctionExpr fun;
  private JooSymbol symGetOrSet;
  private JooSymbol optSymSemicolon;

  boolean isConstructor = false;
  boolean containsSuperConstructorCall = false;

  private static final int defaultAllowedMethodModifers =
    MODIFIER_OVERRIDE | MODIFIER_ABSTRACT | MODIFIER_VIRTUAL | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_NATIVE;

  public FunctionDeclaration(List<JooSymbol> modifiers, JooSymbol symFunction, JooSymbol symGetOrSet, Ide ide, JooSymbol lParen,
                             Parameters params, JooSymbol rParen, TypeRelation optTypeRelation,
                             BlockStatement optBody,
                             JooSymbol optSymSemicolon) {
    super(modifiers.toArray(new JooSymbol[modifiers.size()]), ide, null); //todo pass Function type as typeRelation
    this.fun = new FunctionExpr(this, symFunction, ide, lParen, params, rParen, optTypeRelation, optBody);
    this.symGetOrSet = symGetOrSet;
    this.optSymSemicolon = optSymSemicolon;
    if (isGetterOrSetter() && !(isGetter() || isSetter())) {
      throw Jooc.error(symGetOrSet, "Expected 'get' or 'set'.");
    }
  }

  public boolean overrides() {
    return (getModifiers() & MODIFIER_OVERRIDE) != 0;
  }

  @Override
  public boolean isMethod() {
    return isClassMember();
  }

  public boolean isGetterOrSetter() {
    return symGetOrSet != null;
  }

  public boolean isGetter() {
    return isGetterOrSetter() && SyntacticKeywords.GET.equals(symGetOrSet.getText());
  }

  public boolean isSetter() {
    return isGetterOrSetter() && SyntacticKeywords.SET.equals(symGetOrSet.getText());
  }

  public final boolean isConstructor() {
    return isConstructor;
  }

  public boolean containsSuperConstructorCall() {
    return containsSuperConstructorCall;
  }

  public void setContainsSuperConstructorCall(boolean containsSuperConstructorCallStatement) {
    this.containsSuperConstructorCall = containsSuperConstructorCallStatement;
  }

  public boolean isAbstract() {
    return classDeclaration != null && classDeclaration.isInterface() || super.isAbstract();
  }

  public Parameters getParams() {
    return fun.getParams();
  }

  private boolean hasBody() {
    return fun.hasBody();
  }

  public BlockStatement getBody() {
    return fun.getBody();
  }

  @Override
  public void scope(Scope scope) {
    final ClassDeclaration classDeclaration = scope.getClassDeclaration();
    Ide oldIde = ide;
    if (classDeclaration != null && ide.getName().equals(classDeclaration.getName())) {
      isConstructor = true;
      classDeclaration.setConstructor(this);
      ide = null; // do NOT declare constructor ide in scope, as it would override the class, is not inherited, etc.!
    }
    super.scope(scope);
    ide = oldIde;
    //todo check correct override usage
    if (overrides() && isAbstract()) {
      throw Jooc.error(this, "overriding methods are not allowed to be declared abstract");
    }
    if (isAbstract()) {
      if (classDeclaration == null) {
        throw Jooc.error(this, "package-scoped function " + getName() + " must not be abstract.");
      }
      if (!classDeclaration.isAbstract()) {
        throw Jooc.error(this, classDeclaration.getName() + "is not declared abstract");
      }
      if (hasBody()) {
        throw Jooc.error(this, "abstract method must not be implemented");
      }
    }
    if (isNative() && hasBody()) {
      throw Jooc.error(this, "native method must not be implemented");
    }
    if (!isAbstract() && !isNative() && !hasBody()) {
      throw Jooc.error(this, "method must either be implemented or declared abstract or native");
    }
    //TODO:check whether abstract method does not actually override
    if (!isStatic()) {
      ClassDeclaration currentClass = scope.getClassDeclaration();
      if (classDeclaration != null) { // otherwise we are in a global function - todo parse them as function declaration
        // declare this and super
        final Type thisType = currentClass.getThisType();
        final Parameter thisParam = new Parameter(null, new Ide("this"), new TypeRelation(null, thisType), null);
        fun.addImplicitParam(thisParam);
        final Type superType = currentClass.getSuperType();
        if (superType != null) {
          fun.addImplicitParam(new Parameter(null, new Ide("super"), new TypeRelation(null, superType), null));
        }
      }
    }
    fun.scope(scope);
    if (containsSuperConstructorCall()) {
      // must be contained at top level
      BlockStatement block = (BlockStatement) getBody();
      block.checkSuperConstructorCall();
    }
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context); // computes modifiers
    fun.analyze(this, context);
  }

  @Override
  protected int getAllowedModifiers() {
    return isConstructor() ? MODIFIERS_SCOPE | MODIFIER_NATIVE : defaultAllowedMethodModifers;
  }

  @Override
  void handleDuplicateDeclaration(Scope scope, AstNode oldNode) {
    if (isGetterOrSetter() && oldNode instanceof FunctionDeclaration) {
      FunctionDeclaration other = (FunctionDeclaration) oldNode;
      if (other.isGetterOrSetter() && isGetter() != other.isGetter()) {
        // found counterpart for this getter or setter:
        // replace declaration by a combination of both:
        final GetterSetterPair setterPair = new GetterSetterPair(
          isGetter() ? this : other,
          isSetter() ? this : other);
        setterPair.scope(scope);
        // ...and do not trigger warning or error!
        return;
      }
    }
    super.handleDuplicateDeclaration(scope, oldNode);
  }

  protected void generateAsApiCode(JsWriter out) throws IOException {
    if (!isPrivate()) {
      writeModifiers(out);
      if (!isNative() && !isAbstract() && !isConstructor()) {
        out.writeSymbolWhitespace(fun.getFunSymbol());
        out.writeToken(SyntacticKeywords.NATIVE);
        out.writeSymbol(fun.getFunSymbol(), false);
      } else {
        out.writeSymbol(fun.getFunSymbol());
      }
      if (symGetOrSet != null) {
        out.writeSymbol(symGetOrSet);
      }
      ide.generateCode(out);
      fun.generateSignatureCode(out);
      if (isConstructor() && !isNative()) {
        // ASDoc does not allow a native constructor if the super class constructor needs parameters!
        out.writeToken("{super(");
        if (classDeclaration != null) {
          ClassDeclaration superType = classDeclaration.getSuperTypeDeclaration();
          if (superType != null) {
            FunctionDeclaration superConstructor = superType.getConstructor();
            if (superConstructor != null) {
              Parameters superParameters = superConstructor.getParams();
              boolean first = true;
              while (superParameters != null && superParameters.head.optInitializer == null) {
                if (first) {
                  first = false;
                } else {
                  out.writeToken(",");
                }
                out.write(VariableDeclaration.getDefaultValue(superParameters.head.optTypeRelation));
                superParameters = (Parameters)superParameters.tail;
              }
            }
          }
        }
        out.writeToken(");}");
      } else {
        out.writeToken(";");
      }
    }
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    assert isClassMember() || (!isNative() && !isAbstract());
    if (isConstructor && !containsSuperConstructorCall() && hasBody()) {
      classDeclaration.addSuperCallCodeGenerator(getBody());
    }
    if (!isClassMember()) {
      fun.generateJsCode(out);
    } else {
      if (isAbstract()) {
        out.beginComment();
        writeModifiers(out);
        out.writeSymbol(fun.getFunSymbol());
        ide.generateCode(out);
      } else {
        out.beginString();
        writeModifiers(out);
        out.writeSymbol(fun.getFunSymbol());
        if (isGetterOrSetter()) {
          out.writeSymbol(symGetOrSet);
        }
        ide.generateCode(out);
        out.endString();
        if (isNative()) {
          out.beginComment();
        } else {
          out.write(",");
          out.writeToken("function");
          if (out.getKeepSource()) {
            String functionName = ide.getName();
            if (isConstructor) {
              // do not name the constructor initializer function like the class, or it will be called
              // instead of the constructor function generated by the runtime! So we prefix it with a "$".
              // The name is for debugging purposes only, anyway.
              out.writeToken(functionName + "$");
            } else if (symGetOrSet != null) {
              out.writeToken(functionName + "$" + symGetOrSet.getText());
            } else {
              out.writeToken(functionName);
            }
          }
        }
      }
      fun.generateFunTailCode(out);
      if (isClassMember()) {
        if (isAbstract() || isNative()) {
          out.endComment();
        }
        out.write(',');
      }
    }
  }

  @Override
  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    // todo this looks quirky, try not to define constructor within scope?
    return isConstructor() ? getClassDeclaration() : super.resolveDeclaration();
  }
}
