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
    this.someNative.setConfig("baz" , "foo");
    this.someNative2.setConfig("baz" , "foo");
    var local = ():void => {
      var test = this.someNative2.getConfig("baz");
    };
    var foo = this.getConfig("someNativeAccessor");
    var bar = this.getConfig("anotherNativeAccessor");
  }

  
   getSomeNativeAccessor():SomeNativeClass {
    return this.someNative;
  }

  
   getAnotherNativeAccessor():SomeNativeClass {
    return this.someNative;
  }

  
   getMonkey():boolean {
    return false;
  }

  
   setMonkey(value:boolean) {
  }
}
export default UsingSomeNativeClass;
