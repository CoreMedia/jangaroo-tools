package net.jangaroo.jooc;

import java.io.IOException;

/**
 * Code generators write JavaScript to a {@link net.jangaroo.jooc.JsWriter}.
 */
public interface CodeGenerator {
  void generateCode(JsWriter out, boolean generateApi) throws IOException;
}
