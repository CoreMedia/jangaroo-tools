package{
public class Function extends Object {
  public native function Function(name : String = null, body : String = null);

  public native function get arguments():Arguments;

  public native function get arity():Number;

  public native function apply(thisArg:Object, argArray:Object):Object;

  public native function call(thisArg:Object, ... args):Object;

  public native function bind(thisArg:Object):Function;

  public native function getName():String;
}
}
