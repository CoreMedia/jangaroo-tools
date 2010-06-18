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
 */
public class QualifiedIde extends Ide {

  private Ide qualifier;
  private JooSymbol symDot;

  public QualifiedIde(Ide qualifier, JooSymbol symDot, JooSymbol symIde) {
    super(symIde);
    this.qualifier = qualifier;
    this.symDot = symDot;
    qualifier.setQualified(this);
  }

  @Override
  protected Ide getQualifier() {
    return qualifier;
  }

  public String[] getQualifiedName() {
    return append(qualifier.getQualifiedName(), ide.getText());
  }

  static String[] append(String[] prefixName, String ideText) {
    String[] result = new String[prefixName.length + 1];
    System.arraycopy(prefixName, 0, result, 0, prefixName.length);
    result[prefixName.length] = ideText;
    return result;
  }

  @Override
  public String getQualifiedNameStr() {
    return constructQualifiedNameStr(getQualifiedName(), ".");
  }

  public JooSymbol getSymbol() {
    return qualifier.getSymbol();
  }

  static String constructQualifiedNameStr(String[] qualifiedName, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < qualifiedName.length; i++) {
      if (i > 0)
        sb.append(separator);
      sb.append(qualifiedName[i]);
    }
    return sb.toString();
  }

  @Override
  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return qualifier.qualify(symQualifier, symDot);
  }

  @Override
  public boolean addExternalUsage() {
    if (qualifier.addExternalUsage()) {
      return true;
    }
    return super.addExternalUsage();
  }

  @Override
  public void scope(final Scope scope) {
    qualifier.scope(scope);
    super.scope(scope);
  }

  @Override
  public AstNode analyze(final AstNode parentNode, final AnalyzeContext context) {
    qualifier.analyze(this, context);
    return super.analyze(parentNode, context);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    qualifier.generateCode(out);
    out.writeSymbol(symDot);
    out.writeSymbol(ide);
  }

  @Override
  public void analyzeAsExpr(final AstNode exprParent, final Expr parentExpr, final AnalyzeContext context) {
    qualifier.analyzeAsExpr(exprParent, parentExpr, context);
    super.analyzeAsExpr(exprParent, parentExpr, context);
    if (isQualifiedByThis()|| isQualifiedBySuper()) {
      getDeclaration(true);
    }
  }

  @Override
  protected void generateCodeAsExpr(final Expr parentExpr, final JsWriter out) throws IOException {
    IdeDeclaration declaration = getDeclaration(false);
    if (declaration != null) {
      if (declaration.isClassMember()) {
        // check and handle private instance members and super method access:
        if (isQualifiedByThis() && declaration.isPrivate()
          || isQualifiedBySuper()) {
          writePrivateMemberAccess(qualifier.getSymbol(), symDot, this, declaration.isStatic(), out);
          return;
        }
        // check and handle private static member access:
        if (declaration.isPrivateStatic() && !declaration.isMethod()) {
          IdeDeclaration prefixDeclaration = qualifier.resolveDeclaration();
          // Found private static member access candidate qualifiedName+"."+property
          if (getScope().getClassDeclaration().equals(prefixDeclaration)) {
            // Found private static member access qualifiedName+"."+property
            writePrivateMemberAccess(qualifier.getSymbol(), symDot, this, true, out);
            return;
          }
          // todo flag error, but do that in analyze phase! will cause a runtime error: undefined member
        }
      }
      generateCode(out);
    } else {
      qualifier.generateCodeAsExpr(parentExpr, out);
      out.writeSymbol(symDot);
      out.writeSymbol(ide);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    final QualifiedIde that = (QualifiedIde) o;

    if (qualifier != null ? !qualifier.equals(that.qualifier) : that.qualifier != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
    return result;
  }


}
