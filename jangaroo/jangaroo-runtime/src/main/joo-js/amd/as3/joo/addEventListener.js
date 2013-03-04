define(function() {
  return function addEventListener(config, eventName, eventType, callback) {
    function listener() {
      return callback(new eventType(arguments));
    }

    if (config.isInstance) {
      config.addListener(eventName, listener);
    } else {
      if (!config.listeners) {
        config.listeners = {};
      }
      config.listeners[eventName] = listener;
    }
  }
});
