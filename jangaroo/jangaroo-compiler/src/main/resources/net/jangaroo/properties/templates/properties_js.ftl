<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
<#if comment??>
/**
${comment}
<#else>
/**
 * Properties class for ResourceBundle "${resourceBundle.bundleName}"<#if locale??> and Locale "${locale}"</#if>.
</#if>
 */
Ext.define("${resourceBundle.fullClassName}<#if locale??>_${locale}</#if>", {
<#if locale??>  override: "${resourceBundle.fullClassName}"<#if props?has_content>,</#if></#if>
<#if imports?has_content>
  requires: [
 <#list imports as import>
    "${import}"<#sep>,
</#list>

  ]<#if stringProps?has_content>,</#if>
</#if>
<#list stringProps as property>
    <#if property.comment??>
      /**
      ${property.comment}
      */
    </#if>
  "${property.key?json_string}": "${property.value?json_string}"<#sep>,
</#list>

}, function() {
<#list referenceProps as property>
  <#if property.comment??>
  /**
  ${property.comment}
  */
  </#if>
  this.prototype["${property.key?json_string}"] =  ${property.value};
</#list>
  <#if !locale??>

  ${resourceBundle.fullClassName}.INSTANCE = new ${resourceBundle.fullClassName}();
  </#if>
});