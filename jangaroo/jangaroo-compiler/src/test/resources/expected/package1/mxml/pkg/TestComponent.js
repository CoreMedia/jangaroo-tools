/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;*/
Ext.define("package1.mxml.pkg.TestComponent", function(TestComponent) {/*public class TestComponent extends TestComponentBase{

    public*/function TestComponent$(config/*:TestComponent = null*/){if(arguments.length<=0)config=null;config =net.jangaroo.ext.Exml.apply({
    property_1: "withDefault"
    },config);
    this.super$6zW8( net.jangaroo.ext.Exml.apply( AS3.cast(package1.mxml.pkg.TestComponentBase,{
    emptyText: net.jangaroo.ext.Exml.asString( '<div class=\'widget-content-list-empty\'>' + package1.mxml.pkg.TestComponentBase.DEFAULT + '</div>'),
    letters: [
              'a',
              'b',
              'c'
             ]
    }),config));
  }/*

    [Bindable]
    public var property_1:String;


    [Bindable]
    public var property_2:int;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.TestComponentBase",
      constructor: TestComponent$,
      super$6zW8: function() {
        package1.mxml.pkg.TestComponentBase.prototype.constructor.apply(this, arguments);
      },
      config: {
        property_1: null,
        property_2: 0
      },
      requires: ["package1.mxml.pkg.TestComponentBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
