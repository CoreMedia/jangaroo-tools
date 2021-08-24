package ext.mixin {

[Native("Ext.mixin.IObservable", require)]
public interface IObservable {

  function addEventListener(eventName:String, listener:Function):void;

}
}