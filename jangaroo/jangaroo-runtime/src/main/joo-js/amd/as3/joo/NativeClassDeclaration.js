define("as3/joo/NativeClassDeclaration", [], function() {
  "use strict";
  function NativeClassDeclaration(qName, constructor_) {
    this.qName = this.fullClassName = qName;
    this.name = qName.substr(qName.lastIndexOf(".") + 1);
    this.constructor_ = constructor_;
  }
  Object.defineProperties(NativeClassDeclaration.prototype, {
    "toString": { value: function() {
      return this.qName;
    }},
    "init": { value: function() {
      // no longer necessary!
      return this;
    }}
  });
  return NativeClassDeclaration;
});
