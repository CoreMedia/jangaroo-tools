package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;

public class TrailingCommasPrettyPrinter extends PackageJsonPrettyPrinter {

  public TrailingCommasPrettyPrinter() {
    super();
  }

  @Override
  public DefaultPrettyPrinter createInstance() {
    return new TrailingCommasPrettyPrinter();
  }

  @Override
  public void writeEndObject(JsonGenerator g, int nrOfEntries) throws IOException {
    if (nrOfEntries > 0) {
      writeObjectEntrySeparator(g);
    }
    super.writeEndObject(g, nrOfEntries);
  }

  @Override
  public void writeEndArray(JsonGenerator g, int nrOfValues) throws IOException {
    if (nrOfValues > 0) {
      writeArrayValueSeparator(g);
    }
    super.writeEndArray(g, nrOfValues);
  }
}
