joo.classLoader.prepare("package package1",/* {
import package1.someOtherPackage.SomeOtherClass;*/

"public class NoPrimitiveInit",1,function($$private){return[function(){joo.classLoader.init(package1.someOtherPackage.SomeOtherClass);}, 
  "public function NoPrimitiveInit",function NoPrimitiveInit() {
  },

  "private function method",function method(i/*:int*/)/*:int*/ {
    return package1.someOtherPackage.SomeOtherClass.BLA + $$int.MAX_VALUE;
  },
undefined];},[],["package1.someOtherPackage.SomeOtherClass","int"], "@runtimeVersion", "@version"
);