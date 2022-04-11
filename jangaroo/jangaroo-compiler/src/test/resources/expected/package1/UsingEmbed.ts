import { metadata } from "@jangaroo/runtime";
import Embed from "../joo/flash/Embed";
import first_text_txt from "./first-text.txt";
import jooley_png from "./jooley.png";
import second_text_csv from "./second_text.csv";

/**
 * This is an example of a class using an [Embed] annotation.
 */
class UsingEmbed {

  static someText:Class = Embed({source:first_text_txt, mimeType:"application/octet-stream"});

  static #anotherText:Class = Embed({source:second_text_csv, mimeType:"application/octet-stream"});

  static #jooley:Class = Embed({source:jooley_png});

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
