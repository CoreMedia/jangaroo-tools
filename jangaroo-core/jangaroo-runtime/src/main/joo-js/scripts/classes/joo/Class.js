// simulate ActionScript's Class object for type casts and "is"
Class = function joo$Class(c){return c;};
Class.$class = {
  init: function(){},
  isInstance: function(f){return typeof f=="function";}
};
