package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.ast.CompilationUnit;

/**
 * Code generation unit sink for a compilation unit.
 */
public interface CompilationUnitSink {
  void writeOutput(CompilationUnit compilationUnit);
}
