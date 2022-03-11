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

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An annotation (square bracket meta data).
 *
 * @author Frank Wienberg
 */
public class Annotation extends Directive {

  private JooSymbol leftBracket;
  private Ide ide;
  private JooSymbol optLeftParen;
  private CommaSeparatedList<AnnotationParameter> optAnnotationParameters;
  private JooSymbol optRightParen;
  private JooSymbol rightBracket;

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol rightBracket) {
    this(leftBracket, ide, null, null, null, rightBracket);
  }

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol optLeftParen, CommaSeparatedList<AnnotationParameter> optAnnotationParameters, JooSymbol optRightParen, JooSymbol optRightBracket) {
    this.leftBracket = leftBracket;
    this.ide = ide;
    this.optLeftParen = optLeftParen;
    this.optRightParen = optRightParen;
    this.optAnnotationParameters = optAnnotationParameters;
    this.rightBracket = optRightBracket;
    for (CommaSeparatedList<AnnotationParameter> params = optAnnotationParameters; params != null; params = params.getTail()) {
      params.getHead().setParentAnnotation(this);
    }
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide, optAnnotationParameters);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitAnnotation(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
    if (getOptAnnotationParameters() != null) {
      getOptAnnotationParameters().scope(scope);
    }
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (getOptAnnotationParameters() != null) {
      getOptAnnotationParameters().analyze(this);
      if (Jooc.EMBED_ANNOTATION_NAME.equals(getMetaName())) {
        Scope scope = getIde().getScope();
        scope.getCompilationUnit().addDependency(scope.getCompiler().getCompilationUnit("joo.flash.Embed"), false);
        Object source = getPropertiesByName().get(Jooc.EMBED_ANNOTATION_SOURCE_PROPERTY);
        scope.getCompilationUnit().getResourceDependencies().add(source.toString());
      }
    }
  }

  public JooSymbol getSymbol() {
    return getLeftBracket();
  }

  public String getMetaName() {
    return getIde().getName();
  }

  public JooSymbol getLeftBracket() {
    return leftBracket;
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getOptLeftParen() {
    return optLeftParen;
  }

  public CommaSeparatedList<AnnotationParameter> getOptAnnotationParameters() {
    return optAnnotationParameters;
  }

  public JooSymbol getOptRightParen() {
    return optRightParen;
  }

  public JooSymbol getRightBracket() {
    return rightBracket;
  }

  public Map<String, Object> getPropertiesByName() {
    Map<String, Object> result = new LinkedHashMap<>();
    CommaSeparatedList<AnnotationParameter> annotationParameters = getOptAnnotationParameters();
    while (annotationParameters != null) {
      AnnotationParameter annotationParameter = annotationParameters.getHead();
      String key = annotationParameter.getOptName() == null ? null : annotationParameter.getOptName().getName();
      AstNode valueExpression = annotationParameter.getValue();
      Object value = valueExpression instanceof LiteralExpr ? valueExpression.getSymbol().getJooValue()
              : valueExpression instanceof Ide ? ((Ide) valueExpression).getQualifiedNameStr()
              : null;
      if (result.containsKey(key)) {
        Object oldValue = result.get(key);
        List<Object> valueList;
        if (oldValue instanceof List) {
          //noinspection unchecked
          valueList = (List) oldValue;
        } else {
          valueList = new ArrayList<>();
          valueList.add(oldValue);
        }
        valueList.add(value);
        value = valueList;
      }
      result.put(key, value);
      annotationParameters = annotationParameters.getTail();
    }
    return result;
  }

  public String getEventName() {
    if (Jooc.EVENT_ANNOTATION_NAME.equals(getMetaName())) {
      Map<String, Object> propertiesByName = getPropertiesByName();
      Object eventName = propertiesByName.get(Jooc.EVENT_ANNOTATION_NAME_ATTRIBUTE_NAME);
      if (!(eventName instanceof String)) {
        eventName = propertiesByName.get(null);
      }
      if (eventName instanceof String) {
        return (String) eventName;
      }
    }
    return null;
  }
}
