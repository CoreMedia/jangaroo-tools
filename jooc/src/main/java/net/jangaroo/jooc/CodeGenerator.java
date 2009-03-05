package net.jangaroo.jooc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: fwienber
 * Date: 03.03.2009
 * Time: 23:45:22
 * To change this template use File | Settings | File Templates.
 */
public interface CodeGenerator {
  void generateCode(JsWriter out) throws IOException;
}
