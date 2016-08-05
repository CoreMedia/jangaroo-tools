/*
 * Copyright 2010 CoreMedia AG
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

import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LoopStatement;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Statement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.Typed;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import java.util.List;

public abstract class AbstractScope implements Scope {

  private Scope parent;

  public AbstractScope(Scope parent) {
    this.parent = parent;
  }

  @Override
  public Scope getParentScope() {
    return parent;
  }

  @Override
  public void addImport(final ImportDirective importDirective) {
    mustBeInsideValueScope();
    parent.addImport(importDirective);
  }

  @Override
  public AstNode getDefiningNode() {
    if (parent == null) {
      return null;
    }
    return parent.getDefiningNode();
  }

  @Override
  public IdeDeclaration declareIde(final IdeDeclaration decl) {
    mustBeInsideValueScope();
    return parent.declareIde(decl);
  }

  private void mustBeInsideValueScope() {
    if (parent == null) {
      throw new UnsupportedOperationException("this scope must be wrapped by a ValueScope");
    }
  }

  @Override
  public LabeledStatement lookupLabel(final Ide ide) {
    if (parent == null) {
      throw JangarooParser.error(ide, "undeclared label '" + ide.getName() + "'");
    }
    return parent.lookupLabel(ide);
  }

  @Override
  public IdeDeclaration lookupDeclaration(final Ide ide) {
    return lookupDeclaration(ide, true);
  }

  @Override
  public IdeDeclaration lookupDeclaration(Ide ide, boolean failOnAmbigousImport) {
    return parent == null ? null : parent.lookupDeclaration(ide, failOnAmbigousImport);
  }

  @Override
  public boolean isDeclared(final Ide ide) {
    return parent != null && getParentScope().isDeclared(ide);
  }

  @Override
  public Ide findFreeAuxVar(String preferredName) {
    return parent == null ? null : parent.findFreeAuxVar(preferredName);
  }

  @Override
  public Ide createAuxVar(Scope lookupScope, String preferredName) {
    return parent.createAuxVar(lookupScope, null);
  }

  @Override
  public LoopStatement getCurrentLoop() {
    if (parent == null) {
      return null;
    }
    return parent.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    if (parent == null) {
      return null;
    }
    return parent.getCurrentLoopOrSwitch();
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return parent == null ? null : parent.getCompilationUnit();
  }

  @Override
  public PackageDeclaration getPackageDeclaration() {
    return parent == null ? null : parent.getPackageDeclaration();
  }

  @Override
  public ClassDeclaration getClassDeclaration() {
    return parent == null ? null : parent.getClassDeclaration();
  }

  @Override
  public DeclarationScope getPackageDeclarationScope() {
    return parent == null ? null : parent.getPackageDeclarationScope();
  }

  @Override
  public JangarooParser getCompiler() {
    return parent.getCompiler();
  }

  @Override
  public ClassDeclaration getClassDeclaration(String qname) {
    CompilationUnit compilationUnit = getCompiler().getCompilationUnit(qname);
    if (compilationUnit == null) {
      return null;
    }
    IdeDeclaration declaration = compilationUnit.getPrimaryDeclaration();
    return declaration instanceof ClassDeclaration ? (ClassDeclaration) declaration : null;
  }

  @Override
  public ExpressionType getExpressionType(AS3Type as3Type) {
    return getExpressionType(as3Type, null);
  }

  private ExpressionType getExpressionType(AS3Type as3Type, ExpressionType typeParameter) {
    return new ExpressionType(getClassDeclaration(as3Type.name), typeParameter);
  }

  @Override
  public ExpressionType getFunctionExpressionType(TypeRelation returnTypeRelation) {
    Type returnType = returnTypeRelation.getType();
    return getExpressionType(AS3Type.FUNCTION, returnType == null ? null
            : getExpressionType(returnType.resolveDeclaration()));
  }

  public ExpressionType getExpressionType(IdeDeclaration declaration) {
    if (declaration instanceof TypeDeclaration) {
      return getExpressionType(AS3Type.CLASS, new ExpressionType((TypeDeclaration) declaration));
    }
    if (declaration instanceof Typed) {
      TypeRelation typeRelation = ((Typed) declaration).getOptTypeRelation();
      if (typeRelation != null) {
        ExpressionType expressionType = getExpressionType(typeRelation.getType());
        if (expressionType != null) {
          if (expressionType.getAS3Type() == AS3Type.ARRAY) {
            TypeDeclaration typeDeclaration = findArrayElementType(declaration);
            if (typeDeclaration != null) {
              expressionType = new ExpressionType(typeRelation.getType().getDeclaration(), new ExpressionType(typeDeclaration));
            }
          }
          return declaration instanceof FunctionDeclaration && !((FunctionDeclaration) declaration).isGetterOrSetter()
                          ? getExpressionType(AS3Type.FUNCTION, expressionType) : expressionType;
        }
      }
    }
    return null;
  }

  private ExpressionType getExpressionType(Type type) {
    TypeDeclaration memberTypeDeclaration = type.getDeclaration();
    if (memberTypeDeclaration != null) {
      ExpressionType typeParameter = type.getIde() instanceof IdeWithTypeParam
              ? getExpressionType(((IdeWithTypeParam) type.getIde()).getType()) : null;
      return new ExpressionType(memberTypeDeclaration, typeParameter);
    }
    return null;
  }

  private static final String ARRAY_ELEMENT_TYPE_ANNOTATION_NAME = "ArrayElementType";

  private static TypeDeclaration findArrayElementType(IdeDeclaration declaration) {
    // find [ArrayElementType("...")] annotation:
    Annotation annotation = findAnnotation(declaration, ARRAY_ELEMENT_TYPE_ANNOTATION_NAME);
    if (annotation != null) {
      JangarooParser compiler = declaration.getIde().getScope().getCompiler();
      CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
      if (annotationParameters == null) {
        compiler.getLog().error(declaration.getSymbol(), "[ArrayElementType] must provide a class reference.");
      } else {
        AnnotationParameter firstParameter = annotationParameters.getHead();
        Object elementType = firstParameter.getValue().getSymbol().getJooValue();
        if (elementType instanceof String) {
          CompilationUnit compilationUnit = compiler.getCompilationUnit((String) elementType);
          if (compilationUnit == null) {
            compiler.getLog().error(firstParameter.getSymbol(), String.format("[ArrayElementType] class reference '%s' not found.", elementType));
          } else {
            IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
            if (!(primaryDeclaration instanceof TypeDeclaration)) {
              compiler.getLog().error(firstParameter.getSymbol(), String.format("[ArrayElementType] references '%s', which is not a class.", elementType));
            } else {
              return  (TypeDeclaration) primaryDeclaration;
            }
          }
        }
      }
    }
    return null;
  }

  private static Annotation findAnnotation(IdeDeclaration declaration, String annotationName) {
    ClassDeclaration classDeclaration = declaration.getIde().getScope().getClassDeclaration();
    List<? extends AstNode> children = classDeclaration.getBody().getChildren();
    int declarationIndex = children.indexOf(declaration);
    for (int index = declarationIndex - 1; index >= 0; --index) {
      AstNode astNode = children.get(index);
      if (!(astNode instanceof Annotation)) {
        return null;
      }
      Annotation annotation = (Annotation) astNode;
      if (annotation.getMetaName().equals(annotationName)) {
        return annotation;
      }
    }
    return null;
  }

  @Override
  public FunctionDeclaration getMethodDeclaration() {
    return parent == null ? null : parent.getMethodDeclaration();
  }

  @Override
  public FunctionExpr getFunctionExpr() {
    return parent == null ? null : parent.getFunctionExpr();
  }

  public boolean isPackage(final String fullyQualifiedName) {
    return parent != null && parent.isPackage(fullyQualifiedName);
  }
}
