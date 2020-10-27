package net.jangaroo.jooc.mvnplugin.converter;

public enum ModuleType {
  /**
   * used for a code package or a theme via Maven
   */
  SWC,
  /**
   * used for an app via Maven
   */
  JANGAROO_APP,
  /**
   * used for an app overlay via Maven
   */
  JANGAROO_APP_OVERLAY,
  /**
   * used for apps via Maven
   */
  JANGAROO_APPS,
  /**
   * used for a package (old ext pkg format)
   */
  EXT_PKG,
  /**
   * it is just there, ignore it (e.g. from npm registry)
   */
  IGNORE,
}
