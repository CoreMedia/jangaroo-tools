package net.jangaroo.jooc.model;

/**
 * Tagging interface for all ActionScript models.
 */
public interface ActionScriptModel {

  void visit(ModelVisitor visitor);
}