<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

import ext.ComponentMgr;
import ${superClassName};
import ${componentClassName};

// Do not edit. This is an auto-generated class.

/**
 * ${escapedDescription!}
 *
 * <p>This class serves as a typed config object for constructor of the component class <code>${componentClassName}</code>.
 * Instantiating this class for the first time also registers the corresponding component class under the xtype
 * "${packageName}.${name}" with ExtJS.</p>
 *
 * @see ${componentClassName}
 */
[ExtConfig(target="${componentClassName}", xtype)]
public dynamic class ${name} extends ${superClassName} {

  public static native function get xtype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the component class
   * <code>${componentClassName}</code> and register the component with ExtJS.</p>
   *
   * @see ${componentClassName}
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
