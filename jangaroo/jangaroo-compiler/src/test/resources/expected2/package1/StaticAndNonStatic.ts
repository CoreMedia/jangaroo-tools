

/**
 * Retest for JOO-64.
 */
class StaticAndNonStatic {

  static {
    new StaticAndNonStatic();
  }

  StaticAndNonStatic:string = null;
  
  static {
    new StaticAndNonStatic();
  }
}
export default StaticAndNonStatic;
