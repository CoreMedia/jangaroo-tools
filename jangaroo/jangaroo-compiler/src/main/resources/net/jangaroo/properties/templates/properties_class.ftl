<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {

<#if as3Comment??>
/**
${as3Comment}
<#else>
/**
 * AS3 API stub for ResourceBundle "${resourceBundle.bundleName}".
</#if>
 * @see ${resourceBundle.className}#INSTANCE
 */
[Native("${resourceBundle.fullClassName}", require)]
public class ${resourceBundle.className} {

/**
 * Singleton for the current user Locale's instance of ResourceBundle "${resourceBundle.bundleName}".
 * @see ${resourceBundle.className}
 */
public static const INSTANCE:${resourceBundle.className};

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