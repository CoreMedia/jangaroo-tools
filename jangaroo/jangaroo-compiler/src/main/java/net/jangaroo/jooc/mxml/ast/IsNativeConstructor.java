package net.jangaroo.jooc.mxml.ast;

import com.google.common.base.Predicate;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.FunctionDeclaration;

import javax.annotation.Nullable;

class IsNativeConstructor implements Predicate<Directive> {
  private final MxmlCompilationUnit mxmlCompilationUnit;

  public IsNativeConstructor(MxmlCompilationUnit mxmlCompilationUnit) {
    this.mxmlCompilationUnit = mxmlCompilationUnit;
  }

  @Override
  public boolean apply(@Nullable Directive directive) {
    if (directive instanceof FunctionDeclaration) {
      FunctionDeclaration f = (FunctionDeclaration) directive;
      return !f.isGetterOrSetter() && f.isNative() && f.getIde().getName().equals(mxmlCompilationUnit.getPrimaryDeclaration().getName());
    }
    return false;
  }
}
