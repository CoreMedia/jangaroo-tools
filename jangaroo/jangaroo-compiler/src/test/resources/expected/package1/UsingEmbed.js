Ext.define("package1.UsingEmbed", function(UsingEmbed) {/*package package1 {

[SomeRuntimeAnnotation]
/**
 * This is an example of a class using an [Embed] annotation.
 * /
[SomeRuntimeAnnotation(foo="bar")]
public class UsingEmbed {

  [Embed(source="package1/UsingEmbed.as")]
  public var someText:Class;

  [Embed(source="package1/Interface.as")]
  private static*/ var anotherText$static/*:Class*/=function(){return new String($resource_1)};/*

  [Embed(source="package1/jooley.png")]
  private static*/ var jooley$static/*:Class*/=function(){return flash.display.Bitmap.fromImg($resource_2)};/*

  [SomeRuntimeAnnotation]
  public var annotated1;

  [SomeRuntimeAnnotationWithArg("foo")]
  public*/ function annotated2() {
  }/*

  [SomeRuntimeAnnotationWithNamedArg(foo="bar")]
  public*/ function annotated3() {
  }/*

  [SomeRuntimeAnnotationWithNamedArg(foo="bar")]
  public*/ function annotated4() {
  }/*

  /**
   * multiple
   * /
  [SomeRuntimeAnnotation(type="bar")]
  /**
   * annotations
   * /
  [SomeRuntimeAnnotation(type="baz")]
  public*/ function annotated5() {
  }/*

  [SomePropertyAnnotation(1)]
  public native function get annotated6():String;

  [SomePropertyAnnotation(2)]
  public native function set annotated6(value:String):void;

}
}

============================================== Jangaroo part ==============================================*/
    return {
      metadata: {
        "": [
          "SomeRuntimeAnnotation",
          "SomeRuntimeAnnotation",
          [
            "foo",
            "bar"
          ]
        ],
        annotated1: ["SomeRuntimeAnnotation"],
        annotated2: [
          "SomeRuntimeAnnotationWithArg",
          [
            "",
            "foo"
          ]
        ],
        annotated3: [
          "SomeRuntimeAnnotationWithNamedArg",
          [
            "foo",
            "bar"
          ]
        ],
        annotated4: [
          "SomeRuntimeAnnotationWithNamedArg",
          [
            "foo",
            "bar"
          ]
        ],
        annotated5: [
          "SomeRuntimeAnnotation",
          [
            "type",
            "bar"
          ],
          "SomeRuntimeAnnotation",
          [
            "type",
            "baz"
          ]
        ],
        annotated6: [
          "SomePropertyAnnotation",
          [
            "",
            1
          ],
          "SomePropertyAnnotation",
          [
            "",
            2
          ]
        ]
      },
      someText: function(){return new String($resource_0)},
      annotated1: undefined,
      annotated2: annotated2,
      annotated3: annotated3,
      annotated4: annotated4,
      annotated5: annotated5,
      uses: ["flash.display.Bitmap"]
    };
});
