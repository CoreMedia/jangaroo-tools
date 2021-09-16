import { asConfig } from "@jangaroo/runtime/AS3";
import SomeEvent from "./package1/someOtherPackage/SomeEvent";
import SomeOtherClass from "./package1/someOtherPackage/SomeOtherClass";

   new SomeOtherClass().addListener("clack", (event: SomeEvent):void => {
     const title = event.source.title;
   });
