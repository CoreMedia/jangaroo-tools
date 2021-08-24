/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.WhitespaceAroundBindingExpression", function(WhitespaceAroundBindingExpression) {/*public class WhitespaceAroundBindingExpression extends Panel{

    import ext.layout.ContainerLayout;public*/function WhitespaceAroundBindingExpression$(config/*:WhitespaceAroundBindingExpression=null*/){if(arguments.length<=0)config=null;
    this.super$TIEI(net.jangaroo.ext.Exml.apply(AS3.cast(WhitespaceAroundBindingExpression,{
  layout: new Ext.layout.ContainerLayout()
}),config));
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Panel",
      constructor: WhitespaceAroundBindingExpression$,
      super$TIEI: function() {
        Ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["Ext.Panel"],
      uses: [
        "Ext.layout.ContainerLayout",
        "net.jangaroo.ext.Exml"
      ]
    };
});
