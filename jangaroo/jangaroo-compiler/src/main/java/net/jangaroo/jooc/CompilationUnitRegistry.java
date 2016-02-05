package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.CompilationUnit;

public interface CompilationUnitRegistry {

  CompilationUnit getCompilationUnit(String qname);
}
