<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${model.escapedDescriptionWithoutAts!}
 *
 * <p>This component is created by <#if model.configClass.type.extTypeAttribute??>the ${model.configClass.type.extTypeAttribute} <code>'${model.configClass.fullName}'</code> /</#if> the EXML element <code>&lt;${model.configClass.ns}:${model.configClass.name}></code>
 * with <code>xmlns:${model.configClass.ns}="exml:${model.configClass.packageName}"</code>.</p>
 * <p>See the config class for details.</p>
 *
 * @see ${model.configClass.fullName}
<#if model.escapedDescriptionAts??>
 * ${model.escapedDescriptionAts}
</#if>
 */
<#list model.annotations as annotation>
[${annotation}]
</#list>
public class ${model.className} extends ${model.superClassName} {
<#list model.configClass.constants as constant>
  /**
   * ${constant.escapedDescription!}
   */
  public static const ${constant.name}:${constant.type} = <#if constant.standAloneConstant>${constant.value}</#if><#if !constant.standAloneConstant>${model.configClass.fullName}.${constant.name}</#if>;
</#list>

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${model.configClass.fullName}
   */
  public function ${model.className}(config:${model.configClass.fullName} = null) {
<#if !model.cfgDefaults.empty>
    config = ${model.configClass.fullName}(net.jangaroo.ext.Exml.apply(${formattedCfgDefaults}, config));
</#if>
<#list model.vars as var>
    var ${var.name}:${var.type} = ${var.value};
</#list>
    super(${model.configClass.fullName}(net.jangaroo.ext.Exml.apply(${formattedConfig}, config)));
  }

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${model.configClass.fullName}
   */
  public static function main(config:${model.configClass.fullName} = null):void {
    new ${model.fullClassName}(config);
  }
}
}
