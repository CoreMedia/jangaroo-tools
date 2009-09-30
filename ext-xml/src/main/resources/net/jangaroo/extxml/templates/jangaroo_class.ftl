<#-- @ftlvariable name="" type="net.jangaroo.extxml.ComponentClass" -->
package ${packageName} {

import Ext;
import ext.ComponentMgr;
<#list imports as import>
import ${import};
</#list>

/**
 * Do not edit. this is an auto-generated class.
 * @xtype ${xtype}
 */
public class ${className} extends ${superClassName} {

  public static const xtype:String = "${xtype}";
{
  ext.ComponentMgr.registerType(xtype, ${className});
}


  /**<#list cfgs as cfg>
   * @cfg {${cfg.jsType}} ${cfg.name}</#list>
   */
  public function ${className}(config:* = {}) {
    super(Ext.apply(config, ${json}));
  }

  public static function main(config:* = {}):void {
    new ${fullClassName}(config);
  }

}
}