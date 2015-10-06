package joo {
public function addEventListener(config:Object, eventName:String, eventType:Class, callback:Function, scope:Object = null):void {
  if (!config.listeners) {
    config.listeners = {};
  }
  config.listeners[eventName] = function():void {
    callback.call(scope, new eventType(arguments));
  }
}

}