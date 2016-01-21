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
import net.jangaroo.jooc.sym;

import java.io.IOException;
import java.util.List;

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
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), qualifier);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitQualifiedIde(this);
  }

  @Override
  public Ide getQualifier() {
    return qualifier;
  }

  public JooSymbol getSymDot() {
    return symDot;
  }

  public String[] getQualifiedName() {
    return append(qualifier.getQualifiedName(), getIde().getText());
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

  @Override
  public boolean isQualifiedBySuper() {
    return getQualifier() != null && getQualifier().getSymbol().sym == sym.SUPER;
  }

  public JooSymbol getSymbol() {
    return qualifier.getSymbol();
  }

  public static String constructQualifiedNameStr(String[] qualifiedName, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < qualifiedName.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(qualifiedName[i]);
    }
    return sb.toString();
  }

  @Override
  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return qualifier.qualify(symQualifier, symDot);
  }

  @Override
  public boolean addExternalUsage(boolean required) {
    return qualifier.addExternalUsage(required) || super.addExternalUsage(required);
  }

  @Override
  public void scope(final Scope scope) {
    qualifier.scope(scope);
    super.scope(scope);
  }

  @Override
  public void analyze(final AstNode parentNode) {
    qualifier.analyze(this);
    super.analyze(parentNode);
  }

  @Override
  public void analyzeAsExpr(final AstNode exprParent, final Expr parentExpr) {
    qualifier.analyzeAsExpr(exprParent, parentExpr);
    super.analyzeAsExpr(exprParent, parentExpr);
    final IdeDeclaration qualifierDeclaration = qualifier.getDeclaration(false);
    if (qualifierDeclaration != null) {
      if (qualifierDeclaration instanceof ClassDeclaration) {
        // full qualified access to static class member
        IdeDeclaration memberDeclaration = ((ClassDeclaration) qualifierDeclaration).getStaticMemberDeclaration(this.getName());
        if (memberDeclaration == null) {
          throw Jooc.error(getIde(), "unresolved static member " + getIde().getText());
        }
      } /* else  {
        // todo perform this check also for DotExpr and unqualified Ide
        IdeDeclaration type = qualifierDeclaration.resolveDeclaration();
        if (type != null && type != getScope().getCompilationUnit().getCompiler().getAnyDeclaration()) {
          IdeDeclaration memberDeclaration = type.resolvePropertyDeclaration(this.getName());
          if (memberDeclaration == null ) {
            //todo introduce strict mode where this is an error
            Jooc.warning(ide,
              type == getScope().getCompilationUnit().getCompiler().getVoidDeclaration()
                ? "member access to void type"
                : ("unresolved member " + ide.getText()));
          }
        }
      }*/
    }
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    IdeDeclaration result = super.resolveDeclaration();
    return result == null
            ? resolveQualifiedIdeDeclaration()
            : result;
  }

  @Override
  public IdeDeclaration getMemberDeclaration() {
    return resolveQualifiedIdeDeclaration();
  }

  private IdeDeclaration resolveQualifiedIdeDeclaration() {
    IdeDeclaration prefixDeclaration = getQualifier().resolveDeclaration();
    return prefixDeclaration != null
            ? prefixDeclaration.resolvePropertyDeclaration(this.getName())
            : null;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    final QualifiedIde that = (QualifiedIde) o;

    return qualifier == null ? that.qualifier == null : qualifier.equals(that.qualifier);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
    return result;
  }


}
