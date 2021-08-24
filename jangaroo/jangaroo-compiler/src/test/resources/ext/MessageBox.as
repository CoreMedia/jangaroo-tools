package ext {

[Native("Ext.MessageBox", require)]
public class MessageBox {
  public native static function alert(msg:String):void;
}
}