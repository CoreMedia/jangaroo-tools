

/**
 * Retest for JOO-64.
 */
 class StaticAndNonStatic {

   StaticAndNonStatic:string = null;
  
  // noinspection JSUnusedLocalSymbols
  private static static$yxTL = (() => {
    new StaticAndNonStatic();
  })();
}
export default StaticAndNonStatic;
