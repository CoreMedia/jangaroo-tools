import { asConfig } from "@jangaroo/runtime/AS3";
import SomeOtherClass from "./package1/someOtherPackage/SomeOtherClass";

   new SomeOtherClass().addListener("clickclack", ():void => {
     const title = event.source.title;
   });
