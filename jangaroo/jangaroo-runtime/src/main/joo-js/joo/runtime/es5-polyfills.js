// As a polyfill plugin, this breaks normal modularity by altering globals for sake of enabling standards on non-supporting browsers
define(function () {
  "use strict";
  //----------------------------------------------------------------------
  //
  // ECMAScript 5 Polyfills
  //
  //----------------------------------------------------------------------

  if (!Array.prototype.every) {
    Array.prototype.every = function every(fun /*, thisp */) {
      var t, len, i, thisp;
      if (this == null) {
        throw new TypeError();
      }
      t = Object(this);
      len = t.length >>> 0;
      if (typeof fun != "function") {
        throw new TypeError();
      }

      thisp = arguments[1];
      for (i = 0; i < len; i++) {
        if (i in t && !fun.call(thisp, t[i], i, t)) {
          return false;
        }
      }
      return true;
    };
  }
  if (!Array.prototype.forEach) {
    Array.prototype.forEach = function forEach(callback, thisArg) {
      "use strict";
      var T, k, O, len, kValue;

      if (this == null) {
        throw new TypeError("this is null or not defined");
      }

      // 1. Let O be the result of calling ToObject passing the |this| value as the argument.
      O = Object(this);

      // 2. Let lenValue be the result of calling the Get internal method of O with the argument "length".
      // 3. Let len be ToUint32(lenValue).
      len = O.length >>> 0; // Hack to convert O.length to a UInt32

      // 4. If IsCallable(callback) is false, throw a TypeError exception.
      // See: http://es5.github.com/#x9.11
      if (Object.prototype.toString.call(callback) !== "[object Function]") {
        throw new TypeError(callback + " is not a function");
      }

      // 5. If thisArg was supplied, let T be thisArg; else let T be undefined.
      if (thisArg) {
        T = thisArg;
      }

      // 6. Let k be 0
      k = 0;

      // 7. Repeat, while k < len
      while (k < len) {

        // a. Let Pk be ToString(k).
        //   This is implicit for LHS operands of the in operator
        // b. Let kPresent be the result of calling the HasProperty internal method of O with argument Pk.
        //   This step can be combined with c
        // c. If kPresent is true, then
        if (Object.prototype.hasOwnProperty.call(O, k)) {

          // i. Let kValue be the result of calling the Get internal method of O with argument Pk.
          kValue = O[ k ];

          // ii. Call the Call internal method of callback with T as the this value and
          // argument list containing kValue, k, and O.
          callback.call(T, kValue, k, O);
        }
        // d. Increase k by 1.
        k++;
      }
      // 8. return undefined
    };
  }
  if (!Array.prototype.map) {
    Array.prototype.map = function map(callback, thisArg) {
      "use strict";
      var T, A, k, kValue, mappedValue, O, len;
      if (this === null || typeof this === 'undefined') {
        throw new TypeError(" this is null or not defined");
      }
      O = Object(this);
      len = O.length >>> 0;

      if (Object.prototype.toString.call(callback) !== "[object Function]") {
        throw new TypeError(callback + " is not a function");
      }
      if (thisArg) {
        T = thisArg;
      }

      A = new Array(len);
      k = 0;
      while (k < len) {
        if (k in O) {
          kValue = O[ k ];
          mappedValue = callback.call(T, kValue, k, O);
          A[ k ] = mappedValue;
        }
        k++;
      }
      return A;
    };
  }

  //----------------------------------------------------------------------
  // ES5 15.2 Object Objects
  //----------------------------------------------------------------------

  //
  // ES5 15.2.3 Properties of the Object Constructor
  //

  if (!Object.create) {

    // if Object.create is not defined, *always* also redefine Object.defineProperty and Object.defineProperties,
    // as IE8 implements them only for DOM objects!

    // ES5 15.2.3.5 Object.create ( O [, Properties] )
    Object.create = function (prototype, properties) {
      if (prototype !== Object(prototype)) { throw new TypeError(); }
      /** @constructor */
      function Ctor() {}
      Ctor.prototype = prototype;
      var o = new Ctor();
      o.constructor = Ctor;
      if (arguments.length > 1) {
        if (properties !== Object(properties)) { throw new TypeError(); }
        Object.defineProperties(o, properties);
      }
      return o;
    };

    // ES 15.2.3.6 Object.defineProperty ( O, P, Attributes )
    // Partial support for most common case - getters, setters, and values
    var orig = Object.defineProperty;
    Object.defineProperty = function (o, prop, desc) {
      // In IE8 try built-in implementation for defining properties on DOM prototypes.
      if (orig) { try { return orig(o, prop, desc); } catch (e) {} }

      if (o !== Object(o)) { throw new TypeError("Object.defineProperty called on non-object"); }
      if (('get' in desc)) {
        if (Object.prototype.__defineGetter__) {
          Object.prototype.__defineGetter__.call(o, prop, desc.get);
        } else {
          o["get$" + prop] = desc.get;
        }
      }
      if (('set' in desc)) {
        if (Object.prototype.__defineSetter__) {
          Object.prototype.__defineSetter__.call(o, prop, desc.set);
        } else {
          o["set$" + prop] = desc.set;
        }
      }
      if ('value' in desc) {
        o[prop] = desc.value;
      }
      return o;
    };

    // ES 15.2.3.7 Object.defineProperties ( O, Properties )
    Object.defineProperties = function (o, properties) {
      if (o !== Object(o)) {
        throw new TypeError("Object.defineProperties called on non-object");
      }
      var name;
      for (name in properties) {
        if (Object.prototype.hasOwnProperty.call(properties, name)) {
          Object.defineProperty(o, name, properties[name]);
        }
      }
      return o;
    };
  }

  //----------------------------------------------------------------------
  // ES5 15.3 Function Objects
  //----------------------------------------------------------------------

  // ES5 15.3.4.5 Function.prototype.bind ( thisArg [, arg1 [, arg2, ... ]] )
  // https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Function/bind
  if (!Function.prototype.bind) {
    Function.prototype.bind = function bind (o) {
      if (typeof this !== 'function') { throw new TypeError("Bind must be called on a function"); }
      var slice = [].slice,
              args = slice.call(arguments, 1),
              self = this,
              bound = function () {
                return self.apply(this instanceof nop ? this : (o || {}),
                        args.concat(slice.call(arguments)));
              };

      /** @constructor */
      function nop() {}
      nop.prototype = self.prototype;

      bound.prototype = new nop();

      return bound;
    };
  }
});
