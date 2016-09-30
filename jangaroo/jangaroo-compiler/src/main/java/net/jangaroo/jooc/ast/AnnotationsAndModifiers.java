package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;

import java.util.Collections;
import java.util.List;

public class AnnotationsAndModifiers {

  private List<Annotation> annotations;
  private List<JooSymbol> modifiers;

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public List<JooSymbol> getModifiers() {
    return modifiers;
  }

  public AnnotationsAndModifiers(List<Annotation> annotations, List<JooSymbol> modifiers) {
    this.annotations = annotations == null ? Collections.emptyList() : annotations;
    this.modifiers = modifiers == null ? Collections.emptyList() : modifiers;
  }
}
