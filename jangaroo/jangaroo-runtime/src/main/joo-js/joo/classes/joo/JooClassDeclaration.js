define(["native!Object.create@./es5-polyfills", "./NativeClassDeclaration"], function(create, NativeClassDeclaration) {
  "use strict";
  function JooClassDeclaration(name, packageName, extends_, implements_, metadata) {
    NativeClassDeclaration.call(this, packageName, name);
    this.metadata = metadata || {};
    this.extends_ = extends_;
    this.implements_ = implements_;
  }
  JooClassDeclaration.prototype = create(NativeClassDeclaration.prototype);
  return JooClassDeclaration;
});
