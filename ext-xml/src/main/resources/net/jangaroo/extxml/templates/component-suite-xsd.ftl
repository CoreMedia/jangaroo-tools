<#-- @ftlvariable name="" type="net.jangaroo.extxml.ComponentSuite" -->
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" targetNamespace="${namespace}" xmlns:xs="http://www.w3.org/2001/XMLSchema"${usedComponentSuiteNamespaces}>
<#list componentClasses as componentClass>
  <xs:complexType name='${componentClass.xsType}'>
    <#if componentClass.superClass??>
    <xs:complexContent>
      <xs:extension base='${componentClass.superClass.xsType}'>
    </#if>
        <xs:sequence>
          <xs:any minOccurs='0' maxOccurs='unbounded' processContents='lax'/>
        </xs:sequence>
        <#list componentClass.cfgs as cfg>
        <xs:attribute type='xs:${cfg.xsType}' name='${cfg.name}'>
          <#if cfg.description??>
          <xs:annotation>
            <xs:documentation>
${cfg.description}
            </xs:documentation>
          </xs:annotation>
          </#if>
        </xs:attribute>
        </#list>
    <#if componentClass.superClass??>
      </xs:extension>
    </xs:complexContent>
    </#if>
  </xs:complexType>
  <#if componentClass.superClass??>
  <xs:element name='${componentClass.elementName}' type='${componentClass.xsType}' substitutionGroup='${componentClass.superClass.elementName}'>
  <#else>
  <xs:element name='${componentClass.elementName}' type='${componentClass.xsType}'>
  </#if>
    <#if componentClass.description??>
    <xs:annotation>
      <xs:documentation>
${componentClass.description}
      </xs:documentation>
    </xs:annotation>
    </#if>
  </xs:element>
</#list>
</xs:schema>
