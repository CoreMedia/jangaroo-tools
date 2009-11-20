package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;

/**
 * Code generation unit sink for a compilation unit.
 */
public interface CompilationUnitSink {
  void writeOutput(CodeGenerator codeGenerator);
}
