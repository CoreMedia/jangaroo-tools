import SuperInterface from "./SuperInterface";


/**
 * Some ASDoc.
 */
// this comment should vanish in the API!
abstract class Interface extends SuperInterface {
  abstract doSomething():string;

  abstract property:string;
}
export default Interface;
