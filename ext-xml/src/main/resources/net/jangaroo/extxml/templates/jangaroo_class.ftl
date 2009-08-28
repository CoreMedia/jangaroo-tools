<#-- @ftlvariable name="" type="com.coremedia.editor20xy.xslt.JooClass" -->
package ${packageName} {

<#list imports as import>
import ${import};
</#list>

public class ${className} extends ${extendsClass} {


  public function ${className}(config : Object = null) {
    super(Ext.apply(config, ${json}));
  }

}
}