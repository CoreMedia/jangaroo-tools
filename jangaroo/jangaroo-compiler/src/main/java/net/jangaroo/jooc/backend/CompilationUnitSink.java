package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.ast.CompilationUnit;

import java.io.File;

/**
 * Code generation unit sink for a compilation unit.
 */
public interface CompilationUnitSink {
  File writeOutput(CompilationUnit compilationUnit);
}
