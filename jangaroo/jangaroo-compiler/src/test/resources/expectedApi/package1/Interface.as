package package1 {

[Uses ("package1.SuperInterface")]
/**
 * Some ASDoc.
 */
public interface Interface extends SuperInterface {
  function doSomething():String;

  function get property():String;
}
}