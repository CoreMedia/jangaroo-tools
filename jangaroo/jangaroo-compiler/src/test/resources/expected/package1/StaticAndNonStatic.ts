

/**
 * Retest for JOO-64.
 */
 class StaticAndNonStatic {

  //@ts-expect-error 18022
  static #static = (() => {
    new StaticAndNonStatic();
  })();

   StaticAndNonStatic:string = null;
  
  //@ts-expect-error 18022
  static #static1 = (() => {
    new StaticAndNonStatic();
  })();
}
export default StaticAndNonStatic;
