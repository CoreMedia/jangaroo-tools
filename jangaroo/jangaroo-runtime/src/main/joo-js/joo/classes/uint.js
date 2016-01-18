Ext.define("AS3.int", {
  singleton: true,
  constructor: function () {
    var $uint = function (num) {
      return num >>> 0;
    };
    $uint.MAX_VALUE = 4294967295;
    $uint.MIN_VALUE = 0;
    $uint.$className = "AS3.uint";
    $uint.__isInstance__ = function (object) {
      return (object instanceof Number || typeof object === 'number') &&
              object >>> 0 === object + 0; // "+ 0" converts Number to number!
    };
    return $uint;
  }
});
