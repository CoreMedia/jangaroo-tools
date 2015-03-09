define("as3/package1/PrivateMemberAccess",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {
public class PrivateMemberAccess {

  public static const INSTANCE:PrivateMemberAccess =*/function INSTANCE$static_(){Object.defineProperty(PrivateMemberAccess,"INSTANCE",{value: new PrivateMemberAccess()});}/*;
  private var secret:String;

  public static*/ function doSomething$static()/*:String*/ {
    return PrivateMemberAccess.INSTANCE.secret$1;
  }/*
}*/function PrivateMemberAccess() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {
      members: {
        secret$1: {
          value: null,
          writable: true
        },
        constructor: PrivateMemberAccess
      },
      staticMembers: {doSomething: doSomething$static}
    }));
    INSTANCE$static_();
  });
});
