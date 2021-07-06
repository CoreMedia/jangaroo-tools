package ext.mixin {
public class Observable implements IObservable {

  public var listeners: Object;

  public native function addEventListener(eventName:String, listener:Function):void;

}
}