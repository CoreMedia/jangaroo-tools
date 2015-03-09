define("as3/Date", ["as3/joo/getQualifiedObject"], function(getQualifiedObject) {
  "use strict";
  // Built-in Date does not support certain properties added in the AS3 Date API.
  var Date = getQualifiedObject("Date");
  var datePrototype = Date.prototype;
  ["date", "fullYear", "hours", "milliseconds", "minutes", "month", "seconds"].forEach(function(property) {
    var methodSuffix = property.charAt(0).toUpperCase() + property.substring(1);
    Object.defineProperty(datePrototype, property, {
      get: datePrototype["get" + methodSuffix],
      set: datePrototype["set" + methodSuffix]
    });
    Object.defineProperty(datePrototype, property + "UTC", {
      get: datePrototype["getUTC" + methodSuffix],
      set: datePrototype["setUTC" + methodSuffix]
    });
  });
  Object.defineProperties(datePrototype, {
    day: {get: datePrototype.getDay},
    dayUTC: {get: datePrototype.getUTCDay},
    time: {get: datePrototype.getTime, set: datePrototype.setTime},
    timezoneOffset: {get: datePrototype.getTimezoneOffset}
  });
  return Date;
});