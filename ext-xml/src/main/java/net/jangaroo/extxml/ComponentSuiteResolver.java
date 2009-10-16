package net.jangaroo.extxml;

import java.io.InputStream;
import java.io.IOException;

/**
 * A strategy interface for resolving a ComponentSuite namespace URI to an InputStream to the corresponding XSD.
 */
public interface ComponentSuiteResolver {

  InputStream resolveComponentSuite(String namespaceUri) throws IOException;
}
