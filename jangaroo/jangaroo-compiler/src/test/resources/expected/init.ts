import { asConfig } from "@jangaroo/runtime";
import ConfigClass from "./package1/ConfigClass";
import IncludedClass from "./package1/IncludedClass";
import SomeOtherClass from "./package1/someOtherPackage/SomeOtherClass";

   new SomeOtherClass().addListener("clickclack", (source: ConfigClass, stranger: IncludedClass, anton: string):void => {
      const event = { source, stranger, anton };
     const title = event.source.title;
   });
