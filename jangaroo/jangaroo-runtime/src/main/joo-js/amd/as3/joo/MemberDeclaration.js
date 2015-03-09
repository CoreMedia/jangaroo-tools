define("as3/joo/MemberDeclaration", [], function() {
  "use strict";
  function MemberDeclaration(namespace, name, metadata) {
    this._namespace = namespace;
    this.memberName = name;
    this.metadata = metadata;
  }
  Object.defineProperties(MemberDeclaration, {
    METHOD_TYPE_GET: {value: "get"},
    METHOD_TYPE_SET: {value: "set"},
    MEMBER_TYPE_VAR: {value: "var"},
    MEMBER_TYPE_CONST: {value: "const"},
    MEMBER_TYPE_FUNCTION: {value: "function"},
    MEMBER_TYPE_CLASS: {value: "class"},
    MEMBER_TYPE_INTERFACE: {value: "interface"},
    MEMBER_TYPE_NAMESPACE: {value: "namespace"},
    NAMESPACE_PRIVATE: {value: "private"},
    NAMESPACE_INTERNAL: {value: "internal"},
    NAMESPACE_PROTECTED: {value: "protected"},
    NAMESPACE_PUBLIC: {value: "public"},
    STATIC: {value: "static"},
    FINAL: {value: "final"},
    NATIVE: {value: "native"},
    OVERRIDE: {value: "override"},
    VIRTUAL: {value: "virtual"}
  });
  Object.defineProperties(MemberDeclaration.prototype, {
    isPrivate: {value: function() {
      return this._namespace === MemberDeclaration.NAMESPACE_PRIVATE;
    }},
    getQualifiedName: {value: function() {
      return this._namespace + "::" + this.memberName;
    }},
    "toString": {value: function() {
      return this.memberName;
    }}
  });
  return MemberDeclaration;
});
