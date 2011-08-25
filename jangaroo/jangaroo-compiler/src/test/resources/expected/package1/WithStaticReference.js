joo.classLoader.prepare("package package1",/*{*/
"public class WithStaticReference",1,function($$private){var is=joo.is;return[function(){joo.classLoader.init(package1.WithStaticReference);}, 
  "public static const",{ BLA : "bla"},
  "public function WithStaticReference",function WithStaticReference$() {
    var bla = package1.WithStaticReference.BLA;is(
    bla,  package1.WithStaticReference);
    new package1.WithStaticReference();
  },
  "public static function make",function make()/*:void*/ {
    var bla = package1.WithStaticReference.BLA;is(
    bla,  package1.WithStaticReference);
    new package1.WithStaticReference();
  },
  "public function make2",function make2()/*:void*/ {
    var bla = package1.WithStaticReference.BLA;is(
    bla,  package1.WithStaticReference);
    new package1.WithStaticReference();
  },
];},["make"],[], "0.8.0", "0.8.6-SNAPSHOT"
);