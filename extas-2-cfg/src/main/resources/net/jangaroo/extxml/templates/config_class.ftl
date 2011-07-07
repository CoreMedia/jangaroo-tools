<#-- @ftlvariable name="" type="net.jangaroo.extxml.generation.ConfigClassModel" -->
package ${componentSuite.configClassPackage} {

import ext.ComponentMgr;
import ${componentClass.fullClassName};

/**
 * ${componentClass.description!}
 *
 * @see ${componentClass.fullClassName}
 */
[ExtConfig(target="${componentClass.fullClassName}")]
public class ${className} extends ${parentConfigClassName} {
  /**
   * @see ${componentClass.fullClassName}
   */
  public function ${className}(config:Object = null) {
    super(config || {});
  }

  <#list componentClass.cfgs as cfg>
  /**
   * ${cfg.description!}
   */
  public native function get ${cfg.name}():${cfg.jsType};
  /**
   * @private
   */
  public native function set ${cfg.name}(value:${cfg.jsType}):void;
  </#list>
}
}