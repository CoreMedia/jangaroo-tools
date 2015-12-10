Ext.define("package1.TestArrayForIn", function(TestArrayForIn) {/*package package1 {

public class TestArrayForIn {

  private static const*/var ARRAY$static/*:Array*/;/* =*/function ARRAY$static_(){ARRAY$static=( [1, 2, 3]);};/*
  private static*/ var array$static/*:Array*/;/* =*/function array$static_(){array$static=( [1, 2, 3]);};/*

  public static*/ function test$static()/*:Array*/ {
    var a/*:Array*/ = [1, 2, 3];
    // test rewrite of Array for ... in with local variable
    for (var $1=0;$1</* in*/ a.length;++$1) {var i1/*:String*/ =String($1);
      doSomething$static(a[i1]);
    }var $3;
    // test rewrite of Array for each ... in with Array literal
    for/* each*/ (var $2 in $3= [1, 2, 3]) {var e0/*:int*/ =$3[$2];
      doSomething$static(e0);
    }
    // test rewrite of Array for each ... in with local variable
    for/* each*/ (var $4=0;$4</* in*/ a.length;++$4) {var e1/*:int*/ = a[$4];
      doSomething$static(e1);
    }
    // test rewrite of Array for each ... in with field
    for/* each*/ (var $5=0;$5</* in*/ array$static.length;++$5) {var e2a/*:int*/ = array$static[$5];
      doSomething$static(e2a);
    }
    // test rewrite of Array for each ... in with field using explicit class
    for/* each*/ (var $6 in/* TestArrayForIn.*/array$static) {var e2b/*:int*/ =/* TestArrayForIn.*/array$static[$6];
      doSomething$static(e2b);
    }
    // test rewrite of Array for each ... in with static const
    for/* each*/ (var $7=0;$7</* in*/ ARRAY$static.length;++$7) {var e3/*:int*/ = ARRAY$static[$7];
      doSomething$static(e3);
    }
    var e4/*:int*/;
    // test rewrite of Array for each ... in with pre-declared loop variable
    for/* each*/ (var $8=0;$8</* in*/ a.length;++$8) {e4= a[$8];
      doSomething$static(e4);
    }
    // test rewrite of Array for ... in where the first argument is an lvalue
    var indexes/*:Array*/ = [];
    for (var $9=0;$9</* in*/ a.length;++$9) {indexes[indexes.length]=String($9);}
    doSomething$static(indexes);

    // test rewrite of Array for each ... in where the first argument is an lvalue
    var copy/*:Array*/ = [];
    for/* each*/ (var $10=0;$10</* in*/ a.length;++$10) {copy[copy.length]= a[$10];}
    return copy;
  }/*

  private static*/ function doSomething$static(param/*:**/)/*:void*/ {
    // do something...
  }/*
}*/function TestArrayForIn$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestArrayForIn$,
      statics: {test: test$static}
    };
}, function() {
    ARRAY$static_();
    array$static_();
});
