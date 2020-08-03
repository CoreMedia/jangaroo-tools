/*package package1 {*/
Ext.define("package1.PrivateMemberAccess", function(PrivateMemberAccess) {/*public class PrivateMemberAccess {

  public static const INSTANCE:PrivateMemberAccess =*/function INSTANCE$static_(){PrivateMemberAccess.INSTANCE=( new PrivateMemberAccess());}/*;
  private var secret:String;

  public static*/ function doSomething$static()/*:String*/ {
    return PrivateMemberAccess.INSTANCE.secret$Z3lU;
  }/*
}*/function PrivateMemberAccess$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      secret$Z3lU: null,
      constructor: PrivateMemberAccess$,
      statics: {
        INSTANCE: undefined,
        doSomething: doSomething$static,
        __initStatics__: function() {
          INSTANCE$static_();
        }
      }
    };
});
