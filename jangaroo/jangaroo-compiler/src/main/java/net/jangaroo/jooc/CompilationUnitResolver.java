package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.CompilationUnit;

import javax.annotation.Nonnull;

/**
 * Interface for CompilationUniteModelResolvers with some convenience methods.
 */
public interface CompilationUnitResolver {

  @Nonnull
  CompilationUnit resolveCompilationUnit(@Nonnull String fullClassName, boolean needsScoping);

  @Nonnull
  CompilationUnit resolveCompilationUnit(@Nonnull String qName);

  boolean implementsInterface(CompilationUnit classCompilationUnit, String anInterface);

}
