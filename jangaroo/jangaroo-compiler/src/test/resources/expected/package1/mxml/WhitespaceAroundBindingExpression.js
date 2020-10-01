/*package package1.mxml{
import ext.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.WhitespaceAroundBindingExpression", function(WhitespaceAroundBindingExpression) {/*public class WhitespaceAroundBindingExpression extends Panel{

    import ext.layout.ContainerLayout;public*/function WhitespaceAroundBindingExpression$(config/*:WhitespaceAroundBindingExpression=null*/){if(arguments.length<=0)config=null;
    this.super$TIEI(net.jangaroo.ext.Exml.apply(AS3.cast(WhitespaceAroundBindingExpression,{
  layout: new ext.layout.ContainerLayout()
}),config));
}/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.Panel",
      constructor: WhitespaceAroundBindingExpression$,
      super$TIEI: function() {
        ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["ext.Panel"],
      uses: [
        "ext.layout.ContainerLayout",
        "net.jangaroo.ext.Exml"
      ]
    };
});
