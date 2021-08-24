package ext.mixin {

import ext.Base;

[Native("Ext.mixin.Observable", require)]
public class Observable extends Base implements IObservable {

  public var listeners: Object;

  public native function addEventListener(eventName:String, listener:Function):void;

}
}