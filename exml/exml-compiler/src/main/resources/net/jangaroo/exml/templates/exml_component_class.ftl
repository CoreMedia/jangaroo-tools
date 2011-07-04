<#-- @ftlvariable name="" type="net.jangaroo.exml.generation.RenderableExmlComponent" -->
package ${packageName} {

<#list allImports as import>
import ${import};
</#list>

/**
 * <b>Do not edit. This is an auto-generated class.</b>
 */
public class ${model.name} extends ${model.parentClassName} {
  public function ${model.name}(config:Object = null) {
    super(ext.Ext.apply(${formattedConfig!}, config));
  }
}
}