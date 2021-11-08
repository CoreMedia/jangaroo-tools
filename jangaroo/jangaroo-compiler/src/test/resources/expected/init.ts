import { asConfig } from "@jangaroo/runtime";
import SomeOtherClass from "./package1/someOtherPackage/SomeOtherClass";

   new SomeOtherClass().addListener("clickclack", ():void => {
     const title = event.source.title;
   });
