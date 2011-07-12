<#-- @ftlvariable name="" type="net.jangaroo.extxml.generation.ConfigClassModel" -->
package ${componentSuite.configClassPackage} {

import ext.ComponentMgr;
import ${componentClass.fullClassName};
${importSuperClassPhrase}

/**
 * ${componentClass.description!}
 *
 * <p>
 * Do not instantiate this class! Instead, instantiate the associated
 * component class ${componentClass.className} directly.
 * This class is only provided to document the config attributes
 * to use when building instances of the component class.
 * </p>
 *
 * @see ${componentClass.fullClassName}
 */
[ExtConfig(target="${componentClass.fullClassName}")]
public class ${className}${extendsPhrase} {
  /**
   * @private
   */
  public function ${className}(config:Object = null) {
    throw new Error("do not instantiate the config class ${componentSuite.configClassPackage}.${className}; " +
      "instantiate the component class ${componentClass.fullClassName} instead");
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