package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;

public class PackageJsonPrettyPrinter extends DefaultPrettyPrinter {

  public PackageJsonPrettyPrinter() {
    _arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
    _spacesInObjectEntries = false;
  }

  @Override
  public DefaultPrettyPrinter createInstance() {
    return new PackageJsonPrettyPrinter();
  }

  @Override
  public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
    jg.writeRaw(": ");
  }
}
