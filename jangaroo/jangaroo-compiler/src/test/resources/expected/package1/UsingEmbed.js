/*package package1 {

[SomeRuntimeAnnotation]
/**
 * This is an example of a class using an [Embed] annotation.
 * /
[SomeRuntimeAnnotation(foo="bar")]*/
Ext.define("package1.UsingEmbed", function(UsingEmbed) {/*public class UsingEmbed {

  [Embed(source="first-text.txt", mimeType="application/octet-stream")]
  public var someText:Class;

  [Embed(source="second_text.csv", mimeType="application/octet-stream")]
  private static*/ var anotherText$static/*:Class*/=null;/*

  [Embed(source="jooley.png")]
  private static*/ var jooley$static/*:Class*/=null;/*

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
      annotated1: undefined,
      annotated2: annotated2,
      annotated3: annotated3,
      annotated4: annotated4,
      annotated5: annotated5,
      statics: {someText: null},
      uses: ["joo.flash.Embed"]
    };
});
