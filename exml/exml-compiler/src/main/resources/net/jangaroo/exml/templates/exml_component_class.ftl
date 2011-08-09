<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

// Do not edit. This is an auto-generated class.

/**
 * ${model.description!}
 *
 * <p>This component is created by the xtype '${configClassPackage}.${model.className}' / the EXML element &lt;${configClassPackage}:${model.className}>.</p>
 * <p>See the config class for details.</p>
 *
 * @see ${configClassPackage}.${model.className}
 */
public class ${model.className} extends ${model.superClassName} {

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${configClassPackage}.${model.className}
   */
  public function ${model.className}(config:Object = null) {
    super(ext.Ext.apply(${formattedConfig}, config));
  }

  /**
   * Create a ${model.className}.
   * @param config The configuration options. See the config class for details.
   *
   * @see ${model.fullClassName}
   * @see ${configClassPackage}.${model.className}
   */
  public static function main(config:Object = null) {
    new ${model.fullClassName}(config);
  }
}
}
