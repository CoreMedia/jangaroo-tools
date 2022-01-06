package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class PackageJsonPrettyPrinter extends DefaultPrettyPrinter {

  public PackageJsonPrettyPrinter() {
    super(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE).withSpacesInObjectEntries());
    // we want a space only after the colon:
    _objectFieldValueSeparatorWithSpaces = _separators.getObjectFieldValueSeparator() + " ";
  }

  @Override
  public DefaultPrettyPrinter createInstance() {
    return new PackageJsonPrettyPrinter();
  }
}
