<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {
<#list imports as import>
import ${import};
</#list>
/**
<#if as3Comment??>
${as3Comment}
<#else>
 * Overrides of ResourceBundle "${resourceBundle.bundleName}" for Locale "${locale}".
</#if>
 * @see ${resourceBundle.className}#INSTANCE
 */
public dynamic class ${resourceBundle.bundleName}_${locale}_properties extends ${resourceBundle.className} {

public function ${resourceBundle.bundleName}_${locale}_properties() {
  super();
<#list props as property>
 this<#if property.keyIsIdentifier>.${property.key}<#else>["${property.key?json_string}"]</#if> = <#if property.valueIsReference>${property.value}<#else>"${property.value?json_string}"</#if>;
</#list>
}

}
}