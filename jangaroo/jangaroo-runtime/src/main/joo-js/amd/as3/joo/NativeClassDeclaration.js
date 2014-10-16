define("as3/joo/NativeClassDeclaration", [], function() {
  "use strict";
  function NativeClassDeclaration(packageName, name, constructor_) {
    this.qName = this.fullClassName = packageName ? packageName + "." + name : name;
    this.name = name;
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
