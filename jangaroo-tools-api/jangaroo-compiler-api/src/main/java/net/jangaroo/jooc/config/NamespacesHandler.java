package net.jangaroo.jooc.config;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * An OptionHandler for mxmlc-compatible -namespace argument format.
 */
public class NamespacesHandler extends OptionHandler<List<NamespaceConfiguration>> {

  public NamespacesHandler(CmdLineParser parser, OptionDef option, Setter<? super List<NamespaceConfiguration>> setter) {
    super(parser, option, setter);
  }

  @Override
  public int parseArguments(Parameters parameters) throws CmdLineException {
    List<NamespaceConfiguration> namespaces = new ArrayList<NamespaceConfiguration>();
    String sourcePathString = parameters.getParameter(0).trim();
    if (!sourcePathString.isEmpty()) {
      final String[] namespaceManifestPairs = sourcePathString.split(",");
      if (namespaceManifestPairs.length % 2 != 0) {
        throw new CmdLineException(owner, "Namespaces must be defined by comma-separated pairs of URI and manifest file name.");
      }
      for (int i = 0; i < namespaceManifestPairs.length; i+=2) {
        String namespace = namespaceManifestPairs[i];
        String manifest = namespaceManifestPairs[i + 1];
        namespaces.add(new NamespaceConfiguration(namespace, manifest));
      }
    }
    setter.addValue(namespaces);
    return 1;
  }

  @Override
  public String getDefaultMetaVariable() {
    return "NAMESPACES";
  }
}
