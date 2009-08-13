/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * Returns true if the value is NaN(not a number). The isNaN() function is useful for checking whether a mathematical
 * expression evaluates successfully to a number. The most common use of isNaN() is to check the value returned from
 * the parseInt()and parseFloat() functions. The NaN value is a special member of the Number data type that represents
 * a value that is "not a number."
 * <p>Note: The NaN value is not a member of the int or uint data types.
 * <p>The following table describes the return value of isNaN() on various input types and values. (If your compiler
 * warnings are set to Strict Mode, some of the following operations will generate compiler warnings.)
 * <pre>
 * Input Type/Value                                 Example               Return Value
 * 0 divided by 0                                   isNaN(0/0)            true
 * Non-zero number divided by 0                     isNaN(5/0)            false
 * Square root of a negative number                 isNaN(Math.sqrt(-1))  true
 * Arcsine of number greater than 1 or less than 0  isNaN(Math.asin(2))   true
 * String that can be converted to Number           isNaN("5")            false
 * String that cannot be converted to Number        isNaN("5a")           true
 * </pre>
 * @param num A numeric value or mathematical expression to evaluate.
 * @return Boolean Returns true if the value is NaN(not a number) and false otherwise.
 */
public native function isNaN(num : *) : Boolean;

}