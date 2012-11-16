package testPackage.config {

[ExtConfig(target="ext.Container", xtype="container")]
public class container {

  public function container() {
  }

  public native function get title():String;

  public native function set title(value:String):void;
  
  public native function get items():Array;

  public native function set items(value:Array):void;
  
}
}