package {
[Native]
public class Array {

  public native function push(...any):uint;

  public native function forEach(fn:Function):void;

  public native function get length():uint;
}
}