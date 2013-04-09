define(function() {
  return function addEventListener(observableOrConfig, eventName, callback, eventType) {
    var listener = eventType ? function () {
      return callback(new eventType(arguments));
    } : callback;

    if (observableOrConfig.isInstance) {
      observableOrConfig.addListener(eventName, listener);
    } else {
      if (!observableOrConfig.listeners) {
        observableOrConfig.listeners = {};
      }
      observableOrConfig.listeners[eventName] = listener;
    }
  }
});
