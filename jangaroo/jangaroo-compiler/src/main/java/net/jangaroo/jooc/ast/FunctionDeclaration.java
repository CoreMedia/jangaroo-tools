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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;

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

  private boolean isConstructor = false;
  private boolean isDeclaredInInterface = false;
  private boolean containsSuperConstructorCall = false;
  private boolean thisAliased;

  private Scope scope;

  private static final int DEFAULT_ALLOWED_METHOD_MODIFIERS = // NOSONAR there is no simpler way to tell it; we need all these flags
          MODIFIER_OVERRIDE | MODIFIER_ABSTRACT | MODIFIER_VIRTUAL | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_NATIVE;

  public FunctionDeclaration(AnnotationsAndModifiers am, JooSymbol symFunction, JooSymbol symGetOrSet, Ide ide, JooSymbol lParen,
                             Parameters params, JooSymbol rParen, TypeRelation optTypeRelation,
                             BlockStatement optBody,
                             JooSymbol optSymSemicolon) {
    super(am, ide, optTypeRelation);
    this.fun = new FunctionExpr(this, symFunction, ide, lParen, params, rParen, optTypeRelation, optBody);
    this.symGetOrSet = symGetOrSet;
    this.optSymSemicolon = optSymSemicolon;
    if (isGetterOrSetter() && !(isGetter() || isSetter())) {
      throw JangarooParser.error(symGetOrSet, "Expected 'get' or 'set'.");
    }
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(getAnnotations(), fun); // do not call super.getChildren(), as fun already contains ide!
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitFunctionDeclaration(this);
  }

  public int getModifiers() {
    int modifiers = super.getModifiers();
    if (isDeclaredInInterface) {
      modifiers |= MODIFIER_PUBLIC;
    }
    return modifiers;
  }

  public boolean overrides() {
    return (getModifiers() & MODIFIER_OVERRIDE) != 0;
  }

  @Override
  public boolean isMethod() {
    return isClassMember();
  }

  @Override
  public boolean isWritable() {
    return isSetter();
  }

  public boolean isGetterOrSetter() {
    return symGetOrSet != null;
  }

  public boolean isGetter() {
    return isGetter(symGetOrSet);
  }

  private static boolean isGetter(JooSymbol symGetOrSet) {
    return symGetOrSet != null && SyntacticKeywords.GET.equals(symGetOrSet.getText());
  }

  public boolean isSetter() {
    return isSetter(symGetOrSet);
  }

  private static boolean isSetter(JooSymbol symGetOrSet) {
    return symGetOrSet != null && SyntacticKeywords.SET.equals(symGetOrSet.getText());
  }

  public MethodType getMethodType() {
    return isSetter() ? MethodType.SET : isGetter() ? MethodType.GET : null;
  }

  public final boolean isConstructor() {
    return isConstructor;
  }

  public FunctionExpr getFun() {
    return fun;
  }

  public JooSymbol getSymGetOrSet() {
    return symGetOrSet;
  }

  public JooSymbol getOptSymSemicolon() {
    return optSymSemicolon;
  }

  public boolean containsSuperConstructorCall() {
    return isContainsSuperConstructorCall();
  }

  public void setContainsSuperConstructorCall(boolean containsSuperConstructorCallStatement) {
    this.containsSuperConstructorCall = containsSuperConstructorCallStatement;
  }

  public boolean isAbstract() {
    return getClassDeclaration() != null && getClassDeclaration().isInterface() || super.isAbstract();
  }

  public Parameters getParams() {
    return fun.getParams();
  }

  public boolean hasBody() {
    return fun.hasBody();
  }

  public BlockStatement getBody() {
    return fun.getBody();
  }

  @Override
  public void scope(Scope scope) {
    this.scope = scope;
    final ClassDeclaration classDeclaration = scope.getClassDeclaration();
    // todo: temporarily resetting the ide field looks weird
    Ide oldIde = getIde();
    if (classDeclaration != null && !isGetterOrSetter() && getIde().getName().equals(classDeclaration.getName())) {
      setConstructor(true);
      classDeclaration.setConstructor(this);
      setIde(null); // do NOT declare constructor ide in scope, as it would override the class, is not inherited, etc.!
    }
    isDeclaredInInterface = classDeclaration != null && classDeclaration.isInterface();
    super.scope(scope);
    setIde(oldIde);
    //todo check correct override usage
    if (overrides() && isAbstract()) {
      throw JangarooParser.error(this, "overriding methods are not allowed to be declared abstract");
    }
    if (isAbstract()) {
      if (classDeclaration == null) {
        throw JangarooParser.error(this, "package-scoped function " + getName() + " must not be abstract.");
      }
      if (!classDeclaration.isAbstract()) {
        throw JangarooParser.error(this, classDeclaration.getName() + "is not declared abstract");
      }
      if (hasBody()) {
        throw JangarooParser.error(this, "abstract method must not be implemented");
      }
    }
    if (isNative() && hasBody()) {
      throw JangarooParser.error(this, "native method must not be implemented");
    }
    if (!isAbstract() && !isNative() && !hasBody()) {
      throw JangarooParser.error(this, "method must either be implemented or declared abstract or native");
    }
    //TODO:check whether abstract method does not actually override
    if (!isStatic()) {
      ClassDeclaration currentClass = scope.getClassDeclaration();
      if (classDeclaration != null) { // otherwise we are in a global function - todo parse them as function declaration
        // declare this and super
        fun.setThisDefined();
        final Type superType = currentClass.getSuperType();
        if (superType != null) {
          fun.addImplicitParam(new Parameter(null, new Ide("super"), new TypeRelation(null, superType), null));
        }
      }
    }
    fun.scope(scope);
    if (containsSuperConstructorCall()) {
      // must be contained at top level
      BlockStatement block = getBody();
      block.checkSuperConstructorCall();
    }
  }

  public void analyze(AstNode parentNode) {
    analyzeSymModifiers();
    super.analyze(parentNode); // computes modifiers
    fun.analyze(this);

    if (isOverride()) {
      IdeDeclaration superDeclaration = getClassDeclaration().getSuperTypeDeclaration().resolvePropertyDeclaration(getIde().getName(), isStatic());
      CompileLog log = getIde().getScope().getCompiler().getLog();
      if (superDeclaration instanceof PropertyDeclaration) {
        superDeclaration = ((PropertyDeclaration) superDeclaration).getAccessor(isSetter());
      }
      if (!(superDeclaration instanceof FunctionDeclaration)) {
        log.error(getFun().getSymbol(), "Method does not override method from super class");
      } else {
        FunctionDeclaration superMethodDeclaration = (FunctionDeclaration) superDeclaration;
        FunctionSignature methodSignature = getMethodSignature();
        FunctionSignature superMethodSignature = superMethodDeclaration.getMethodSignature();
        if (!methodSignature.equals(superMethodSignature)) {
          log.error(getFun().getSymbol(), "Incompatible override, should have signature '" + superMethodSignature.toString() + "'");
        }
      }
    }
    // TODO: else check that method does not conflict with an existing member.

    if (isInitMethod()) {
      ExpressionType returnType = getType().getTypeParameter();
      if (returnType != null && returnType.getAS3Type() != AS3Type.VOID) {
        if (!returnType.equals(new ExpressionType(getClassDeclaration()))) {
          scope.getCompiler().getLog().error(
                  getOptTypeRelation().getType().getSymbol(),
                  String.format("Invalid MXML __initialize__ method return type, must be void or '%s'.", getClassDeclaration().getQualifiedNameStr()));
        }
      }
    }

    if (isPublicApi()) {
      // This method will be rendered into the public API stubs.
      Parameters params = getParams();
      while (params != null) {
        Parameter parameter = params.getHead();
        if (isClassMember() && isPublicApi() && parameter.getOptInitializer() != null) {
          parameter.getOptInitializer().addPublicApiDependencies();
        }
        addPublicApiDependencyOn(parameter.getOptTypeRelation());
        params = params.getTail();
      }

      addPublicApiDependencyOn(fun.getOptTypeRelation());
    }
  }

  public boolean isInitMethod() {
    return !isGetterOrSetter() && isPrivate() && getIde().getName().equals("__initialize__");
  }

  public FunctionSignature getMethodSignature() {
    return (FunctionSignature) getType();
  }

  /**
   * Check if methods defined in interfaces do not have any modifiers as described
   * <a href="http://help.adobe.com/en_US/ActionScript/3.0_ProgrammingAS3/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f41.html">here</a>.
   */
  private void analyzeSymModifiers() {
    ClassDeclaration classDeclaration = getClassDeclaration();
    if(null != classDeclaration && classDeclaration.isInterface()) {
      for (JooSymbol symModifier : getSymModifiers()) {
        throw JangarooParser.error(symModifier, "illegal modifier: " + symModifier.getText());
      }
    }
  }

  void aliasThis() {
    if (!thisAliased) {
      thisAliased = true;
    }
  }

  public boolean isThisAliased() {
    return thisAliased;
  }

  @Override
  protected boolean propagateInstanceThisUsed() {
    return !isClassMember();
  }

  @Override
  protected int getAllowedModifiers() {
    return isConstructor() ? MODIFIERS_SCOPE | MODIFIER_NATIVE : DEFAULT_ALLOWED_METHOD_MODIFIERS;
  }

  @Override
  public void handleDuplicateDeclaration(Scope scope, AstNode oldNode) {
    if (isGetterOrSetter() && oldNode instanceof FunctionDeclaration) {
      FunctionDeclaration other = (FunctionDeclaration) oldNode;
      if (other.isGetterOrSetter() && isGetter() != other.isGetter()) {
        // found counterpart for this getter or setter:
        // do not trigger warning or error!
        return;
      }
    }
    super.handleDuplicateDeclaration(scope, oldNode);
  }

  @Override
  public JooSymbol getDeclarationSymbol() {
    return getFun().getSymbol();
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    // todo this looks quirky, try not to define constructor within scope?
    return isConstructor() ? getClassDeclaration() : super.resolveDeclaration();
  }

  public void setConstructor(boolean constructor) {
    isConstructor = constructor;
  }

  public boolean isContainsSuperConstructorCall() {
    return containsSuperConstructorCall;
  }

  @Override
  public boolean isExtConfig() {
    return false; // an accessor itself is never a config; only a PropertyDeclaration comprised of accessors may be one
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FunctionDeclaration that = (FunctionDeclaration) o;

    return getIde().getName().equals(that.getIde().getName()) &&
            (symGetOrSet == that.symGetOrSet || symGetOrSet != null && that.symGetOrSet != null &&
                    symGetOrSet.getText().equals(that.symGetOrSet.getText()));
  }

  @Override
  public int hashCode() {
    int result = getIde().getName().hashCode();
    result = 31 * result + (symGetOrSet != null ? symGetOrSet.getText().hashCode() : 0);
    return result;
  }
}
