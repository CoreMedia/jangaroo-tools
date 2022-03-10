import { metadata } from "@jangaroo/runtime";
import Bitmap from "../flash/display/Bitmap";

/**
 * This is an example of a class using an [Embed] annotation.
 */
class UsingEmbed {

  someText:Class = null;

  static #anotherText:Class = null;

  static #jooley:Class = null;

  annotated1;

  annotated2() {
  }

  annotated3() {
  }

  annotated4() {
  }

  /**
   * multiple
   */
  
  /**
   * annotations
   */
  annotated5() {
  }

  annotated6:string;

}
metadata(UsingEmbed, ["SomeRuntimeAnnotation"],
    ["SomeRuntimeAnnotation", {foo: "bar"}]);

export default UsingEmbed;
