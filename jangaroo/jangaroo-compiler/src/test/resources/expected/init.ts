import { asConfig } from "@jangaroo/runtime";
import ConfigClass from "./package1/ConfigClass";
import IncludedClass from "./package1/IncludedClass";
import SomeOtherClass from "./package1/someOtherPackage/SomeOtherClass";

   new SomeOtherClass().addListener("clickclack", (source: ConfigClass, stranger: IncludedClass):void => {
      const event = { source, stranger };
     const title = event.source.title;
   });
