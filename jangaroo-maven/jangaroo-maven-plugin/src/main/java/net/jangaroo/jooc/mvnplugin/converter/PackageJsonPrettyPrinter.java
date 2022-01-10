package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class PackageJsonPrettyPrinter extends DefaultPrettyPrinter {

  private static final DefaultIndenter DEFAULT_INDENTER = new DefaultIndenter("  ", "\n");

  public PackageJsonPrettyPrinter() {
    super(new DefaultPrettyPrinter()
            .withArrayIndenter(DEFAULT_INDENTER)
            .withObjectIndenter(DEFAULT_INDENTER)
            .withSpacesInObjectEntries());
    // we want a space only after the colon:
    _objectFieldValueSeparatorWithSpaces = _separators.getObjectFieldValueSeparator() + " ";
  }

  @Override
  public DefaultPrettyPrinter createInstance() {
    return new PackageJsonPrettyPrinter();
  }
}
