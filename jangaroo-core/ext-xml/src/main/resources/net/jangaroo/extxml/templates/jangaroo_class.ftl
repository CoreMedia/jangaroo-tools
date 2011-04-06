<#-- @ftlvariable name="" type="net.jangaroo.extxml.model.ComponentClass" -->
package ${packageName} {

import ext.Ext;
import ext.ComponentMgr;
<#list imports as import>
import ${import};
</#list>

/**
 * ${description!}
 *
 * <b>Do not edit. this is an auto-generated class.</b>
 */
public class ${className} extends ${superClassName} {

  public static const xtype:String = "${xtype}";
{
  ext.ComponentMgr.registerType(xtype, ${className});
}

  /**<#list cfgs as cfg>
   * @cfg {${cfg.jsType}} ${cfg.name}</#list>
   *
   * @see ${className} 
   */
  public function ${className}(config:Object = null) {
    if(config == null) {
      config = {};
    }
    super(Ext.apply(${jsonForTemplate!"{}"}, config));
  }

  public static function main(config:Object = null):void {
    new ${fullClassName}(config);
  }

}
}