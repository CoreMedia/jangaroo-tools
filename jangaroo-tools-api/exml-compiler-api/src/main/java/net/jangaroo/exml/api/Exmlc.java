package net.jangaroo.exml.api;

import net.jangaroo.exml.config.ExmlConfiguration;

import java.io.File;

/**
 * Interface for EXML compiler, used by universal Jangaroo IDEA Plugin.
 */
public interface Exmlc {

  String EXML_NAMESPACE_URI = "http://www.jangaroo.net/exml/0.8";
  @SuppressWarnings({"UnusedDeclaration"}) // used by IDEA plugin!
    String EXML_SCHEMA_LOCATION = "/net/jangaroo/exml/schemas/exml.xsd";
  String EXML_CONFIG_URI_PREFIX = "exml:";
  @SuppressWarnings({"UnusedDeclaration"}) // used by IDEA plugin!
    String EXML_UNTYPED_NAMESPACE_URI = EXML_CONFIG_URI_PREFIX + "untyped";
  @SuppressWarnings({"UnusedDeclaration"}) // used by IDEA plugin!
    String EXML_UNTYPED_SCHEMA_LOCATION = "/net/jangaroo/exml/schemas/untyped.xsd";
  String EXML_COMPONENT_NODE_NAME = "component";
  String EXML_OBJECT_NODE_NAME = "object";
  String EXML_IMPORT_NODE_NAME = "import";
  String EXML_IMPORT_CLASS_ATTRIBUTE = "class";
  String EXML_DESCRIPTION_NODE_NAME = "description";
  String EXML_CFG_NODE_NAME = "cfg";
  String EXML_CFG_NAME_ATTRIBUTE = "name";
  String EXML_CFG_TYPE_ATTRIBUTE = "type";
  String EXML_SUFFIX = ".exml";
  String OUTPUT_CHARSET = "UTF-8";

  void setConfig(ExmlConfiguration config);

  ExmlConfiguration getConfig();

  void generateAllConfigClasses();

  File generateConfigClass(File source);

  File generateComponentClass(File exmlSourceFile);

  void generateAllComponentClasses();

  File generateXsd();
}
