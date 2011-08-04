<#-- @ftlvariable name="" type="net.jangaroo.exml.generator.ExmlConfigPackage" -->
<#assign nsKeys = usedNamespaces?keys>
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" targetNamespace="exml:${packageName}" xmlns:${ns}="exml:${packageName}" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:exml="http://www.jangaroo.net/exml/0.8" <#list nsKeys as ns>xmlns:${ns}="exml:${usedNamespaces[ns]}" </#list>>
  <xs:import namespace="http://www.jangaroo.net/exml/0.8"/>
<#list nsKeys as key>
  <xs:import namespace="exml:${usedNamespaces[key]}"/>
</#list>
<#list configClasses as configClass>
  <xs:complexType name='${configClass.fullName}'>
    <#if configClass.superClass??>
    <xs:complexContent>
      <xs:extension base='${configClass.superClass.ns}:${configClass.superClass.fullName}'>
    </#if>
        <xs:sequence>
          <#list configClass.directCfgs as cfg>
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
                <xs:anyAttribute processContents="skip"/>
              </xs:complexType>
            </xs:element>
          </#if>
          </#list>
        </xs:sequence>
        <#list configClass.directCfgs as cfg>
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
    <#if configClass.superClass??>
      </xs:extension>
    </xs:complexContent>
    </#if>
  </xs:complexType>
  <#if configClass.superClass??>
  <xs:element id='${configClass.fullName}' name='${configClass.name}' type='${configClass.ns}:${configClass.fullName}' substitutionGroup='${configClass.superClass.ns}:${configClass.superClass.name}'>
  <#else>
  <xs:element id='${configClass.fullName}' name='${configClass.name}' type='${configClass.ns}:${configClass.fullName}'>
  </#if>
    <#if configClass.description??>
    <xs:annotation>
      <xs:documentation>
        ${configClass.description}
      </xs:documentation>
    </xs:annotation>
    </#if>
  </xs:element>
</#list>
</xs:schema>
