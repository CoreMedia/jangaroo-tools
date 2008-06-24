/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * A class to provide debug facilities in java.
 *
 * For <CODE>assertTrue</CODE>, to avoid evaluation of the boolean expression with
 * debugging turned off on sun's java interpreter and with sun's javac use the
 * following:
 *
 * <pre>
 *     Debug.assertTrue(Debug.enabled && anExpression, "error message");
 * </pre>
 *
 * The short-cut semantics of '&&' will guarantee that anExpression will not be
 * evaluated if enabled is set to false.
 *
 * <CODE>Debug.print</CODE> also won't print its argument if <CODE>Debug.enabled</CODE> is false. If
 * the generation of the argument is already costly, you have to wrap it in an if-statement.
 *
 * @author Andreas Gawecki
 */
public class Debug {

  public static boolean enabled = true;

  public static final void assertTrue(boolean b, String s) {
    if (enabled && !b)
      throw new RuntimeException("Assertion failed: " + s);
  }

  /**
   * Method to use to assert that an array is sorted
   */
  public static boolean isSorted(int[] array){
    for(int i = 1; i < array.length; i++){
      if(array[i] < array[i-1])
        return false;
    }
    return true;
  }

  /**
   * Method to use to assert that an array contains an object.
   * Uses ==
   */
  public static boolean isInArray(Object[] array, Object find) {
    for(int i=0; i < array.length; i++)
      if(array[i] == find)
        return true;
    return false;
  }

  /**
   * Method to use to assert that an array contains a String.
   * Uses equals.
   */
  public static boolean isInArray(String[] array, String find) {
    for(int i=0; i < array.length; i++)
      if(array[i].equals(find))
        return true;
    return false;
  }

  /**
   * Method to use to assert that an array contains an int.
   */
  public static boolean isInArray(int[] array, int find) {
    for(int i=0; i < array.length; i++)
      if(array[i] == find)
        return true;
    return false;
  }

  /** Print a String, and then finish the line. */
  public static final void println(String x) {
    if (enabled) System.out.println(x);
  }

  /** Print an Object, and then finish the line. */
  public static final void println(Object x) {
    if (enabled) System.out.println(x);
  }

  /** Print an Array. */
  public static final void printArray(Object[] x) {
    if (enabled) {
      System.out.print(printArrayToString(x));
    }
  }

  /** Print an Object Array to a String. */
  public static final String printArrayToString(Object[] x) {
    if(x == null)
      return "null";

    StringBuffer buffer = new StringBuffer();
    buffer.append('{');
    for(int i = 0; i < x.length; i++) {
      if (i > 0)
        buffer.append(',');
      buffer.append(x[i]);
    }
    buffer.append('}');
    return buffer.toString();
  }

  /** Print an int Array to a String. */
  public static final String printArrayToString(int[] x) {
    StringBuffer buffer = new StringBuffer();
    buffer.append('{');
    for(int i = 0; i < x.length; i++) {
      if (i > 0)
        buffer.append(',');
      buffer.append(x[i]);
    }
    buffer.append('}');
    return buffer.toString();
  }

  /** Print an Array. */
  public static final void printArray(int[] x) {
    if (enabled) {
      System.out.print('{');
      for(int i = 0; i < x.length; i++){
        if (i > 0)
          System.out.print(',');
        System.out.print(x[i]);
      }
      System.out.println('}');
    }
  }

  /**
   * prints stack trace where you are
   */
  public static void printStackTrace(){
    try{
      throw new Exception();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
