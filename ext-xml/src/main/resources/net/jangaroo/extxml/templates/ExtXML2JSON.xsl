<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:ext="http://extjs.com/ext3"
                xmlns:joo="http://net.jangaroo.com/extxml"
                version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <xsl:output method="text"/>

    <!--<xsl:template match="/">-->
    <!-- entry point: find anyhing below the 'ext:viewport' element -->
    <!--<xsl:for-each select="ext:viewport">-->
    <!--<xsl:call-template name="main"/>-->
    <!--</xsl:for-each>-->
    <!--</xsl:template>-->

    <xsl:template match="/">
        <!-- entry point: find anyhing below the 'ext:viewport' element -->
        <xsl:for-each select="joo:component">
            <xsl:call-template name="main"/>
        </xsl:for-each>
    </xsl:template>


    <!-- called from the 'ext:viewport' element or from anything insid an 'items' element -->
    <xsl:template name="main">
        <xsl:for-each select="*">
            <xsl:choose>
                <!-- plain JSON in '<json><![CDATA[ ... ]]></json>' section -->
                <xsl:when test="local-name() = 'json'">
                    <xsl:value-of select="." disable-output-escaping="yes"/>
                    <xsl:if test="position()!=last()">,&#xa;</xsl:if>
                </xsl:when>
                <!-- all other elements -->
                <xsl:otherwise>
                    <xsl:text>{</xsl:text>
                    <xsl:choose>
                        <!-- handle our custom 'layout' element ... -->
                        <xsl:when test="local-name() = 'layout'">
                            <!-- which has a mandatory 'region' attribute-->
                            <xsl:text>region: "</xsl:text>
                            <xsl:value-of select="@region"/>
                            <xsl:text>"</xsl:text>
                            <!-- add other elements as attributes -->
                            <xsl:for-each select="*">
                                <xsl:text>,&#xa;</xsl:text>
                                <xsl:call-template name="element"/>
                                <xsl:if test="position()!=last()">,</xsl:if>
                            </xsl:for-each>
                        </xsl:when>
                        <!-- all other elements -->
                        <xsl:otherwise>
                            <xsl:call-template name="element"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:text>}&#xa;</xsl:text>
                    <xsl:if test="position()!=last()">,</xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

    <!-- handle an XML element -->
    <xsl:template name="element">
        <!-- element name is the xtype! -->
        <xsl:text>xtype: "</xsl:text>
        <xsl:value-of select="local-name()"/>
        <xsl:text>"</xsl:text>
        <!-- add all xml attributes as JSON attributes -->
        <xsl:for-each select="@*">
            <xsl:text>,&#xa;</xsl:text>
            <xsl:call-template name="attribute"/>
        </xsl:for-each>
        <xsl:for-each select="*">
            <xsl:if test="position()=1">,&#xa;</xsl:if>
            <xsl:choose>
                <!-- add attributes of the 'defaults' element as JSON attributes -->
                <xsl:when test="local-name() = 'defaults'">
                    <xsl:call-template name="attributes"/>
                </xsl:when>
                <!-- add attributes of the 'tinymceSettings' element as JSON attributes -->
                <xsl:when test="local-name() = 'tinymceSettings'">
                    <xsl:call-template name="attributes"/>
                </xsl:when>
                <!-- add elements inside the 'items' element as JSON array -->
                <xsl:when test="local-name() = 'items'">
                    <xsl:value-of select="local-name()"/>
                    <xsl:text>: [</xsl:text>
                    <xsl:call-template name="main"/>
                    <xsl:text>]&#xa;</xsl:text>
                </xsl:when>
                <!-- add elements inside the 'menu' element as JSON array -->
                <xsl:when test="local-name() = 'menu'">
                    <xsl:value-of select="local-name()"/>
                    <xsl:text>: [</xsl:text>
                    <xsl:call-template name="main"/>
                    <xsl:text>]</xsl:text>
                </xsl:when>
            </xsl:choose>
            <xsl:if test="position()!=last()">,</xsl:if>
        </xsl:for-each>
    </xsl:template>

    <!-- add all XML attributes as JSON attributes -->
    <xsl:template name="attributes">
        <xsl:value-of select="local-name()"/>
        <xsl:text>: {</xsl:text>
        <xsl:for-each select="@*">
            <xsl:call-template name="attribute"/>
            <xsl:if test="position()!=last()">,</xsl:if>
        </xsl:for-each>
        <xsl:text>}&#xa;</xsl:text>
    </xsl:template>

    <!-- add a single XML attribute as JSON attribute-->
    <xsl:template name="attribute">
        <xsl:value-of select="local-name()"/>
        <xsl:text>: </xsl:text>
        <xsl:choose>
            <!-- plain javascript in '{' and '}' -->
            <xsl:when test="matches(., '\{.*\}')">
                <!-- remove curly braces -->
                <xsl:value-of select="substring(., 2, string-length(.) - 2)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <!-- if it seems to be a boolean or a number, we don't quote it -->
                    <xsl:when test="string(.) castable as xs:float or matches(., 'true|false')">
                        <xsl:value-of select="."/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>"</xsl:text>
                        <xsl:value-of select="."/>
                        <xsl:text>"</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
