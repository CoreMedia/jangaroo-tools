package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JsWriter;

import java.io.IOException;

/**
 * Code generators write JavaScript to a {@link net.jangaroo.jooc.JsWriter}.
 */
public interface CodeGenerator {
  void generateCode(JsWriter out) throws IOException;
}
