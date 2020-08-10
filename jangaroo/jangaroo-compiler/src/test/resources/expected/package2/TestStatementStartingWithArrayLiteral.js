/*package package2 {*/
Ext.define("package2.TestStatementStartingWithArrayLiteral", function(TestStatementStartingWithArrayLiteral) {/*public class TestStatementStartingWithArrayLiteral {

  public static*/ function sum1_2_3$static()/*:int*/ {
    var result/*:int*/ = 0;
    [1, 2, 3].forEach(function(x/*:int*/)/*:void*/ { result += x; });
    return result;
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {statics: {sum1_2_3: sum1_2_3$static}};
});
