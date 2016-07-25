package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;

/**
 * A type declaration can either be predefined (*, void) or defined by a class or interface (ClassDeclaration).
 */
public abstract class TypeDeclaration extends IdeDeclaration {
  public TypeDeclaration(JooSymbol[] modifiers, Ide ide) {
    super(modifiers, ide);
  }

  public TypeDeclaration(Ide ide) {
    super(ide);
  }

  public abstract TypedIdeDeclaration getMemberDeclaration(String memberName);

  public abstract TypedIdeDeclaration getStaticMemberDeclaration(String memberName);

  public abstract ClassDeclaration getSuperTypeDeclaration();
}
