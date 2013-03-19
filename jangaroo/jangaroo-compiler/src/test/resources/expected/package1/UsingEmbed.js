define(["exports","as3-rt/AS3","as3/flash/display/Bitmap","text!package1/UsingEmbed.as","text!package1/Interface.as","image!package1/jooley.png"], function($exports,AS3,Bitmap,$resource_0,$resource_1,$resource_2) { AS3.compilationUnit($exports, function($primaryDeclaration){/*package package1 {

/**
 * This is an example of a class using an [Embed] annotation.
 * /
public class UsingEmbed {

  [Embed(source="package1/UsingEmbed.as")]
  public var someText:Class;

  [Embed(source="package1/Interface.as")]
  private static*/ var anotherText$static/*:Class*/=function(){return new String($resource_1)};/*

  [Embed(source="package1/jooley.png")]
  private static*/ var jooley$static/*:Class*/=function(){return (Bitmap._||Bitmap._$get()).fromImg($resource_2)};/*

}*/function UsingEmbed() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_({
      package_: "package1",
      class_: "UsingEmbed",
      members: {
        someText: {
          value: function(){return new String($resource_0)},
          writable: true
        },
        constructor: UsingEmbed
      }
    }));
  });
});
