define(function() {
  "use strict";
  function int_(num) {
    return num >> 0;
  }
  int_.MAX_VALUE =  2147483647;
  int_.MIN_VALUE = -2147483648;
  return { _: int_ };
});
