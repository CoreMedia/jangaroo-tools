package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.CompilationUnit;

class Dependency {
  private CompilationUnit compilationUnit;
  private DependencyLevel level;

  public Dependency(CompilationUnit compilationUnit, DependencyLevel level) {
    this.compilationUnit = compilationUnit;
    this.level = level;
  }

  public CompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

  public String getCompilationUnitName() {
    return compilationUnit.getPrimaryDeclaration().getIde().getIde().getText();
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
    return compilationUnit.equals(dependency.compilationUnit);
  }

  @Override
  public int hashCode() {
    int result = compilationUnit.hashCode();
    result = 31 * result + level.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return getCompilationUnitName() + level.suffix;
  }
}
