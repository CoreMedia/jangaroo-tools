import {metadata} from '@jangaroo/joo/AS3';
import Bitmap from '../flash/display/Bitmap';
/*

@SomeRuntimeAnnotation
/**
 * This is an example of a class using an [Embed] annotation.
 * /
@SomeRuntimeAnnotation({foo:"bar"})*/
class UsingEmbed {

  
  someText:Class = null;

  
  //@ts-expect-error 18022
  static #anotherText:Class = null;

  
  //@ts-expect-error 18022
  static #jooley:Class = null;/*

  @SomeRuntimeAnnotation*/
  annotated1;/*

  @SomeRuntimeAnnotationWithArg({"_""foo"})*/
  annotated2() {
  }/*

  @SomeRuntimeAnnotationWithNamedArg({foo:"bar"})*/
  annotated3() {
  }/*

  @SomeRuntimeAnnotationWithNamedArg({foo:"bar"})*/
  annotated4() {
  }/*

  /**
   * multiple
   * /
  @SomeRuntimeAnnotation({type:"bar"})
  /**
   * annotations
   * /
  @SomeRuntimeAnnotation({type:"baz"})*/
  annotated5() {
  }/*

  @SomePropertyAnnotation({"_"1})*/
  annotated6:string;

}
metadata(UsingEmbed, ["SomeRuntimeAnnotation"],
    "SomeRuntimeAnnotation", {foo: "bar"}]);

export default UsingEmbed;
