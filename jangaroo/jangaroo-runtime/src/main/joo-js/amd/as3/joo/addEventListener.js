define(function() {
  return function addEventListener(observableOrConfig, eventName, callback, eventType) {
    var listener = eventType && eventType !== Object ? function () {
      return callback(new eventType(arguments));
    } : callback;

    if (typeof observableOrConfig.addListener === "function") {
      observableOrConfig.addListener(eventName, listener);
    } else {
      if (!observableOrConfig.listeners) {
        observableOrConfig.listeners = {};
      }
      observableOrConfig.listeners[eventName] = listener;
    }
  }
});
