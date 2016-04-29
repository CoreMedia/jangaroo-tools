Ext.define("package1.PrivateMemberAccess", function(PrivateMemberAccess) {/*package package1 {
public class PrivateMemberAccess {

  public static const INSTANCE:PrivateMemberAccess =*/function INSTANCE$static_(){PrivateMemberAccess.INSTANCE=( new PrivateMemberAccess());}/*;
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
        INSTANCE: undefined,
        doSomething: doSomething$static,
        __initStatics__: function() {
          INSTANCE$static_();
        }
      }
    };
});
