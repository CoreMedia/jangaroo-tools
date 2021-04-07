/*package package2 {

/**
 * This is some class documentation.
 * /
[RestResource(uriTemplate="test/{id:[0-9]+}")]*/
Ext.define("package2.TestRestResourceAnnotation", function(TestRestResourceAnnotation) {/*public class TestRestResourceAnnotation {
  /**
   * This is some member documentation.
   * /
  private static const*/var ANOTHER_CONST$static/*: Number*/ = 42;/*
}
}

============================================== Jangaroo part ==============================================*/
    return {metadata: {"": [
      "RestResource",
      [
        "uriTemplate",
        "test/{id:[0-9]+}"
      ]
    ]}};
});
