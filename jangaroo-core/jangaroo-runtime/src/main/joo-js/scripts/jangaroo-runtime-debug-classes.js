(function() {
  var SYSTEM_CLASSES = [
    "joo.BootstrapClassLoader",
    "joo.assert",
    "joo.Class",
    "joo.trace",
    "joo.MemberDeclaration",
    "joo.NativeClassDeclaration",
    "joo.SystemClassDeclaration",
    "joo.SystemClassLoader",
    "Array",
    "Date",
    "joo.ClassDeclaration",
    "joo.StandardClassLoader",
    "joo.DynamicClassLoader",
    "joo.ResourceBundleAwareClassLoader"
  ];
  var RUNTIME_URL_PATTERN = /^(.*)\bjangaroo-runtime-debug-classes.js$/;
  var urlPrefix = "";
  var scripts = window.document.getElementsByTagName("SCRIPT");
  for (var i=0; i<scripts.length; ++i) {
    var match = RUNTIME_URL_PATTERN.exec(scripts[i].src);
    if (match) {
      urlPrefix = match[1];
      break;
    }
  }
  urlPrefix += "classes/";
  for (var c=0; c<SYSTEM_CLASSES.length; ++c) {
    var url = urlPrefix + SYSTEM_CLASSES[c].replace(/\./g,"/") + ".js";
    window.document.writeln('<script type="text/javascript" src="'+url+'"></script>');
  }
  window.document.writeln('<script defer type="text/javascript">\n'
    + '  new joo.DynamicClassLoader().debug = true;\n'
    + '</script>'
    );
})();
