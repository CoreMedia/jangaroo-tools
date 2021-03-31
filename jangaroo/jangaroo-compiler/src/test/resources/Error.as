package {

[Native("AS3.Error", require)]
public class Error {
  public native function get message():String;

  public native function set message(value:String):void;

  public native function get name():String;

  public native function set name(value:String):void;

  public native function Error(message : String = "", id:int = 0);
}
}
