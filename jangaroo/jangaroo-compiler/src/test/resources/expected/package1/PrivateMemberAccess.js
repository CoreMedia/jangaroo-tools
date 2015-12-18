Ext.define("package1.PrivateMemberAccess", function(PrivateMemberAccess) {/*package package1 {
public class PrivateMemberAccess {

  public static const INSTANCE:PrivateMemberAccess =*/function INSTANCE$static_(){Object.defineProperty(PrivateMemberAccess,"INSTANCE",{value: new PrivateMemberAccess()});}/*;
  private var secret:String;

  public static*/ function doSomething$static()/*:String*/ {
    return PrivateMemberAccess.INSTANCE.secret$1;
  }/*
}*/function PrivateMemberAccess$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      secret$1: null,
      constructor: PrivateMemberAccess$,
      statics: {
        doSomething: doSomething$static,
        __initStatics__: function() {
          INSTANCE$static_();
        }
      }
    };
}, function(clazz) {
  clazz.__initStatics__();
});
