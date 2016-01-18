(function(theGlobalObject) {
  theGlobalObject['int'] = theGlobalObject['$$int'] = function $$int(num) {
    return num >> 0;
  };
  theGlobalObject['int'].MAX_VALUE =  2147483647;
  theGlobalObject['int'].MIN_VALUE = -2147483648;
  theGlobalObject['int'].$className = "AS3.int";
  theGlobalObject['int'].__isInstance__ = function(object) {
    return (object instanceof Number || typeof object === 'number') &&
            object >> 0 === object + 0; // "+ 0" converts Number to number!
  };
})(AS3);
