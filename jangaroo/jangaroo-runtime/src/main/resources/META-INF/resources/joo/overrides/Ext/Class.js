Ext.define('JooOverrides.Ext.Class', {
  override: 'Ext.Class'
}, function() {
  Ext.Class.registerPreprocessor('__accessors__', function (Class, data) {
    if (data.__accessors__) {
      if (data.__accessors__.statics) {
        Object.defineProperties(Class, data.__accessors__.statics);
        delete data.__accessors__.statics;
      }
      Object.defineProperties(Class.prototype, data.__accessors__);
      delete data.__accessors__;
    }
  });

  var wrapConstructor = function(Class) {
    return function() {
      // console.log("*** called constructor of " + Ext.getClassName(Class) + " for the first time.");
      Class.__doInit__();
      return Class.prototype.constructor.apply(this, arguments);
    };
  };
  var wrapStaticMember = function(staticMemberName) {
    return {
      get: function() {
        // console.log("*** read " + Ext.getClassName(this) + "." + staticMemberName + " for the first time.");
        this.__doInit__();
        return this[staticMemberName];
      },
      set: function(value) {
        // console.log("*** set " + Ext.getClassName(this) + "." + staticMemberName + " for the first time.");
        this.__doInit__();
        this[staticMemberName] = value;
      },
      enumerable: true,
      configurable: true
    };
  };
  Ext.Class.registerPreprocessor('statics', function (Class, data) {
    var statics = data.statics;
    if (statics) {
      var initStatics = statics.__initStatics__;
      if (!initStatics) {
        Class.addStatics(statics);
      } else {
        delete statics.__initStatics__;
        var originalConstructor = data.constructor;
        data.constructor = wrapConstructor(Class);
        for (var staticMemberName in statics) {
          Object.defineProperty(Class, staticMemberName, wrapStaticMember(staticMemberName));
        }
        Class.__doInit__ = function () {
          delete this.__doInit__; // self-destruct!
          // remove all initializing interceptors:
          this.prototype.constructor = data.constructor = originalConstructor;
          for (var staticMemberName in statics) {
            delete this[staticMemberName];
          }
          // now, define the real statics:
          this.addStatics(statics);
          initStatics();
        }
      }
      delete data.statics;
    }
  });
});
