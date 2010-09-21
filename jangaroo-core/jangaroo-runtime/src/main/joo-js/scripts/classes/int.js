(function(theGlobalObject) {
  theGlobalObject['int'] = theGlobalObject['$$int'] = function $int(num) {
    var i = Math.floor(num);
    if (this===theGlobalObject) {
      return i;
    }
    var result = new Number(i);
    result.constructor = $int;
    return result;
  };
  theGlobalObject['int'].MAX_VALUE =  2147483647;
  theGlobalObject['int'].MIN_VALUE = -2147483648;
})(this);