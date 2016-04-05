package ext.mixin {
public class Observable implements IObservable {

  public native function addEventListener(eventName:String, listener:Function):void;

}
}