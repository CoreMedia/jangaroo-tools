define(function() {
  "use strict";
  function int_(num) {
    return num >> 0;
  }
  int_.$class = {
    isInstance: function(o) {
      return o instanceof Number && o >> 0 === o.valueOf();
    }
  };
  int_.MAX_VALUE =  2147483647;
  int_.MIN_VALUE = -2147483648;
  return int_;
});
