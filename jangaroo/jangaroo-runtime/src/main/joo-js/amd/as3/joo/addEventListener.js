define(function() {
  return function addEventListener(config, eventName, eventType, callback) {
    if (!config.listeners) {
      config.listeners = {};
    }
    config.listeners[eventName] = function() {
      callback(new eventType(arguments));
    }
  }
});
