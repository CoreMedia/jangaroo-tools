define("as3/joo/JooClassDeclaration", ["as3/joo/NativeClassDeclaration", "as3/joo/MemberDeclaration"], function(NativeClassDeclaration, MemberDeclaration) {
  "use strict";
  function JooClassDeclaration(qName, constructor_, extends_, implements_, rawMetadataByMember) {
    NativeClassDeclaration.call(this, qName, constructor_);
    this.rawMetadataByMember = rawMetadataByMember || {};
    this.extends_ = extends_;
    this.implements_ = implements_;
  }
  function isArray(it) {
    return Object.prototype.toString.call(it) === '[object Array]';
  }
  function convertMetadata(rawMetadata) {
    var metadata = {};
    if (rawMetadata && rawMetadata.length) {
      for (var i=0; i < rawMetadata.length; ++i) {
        var key = rawMetadata[i];
        var value = {};
        var argsNameValues = rawMetadata[i + 1];
        if (isArray(argsNameValues)) {
          ++i;
          for (var j=0; j < argsNameValues.length; j += 2) {
            value[argsNameValues[j] || "$value"] = argsNameValues[j + 1];
          }
        }
        metadata[key] = value;
      }
    }
    return metadata;
  }
  JooClassDeclaration.prototype = Object.create(NativeClassDeclaration.prototype, {
    superClassDeclaration: {
      get: function() {
        return this.extends_ && this.extends_.$class;
      }
    },
    metadata: {
      get: function() {
        var metadata = convertMetadata(this.rawMetadataByMember[""]);
        Object.defineProperty(this, "metadata", { value: metadata, enumerable: true });
        return metadata;
      },
      enumerable: true,
      configurable: true
    },
    memberDeclarations: {
      get: function() {
        var rawMetadataByMember = this.rawMetadataByMember;
        var mds = Object.getOwnPropertyNames(this.constructor_.prototype).map(function(member) {
          var namespace = MemberDeclaration.NAMESPACE_PUBLIC;
          var metadata = convertMetadata(rawMetadataByMember[member]);
          var privateMatch = member.match(/^(.+)\$[0-9]+$/);
          if (privateMatch) {
            member = privateMatch[1];
            namespace = MemberDeclaration.NAMESPACE_PRIVATE;
          }
          return new MemberDeclaration(namespace, member, metadata);
        });
        // Workaround for PhantomJS bug: when run in non-debug mode, PhantomJS complains that a non-writable
        // property is written if we leave out writable: true.
        Object.defineProperty(this, "memberDeclarations", { value: mds, writable: true, enumerable: true });
        return mds;
      },
      enumerable: true,
      configurable: true
    },
    getMemberDeclaration: {
      value: function(namespace, memberName) {
        var mds = this.memberDeclarations;
        for (var i=0; i < mds.length; ++i) {
          var memberDeclaration = mds[i];
          if (memberDeclaration._namespace === namespace && memberDeclaration.memberName === memberName) {
            return mds[i];
          }
        }
        return namespace === MemberDeclaration.NAMESPACE_PRIVATE || !this.superClassDeclaration || !this.superClassDeclaration.getMemberDeclaration
                ? null
                : this.superClassDeclaration.getMemberDeclaration(namespace, memberName);
      }
    }
  });
  return JooClassDeclaration;
});
