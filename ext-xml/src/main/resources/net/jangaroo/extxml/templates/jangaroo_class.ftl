<#-- @ftlvariable name="" type="net.jangaroo.extxml.ComponentClass" -->
package ${packageName} {

import Ext;
import ext.ComponentMgr;
<#list imports as import>
import ${import};
</#list>
/**
 * @xtype ${xtype}
 */
public class ${className} extends ${superClassName} {

  public static const xtype:String = "${xtype}";
{
  ext.ComponentMgr.registerType(xtype, ${className});
}

  public function ${className}(config:* = {}) {
    super(Ext.apply(config, ${json}));
  }

  public static function main(config:* = {}):void {
    new ${fullClassName}(config);
  }

}
}