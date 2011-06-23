package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.IdeDeclaration;

import java.io.File;

/**
 * Interface for {@link CompilationUnitSink} factories.
 */
public interface CompilationUnitSinkFactory {
  CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                 IdeDeclaration primaryDeclaration, File sourceFile,
                                 boolean verbose);
}
