// simulate ActionScript's meta class "Class":
define(function() {
  "use strict";
  // placeholder that "casts" by returning the argument itself:
  function Class(c){
    return c;
  }
  // consider any Function a Class:
  Class.isInstance = function isInstance(o) {
    return typeof o === "function";
  };
  return { _: Class };
});
