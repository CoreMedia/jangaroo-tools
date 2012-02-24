joo.classLoader.prepare("package package1",/* {

import package1.someOtherPackage.SomeOtherClass;*/

"public class ChainedConstants",1,function($$private){var $1=package1,$2=package1.someOtherPackage;return[function(){joo.classLoader.init(package1.someOtherPackage.SomeOtherClass);}, 
  "public static const",{ METHOD_TYPE_GET/* : String*/ : "get"},

  "public static const",{ DEFAULT_METHOD_TYPE/* : String*/ :function(){return( $1.ChainedConstants.METHOD_TYPE_GET);}},

  "public static const",{ THE_METHOD_TYPE/* : String*/ :function(){return( package1.ChainedConstants.METHOD_TYPE_GET);}},

  "public static const",{ ANOTHER_METHOD_TYPE/* : String*/ :function(){return( package1.ChainedConstants.METHOD_TYPE_GET.substr(0));}},

  "public static const",{ THE_BLA/* : String*/ :function(){return( $2.SomeOtherClass.BLA);}},
undefined];},[],["package1.someOtherPackage.SomeOtherClass"], "0.8.0", "0.9.11-SNAPSHOT"
);