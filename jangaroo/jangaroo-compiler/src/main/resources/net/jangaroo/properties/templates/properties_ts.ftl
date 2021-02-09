<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
<#list tsImports?keys>
  <#items as identifier>
import ${identifier} from "${tsImports[identifier]}";
  </#items>

</#list>
<#if tsComment??>
/**
${tsComment}
 */
</#if>
<#if !locale??>
interface ${resourceBundle.className} extends ResourceBundleProperties {
<#list props as property>
  <#assign keyQuote=property.keyIsIdentifier?then("", "\"") />
  <#assign valueQuote=property.valueIsReference?then("", "\"") />
  <#if property.comment??>
  /**
  ${property.comment?replace("\n", "\n  ")}
   */
  </#if>
  ${keyQuote}${property.key?json_string}${keyQuote}: string;
</#list>
}

</#if>
<#if locale??>
ResourceBundleUtil.override(${resourceBundle.className}, {
<#else>
const ${resourceBundle.className}: ${resourceBundle.className} = {
</#if>
<#list props as property>
  <#assign keyQuote=property.keyIsIdentifier?then("", "\"") />
  <#assign valueQuote=property.valueIsReference?then("", "\"") />
  ${keyQuote}${property.key?json_string}${keyQuote}: ${valueQuote}${property.tsValue?json_string}${valueQuote},
</#list>
<#if locale??>
});
<#else>
};
</#if>
<#if !locale??>

export default ${resourceBundle.className};
</#if>
