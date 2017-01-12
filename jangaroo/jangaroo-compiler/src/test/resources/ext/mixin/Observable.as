package ext.mixin {
import ext.Base;

public class Observable extends Base implements IObservable {

  public native function addEventListener(eventName:String, listener:Function):void;

}
}