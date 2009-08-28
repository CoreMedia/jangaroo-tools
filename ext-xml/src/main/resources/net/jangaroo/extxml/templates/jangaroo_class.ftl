<#-- @ftlvariable name="" type="net.jangaroo.extxml.JooClass" -->
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