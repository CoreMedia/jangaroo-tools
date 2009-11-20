// simulate ActionScript's Class object for type casts and "is"
joo.Class = function joo$Class(c){return c;};
joo.Class.$class = {
  isInstance: function(f){return typeof f=="function";}
};
