import {asConfig} from '@jangaroo/joo/AS3';
import package1_someOtherPackage_SomeNativeClass from './someOtherPackage/SomeNativeClass';
import SomeOtherNativeClass from '../SomeOtherNativeClass';


/**
 * This is an example of a class using a "native" class.
 */
 class UsingSomeNativeClass {

   someNative:SomeNativeClass = new SomeNativeClass();
   someOtherNative:SomeOtherNativeClass = new SomeOtherNativeClass();
   readonly someNative2:SomeNativeClass;

   constructor() {const this$=this;
    new package1_someOtherPackage_SomeNativeClass();
    asConfig(this.someNative).baz = "foo";
    asConfig(this.someNative2).baz = "foo";
    var local = ():void => {
      var test = asConfig(this.someNative2).baz;
    };
    var foo = this.someNativeAccessor;
    var bar = this.anotherNativeAccessor;
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
export default UsingSomeNativeClass;
