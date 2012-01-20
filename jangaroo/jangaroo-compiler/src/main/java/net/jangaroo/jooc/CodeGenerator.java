package net.jangaroo.jooc;

import java.io.IOException;

/**
 * Code generators write JavaScript to a {@link net.jangaroo.jooc.JsWriter}.
 */
public interface CodeGenerator {
  void generate(final JsWriter out, boolean first) throws IOException;
}
