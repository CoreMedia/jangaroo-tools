import { metadata } from "@jangaroo/runtime";
import Embed from "../joo/flash/Embed";
import first_text_txt from "./first-text.txt";
import jooley_png from "./jooley.png";
import second_text_csv from "./second_text.csv";

/**
 * This is an example of a class using an [Embed] annotation.
 */
class UsingEmbed {

  static someText = Embed(first_text_txt, "application/octet-stream");

  static #anotherText = Embed(second_text_csv, "application/octet-stream");

  static #jooley = Embed(jooley_png);

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
