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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.types.ExpressionType;

import java.util.List;
import java.util.regex.Pattern;


/**
 * @author Andreas Gawecki
 */
public abstract class IdeDeclaration extends Declaration {

  private static final Pattern PRIVATE_MEMBER_NAME = Pattern.compile("[^$]\\$[0-9]+$");

  private Ide ide;
  private ExpressionType type;

  protected IdeDeclaration(AnnotationsAndModifiers am, Ide ide) {
    super(am.getAnnotations(), toSymbolArray(am.getModifiers()));
    this.setIde(ide);
    if (ide != null && PRIVATE_MEMBER_NAME.matcher(ide.getName()).matches()) {
      JangarooParser.warning(ide.getSymbol(), "Jangaroo identifier must not be an ActionScript identifier postfixed with a dollar sign ('$') followed by a number.");
    }
  }

  protected IdeDeclaration(Ide ide) {
    this(new AnnotationsAndModifiers(null, null), ide);
  }

  protected static JooSymbol[] toSymbolArray(List symbols) {
    return (JooSymbol[])symbols.toArray(new JooSymbol[symbols.size()]);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide);
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymbol() {
    JooSymbol symbol = super.getSymbol();
    return symbol != null ? symbol : getDeclarationSymbol();
  }

  public JooSymbol getDeclarationSymbol() {
    return getIde().getSymbol();
  }

  public String getName() {
    return getIde() == null ? "" : getIde().getName();
  }

  public String[] getQualifiedName() {
    AstNode parentDeclaration = getParentDeclaration();
    if (!(parentDeclaration instanceof IdeDeclaration)) {
      return getIde() == null ? new String[0] : getIde().getQualifiedName();
    } else {
      String[] prefixName = ((IdeDeclaration) parentDeclaration).getQualifiedName();
      String[] result = new String[prefixName.length + 1];
      System.arraycopy(prefixName, 0, result, 0, prefixName.length);
      result[prefixName.length] = getIde().getName();
      return result;
    }
  }

  public String getTargetQualifiedNameStr() {
    Annotation nativeAnnotation = getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    String targetName = null;
    boolean require = true;
    if (nativeAnnotation != null) {
      targetName = (String) nativeAnnotation.getPropertiesByName().get(null);
      if (!nativeAnnotation.getPropertiesByName().containsKey(Jooc.NATIVE_ANNOTATION_REQUIRE_PROPERTY)) {
        require = false;
      }
    } else {
      Annotation renameAnnotation = getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
      if (renameAnnotation != null) {
        targetName = (String) renameAnnotation.getPropertiesByName().get(null);
      }
    }
    if (targetName == null || targetName.isEmpty()) {
      targetName = getQualifiedNameStr();
    }
    return targetName;
  }

  public String getExtNamespaceRelativeTargetQualifiedNameStr() {
    String targetName = getTargetQualifiedNameStr();
    InputSource inputSource = getCompilationUnit().getInputSource();
    String extNamespace = inputSource.getExtNamespace();
    if (extNamespace != null && !extNamespace.isEmpty()) {
      if (!targetName.startsWith(extNamespace + ".")) {
        throw JangarooParser.error("Source file fully-qualified name " + targetName + " does not start with configured extNamespace " + extNamespace);
      } else {
        targetName = targetName.substring(extNamespace.length() + 1);
      }
    }
    return targetName;
  }

  public String getQualifiedNameStr() {
    return QualifiedIde.constructQualifiedNameStr(getQualifiedName(), ".");
  }

  public ExpressionType getType() {
    return type;
  }

  void setType(final ExpressionType type) {//TODO compute type in more subclasses during analyze()
    this.type = type;
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getIde() != null) {
      getIde().scope(scope);
      AstNode oldNode = scope.declareIde(this);
      if (oldNode != null) {
        handleDuplicateDeclaration(scope, oldNode);
      }
    }
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (ide != null) {
      ide.analyze(this);
    }
  }

  public void handleDuplicateDeclaration(Scope scope, AstNode oldNode) {
    String msg = "Duplicate declaration of identifier '" + getName() + "'";
    if (allowDuplicates(scope)) {
      JangarooParser.warning(getSymbol(), msg);
    } else {
      throw JangarooParser.error(getSymbol(), msg);
    }
  }

  boolean allowDuplicates(Scope scope) {
    return false;
  }

  public boolean isMethod() {
    return false;
  }

  public boolean isConstructor() {
    return false;
  }

  /**
   * Resolve this declaration to the underlying Class or PredefinedType declaration
   * @return the declaration
   */
  public IdeDeclaration resolveDeclaration() {
    return null;
  }

  @Override
  public String toString() {
    return getQualifiedNameStr();
  }

  public boolean isPrimaryDeclaration() {
    return getIde() != null &&
            getIde().getScope() != null &&
            getIde().getScope().getCompilationUnit() != null &&
            this == getIde().getScope().getCompilationUnit().getPrimaryDeclaration();
  }

  public boolean isDeclaringCompileTimeConstant() {
    return false;
  }

  public void setIde(Ide ide) {
    this.ide = ide;
  }

  public PackageDeclaration getPackageDeclaration() {
    AstNode parentDeclaration = getParentDeclaration();
    return parentDeclaration instanceof IdeDeclaration ? ((IdeDeclaration)parentDeclaration).getPackageDeclaration() : null;
  }
}
