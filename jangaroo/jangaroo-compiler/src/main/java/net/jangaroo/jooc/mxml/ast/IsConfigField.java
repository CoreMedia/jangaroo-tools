package net.jangaroo.jooc.mxml.ast;

import com.google.common.base.Predicate;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.FunctionDeclaration;

import javax.annotation.Nullable;

class IsConfigField implements Predicate<Directive> {
  private final MxmlCompilationUnit mxmlCompilationUnit;

  IsConfigField(MxmlCompilationUnit mxmlCompilationUnit) {
    this.mxmlCompilationUnit = mxmlCompilationUnit;
  }

  @Override
  public boolean apply(@Nullable Directive directive) {
    if (directive instanceof FunctionDeclaration) {
      FunctionDeclaration f = (FunctionDeclaration) directive;
      if (f.isGetter() && f.isPrivate() && f.isNative() && !f.isStatic() && f.getOptTypeRelation() != null
              && f.getOptTypeRelation().getType().getIde().getName().equals(mxmlCompilationUnit.getPrimaryDeclaration().getName())) {
        return true;
      }
    }
    return false;
  }
}
