<#-- @ftlvariable name="" type="net.jangaroo.exml.config.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassPackage}.${superClassName};
import ${componentFullQualifiedName};

/**
 * ${description!}
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see ${componentFullQualifiedName}
 */
[ExtConfig(target=${componentFullQualifiedName})]
public class ${name} extends ${superClassName} {

  ComponentMgr.registerType("${componentFullQualifiedName}", ${componentFullQualifiedName});

  /**
   * @see ${componentFullQualifiedName}
   */
  public function ${name}(config:Object = null) {
    super(config || {});
  }

  <#list cfgs as cfg>
  /**
   * ${cfg.description}
   */
  public native function get ${cfg.name}():${cfg.jsType};
  /**
   * @private
   */
  public native function set ${cfg.name}(value:${cfg.jsType}):void;
  </#list>
}
}