<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassName};
import ${componentClassName};

// Do not edit. This is an auto-generated class.

/**
 * ${escapedDescription!}
 *
 * <p>
 * <b>Do not instantiate this class!</b> Instead, instantiate the associated
 * component class ${componentClassName} directly.
 * This class is only provided to document the config attributes
 * to use when building instances of the component class.
 * </p>
 *
 * @see ${componentClassName}
 */
[ExtConfig(target="${componentClassName}", xtype)]
public dynamic class ${name} extends ${superClassName} {

  public static native function get xtype():String;

  /**
   * @private
   */
  public function ${name}(config:Object = null) {
    super(config || {});
  }

  <#list cfgs as cfg>
  /**
   * ${cfg.escapedDescription!}
   */
  public native function get ${cfg.name}():${cfg.type};
  /**
   * @private
   */
  public native function set ${cfg.name}(value:${cfg.type}):void;
  </#list>
}
}
