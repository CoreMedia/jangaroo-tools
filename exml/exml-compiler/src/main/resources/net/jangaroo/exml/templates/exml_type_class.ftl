<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassPackage}.${superClassName};
import ${componentName};

/**
 * ${description!}
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see ${componentName}
 */
[ExtConfig(target="${componentName}")]
public class ${name} extends ${superClassName} {
  /**
   * @see ${componentName}
   */
  public function ${name}(config:Object = null) {
    super(config || {});
  }

  <#list cfgs as cfg>
  /**
   * ${cfg.description}
   */
  public native function get ${cfg.name}():${cfg.type};
  /**
   * @private
   */
  public native function set ${cfg.name}(value:${cfg.type}):void;
  </#list>
}
}