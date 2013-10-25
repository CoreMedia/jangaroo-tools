define([], function() {
  "use strict";
  function MemberDeclaration(name, metadata) {
    this.memberName = name;
    this.metadata = metadata;
  }
  Object.defineProperties(MemberDeclaration.prototype, {
    "toString": { value: function() {
        return this.memberName;
      }}
  });
  return MemberDeclaration;
});
