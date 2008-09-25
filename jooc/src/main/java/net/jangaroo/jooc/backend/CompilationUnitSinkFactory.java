package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.PackageDeclaration;
import net.jangaroo.jooc.ClassDeclaration;

import java.io.File;

/**
 * Interface for {@link CompilationUnitSink} factories.
 */
public interface CompilationUnitSinkFactory {
  CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                 ClassDeclaration classDeclaration, File sourceFile,
                                 boolean verbose);
}
