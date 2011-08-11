<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${model.description!}
 *
 * <p>This component is created by the xtype '${configClassPackage}.${model.configClassName}' / the EXML element &lt;${configClassPackage}:${model.configClassName}>.</p>
 * <p>See the config class for details.</p>
 *
 * @see ${configClassPackage}.${model.configClassName}
 */
public class ${model.className} extends ${model.superClassName} {

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${configClassPackage}.${model.configClassName}
   */
  public function ${model.className}(config:${configClassPackage}.${model.configClassName} = null) {
    super(${configClassPackage}.${model.configClassName}(ext.Ext.apply(${formattedConfig}, config)));
  }

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${configClassPackage}.${model.configClassName}
   */
  public static function main(config:${configClassPackage}.${model.configClassName} = null):void {
    new ${model.fullClassName}(config);
  }
}
}
