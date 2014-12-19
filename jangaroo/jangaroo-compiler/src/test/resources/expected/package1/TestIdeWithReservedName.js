define("as3/package1/TestIdeWithReservedName",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
public class TestIdeWithReservedName {
  public*/ function parameterWithReservedName(char_/*:String*/)/*:String*/ {
    char_ = char_ + "!";
    return char_;
  }/*

  public*/ function localVarWithReservedName()/*:String*/ {
    var char_/*:String*/ = "x";
    char_ = char_.substr(0,0) + "u";
    return char_;
  }/*
}*/function TestIdeWithReservedName() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      parameterWithReservedName: parameterWithReservedName,
      localVarWithReservedName: localVarWithReservedName,
      constructor: TestIdeWithReservedName
    }}));
  });
});
