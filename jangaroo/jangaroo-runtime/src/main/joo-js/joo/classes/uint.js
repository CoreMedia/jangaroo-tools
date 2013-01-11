define(function() {
  "use strict";
  function uint_(num) {
    return num >>> 0;
  }
  uint_.MAX_VALUE = 4294967295;
  uint_.MIN_VALUE = 0;
  return { _: uint_ };
});
