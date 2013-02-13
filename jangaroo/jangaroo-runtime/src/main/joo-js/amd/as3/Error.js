define(["native!Error"], function(NativeError) {
  "use strict";
  // built-in Error constructor called as function unfortunately always creates a new Error object,
  // so we have to emulate it:
  function Error(message/*String*/, id/*:int*/) {
    this.message = message || "";
    this.id = id >> 0;
  }
  Error.prototype = NativeError.prototype;
  return Error;
});