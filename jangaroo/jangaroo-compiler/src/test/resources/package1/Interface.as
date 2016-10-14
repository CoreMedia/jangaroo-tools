package package1 {

/**
 * Some ASDoc.
 */
// this comment should vanish in the API!
public interface Interface extends SuperInterface {
  function doSomething():String;

  function get property():String;

  function set property(value:String):void;
}
}
