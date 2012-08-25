(function(theGlobalObject) {
  theGlobalObject['int'] = theGlobalObject['$$int'] = function $$int(num) {
    return Number(num) | 0;
  };
  theGlobalObject['int'].MAX_VALUE =  Math.pow(2, 31) - 1;
  theGlobalObject['int'].MIN_VALUE = -Math.pow(2, 31);
})(this);
