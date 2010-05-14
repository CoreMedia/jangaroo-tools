package net.jangaroo.jooc;

public abstract class ScopeImplBase implements Scope {

  private Scope parent;

  public ScopeImplBase(Scope parent) {
    this.parent = parent;
  }

  @Override
  public Scope getParentScope() {
    return parent;
  }

  @Override
  public AstNode getDefiningNode() {
    if (parent == null) return null;
    return parent.getDefiningNode();
  }

  @Override
  public AstNode declareIde(final String name, final AstNode decl) {
    mustBeInsideValueScope();
    return parent.declareIde(name, decl);
  }

  @Override
  public AstNode declareIde(final String name, final AstNode node, final boolean allowDuplicates, final JooSymbol ideSymbol) {
    mustBeInsideValueScope();
    return parent.declareIde(name, node, allowDuplicates, ideSymbol);
  }

  private void mustBeInsideValueScope() {
    if (parent == null)
      throw new UnsupportedOperationException("this scope must be wrapped by a ValueScope");
  }

  @Override
  public LabeledStatement findLabel(final Ide ide) {
    if (parent == null) return null;
    return parent.findLabel(ide);
  }

  @Override
  public AstNode getIdeDeclaration(final Ide ide) {
    if (parent == null) return null;
    return parent.getIdeDeclaration(ide);
  }

  @Override
  public AstNode getIdeDeclaration(final String name) {
    if (parent == null) return null;
    return parent.getIdeDeclaration(name);
  }

  @Override
  public Scope findScopeThatDeclares(final Ide ide) {
    if (parent == null) return null;
    return parent.findScopeThatDeclares(ide);
  }

  @Override
  public Scope findScopeThatDeclares(final String name) {
    if (parent == null) return null;
    return parent.findScopeThatDeclares(name);
  }

  @Override
  public Ide createAuxVar() {
    return parent.createAuxVar();
  }

  @Override
  public LoopStatement getCurrentLoop() {
    if (parent == null) return null;
    return parent.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    if (parent == null) return null;
    return parent.getCurrentLoopOrSwitch();
  }

  @Override
  public void addExternalUsage(final Ide ide) {
    mustBeInsideValueScope();
    parent.addExternalUsage(ide);
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return parent == null ? null : parent.getCompilationUnit();
  }

  @Override
  public PackageDeclaration getPackageDeclaration() {
    return parent == null ? null : parent.getPackageDeclaration();
  }

  @Override
  public ClassDeclaration getClassDeclaration() {
    return parent == null ? null : parent.getClassDeclaration();
  }

  @Override
  public MethodDeclaration getMethodDeclaration() {
    return parent == null ? null : parent.getMethodDeclaration();
  }
}
