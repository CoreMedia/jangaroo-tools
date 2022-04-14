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

import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author Frank Wienberg
 */
public class AnnotationParameter extends NodeImplBase {

  private Ide optName;
  private JooSymbol optSymEq;
  private AstNode value;
  private Annotation parentAnnotation;

  public AnnotationParameter(Ide optName, JooSymbol optSymEq, AstNode value) {
    this.optName = optName;
    this.optSymEq = optSymEq;
    this.value = value;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optName, value);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitAnnotationParameter(this);
  }

  public Annotation getParentAnnotation() {
    return parentAnnotation;
  }

  public void setParentAnnotation(Annotation parentAnnotation) {
    this.parentAnnotation = parentAnnotation;
  }

  @Override
  public void scope(final Scope scope) {
    if (getValue() != null) {
      getValue().scope(scope);
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    AstNode value = getValue();
    if (value != null) {
      value.analyze(this);
      if (value instanceof Ide) {
        ((Ide) value).analyzeAsExpr(this, null);
      }
      JooSymbol valueSymbol = value.getSymbol();
      String metaName = parentAnnotation.getMetaName();
      if (Jooc.EMBED_ANNOTATION_NAME.equals(metaName) && getOptName() != null) {
        if ((Jooc.EMBED_ANNOTATION_SOURCE_PROPERTY.equals(getOptName().getName()) || Jooc.EMBED_ANNOTATION_MIME_TYPE_PROPERTY.equals(getOptName().getName()))
                && valueSymbol.sym != sym.STRING_LITERAL) {
          throw new CompilerError(valueSymbol, "The " + getOptName().getName() + " parameter of an [Embed] annotation must be a string literal");
        }
      } else if (Jooc.RESOURCE_BUNDLE_ANNOTATION_NAME.equals(metaName) && getOptName() == null) {
        if (valueSymbol.sym != sym.STRING_LITERAL) {
          throw new CompilerError(valueSymbol, "The parameter of a [ResourceBundle] annotation must be a string literal");
        }
        Scope scope = parentAnnotation.getIde().getScope();
        String resourceBundleName = (String) valueSymbol.getJooValue();
        CompilationUnit resourceBundleCompilationUnit = scope.getCompiler().getCompilationUnit(resourceBundleName + CompilerUtils.PROPERTIES_CLASS_SUFFIX);
        if (resourceBundleCompilationUnit == null) {
          throw new CompilerError(valueSymbol, "unable to resolve resource bundle " + resourceBundleName);
        }
        scope.getCompilationUnit().addDependency(resourceBundleCompilationUnit, true);
      } else if (Jooc.ARRAY_ELEMENT_TYPE_ANNOTATION_NAME.equals(metaName) && getOptName() == null) {
        if (valueSymbol.sym != sym.STRING_LITERAL) {
          throw new CompilerError(valueSymbol, "An [ArrayElementType] annotation must specify the element type as its only parameter.");
        }
        Scope scope = parentAnnotation.getIde().getScope();
        String arrayElementType = (String) valueSymbol.getJooValue();
        CompilationUnit arrayElementTypeCompilationUnit = scope.getCompiler().getCompilationUnit(arrayElementType);
        if (arrayElementTypeCompilationUnit == null) {
          throw new CompilerError(valueSymbol, "Unable to resolve array element type " + arrayElementType);
        }
        scope.getCompilationUnit().addDependency(arrayElementTypeCompilationUnit, false);
      }
    }
  }

  public JooSymbol getSymbol() {
    return getOptName() == null ? getValue().getSymbol() : getOptName().getSymbol();
  }

  public Ide getOptName() {
    return optName;
  }

  public JooSymbol getOptSymEq() {
    return optSymEq;
  }

  public AstNode getValue() {
    return value;
  }

  public void setValue(LiteralExpr value) {
    this.value = value;
  }
}
