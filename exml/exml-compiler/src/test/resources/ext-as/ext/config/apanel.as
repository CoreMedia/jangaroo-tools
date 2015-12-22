package ext.config {

[ExtConfig(target="ext.APanel", xtype="apanel")]
public class apanel extends panel{

  public native function get foo():String;
  public native function set foo(value:String):void;
}
}
