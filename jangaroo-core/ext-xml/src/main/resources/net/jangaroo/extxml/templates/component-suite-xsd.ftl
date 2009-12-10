<#-- @ftlvariable name="" type="net.jangaroo.extxml.model.ComponentSuite" -->
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" targetNamespace="${namespace}" xmlns:${ns}="${namespace}" xmlns:xs="http://www.w3.org/2001/XMLSchema"${usedComponentSuiteNamespaces}>
<#list usedComponentSuites as usedComponentSuite>
  <xs:import namespace="${usedComponentSuite.namespace}"/>
</#list>
<#list componentClasses as componentClass>
  <xs:complexType name='${componentClass.fullClassName}'>
    <#if componentClass.superClass??>
    <xs:complexContent>
      <xs:extension base='${componentClass.superClass.xsType}'>
    </#if>
        <xs:all>
          <#list componentClass.directCfgs as cfg>
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
                <#if cfg.sequence>
                <xs:sequence>
                  <xs:any minOccurs="0" maxOccurs="unbounded" processContents="lax"/>
                </xs:sequence>
                <#if cfg.object>
                <xs:anyAttribute processContents="skip"/>
                </#if>
                <#else>
                <xs:simpleContent>
                  <xs:extension base="xs:string">
                    <xs:anyAttribute processContents="skip"/>
                  </xs:extension>
                </xs:simpleContent>
                </#if>
              </xs:complexType>
            </xs:element>
          </#if>
          </#list>
        </xs:all>
        <#list componentClass.directCfgs as cfg>
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
  <xs:element name='${componentClass.elementName}' type='${componentClass.xsType}' substitutionGroup='${componentClass.superClass.suite.prefix}${componentClass.superClass.elementName}'>
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
