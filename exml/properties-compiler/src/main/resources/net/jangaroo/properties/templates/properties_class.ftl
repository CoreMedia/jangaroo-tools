<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {
<#if !locale??>
import joo.ResourceBundleAwareClassLoader;
import joo.JavaScriptObject;
</#if>
<#list imports as import>
import ${import};
</#list>

<#if comment??>
/**${comment}
<#else>
/**
 * Properties class for ResourceBundle "${resourceBundle.className}"<#if locale??> and Locale "${locale}"</#if>.
</#if>
 * @see ${resourceBundle.className}_properties#INSTANCE
 */
<#if locale??>[Override]
</#if>public class ${resourceBundle.className}_properties<#if locale??>_${locale} extends ${resourceBundle.className}_properties<#else> extends joo.JavaScriptObject</#if> {

<#if !locale??>
/**
 * Singleton for the current user Locale's instance of ResourceBundle "${resourceBundle.className}".
 * @see ${resourceBundle.className}_properties
 */
public static const INSTANCE:${resourceBundle.className}_properties = ResourceBundleAwareClassLoader.INSTANCE.createSingleton(${resourceBundle.className}_properties) as ${resourceBundle.className}_properties;

<#list props as property>
<#if property.comment??>
/**${property.comment}
 */
</#if>
<#if property.keyIsIdentifier>
public native function get ${property.key}():String;
</#if>
</#list>
</#if>

public function ${resourceBundle.className}_properties<#if locale??>_${locale}</#if>() {
<#list props as property>
<#if property.valueIsString>
  this["${property.key}"] = "${property.value?js_string}";
<#else>
  this["${property.key}"] = ${property.value};
</#if>
</#list>
}
}
}