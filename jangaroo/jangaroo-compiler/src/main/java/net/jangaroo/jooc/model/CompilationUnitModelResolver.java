package net.jangaroo.jooc.model;

import javax.annotation.Nonnull;

public interface CompilationUnitModelResolver {
  @Nonnull
  CompilationUnitModel resolveCompilationUnit(@Nonnull String qName);
}
