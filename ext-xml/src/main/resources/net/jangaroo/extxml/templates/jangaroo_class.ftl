<#-- @ftlvariable name="" type="net.jangaroo.extxml.JooClass" -->
package ${packageName} {

import ext.ComponentMgr;
<#list imports as import>
import ${import};
</#list>

public class ${className} extends ${extendsClass} {

  public const xtype:String = "${xtype}";
{
  ext.ComponentMgr.registerType(xtype, ${className});
}

  public function ${className}(config:* = undefined) {
    super(Ext.apply(config, ${json}));
  }

}
}