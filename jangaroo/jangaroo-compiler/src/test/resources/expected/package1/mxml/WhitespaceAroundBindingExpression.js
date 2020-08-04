/*package package1.mxml{
import ext.*;*/
Ext.define("package1.mxml.WhitespaceAroundBindingExpression", function(WhitespaceAroundBindingExpression) {/*public class WhitespaceAroundBindingExpression extends Panel{

    import ext.layout.ContainerLayout;public*/function WhitespaceAroundBindingExpression$(config/*:WhitespaceAroundBindingExpression=null*/){if(arguments.length<=0)config=null;this.super$TIEI();
    var config_$1/*: ext.Panel*/ =AS3.cast(ext.Panel,{});
    config_$1.layout = new ext.layout.ContainerLayout();
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: WhitespaceAroundBindingExpression$,
      super$TIEI: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"],
      uses: ["ext.layout.ContainerLayout"]
    };
});
