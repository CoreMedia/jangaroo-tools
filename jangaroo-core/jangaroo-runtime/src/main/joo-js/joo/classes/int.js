(function(theGlobalObject) {
  theGlobalObject['int'] = theGlobalObject['$$int'] = function $int(num) {
    num = Number(num);
    return new Number(num < 0 ? Math.ceil(num) : num > 0 ? Math.floor(num) : 0);
  };
  theGlobalObject['int'].MAX_VALUE =  2147483647;
  theGlobalObject['int'].MIN_VALUE = -2147483648;
})(this);
