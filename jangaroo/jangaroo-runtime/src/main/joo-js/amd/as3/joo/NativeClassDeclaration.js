define("as3/joo/NativeClassDeclaration", [], function() {
  "use strict";
  function NativeClassDeclaration(packageName, name) {
    this.qName = this.fullClassName = packageName ? packageName + "." + name : name;
    this.name = name;
  }
  Object.defineProperty(NativeClassDeclaration.prototype, "toString", { value: function() {
      return this.qName;
  }});
  return NativeClassDeclaration;
});
