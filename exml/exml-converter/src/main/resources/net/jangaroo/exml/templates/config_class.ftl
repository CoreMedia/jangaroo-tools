<#-- @ftlvariable name="" type="net.jangaroo.exml.configconverter.generation.ConfigClassModel" -->
package ${componentSuite.configClassPackage} {

${importSuperClassPhrase}

/**
 * ${componentClass.description!}
 *
 * <p>This class serves as a typed config object for the constructor of the <#if type == 'xtype'>component<#elseif type == 'ptype'>plugin<#else>action</#if> class <code>${componentClass.className}</code>.
<#if type == 'xtype'>
 * Instantiating this class for the first time also registers the corresponding component class under the xtype
 * "${componentSuite.configClassPackage}.${className}" with ExtJS.
<#elseif type == 'ptype'>
 * Instantiating this class for the first time also registers the corresponding plugin class under the ptype
 * "${componentSuite.configClassPackage}.${className}" with ExtJS.
</#if> * </p>
 *
 * @see ${componentClass.fullClassName}
 */
[ExtConfig(target="${componentClass.fullClassName}"<#if type != ''>, ${type}</#if>)]
public dynamic class ${className}${extendsPhrase} {

<#if type == 'xtype'>
  public static native function get xtype():String;
<#elseif type == 'ptype'>
  public static native function get ptype():String;
</#if>
  /**
   * <p>Use this constructor to create a typed config object for the constructor of the <#if type == 'xtype'>component<#elseif type == 'ptype'>plugin<#else>action</#if> class
   * <code>${componentClass.className}</code><#if type == 'xtype'> and to register the component with ExtJS<#elseif type == 'ptype'> and to register the plugin with ExtJS</#if>.</p>
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