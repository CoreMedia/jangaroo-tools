<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {
<#if !locale??>
import joo.JavaScriptObject;
</#if>

<#if comment??>
/**${comment}
<#else>
/**
 * Properties class for ResourceBundle "${resourceBundle.className}"<#if locale??> and Locale "${locale}"</#if>.
</#if>
 * @see ${resourceBundle.className}_properties#INSTANCE
 */
public class ${resourceBundle.className}_properties<#if locale??>_${locale} extends ${resourceBundle.className}_properties<#else> extends joo.JavaScriptObject</#if> {

<#if !locale??>
/**
 * Singleton for the current user Locale's instance of ResourceBundle "${resourceBundle.className}".
 * @see ${resourceBundle.className}_properties
 */
public static const INSTANCE:${resourceBundle.className}_properties = new ${resourceBundle.className}_properties();

<#list props as property>
<#if property.comment??>
/**${property.comment}
 */
</#if>
<#if property.keyIsIdentifier>
[Resource(key='${property.key}',bundle='${resourceBundle.className}<#if locale??>_${locale}</#if>')]
public native function get ${property.key}():String;
</#if>
</#list>
</#if>

public function ${resourceBundle.className}_properties<#if locale??>_${locale}</#if>() {
<#list props as property>
  this["${property.key}"] = "${property.value?js_string}";
</#list>
}
}
}