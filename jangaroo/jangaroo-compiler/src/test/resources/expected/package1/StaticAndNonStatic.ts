

/**
 * Retest for JOO-64.
 */
class StaticAndNonStatic {

  StaticAndNonStatic:string = null;

  static {

  new StaticAndNonStatic();

  }
  
  static {
  
  new StaticAndNonStatic();
  
  }
}
export default StaticAndNonStatic;
