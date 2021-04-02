import { metadata } from "@jangaroo/joo/AS3";
import Bitmap from "../flash/display/Bitmap";

/**
 * This is an example of a class using an [Embed] annotation.
 */
@metadata("SomeRuntimeAnnotation")
@metadata("SomeRuntimeAnnotation", {foo: "bar"})
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
export default UsingEmbed;
