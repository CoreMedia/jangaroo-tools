<#-- @ftlvariable name="" type="net.jangaroo.exml.model.ConfigClass" -->
package ${packageName} {

<#list imports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${escapedDescriptionWithoutAts!}
 *
 * <p>This class serves as a typed config object for the constructor of the class <code>${componentClassName}</code>.
 * It defines the EXML element <code>&lt;${ns}:${name}></code> with <code>xmlns:${ns}="exml:${packageName}"</code>.</p>
<#if type.extTypeAttribute??>
 * <p>Using this config class also takes care of registering the target class under the ${type.extTypeAttribute}
 * <code>"${packageName}.${name}"</code> with Ext JS.</p>
</#if>
 *
 * @see ${componentClassName}
<#if escapedDescriptionAts??>
 * ${escapedDescriptionAts}
</#if>
 */
[ExtConfig(target="${componentClassName}"<#if type.type??>, ${type.type}</#if>)]
<#list annotations as annotation>
[${annotation}]
</#list>
public class ${name} extends ${superClassName} {
<#list constants as constant>
  /**
   * ${constant.escapedDescription!}
   */
  public static const ${constant.name}:${constant.type} = ${constant.value};
</#list>

<#if type.type??>
  public static native function get ${type.type}():String;

</#if>
  /**
   * <p>Use this constructor to create a typed config object for the constructor of the class
   * <code>${componentClassName}</code>.
<#if type.extTypeAttribute??>
   * Using this config class also takes care of registering the target class under the ${type.extTypeAttribute}
   * "${packageName}.${name}" with Ext JS.
</#if>
   * </p>
   *
   * @see ${componentClassName}
   */
  public function ${name}(config:Object = null) {
    super(config || {});
  }

  <#list directCfgs as cfg>
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
