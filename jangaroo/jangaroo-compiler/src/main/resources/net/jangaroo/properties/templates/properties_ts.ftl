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
class ${resourceBundle.className}<#if locale??>_${locale} extends ${resourceBundle.className}</#if> {
  static INSTANCE: ${resourceBundle.className};

<#list props as property>
  <#assign keyQuote=property.keyIsIdentifier?then("", "\"") />
  <#assign valueQuote=property.valueIsReference?then("", "\"") />
  <#if property.comment??>
  /**
  ${property.comment}
   */
  </#if>
  ${keyQuote}${property.key?json_string}${keyQuote} = ${valueQuote}${property.tsValue?json_string}${valueQuote};
</#list>
}

export default ${resourceBundle.className}<#if locale??>_${locale}</#if>;
