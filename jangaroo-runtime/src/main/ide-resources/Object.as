package {
public class Object {
  public native function get constructor():Object;

  public native function get prototype():Object;

  public native function set prototype(o:Object):void;

  public native function toLocaleString():Object;

  public native function toSource():Object;

  //public native function unwatch(prop:String):void;
  //public native function watch(prop:String, handler:Object):void;
  public native function valueOf(o:Object):String;

  public native function hasOwnProperty(propertyName:String):Boolean;

  public native function isPrototypeOf(o:Object):Boolean;

  public native function propertyIsEnumerable(propertyName:String):Boolean;

  public native function toString():String;
}
}
