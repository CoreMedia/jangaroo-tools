define("as3/uint", [], function() {
  "use strict";
  function uint_(num) {
    return num >>> 0;
  }
  uint_.$class = {
    isInstance: function(o) {
      return o instanceof Number && o >>> 0 === o.valueOf();
    }
  };
  uint_.MAX_VALUE = 4294967295;
  uint_.MIN_VALUE = 0;
  return uint_;
});
