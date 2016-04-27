package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.utils.CompilerUtils;

class Dependency {
  private String compilationUnitId;
  private final String name;
  private DependencyLevel level;

  public Dependency(String compilationUnitId, DependencyLevel level) {
    this.compilationUnitId = compilationUnitId;
    this.name = CompilerUtils.className(compilationUnitId);
    this.level = level;
  }

  public Dependency(CompilationUnit compilationUnit, DependencyLevel level) {
    this.compilationUnitId = getCompilationUnitId(compilationUnit);
    this.name = compilationUnit.getPrimaryDeclaration().getIde().getIde().getText();
    this.level = level;
  }

  static String getCompilationUnitId(CompilationUnit compilationUnit) {
    return compilationUnit.getPrimaryDeclaration().getQualifiedNameStr();
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
