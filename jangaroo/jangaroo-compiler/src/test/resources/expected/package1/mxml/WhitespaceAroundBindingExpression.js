Ext.define("package1.mxml.WhitespaceAroundBindingExpression", function(WhitespaceAroundBindingExpression) {/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;
public class WhitespaceAroundBindingExpression extends Panel{

    import ext.layout.ContainerLayout;override protected*/function initConfig(_config/*:Object*/)/*:void*/{
    var config/*:WhitespaceAroundBindingExpression*/ =AS3.cast(WhitespaceAroundBindingExpression,_config);
    var config_$1/*:WhitespaceAroundBindingExpression*/ =AS3.cast(WhitespaceAroundBindingExpression,{});
    var defaults_$1/*:WhitespaceAroundBindingExpression*/ =AS3.cast(WhitespaceAroundBindingExpression,{});
    config= net.jangaroo.ext.Exml.apply(defaults_$1,config);
    config_$1["layout"] = new ext.layout.ContainerLayout(); net.jangaroo.ext.Exml.apply(config_$1,config);ext.Panel.prototype.initConfig.call(this,config_$1);
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      initConfig: initConfig,
      requires: ["ext.Panel"],
      uses: [
        "ext.layout.ContainerLayout",
        "net.jangaroo.ext.Exml"
      ]
    };
});
