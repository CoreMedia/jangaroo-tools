<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {
<#list imports as import>
import ${import};
</#list>
<#if annotations??>
${annotations}
</#if>
/**
<#if as3Comment??>
${as3Comment}
<#else>
 * Interface values for ResourceBundle "${resourceBundle.bundleName}".
</#if>
 * @see ${resourceBundle.className}#INSTANCE
 */
public class ${resourceBundle.className} {

/**
 * Singleton for the current user Locale's instance of ResourceBundle "${resourceBundle.bundleName}".
 * @see ${resourceBundle.className}
 */
public static const INSTANCE: ${resourceBundle.className} = new ${resourceBundle.className}();

<#list props as property>
<#if property.keyIsIdentifier>
<#if property.comment??>
/**
${property.comment}
 */
</#if>
public var ${property.key}: String;
</#if>
</#list>

public function ${resourceBundle.className}() {
<#list props as property>
<#if property.keyIsIdentifier>
  ${property.key}<#else><#if property.comment??>
/*
${property.comment}
 */
</#if>
  this["${property.key?json_string}"]</#if> = <#if property.valueIsReference>${property.value}<#else>"${property.value?json_string}"</#if>;
</#list>
}

}
}