package ext.config {

[ExtConfig(target="ext.Container")]
public class container {

  public function container() {
  }

  public native function get title():String;

  public native function set title(value:String):void;
  
}
}