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
<#if imports?has_content>
  requires: [
 <#list imports as import>
    "AS3.${import}"<#sep>,
</#list>

  ],
</#if>
<#list props as property>
  <#if property.comment??>
  /**${property.comment}
  */
  </#if>
  "${property.key}": <#if property.valueIsString>"${property.value?js_string}"<#else>AS3.${property.value}</#if><#sep>,
</#list>
<#if !locale??>

}, function() {
  AS3.${resourceBundle.fullClassName}_properties.INSTANCE = new AS3.${resourceBundle.fullClassName}_properties();</#if>
});