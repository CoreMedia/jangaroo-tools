<#-- @ftlvariable name="" type="net.jangaroo.extxml.generation.ConfigClassModel" -->
package ${componentSuite.configClassPackage} {

${importSuperClassPhrase}

/**
 * ${componentClass.description!}
 *
 * <p>This class serves as a typed config object for the constructor of the component class <code>${componentClass.className}</code>.
 * Instantiating this class for the first time also registers the corresponding component class under the xtype
 * "${componentSuite.configClassPackage}.${className}" with ExtJS.</p>
 *
 * @see ${componentClass.fullClassName}
 */
[ExtConfig(target="${componentClass.fullClassName}", xtype)]
public dynamic class ${className}${extendsPhrase} {

  public static native function get xtype():String;

  /**
   * <p>Use this constructor to create a typed config object for the constructor of the component class
   * <code>${componentClass.className}</code> and register the component with ExtJS.</p>
   *
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