package joo.binding {

[Native(amd)]
public class Binding {
  public native static function get(object:Object, getter:String, event:String):*;
  
  public function Binding(expression:Function) {
  }

  public native function execute():void;
}
}
