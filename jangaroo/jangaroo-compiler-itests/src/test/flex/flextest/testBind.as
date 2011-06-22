package flextest {

import package1.TestBind;

public function testBind():void {
  var obj:TestBind = new TestBind('foo');
  expectString("foo", obj.getState());
  expectString("foo", obj.testInvokeLocalVar());
  expectString("foo", obj.testInvokeLocalVarUnqualified());
  expectString("foo", obj.testInvokeParameter());
  expectString("foo", obj.testInvokeParameterUnqualified());
  expectString("foo", obj.testInvokeParameterUnqualifiedPrivate());
  expectString("foo", obj.testInvokeObjectField());
  expect(55, obj.testLocalFunctionIde());
  expectString("foo", obj.testLocalFunction());
  expectString("foo", obj.testLocalFunctionUnqualified());

  //todo this method _is_ 'bound' in Flex:
  expectString("foo", obj.testNotBound.call({getState: function():String{return 'bar';}}));

  expectBoolean(true, package1.TestBind.testStaticNotBound().call('bar')=='bar');
  expectString("foo", obj.testInvokeLocalVarMethod());
  expectString("foo", obj.testInvokeFieldMethod());
  expectString("foo", obj.testInvokeFieldMethodThroughLocalFunction());
  expectString("foo", obj.testInvokeFieldMethodThroughLocalFunctionExpr());
  expectString("sounds good", obj.testDontInvokeFieldMethodThroughLocalFunction());
  expectString("sounds good", obj.testDontInvokeFieldMethodThroughLocalFunctionExpr());
  expectString("foo", obj.testReturn());
  expectBoolean(true, obj.testBindMethodInBinaryOpExpr());

  //todo re-write this test to pass in Flex?!
  //expectString("undefined", typeof obj.testBindNonExistentMethod());
  
  expectBoolean(true, obj.testBindTwiceReturnsSameFunction());
}

}

function fail(msg:String):void {
  throw new Error(msg);
}

function expect(value:*, o:*):void {
  if (value !== o) {

    fail("Expected " + typeof(value) + " value: '" + value + "', found " + typeof(o) + ": '" + o + "'");
  }
}

function expectString(value:String, o:*):void {
  expect(value, o);
}

function expectBoolean(value:Boolean, o:*):void {
  expect(value, o);
}
