<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
<#if comment??>
/**
${comment}
<#else>
/**
 * Properties class for ResourceBundle "${resourceBundle.className}"<#if locale??> and Locale "${locale}"</#if>.
</#if>
 */
Ext.define("${resourceBundle.fullClassName}_properties<#if locale??>_${locale}</#if>", {
<#if locale??>  override: "${resourceBundle.fullClassName}_properties"<#if props?has_content>,</#if></#if>
<#if imports?has_content>
  requires: [
 <#list imports as import>
    "${import}"<#sep>,
</#list>

  ],
</#if>
<#list stringProps as property>
    <#if property.comment??>
      /**
      ${property.comment}
      */
    </#if>
  "${property.key}": "${property.value?js_string}"<#sep>,
</#list>

}, function() {
<#list referenceProps as property>
  <#if property.comment??>
  /**
  ${property.comment}
  */
  </#if>
  this.prototype["${property.key}"] =  ${property.value};
</#list>
  <#if !locale??>

  ${resourceBundle.fullClassName}_properties.INSTANCE = new ${resourceBundle.fullClassName}_properties();
  </#if>
});