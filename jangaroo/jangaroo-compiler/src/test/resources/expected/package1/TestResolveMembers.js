Ext.define("package1.TestResolveMembers", function(TestResolveMembers) {/*package package1 {
public class TestResolveMembers {

  public*/ function TestResolveMembers$() {
    // resolve Object methods on something typed by an interface:
    var someInterface/*:Interface*/;
    var str/*:String*/ = someInterface.toString();
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {constructor: TestResolveMembers$};
});
