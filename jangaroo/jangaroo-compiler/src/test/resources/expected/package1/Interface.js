define(["exports","as3-rt/AS3","as3/package1/SuperInterface"], function($exports,AS3,SuperInterface) { AS3.interface_($exports, /*package package1 {

/**
 * Some ASDoc.
 * /
// this comment should vanish in the API!
public interface Interface extends SuperInterface {
  function doSomething():String;

  function get property():String;
}
}

============================================== Jangaroo part ==============================================*/
{
  package_: "package1",
  interface_: "Interface",
  extends_: [SuperInterface]
}
);});
