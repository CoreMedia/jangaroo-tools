(function(theGlobalObject) {
  theGlobalObject['int'] = theGlobalObject['$$int'] = function $$int(num) {
    return num >> 0;
  };
  theGlobalObject['int'].MAX_VALUE =  2147483647;
  theGlobalObject['int'].MIN_VALUE = -2147483648;
})(this);
