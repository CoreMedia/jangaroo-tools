/*package package1 {*/
Ext.define("package1.TestResolveMembers", function(TestResolveMembers) {/*public class TestResolveMembers {

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
