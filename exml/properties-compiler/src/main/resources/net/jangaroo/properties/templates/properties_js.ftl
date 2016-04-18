<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
<#if comment??>
/**${comment}
<#else>
/**
* Properties class for ResourceBundle "${resourceBundle.className}"<#if locale??> and Locale "${locale}"</#if>.
</#if>
*/
Ext.define("AS3.${resourceBundle.fullClassName}_properties<#if locale??>_${locale}</#if>", {
  <#if locale??>override: "AS3.${resourceBundle.fullClassName}_properties"<#if props?has_content>,</#if></#if>
<#list props as property>
  <#if property.comment??>
  /**${property.comment}
  */
  </#if>
   "${property.key}": "${property.value?js_string}"<#sep>,
</#list>
<#if !locale??>

}, function() {
  AS3.${resourceBundle.fullClassName}_properties.INSTANCE = new AS3.${resourceBundle.fullClassName}_properties();</#if>
});