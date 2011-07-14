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
 * <p/>
 * For <CODE>assertTrue</CODE>, to avoid evaluation of the boolean expression with
 * debugging turned off on sun's java interpreter and with sun's javac use the
 * following:
 * <p/>
 * <pre>
 *     Debug.assertTrue(Debug.enabled && anExpression, "error message");
 * </pre>
 * <p/>
 * The short-cut semantics of '&&' will guarantee that anExpression will not be
 * evaluated if enabled is set to false.
 * <p/>
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
      throw new RuntimeException("Assertion failed: " + s);
    }
  }

  /**
   * Method to use to assert that an array is sorted
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
   */
  public static boolean isInArray(Object[] array, Object find) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == find) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to use to assert that an array contains a String.
   * Uses equals.
   */
  public static boolean isInArray(String[] array, String find) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(find)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to use to assert that an array contains an int.
   */
  public static boolean isInArray(int[] array, int find) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == find) {
        return true;
      }
    }
    return false;
  }

  /**
   * Print a String, and then finish the line.
   */
  public static void println(String x) {
    if (enabled) {
      System.out.println(x);
    }
  }

  /**
   * Print an Object, and then finish the line.
   */
  public static void println(Object x) {
    if (enabled) {
      System.out.println(x);
    }
  }

  /**
   * Print an Array.
   */
  public static void printArray(Object[] x) {
    if (enabled) {
      System.out.print(printArrayToString(x));
    }
  }

  /**
   * Print an Object Array to a String.
   */
  public static String printArrayToString(Object[] x) {
    if (x == null) {
      return "null";
    }

    StringBuffer buffer = new StringBuffer();
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
   */
  public static String printArrayToString(int[] x) {
    StringBuffer buffer = new StringBuffer();
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
   */
  public static void printArray(int[] x) {
    if (enabled) {
      System.out.print('{');
      for (int i = 0; i < x.length; i++) {
        if (i > 0) {
          System.out.print(',');
        }
        System.out.print(x[i]);
      }
      System.out.println('}');
    }
  }

  /**
   * prints stack trace where you are
   */
  public static void printStackTrace() {
    try {
      throw new Exception();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
