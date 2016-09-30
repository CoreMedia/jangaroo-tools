package net.jangaroo.jooc.ast;

/**
 * A type declaration can either be predefined (*, void) or defined by a class or interface (ClassDeclaration).
 */
public abstract class TypeDeclaration extends IdeDeclaration {
  public TypeDeclaration(AnnotationsAndModifiers am, Ide ide) {
    super(am, ide);
  }

  public TypeDeclaration(Ide ide) {
    super(ide);
  }

  public abstract TypedIdeDeclaration getMemberDeclaration(String memberName);

  public abstract TypedIdeDeclaration getStaticMemberDeclaration(String memberName);

  public abstract ClassDeclaration getSuperTypeDeclaration();

  /**
   * Lookup a non-static member of the given name
   *
   * @param ide the member name
   * @return a non-static member if found, null otherwise
   */
  public IdeDeclaration resolvePropertyDeclaration(String ide) {
    return resolvePropertyDeclaration(ide, false);
  }

  public IdeDeclaration resolvePropertyDeclaration(String ide, boolean isStatic) {
    return null;
  }


}
