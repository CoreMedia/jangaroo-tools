<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {

<#if as3Comment??>
/**
${as3Comment}
<#else>
/**
 * AS3 API stub for ResourceBundle "${resourceBundle.className}".
</#if>
 * @see ${resourceBundle.className}_properties#INSTANCE
 */
[Native("AS3.${resourceBundle.fullClassName}_properties", require)]
public class ${resourceBundle.className}_properties {

/**
 * Singleton for the current user Locale's instance of ResourceBundle "${resourceBundle.className}".
 * @see ${resourceBundle.className}_properties
 */
public static const INSTANCE:${resourceBundle.className}_properties;

<#list props as property>
<#if property.keyIsIdentifier>
<#if property.comment??>
/**
${property.comment}
*/
</#if>
public native function get ${property.key}():String;
</#if>
</#list>

}
}