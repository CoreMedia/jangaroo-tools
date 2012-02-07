package net.jangaroo.exml.api;

import net.jangaroo.exml.config.ExmlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
  String EXML_PLUGIN_NODE_NAME = "plugin";
  String EXML_LAYOUT_NODE_NAME = "layout";
  String EXML_GRID_COLUMN_NODE_NAME = "gridColumn";
  String EXML_CLASS_NODE_NAME = "class";
  Set<String> EXML_ROOT_NODE_NAMES = new HashSet<String>(Arrays.asList(
    EXML_COMPONENT_NODE_NAME,
    EXML_PLUGIN_NODE_NAME,
    EXML_LAYOUT_NODE_NAME,
    EXML_GRID_COLUMN_NODE_NAME,
    EXML_CLASS_NODE_NAME
  ));

  String EXML_CONSTANT_NODE_NAME = "constant";
  /**
   * Use {@link #EXML_DECLARATION_NAME_ATTRIBUTE} instead.
   */
  @Deprecated
  @SuppressWarnings({"UnusedDeclaration"}) // needed by IDEA plugin for old compiler versions!
  String EXML_CONSTANT_NAME_ATTRIBUTE = "name";
  /**
   * Use {@link #EXML_DECLARATION_TYPE_ATTRIBUTE} instead.
   */
  @Deprecated
  @SuppressWarnings({"UnusedDeclaration"}) // needed by IDEA plugin for old compiler versions!
  String EXML_CONSTANT_TYPE_ATTRIBUTE = "type";
  /**
   * Use {@link #EXML_DECLARATION_VALUE_ATTRIBUTE} instead.
   */
  @Deprecated
  @SuppressWarnings({"UnusedDeclaration"}) // needed by IDEA plugin for old compiler versions!
  String EXML_CONSTANT_VALUE_ATTRIBUTE = "value";
  String EXML_VAR_NODE_NAME = "var";
  /**
   * The name of a constant or var.
   */
  String EXML_DECLARATION_NAME_ATTRIBUTE = "name";
  /**
   * The type of a constant or var.
   */
  String EXML_DECLARATION_TYPE_ATTRIBUTE = "type";
  /**
   * The value of a constant or var.
   */
  String EXML_DECLARATION_VALUE_ATTRIBUTE = "value";
  String EXML_BASE_CLASS_ATTRIBUTE = "baseClass";
  String EXML_EXCLUDE_CLASS_ATTRIBUTE = "excludeClass";
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
