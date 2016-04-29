Ext.define("package1.NoCodeExpression", function(NoCodeExpression) {/*package package1 {
[RemoteClass("{this is not a code expression}", alias="{this is also not a code expression}")]
public class NoCodeExpression {
}*/function NoCodeExpression$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      metadata: {"": [
        "RemoteClass",
        [
          "",
          "{this is not a code expression}",
          "alias",
          "{this is also not a code expression}"
        ]
      ]},
      constructor: NoCodeExpression$
    };
});
