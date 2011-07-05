<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlComponentClassModel" -->
package ${model.packageName} {

<#list allImports as import>
import ${import};
</#list>

/**
 * <b>Do not edit. This is an auto-generated class.</b>
 */
public class ${model.className} extends ${model.parentClassName} {
  public function ${model.className}(config:Object = null) {
    super(ext.Ext.apply(${formattedConfig}, config));
  }
}
}