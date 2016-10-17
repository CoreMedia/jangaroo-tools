package package1 {

[Uses("package1.SuperInterface")]
/**
 * Some ASDoc.
 */
public interface Interface extends package1.SuperInterface {
  function doSomething():String;

  function get property():String;

  function set property(value:String):void;
}
}