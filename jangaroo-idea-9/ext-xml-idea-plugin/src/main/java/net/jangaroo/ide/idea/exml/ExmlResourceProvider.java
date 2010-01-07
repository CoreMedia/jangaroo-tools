package net.jangaroo.ide.idea.exml;

import com.intellij.javaee.ResourceRegistrar;
import com.intellij.javaee.StandardResourceProvider;

/**
 * Adds XML Schema for EXML 0.1.
 */
public class ExmlResourceProvider implements StandardResourceProvider {

  public static final String EXML_NAMESPACE_URI = "http://net.jangaroo.com/extxml/0.1";

  public void registerResources(ResourceRegistrar resourceRegistrar) {
    resourceRegistrar.addStdResource(EXML_NAMESPACE_URI, "/net/jangaroo/extxml/schemas/extxml.xsd", getClass());
  }

}
