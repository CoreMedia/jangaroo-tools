package net.jangaroo.jooc.mxml.ast;

import com.google.common.base.Predicate;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.FunctionDeclaration;

import javax.annotation.Nullable;

class IsInitMethod implements Predicate<Directive> {
  @Override
  public boolean apply(@Nullable Directive directive) {
    if (directive instanceof FunctionDeclaration) {
      FunctionDeclaration f = (FunctionDeclaration) directive;
      return !f.isGetterOrSetter() && f.isPrivate() && f.getIde().getName().equals("__initialize__");
    }
    return false;
  }
}
