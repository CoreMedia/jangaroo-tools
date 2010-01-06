<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
package ${resourceBundle.packageName} {

/**
 * Properties class for Locale ${locale}
 */
public class ${resourceBundle.className}_properties {

<#list props as property>
public static const ${property.key} = "${property.value}";
</#list>
}
}