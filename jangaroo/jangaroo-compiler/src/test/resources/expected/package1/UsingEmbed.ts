import {metadata} from '@jangaroo/joo/AS3';
import Bitmap from '../flash/display/Bitmap';

/**
 * This is an example of a class using an [Embed] annotation.
 */
class UsingEmbed {

  someText:Class = null;

  //@ts-expect-error 18022
  static #anotherText:Class = null;

  //@ts-expect-error 18022
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
    "SomeRuntimeAnnotation", {foo: "bar"}]);

export default UsingEmbed;
