<#-- @ftlvariable name="" type="net.jangaroo.properties.model.PropertiesClass" -->
<#list tsImports?keys>
  <#items as identifier>
import ${identifier} from "${tsImports[identifier]}";
  </#items>

</#list>
<#if locale??>
${resourceBundle.className}.assign({
<#else>
const DEFAULTS = {
</#if>
<#list props as property>
  <#assign keyQuote=property.keyIsIdentifier?then("", "\"") />
  <#assign valueQuote=property.valueIsReference?then("", "\"") />
  <#if property.comment??>
  /**
  ${property.comment}
  */
  </#if>
  ${keyQuote}${property.key?json_string}${keyQuote}: ${valueQuote}${property.tsValue?json_string}${valueQuote},
</#list>
<#if locale??>
});
<#else>
};
</#if>
<#if !locale??>

type ${resourceBundle.className} = PropertiesClass<typeof DEFAULTS>;
<#if tsComment??>

  /**
  ${tsComment}
  */
</#if>
declare const ${resourceBundle.className}: ${resourceBundle.className};

export default ${resourceBundle.className};
</#if>
