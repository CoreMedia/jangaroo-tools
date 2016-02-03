Ext.define("AS3.$int", {
  __factory__: function() {
    var $int = function(num) {
      return num >> 0;
    };
    $int.MAX_VALUE =  2147483647;
    $int.MIN_VALUE = -2147483648;
    $int.$className = "AS3.$int";
    $int.__isInstance__ = function(object) {
      return (object instanceof Number || typeof object === 'number') &&
              object >> 0 === object + 0; // "+ 0" converts Number to number!
    };
    return $int;
  }
});
