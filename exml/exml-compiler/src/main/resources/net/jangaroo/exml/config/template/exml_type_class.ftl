<#-- @ftlvariable name="" type="net.jangaroo.exml.config.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassPackage}.${superClassName};
import ${fullQualifiedName};

/**
 * ${description!}
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see ${fullQualifiedName}
 */
[ExtConfig(target=${fullQualifiedName})]
public class ${name} extends ${superClassName} {

  ComponentMgr.registerType("${fullQualifiedName}", ${fullQualifiedName});

  /**
   * @see ${fullQualifiedName}
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