<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

//Do not edit. This is an auto-generated class.

/**
 * <p>This component is created by the xtype '${configClassPackage}.${model.className}' / the EXML element &lt;${configClassPackage}:${model.className}>.</p>
 * <p>See the config class for details.</p>
 *
 * @see ${configClassPackage}.${model.className}
 */
public class ${model.className} extends ${model.superClassName} {
  public static const xtype:String = "${configClassPackage}.${model.className}";
  ext.ComponentMgr.registerType(xtype, ${model.className});

  /**
   * @param config The configuration options. See the config class for details.
   *
   * @see ${configClassPackage}.${model.className}
   */
  public function ${model.className}(config:Object = null) {
    super(ext.Ext.apply(${formattedConfig}, config));
  }
}
}