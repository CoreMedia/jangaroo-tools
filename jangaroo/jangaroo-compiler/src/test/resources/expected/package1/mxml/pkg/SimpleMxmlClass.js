/*package package1.mxml.pkg{
import ext.*;
import net.jangaroo.ext.Exml;*/
/**
 * Created by fwienber on 22.02.2021.
 */
Ext.define("package1.mxml.pkg.SimpleMxmlClass", function(SimpleMxmlClass) {/*public class SimpleMxmlClass extends Panel{

        import package1.mxml.SimpleMxmlClass;

        public static const xtype:String = "testNamespace.pkg.config.simpleMxmlClass";

        public*/function SimpleMxmlClass$(config/*:SimpleMxmlClass = null*/){if(arguments.length<=0)config=null;
    this.super$S4cv( net.jangaroo.ext.Exml.apply(AS3.cast(SimpleMxmlClass,{
        title:net.jangaroo.ext.Exml.asString( package1.mxml.SimpleMxmlClass.xtype)

}),config));
  }/*}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "Ext.Panel",
      alias: "widget.testNamespace.pkg.config.simpleMxmlClass",
      constructor: SimpleMxmlClass$,
      super$S4cv: function() {
        Ext.Panel.prototype.constructor.apply(this, arguments);
      },
      requires: ["Ext.Panel"],
      uses: [
        "net.jangaroo.ext.Exml",
        "package1.mxml.SimpleMxmlClass"
      ]
    };
});
