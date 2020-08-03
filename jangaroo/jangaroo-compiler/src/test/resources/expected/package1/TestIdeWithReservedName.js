/*package package1 {*/
Ext.define("package1.TestIdeWithReservedName", function(TestIdeWithReservedName) {/*public class TestIdeWithReservedName {
  public*/ function parameterWithReservedName(char_/*:String*/)/*:String*/ {
    char_ = char_ + "!";
    return char_;
  }/*

  public*/ function localVarWithReservedName()/*:String*/ {
    var char_/*:String*/ = "x";
    char_ = char_.substr(0,0) + "u";
    return char_;
  }/*
}*/function TestIdeWithReservedName$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      parameterWithReservedName: parameterWithReservedName,
      localVarWithReservedName: localVarWithReservedName,
      constructor: TestIdeWithReservedName$
    };
});
