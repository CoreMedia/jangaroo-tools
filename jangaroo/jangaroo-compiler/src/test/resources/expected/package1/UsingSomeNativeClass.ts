import { asConfig } from "@jangaroo/runtime";
import SomeOtherNativeClass from "../SomeOtherNativeClass";
import package1_someOtherPackage_SomeNativeClass from "./someOtherPackage/SomeNativeClass";


/**
 * This is an example of a class using a "native" class.
 */
class UsingSomeNativeClass {

  someNative:SomeNativeClass = new SomeNativeClass();
  someOtherNative:SomeOtherNativeClass = new SomeOtherNativeClass();
  readonly someNative2:SomeNativeClass;

  constructor() {
    new package1_someOtherPackage_SomeNativeClass();
    this.someNative.baz = "foo";
    this.someNative2.baz = "foo";
    var local = ():void => {
      var test = this.someNative2.baz;
    };
    var foo = this.someNativeAccessor;
    var bar = this.anotherNativeAccessor;
    this.someNative.delete();
  }

   get someNativeAccessor():SomeNativeClass {
    return this.someNative;
  }

   get anotherNativeAccessor():SomeNativeClass {
    return this.someNative;
  }

   get monkey():boolean {
    return false;
  }

   set monkey(value:boolean) {
  }
}
interface UsingSomeNativeClass{

  abstractMethod(param1: string, param2?: number):void;}

export default UsingSomeNativeClass;
