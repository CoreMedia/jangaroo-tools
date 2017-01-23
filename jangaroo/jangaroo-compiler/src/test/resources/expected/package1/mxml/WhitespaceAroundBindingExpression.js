Ext.define("package1.mxml.WhitespaceAroundBindingExpression", function(WhitespaceAroundBindingExpression) {/*package package1.mxml{
import ext.*;
public class WhitespaceAroundBindingExpression extends Panel{

    import ext.layout.ContainerLayout;public*/function WhitespaceAroundBindingExpression$(config/*:WhitespaceAroundBindingExpression=null*/){ext.Panel.prototype.constructor.call(this);if(arguments.length<=0)config=null;
    var config_$1/*:WhitespaceAroundBindingExpression*/ =AS3.cast(WhitespaceAroundBindingExpression,{});
    config_$1["layout"] = new ext.layout.ContainerLayout();
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: WhitespaceAroundBindingExpression$,
      requires: ["ext.Panel"],
      uses: ["ext.layout.ContainerLayout"]
    };
});
