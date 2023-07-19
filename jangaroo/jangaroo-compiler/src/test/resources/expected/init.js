/*package {
import package1.someOtherPackage.ConfigClass_stranger_antonEvent;
import package1.someOtherPackage.SomeOtherClass;*/

Ext.define("init", function(init) {/*public*/ function init()/*:void*/ {
   new package1.someOtherPackage.SomeOtherClass().addEventListener(package1.someOtherPackage.ConfigClass_stranger_antonEvent.CLICK_CLACK, function (event/*: ConfigClass_stranger_antonEvent*/)/*:void*/ {/*
     const*/var title/*: String*/ = event.source.title;
   });
}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      __factory__: function() {
        return init;
      },
      requires: [
        "package1.someOtherPackage.ConfigClass_stranger_antonEvent",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
