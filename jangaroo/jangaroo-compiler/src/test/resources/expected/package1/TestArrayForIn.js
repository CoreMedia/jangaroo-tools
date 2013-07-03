joo.classLoader.prepare("package package1",/* {*/

"public class TestArrayForIn",1,function($$private){var $11=package1;return[ 

  "private static const",{ ARRAY/*:Array*/ :function(){return( [1, 2, 3]);}},
  "private static var",{ array/*:Array*/ :function(){return( [1, 2, 3]);}},

  "public static function test",function test()/*:Array*/ {
    var a/*:Array*/ = [1, 2, 3];
    // test rewrite of Array for ... in with local variable
    for (var $1=0;$1</* in*/ a.length;++$1) {var i1/*:String*/ =String($1);
      $$private.doSomething(a[i1]);
    }
    // test rewrite of Array for each ... in with Array literal
    for/* each*/ (var $2=0,$3=/* in*/ [1, 2, 3];$2<$3.length;++$2) {var e0/*:int*/ =$3[$2];
      $$private.doSomething(e0);
    }
    // test rewrite of Array for each ... in with local variable
    for/* each*/ (var $4=0;$4</* in*/ a.length;++$4) {var e1/*:int*/ = a[$4];
      $$private.doSomething(e1);
    }
    // test rewrite of Array for each ... in with field
    for/* each*/ (var $5=0;$5</* in*/ $$private.array.length;++$5) {var e2a/*:int*/ = $$private.array[$5];
      $$private.doSomething(e2a);
    }
    // test rewrite of Array for each ... in with field using explicit class
    for/* each*/ (var $6=0;$6</* in TestArrayForIn*/$$private.array.length;++$6) {var e2b/*:int*/ =/* TestArrayForIn*/$$private.array[$6];
      $$private.doSomething(e2b);
    }
    // test rewrite of Array for each ... in with static const
    for/* each*/ (var $7=0;$7</* in*/ $$private.ARRAY.length;++$7) {var e3/*:int*/ = $$private.ARRAY[$7];
      $$private.doSomething(e3);
    }
    var e4/*:int*/;
    // test rewrite of Array for each ... in with pre-declared loop variable
    for/* each*/ (var $8=0;$8</* in*/ a.length;++$8) {e4= a[$8];
      $$private.doSomething(e4);
    }
    // test rewrite of Array for ... in where the first argument is an lvalue
    var indexes/*:Array*/ = [];
    for (var $9=0;$9</* in*/ a.length;++$9) {indexes[indexes.length]=String($9);}
    $$private.doSomething(indexes);

    // test rewrite of Array for each ... in where the first argument is an lvalue
    var copy/*:Array*/ = [];
    for/* each*/ (var $10=0;$10</* in*/ a.length;++$10) {copy[copy.length]= a[$10];}
    return copy;
  },

  "private static function doSomething",function doSomething(param/*:**/)/*:void*/ {
    // do something...
  },
undefined];},["test"],[], "@runtimeVersion", "@version"
);