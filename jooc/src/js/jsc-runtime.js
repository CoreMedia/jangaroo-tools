 function _jsc_assert(value, msg) {
   if (!value) {
     m = "Assertion failed: " + msg;
     alert(m);
     throw new Error(m);
   }
 }
 
 
 function _unparse(o) {
   if (o == null)
     return "null";
   if (!o)
     return "null";
   var result = null;
   switch(typeof(o)) {
   case "undefined":
     result = "undefined";
     break;
   case "string":
     result = '"' + o + '"';
     break;
   case "object":
     result = "{";
     var isFirstProp = true;
     for (var prop in o) {
       if (!isFirstProp)
         result += ", ";
       isFirstProp = false;
       result += prop + ": " + _unparse(o[prop]);
     }
     result += "}";
     break;
   default:
     result = o.toString();
     break;
   }
   return result;
 }
 
 function _jsc_package(qualifiedName) {
   //print("_jsc_package(" + _unparse(qualifiedName) + ")");
   var n = qualifiedName.length;
   var currentPackage = typeof(window) == "undefined" ? null : window;
   for(var i = 0; i < n; i++) {
     var name = qualifiedName[i];
     if (!currentPackage) {
       _jsc_assert(i == 0, "i == 0");
       try {
         currentPackage = eval(name);
       } catch (e) {
         //print(name+"=new Object();");
         eval(name+"=new Object();");
         currentPackage = eval(name);
       }
     } else if (!currentPackage[name])
       currentPackage = currentPackage[name] = new Object();
     else
       currentPackage = currentPackage[name];
     _jsc_assert(currentPackage && typeof(currentPackage) == "object", "packages must have object type");
   }
 }
