Ext.define("package1.mxml.pkg.TestComponent", function(TestComponent) {/*package package1.mxml.pkg{
import package1.mxml.pkg.*;
import net.jangaroo.ext.Exml;
public class TestComponent extends TestComponentBase{

    public*/function TestComponent$(config/*:TestComponent = null*/){if(arguments.length<=0)config=null;config = net.jangaroo.ext.Exml.apply(
  {
    property_1: "withDefault"
  },config);config = net.jangaroo.ext.Exml.apply(AS3.cast(TestComponent,{
        emptyText: '<div class=\'widget-content-list-empty\'>' + package1.mxml.pkg.TestComponentBase.DEFAULT + '</div>',
        letters: [
              'a',
              'b',
              'c'
             ]}),config);
    package1.mxml.pkg.TestComponentBase.prototype.constructor.call(this,config);
  }/*

    [Bindable]
    public var property_1:String;


    [Bindable]
    public var property_2:int;}}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package1.mxml.pkg.TestComponentBase",
      constructor: TestComponent$,
      config: {
        property_1: null,
        property_2: 0
      },
      requires: ["package1.mxml.pkg.TestComponentBase"],
      uses: ["net.jangaroo.ext.Exml"]
    };
});
