define("as3/package1/StaticAndNonStatic",["module","as3-rt/AS3"], function($module,AS3) { AS3.compilationUnit($module,function($primaryDeclaration){/*package package1 {

/**
 * Retest for JOO-64.
 * /
public class StaticAndNonStatic {

  public var StaticAndNonStatic:String;*/function static$0(){
  
  new StaticAndNonStatic();}/*
}*/function StaticAndNonStatic() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      StaticAndNonStatic: {
        value: null,
        writable: true
      },
      constructor: StaticAndNonStatic
    }}));
    static$0();
  });
});
