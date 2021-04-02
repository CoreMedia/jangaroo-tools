

/**
 * Retest for JOO-64.
 */
class StaticAndNonStatic {

  static #static = (() => {
    new StaticAndNonStatic();
  })();

  StaticAndNonStatic:string = null;
  
  static #static1 = (() => {
    new StaticAndNonStatic();
  })();
}
export default StaticAndNonStatic;
