package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.CompilationUnit;

class Dependency {
  private final CompilationUnit compilationUnit;
  private String compilationUnitId;
  private final String name;
  private DependencyLevel level;

  public Dependency(CompilationUnit compilationUnit, DependencyLevel level) {
    this.compilationUnit = compilationUnit;
    this.compilationUnitId = getCompilationUnitId(compilationUnit);
    this.name = compilationUnit.getPrimaryDeclaration().getIde().getIde().getText();
    this.level = level;
  }

  static String getCompilationUnitId(CompilationUnit compilationUnit) {
    return compilationUnit.getPrimaryDeclaration().getQualifiedNameStr();
  }

  public CompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

  public String getCompilationUnitId() {
    return compilationUnitId;
  }

  public String getCompilationUnitName() {
    return name;
  }

  public DependencyLevel getLevel() {
    return level;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Dependency dependency = (Dependency) o;

    if (level != dependency.level) {
      return false;
    }
    return compilationUnitId.equals(dependency.compilationUnitId);
  }

  @Override
  public int hashCode() {
    int result = compilationUnitId.hashCode();
    result = 31 * result + level.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return name + level.suffix;
  }
}
