define("as3/package1/NoCodeExpression",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
[RemoteClass("{this is not a code expression}", alias="{this is also not a code expression}")]
public class NoCodeExpression {
}*/function NoCodeExpression() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {
      metadata: {"": [
        "RemoteClass",
        [
          "",
          "{this is not a code expression}",
          "alias",
          "{this is also not a code expression}"
        ]
      ]},
      members: {constructor: NoCodeExpression}
    }));
  });
});
