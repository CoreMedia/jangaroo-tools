<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

<#list imports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${escapedDescription!}
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>${componentClassName}</code>.
 * It defines the EXML element <code>&lt;${ns}:${name}></code> with <code>xmlns:${ns}="exml:${packageName}"</code>.</p>
<#if type??>
 * <p>Using this config class also takes care of registering the target class under the ${type.type}
 * <code>"${packageName}.${name}"</code> with Ext JS.</p>
</#if>
 *
 * @see ${componentClassName}
 */
[ExtConfig(target="${componentClassName}"<#if type??>, ${type.type}</#if>)]
<#if excluded>
[ExcludeClass]
</#if>
public dynamic class ${name} extends ${superClassName} {
<#list constants as constant>
  /**
   * ${constant.escapedDescription!}
   */
  public static const ${constant.name}:${constant.type} = ${constant.value};
</#list>

<#if type??>
  public static native function get ${type.type}():String;

</#if>
  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>${componentClassName}</code>.
<#if type??>
   * Using this config class also takes care of registering the target class under the ${type.type}
   * "${packageName}.${name}" with Ext JS.
</#if>
   * </p>
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
