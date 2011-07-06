<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassName};
import ${componentClassName};

/**
 * ${description!}
 *
 * <b>Do not edit. This is an auto-generated class.</b>
 *
 * @see ${componentClassName}
 */
[ExtConfig(target="${componentClassName}")]
public class ${name} extends ${superClassName} {
  /**
   * @see ${componentClassName}
   */
  public function ${name}(config:Object = null) {
    super(config || {});
  }

  <#list cfgs as cfg>
  /**
   * ${cfg.description!}
   */
  public native function get ${cfg.name}():${cfg.type};
  /**
   * @private
   */
  public native function set ${cfg.name}(value:${cfg.type}):void;
  </#list>
}
}