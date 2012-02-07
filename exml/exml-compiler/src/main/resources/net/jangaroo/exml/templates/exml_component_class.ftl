<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${model.escapedDescription!}
 *
 * <p>This component is created by <#if model.configClass.type??>the ${model.configClass.type.type} <code>'${model.configClass.fullName}'</code> /</#if> the EXML element <code>&lt;${model.configClass.ns}:${model.configClass.name}></code>
 * with <code>xmlns:${model.configClass.ns}="exml:${model.configClass.packageName}"</code>.</p>
 * <p>See the config class for details.</p>
 *
 * @see ${model.configClass.fullName}
 */
<#if model.excluded>
[ExcludeClass]
</#if>
public class ${model.className} extends ${model.superClassName} {
<#list model.configClass.constants as constant>
  /**
   * ${constant.escapedDescription!}
   */
  public static const ${constant.name}:${constant.type} = ${model.configClass.fullName}.${constant.name};
</#list>

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${model.configClass.fullName}
   */
  public function ${model.className}(config:${model.configClass.fullName} = null) {
<#list model.vars as var>
    var ${var.name}:${var.type} = ${var.value};
</#list>
    super(${model.configClass.fullName}(ext.Ext.apply(${formattedConfig}, config)));
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
