/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

/**
 * A class to provide debug facilities in java.
 * <p>
 * For <CODE>assertTrue</CODE>, to avoid evaluation of the boolean expression with
 * debugging turned off on sun's java interpreter and with sun's javac use the
 * following:
 * <pre>
 *     Debug.assertTrue(Debug.enabled &amp;&amp; anExpression, "error message");
 * </pre>
 * <p>
 * The short-cut semantics of '&amp;&amp;' will guarantee that anExpression will not be
 * evaluated if enabled is set to false.
 * <p>
 * <CODE>Debug.print</CODE> also won't print its argument if <CODE>Debug.enabled</CODE> is false. If
 * the generation of the argument is already costly, you have to wrap it in an if-statement.
 *
 * @author Andreas Gawecki
 */
public final class Debug {

  public static final boolean enabled = true;

  private Debug() {
    //hide public constructor
  }

  public static void assertTrue(boolean b, String s) {
    if (enabled && !b) {
      throw new AssertionError("Assertion failed: " + s);
    }
  }

  /**
   * Method to use to assert that an array is sorted
   * @param array the array
   * @return true/false
   */
  public static boolean isSorted(int[] array) {
    for (int i = 1; i < array.length; i++) {
      if (array[i] < array[i - 1]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method to use to assert that an array contains an object.
   * Uses ==
   * @param array the array
   * @param find the Object to find
   * @return true/false
   */
  public static boolean isInArray(Object[] array, Object find) {
    for (Object anArray : array) {
      if (anArray == find) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to use to assert that an array contains a String.
   * Uses equals.
   * @param array the array
   * @param find the string to find
   * @return if the array contains the string or not
   */
  public static boolean isInArray(String[] array, String find) {
    for (String anArray : array) {
      if (anArray.equals(find)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to use to assert that an array contains an int.
   * @param array the array
   * @param find the int to find in the array
   * @return if the array contains the int or not
   */
  public static boolean isInArray(int[] array, int find) {
    for (int anArray : array) {
      if (anArray == find) {
        return true;
      }
    }
    return false;
  }

  /**
   * Print a String, and then finish the line.
   * @param x the string
   */
  public static void println(String x) {
    if (enabled) {
      System.out.println(x); // NOSONAR this is a cmd line tool
    }
  }

  /**
   * Print an Object, and then finish the line.
   * @param x the object
   */
  public static void println(Object x) {
    if (enabled) {
      System.out.println(x); // NOSONAR this is a cmd line tool
    }
  }

  /**
   * Print an Array.
   * @param x the array
   */
  public static void printArray(Object[] x) {
    if (enabled) {
      System.out.print(printArrayToString(x)); // NOSONAR this is a cmd line tool
    }
  }

  /**
   * Print an Object Array to a String.
   * @param x the array
   * @return the string
   */
  public static String printArrayToString(Object[] x) {
    if (x == null) {
      return "null";
    }

    StringBuilder buffer = new StringBuilder();
    buffer.append('{');
    for (int i = 0; i < x.length; i++) {
      if (i > 0) {
        buffer.append(',');
      }
      buffer.append(x[i]);
    }
    buffer.append('}');
    return buffer.toString();
  }

  /**
   * Print an int Array to a String.
   * @param x the array
   * @return the string
   */
  public static String printArrayToString(int[] x) {
    StringBuilder buffer = new StringBuilder();
    buffer.append('{');
    for (int i = 0; i < x.length; i++) {
      if (i > 0) {
        buffer.append(',');
      }
      buffer.append(x[i]);
    }
    buffer.append('}');
    return buffer.toString();
  }

  /**
   * Print an Array.
   * @param x the array
   */
  public static void printArray(int[] x) {
    if (enabled) {
      System.out.print('{'); // NOSONAR this is a cmd line tool
      for (int i = 0; i < x.length; i++) {
        if (i > 0) {
          System.out.print(',');
        }
        System.out.print(x[i]); // NOSONAR this is a cmd line tool
      }
      System.out.println('}'); // NOSONAR this is a cmd line tool
    }
  }
}
