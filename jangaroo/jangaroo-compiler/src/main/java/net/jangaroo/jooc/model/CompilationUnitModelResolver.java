package net.jangaroo.jooc.model;

import java.io.IOException;

public interface CompilationUnitModelResolver {
  CompilationUnitModel resolveCompilationUnit(String qName) throws IOException;
}
