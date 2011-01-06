(function() {
  var SYSTEM_CLASSES = [
    "int",
    "uint",
    "joo.BootstrapClassLoader",
    "joo.assert",
    "joo.Class",
    "joo.trace",
    "joo.MemberDeclaration",
    "joo.NativeClassDeclaration",
    "joo.SystemClassDeclaration",
    "joo.SystemClassLoader",
    "ArgumentError",
    "DefinitionError",
    "SecurityError",
    "Array",
    "Date",
    "joo.ClassDeclaration",
    "joo.StandardClassLoader",
    "joo.DynamicClassLoader",
    "joo.ResourceBundleAwareClassLoader",
    "joo._createClassLoader"
  ];
  for (var c=0; c<SYSTEM_CLASSES.length; ++c) {
    var url = "joo/classes/" + SYSTEM_CLASSES[c].replace(/\./g,"/") + ".js";
    joo.loadScript(url);
  }
})();
