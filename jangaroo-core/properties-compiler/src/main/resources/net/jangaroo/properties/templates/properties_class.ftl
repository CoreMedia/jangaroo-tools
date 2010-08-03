<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {

import joo.ResourceBundleAwareClassLoader;

/**
 * Properties class for ResourceBundle ${resourceBundle.className}<#if locale??> and Locale ${locale}</#if>.
 */
[ResourceBundle('${resourceBundle.className}<#if locale??>_${locale}</#if>')]
public class ${resourceBundle.className}_properties<#if locale??>_${locale} extends ${resourceBundle.className}_properties</#if> {

<#if !locale??>
public static const INSTANCE:${resourceBundle.className}_properties = ResourceBundleAwareClassLoader.INSTANCE.createSingleton(${resourceBundle.className}_properties) as ${resourceBundle.className}_properties;
</#if>

<#list props as property>
[Resource(key='${property.key}',bundle='${resourceBundle.className}<#if locale??>_${locale}</#if>')]
public const ${property.key}:String = "${property.value?js_string}";
</#list>

}
}