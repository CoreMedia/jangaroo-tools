<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlConfigPackage" -->
<#assign namespaces = usedNamespaces?keys>
<#assign nsValues = usedNamespaces?values>
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" targetNamespace="exml:${packageName}" xmlns:${ns}="exml:${packageName}" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:exml="http://www.jangaroo.net/exml/0.8" <#list namespaces as namespace>xmlns:${usedNamespaces[namespace]}="exml:${namespace}" </#list>>
  <xs:import namespace="http://www.jangaroo.net/exml/0.8"/>
<#list namespaces as namespace>
  <xs:import namespace="exml:${namespace}"/>
</#list>
<#list exmlElements as exmlElement>
  <xs:complexType name='${exmlElement.typeName}'>
    <#if exmlElement.superElement??>
    <xs:complexContent>
      <xs:extension base='${exmlElement.superElement.fullTypeName}'>
    </#if>
        <xs:sequence>
          <#list exmlElement.directCfgs as cfg>
          <#if cfg.sequence || cfg.object>
            <xs:element name='${cfg.name}' minOccurs="0" maxOccurs="1">
              <#if cfg.description??>
              <xs:annotation>
                <xs:documentation>
                  ${cfg.description}
                </xs:documentation>
              </xs:annotation>
              </#if>
              <xs:complexType>
                <xs:sequence>
                  <xs:any minOccurs="0" maxOccurs="unbounded" processContents="lax"/>
                </xs:sequence>
                <#if cfg.sequence>
                <xs:attribute name="mode" type="exml:configMode"/>
                </#if>
              </xs:complexType>
            </xs:element>
          </#if>
          </#list>
        </xs:sequence>
        <#list exmlElement.directCfgs as cfg>
        <xs:attribute type='exml:${cfg.xsType}' name='${cfg.name}'>
          <#if cfg.description??>
          <xs:annotation>
            <xs:documentation>
              ${cfg.description}
            </xs:documentation>
          </xs:annotation>
          </#if>
        </xs:attribute>
        </#list>
        <xs:anyAttribute namespace="http://www.jangaroo.net/exml/0.8 exml:untyped" processContents="skip"/>
    <#if exmlElement.superElement??>
      </xs:extension>
    </xs:complexContent>
    </#if>
  </xs:complexType>
  <#if exmlElement.superElement??>
  <xs:element name='${exmlElement.name}' type='${exmlElement.fullTypeName}' substitutionGroup='${exmlElement.superElement.ns}:${exmlElement.superElement.name}'>
  <#else>
  <xs:element name='${exmlElement.name}' type='${exmlElement.fullTypeName}'>
  </#if>
    <#if exmlElement.description??>
    <xs:annotation>
      <xs:documentation>
        ${exmlElement.description}
      </xs:documentation>
    </xs:annotation>
    </#if>
  </xs:element>
</#list>
</xs:schema>