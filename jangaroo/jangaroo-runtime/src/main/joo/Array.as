/**
 * API and documentation by Adobe�.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {


/**
 * The Array class lets you access and manipulate arrays. Array indices are zero-based, which means that the first element in the array is <code>[0]</code>, the second element is <code>[1]</code>, and so on. To create an Array object, you use the <code>new Array()</code> constructor . <code>Array()</code> can also be invoked as a function. In addition, you can use the array access (<code>[]</code>) operator to initialize an array or access the elements of an array.
 * <p>You can store a wide variety of data types in an array element, including numbers, strings, objects, and even other arrays. You can create a <i>multidimensional</i> array by creating an indexed array and assigning to each of its elements a different indexed array. Such an array is considered multidimensional because it can be used to represent data in a table.</p>
 * <p>Arrays are <i>sparse arrays</i>, meaning there might be an element at index 0 and another at index 5, but nothing in the index positions between those two elements. In such a case, the elements in positions 1 through 4 are undefined, which indicates the absence of an element, not necessarily the presence of an element with the value <code>undefined</code>.</p>
 * <p>Array assignment is by reference rather than by value. When you assign one array variable to another array variable, both refer to the same array:</p>
 * <listing>
 *  var oneArray:Array = new Array("a", "b", "c");
 *  var twoArray:Array = oneArray; // Both array variables refer to the same array.
 *  twoArray[0] = "z";
 *  trace(oneArray);               // Output: z,b,c.
 * </listing>
 * <p>Do not use the Array class to create <i>associative arrays</i> (also called <i>hashes</i>), which are data structures that contain named elements instead of numbered elements. To create associative arrays, use the Object class. Although ActionScript permits you to create associative arrays using the Array class, you cannot use any of the Array class methods or properties with associative arrays.</p>
 * <p>You can extend the Array class and override or add methods. However, you must specify the subclass as <code>dynamic</code> or you will lose the ability to store data in an array.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./Array.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#array_access
 * @see Object
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7fd0.html Functions
 * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f56.html Function parameters
 *
 */
[Native]
public dynamic class Array {
  /**
   * A non-negative integer specifying the number of elements in the array. This property is automatically updated when new elements are added to the array. When you assign a value to an array element (for example, <code>my_array[index] = value</code>), if <code>index</code> is a number, and <code>index+1</code> is greater than the <code>length</code> property, the <code>length</code> property is updated to <code>index+1</code>.
   * <p><b>Note:</b> If you assign a value to the <code>length</code> property that is shorter than the existing length, the array will be truncated.</p>
   * @example The following code creates an Array object <code>names</code> with the string element <code>Bill</code>. It then uses the <code>push()</code> method to add another string element <code>Kyle</code>. The length of the array, as determined by the <code>length</code> property, was one element before the use of <code>push()</code> and is two elements after <code>push()</code> is called. Another string, <code>Jeff</code>, is added to make the length of <code>names</code> three elements. The <code>shift()</code> method is then called twice to remove <code>Bill</code> and <code>Kyle</code>, making the final array of <code>length</code> one.
   * <listing>
   * var names:Array = new Array("Bill");
   * names.push("Kyle");
   * trace(names.length); // 2
   *
   * names.push("Jeff");
   * trace(names.length); // 3
   *
   * names.shift();
   * names.shift();
   * trace(names.length); // 1
   * </listing>
   */
  public native function get length():uint;

  /**
   * @private
   */
  public native function set length(value:uint):void;

  /**
   * Lets you create an array of the specified number of elements. If you don't specify any parameters, an array containing 0 elements is created. If you specify a number of elements, an array is created with <code>numElements</code> number of elements.
   * <p><b>Note:</b> The constructor accepts variable types of arguments. The constructor behaves differently depending on the type and number of arguments passed, as detailed in the following. ActionScript 3.0 does not support method or constructor overloading.</p>
   * <p>If there are more than one parameter or one parameter that is not a number, you create an array that contains the specified elements. You can specify values of any type. The first element in an array always has an index (or position) of 0.</p>
   *
   * @param values A comma-separated list of one or more arbitrary values or an integer that specifies the number of elements in the array. If only a single numeric parameter is passed to the Array constructor, it is assumed to specify the array's <code>length</code> property.
   *
   * @throws RangeError The argument is a number that is not an integer greater than or equal to 0.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#array_access
   * @see #length
   *
   * @example The following example creates the Array object <code>myArr</code> with no arguments and an initial length of 0:
   * <listing>
   * package {
   * import flash.display.Sprite;
   *
   * public class Array_Array extends Sprite {
   *
   *   public function Array_Array() {
   *     var myArr:Array = new Array();
   *     trace(myArr.length); // 0
   *   }
   * }
   * }
   * </listing>
   * <div>The following example creates an Array object with 5 initial elements, with a length of 5, and populates the first element with the string <code>"one"</code>, and adds the string element <code>"six"</code> to the end of the array by using the <code>push()</code> method:
   * <listing>
   * package {
   * import flash.display.Sprite;
   *
   * public class Array_Array_2 extends Sprite {
   *
   *   public function Array_Array_2() {
   *     var myArr:Array = new Array(5);
   *     trace(myArr.length); // 5
   *     myArr[0] = "one";
   *     myArr.push("six");
   *     trace(myArr);         // one,,,,,six
   *     trace(myArr.length); // 6
   *   }
   * }
   * }
   * </listing></div>
   * @example The following example creates a new Array object with an initial length of 3, populates the array with the string elements <code>one</code>, <code>two</code>, and <code>three</code>, and then converts the elements to a string.
   * <listing>
   * package {
   * import flash.display.Sprite;
   *
   * public class Array_Array_3 extends Sprite {
   *
   *   public function Array_Array_3() {
   *     var myArr:Array = new Array("one", "two", "three");
   *     trace(myArr.length); // 3
   *     trace(myArr);          // one,two,three
   *   }
   * }
   * }
   * </listing>
   */
  public native function Array(... values);

  /**
   * Concatenates the elements specified in the parameters with the elements in an array and creates a new array. If the parameters specify an array, the elements of that array are concatenated. If you don't pass any parameters, the new array is a duplicate (shallow clone) of the original array.
   * @param args A value of any data type (such as numbers, elements, or strings) to be concatenated in a new array.
   *
   * @return An array that contains the elements from this array followed by elements from the parameters.
   *
   * @example The following code creates four Array objects:
   * <ul>
   * <li>The <code>numbers</code> array, which contains the numbers <code>1</code>, <code>2</code>, and <code>3</code>.</li>
   * <li>The <code>letters</code> array, which contains the letters <code>a</code>, <code>b</code>, and <code>c</code>.</li>
   * <li>The <code>numbersAndLetters</code> array, which calls the <code>concat()</code> method to produce the array <code>[1,2,3,a,b,c]</code>.</li>
   * <li>The <code>lettersAndNumbers</code> array, which calls the <code>concat()</code> method to produce the array <code>[a,b,c,1,2,3]</code>.</li></ul>
   * <listing>
   * var numbers:Array = new Array(1, 2, 3);
   * var letters:Array = new Array("a", "b", "c");
   * var numbersAndLetters:Array = numbers.concat(letters);
   * var lettersAndNumbers:Array = letters.concat(numbers);
   *
   * trace(numbers);       // 1,2,3
   * trace(letters);       // a,b,c
   * trace(numbersAndLetters); // 1,2,3,a,b,c
   * trace(lettersAndNumbers); // a,b,c,1,2,3
   * </listing>
   */
  public native function concat(... args):Array;

  /**
   * Executes a test function on each item in the array until an item is reached that returns <code>false</code> for the specified function. You use this method to determine whether all items in an array meet a criterion, such as having values less than a particular number.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. Suppose you create a function in a movie clip called <code>me</code>:</p>
   * <pre>
   * function myFunction(obj:Object):void {
   *     //your code here
   * }
   * </pre>
   * <p>Suppose you then use the <code>every()</code> method on an array called <code>myArray</code>:</p>
   * <pre>
   * myArray.every(myFunction, me);
   * </pre>
   * <p>Because <code>myFunction</code> is a member of the Timeline class, which cannot be overridden by <code>me</code>, the Flash runtime will throw an exception. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>
   * var myFunction:Function = function(obj:Object):void {
   *     //your code here
   * };
   * myArray.every(myFunction, me);
   * </pre>
   * @param callback The function to run on each item in the array. This function can contain a simple comparison (for example, <code>item < 20</code>) or a more complex operation, and is invoked with three arguments; the value of an item, the index of an item, and the Array object:
   * <pre>function callback(item:*, index:int, array:Array):Boolean;</pre>
   * @param thisObject An object to use as <code>this</code> for the function.
   *
   * @return A Boolean value of <code>true</code> if all items in the array return <code>true</code> for the specified function; otherwise, <code>false</code>.
   *
   * @see #some()
   *
   * @example The following example tests two arrays to determine whether every item in each array is a number. It also outputs the results of the test, showing that <code>isNumeric</code> is <code>true</code> for the first array and <code>false</code> for the second:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_every extends Sprite {
   *         public function Array_every() {
   *             var arr1:Array = new Array(1, 2, 4);
   *             var res1:Boolean = arr1.every(isNumeric);
   *             trace("isNumeric:", res1); // true
   *
   *             var arr2:Array = new Array(1, 2, "ham");
   *             var res2:Boolean = arr2.every(isNumeric);
   *             trace("isNumeric:", res2); // false
   *         }
   *         private function isNumeric(element:*, index:int, arr:Array):Boolean {
   *             return (element is Number);
   *         }
   *     }
   * }
   * </listing>
   */
  public function every(callback:Function, thisObject:* = null):Boolean {
    var i:uint = 0,
      j:uint = this.length;
    // for maximum performance, repeat for-loop code with different function invocations:
    if (thisObject) {
      for (; i < j; i++) {
        if (i in this) {
          if (!callback.call(thisObject, this[i], i, this)) {
            return false;
          }
        }
      }
    } else {
      for (; i < j; i++) {
        if (i in this) {
          if (!callback(this[i], i, this)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Executes a test function on each item in the array and constructs a new array for all items that return <code>true</code> for the specified function. If an item returns <code>false</code>, it is not included in the new array.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. Suppose you create a function in a movie clip called <code>me</code>:</p>
   * <pre>
   * function myFunction(obj:Object):void {
   *     //your code here
   * }
   * </pre>
   * <p>Suppose you then use the <code>filter()</code> method on an array called <code>myArray</code>:</p>
   * <pre>
   * myArray.filter(myFunction, me);
   * </pre>
   * <p>Because <code>myFunction</code> is a member of the Timeline class, which cannot be overridden by <code>me</code>, the Flash runtime will throw an exception. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>
   * var myFunction:Function = function(obj:Object):void {
   *     //your code here
   * };
   * myArray.filter(myFunction, me);
   * </pre>
   * @param callback The function to run on each item in the array. This function can contain a simple comparison (for example, <code>item < 20</code>) or a more complex operation, and is invoked with three arguments; the value of an item, the index of an item, and the Array object:
   * <pre>function callback(item:*, index:int, array:Array):Boolean;</pre>
   * @param thisObject An object to use as <code>this</code> for the function.
   *
   * @return A new array that contains all items from the original array that returned <code>true</code>.
   *
   * @see #map()
   *
   * @example The following example creates an array of all employees who are managers:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_filter extends Sprite {
   *         public function Array_filter() {
   *             var employees:Array = new Array();
   *             employees.push({name:"Employee 1", manager:false});
   *             employees.push({name:"Employee 2", manager:true});
   *             employees.push({name:"Employee 3", manager:false});
   *             trace("Employees:");
   *             employees.forEach(traceEmployee);
   *
   *             var managers:Array = employees.filter(isManager);
   *             trace("Managers:");
   *             managers.forEach(traceEmployee);
   *         }
   *         private function isManager(element:*, index:int, arr:Array):Boolean {
   *             return (element.manager == true);
   *         }
   *         private function traceEmployee(element:*, index:int, arr:Array):void {
   *             trace("\t" + element.name + ((element.manager) ? " (manager)" : ""));
   *         }
   *     }
   * }
   * </listing>
   */
  public function filter(callback:Function, thisObject:* = null):Array {
    var len:uint = this.length;
    var res:Array = [];
    var i:uint = 0;
    var val:*;
    if (thisObject) {
      // for maximum performance, repeat for-loop code with different function invocations:
      for (; i < len; i++) {
        if (i in this) {
          val = this[i];
          if (callback.call(thisObject, val, i, this)) {
            res.push(val);
          }
        }
      }
    } else {
      for (; i < len; i++) {
        if (i in this) {
          val = this[i];
          if (callback(val, i, this)) {
            res.push(val);
          }
        }
      }
    }
    return res;
  }

  /**
   * Executes a function on each item in the array.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. Suppose you create a function in a movie clip called <code>me</code>:</p>
   * <pre>
   * function myFunction(obj:Object):void {
   *     //your code here
   * }
   * </pre>
   * <p>Suppose you then use the <code>forEach()</code> method on an array called <code>myArray</code>:</p>
   * <pre>
   * myArray.forEach(myFunction, me);
   * </pre>
   * <p>Because <code>myFunction</code> is a member of the Timeline class, which cannot be overridden by <code>me</code>, the Flash runtime will throw an exception. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>
   * var myFunction:Function = function(obj:Object):void {
   *     //your code here
   * };
   * myArray.forEach(myFunction, me);
   * </pre>
   * @param callback The function to run on each item in the array. This function can contain a simple command (for example, a <code>trace()</code> statement) or a more complex operation, and is invoked with three arguments; the value of an item, the index of an item, and the Array object:
   * <pre>function callback(item:*, index:int, array:Array):void;</pre>
   * @param thisObject An object to use as <code>this</code> for the function.
   *
   * @example The following example runs the <code>trace()</code> statement in the <code>traceEmployee()</code> function on each item in the array:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_forEach extends Sprite {
   *         public function Array_forEach() {
   *             var employees:Array = new Array();
   *             employees.push({name:"Employee 1", manager:false});
   *             employees.push({name:"Employee 2", manager:true});
   *             employees.push({name:"Employee 3", manager:false});
   *             trace(employees);
   *             employees.forEach(traceEmployee);
   *         }
   *         private function traceEmployee(element:*, index:int, arr:Array):void {
   *             trace(element.name + " (" + element.manager + ")");
   *         }
   *     }
   * }
   * </listing>
   * <div>The following example also runs the <code>trace()</code> statement in a slightly altered <code>traceEmployee()</code> function on each item in the array:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_forEach_2 extends Sprite {
   *         public function Array_forEach_2() {
   *             var employeeXML:XML = <employees>
   *                     <employee name="Steven" manager="false" />
   *                     <employee name="Bruce" manager="true" />
   *                     <employee name="Rob" manager="false" />
   *                 </employees>;
   *             var employeesList:XMLList = employeeXML.employee;
   *             var employeesArray:Array = new Array();
   *             for each (var tempXML:XML in employeesList) {
   *                 employeesArray.push(tempXML);
   *             }
   *             employeesArray.sortOn("@name");
   *             employeesArray.forEach(traceEmployee);
   *         }
   *         private function traceEmployee(element:*, index:Number, arr:Array):void {
   *             trace(element.@name + ((element.@manager == "true") ? " (manager)" : ""));
   *         }
   *     }
   * }
   * </listing></div>
   */
  public function forEach(callback:Function, thisObject:* = null):void {
    var i:uint = 0,
      j:uint = this.length;
    // for maximum performance, repeat for-loop code with different function invocations:
    if (thisObject) {
      for (; i < j; i++) {
        if (i in this) {
          callback.call(thisObject, this[i], i, this);
        }
      }
    } else {
      for (; i < j; i++) {
        if (i in this) {
          callback(this[i], i, this);
        }
      }
    }
  }

  /**
   * Searches for an item in an array by using strict equality (<code>===</code>) and returns the index position of the item.
   * @param searchElement The item to find in the array.
   * @param fromIndex The location in the array from which to start searching for the item.
   *
   * @return A zero-based index position of the item in the array. If the <code>searchElement</code> argument is not found, the return value is -1.
   *
   * @see #lastIndexOf()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#strict_equality
   *
   * @example The following example displays the position of the specified array:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_indexOf extends Sprite {
   *         public function Array_indexOf() {
   *             var arr:Array = new Array(123,45,6789);
   *             arr.push("123-45-6789");
   *             arr.push("987-65-4321");
   *
   *             var index:int = arr.indexOf("123");
   *             trace(index); // -1
   *
   *             var index2:int = arr.indexOf(123);
   *             trace(index2); // 0
   *         }
   *     }
   * }
   * </listing>
   */
  public function indexOf(searchElement:*, fromIndex:int = 0):int {
    var len:uint = this.length;
    for (var i:uint = (fromIndex < 0) ? Math.max(0, len + fromIndex) : fromIndex || 0; i < len; i++) {
      if (searchElement === this[i])
        return i;
    }
    return -1;
  }


  /**
   * Converts the elements in an array to strings, inserts the specified separator between the elements, concatenates them, and returns the resulting string. A nested array is always separated by a comma (,), not by the separator passed to the <code>join()</code> method.
   * @param sep A character or string that separates array elements in the returned string. If you omit this parameter, a comma is used as the default separator.
   *
   * @return A string consisting of the elements of an array converted to strings and separated by the specified parameter.
   *
   * @see String#split()
   *
   * @example The following code creates an Array object <code>myArr</code> with elements <code>one</code>, <code>two</code>, and <code>three</code> and then a string containing <code>one and two and three</code> using the <code>join()</code> method.
   * <listing>
   * var myArr:Array = new Array("one", "two", "three");
   * var myStr:String = myArr.join(" and ");
   * trace(myArr); // one,two,three
   * trace(myStr); // one and two and three
   * </listing>
   * <div>The following code creates an Array object <code>specialChars</code> with elements <code>(</code>, <code>)</code>, <code>-</code>, and a blank space and then creates a string containing <code>(888) 867-5309</code>. Then, using a <code>for</code> loop, it removes each type of special character listed in <code>specialChars</code> to produce a string (<code>myStr</code>) that contains only the digits of the phone number remaining: <code>888675309</code>. Note that other characters, such as <code>+</code>, could have been added to <code>specialChars</code> and then this routine would work with international phone number formats.
   * <listing>
   * var phoneString:String = "(888) 867-5309";
   *
   * var specialChars:Array = new Array("(", ")", "-", " ");
   * var myStr:String = phoneString;
   *
   * var ln:uint = specialChars.length;
   * for(var i:uint; i < ln; i++) {
   *     myStr = myStr.split(specialChars[i]).join("");
   * }
   *
   * var phoneNumber:Number = new Number(myStr);
   *
   * trace(phoneString); // (888) 867-5309
   * trace(phoneNumber); // 8888675309
   * </listing></div>
   */
  public native function join(sep:* = null):String;

  /**
   * Searches for an item in an array, working backward from the last item, and returns the index position of the matching item using strict equality (<code>===</code>).
   * @param searchElement The item to find in the array.
   * @param fromIndex The location in the array from which to start searching for the item. The default is the maximum value allowed for an index. If you do not specify <code>fromIndex</code>, the search starts at the last item in the array.
   *
   * @return A zero-based index position of the item in the array. If the <code>searchElement</code> argument is not found, the return value is -1.
   *
   * @see #indexOf()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#strict_equality
   *
   * @example The following example displays the position of the specified array:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_lastIndexOf extends Sprite {
   *         public function Array_lastIndexOf() {
   *             var arr:Array = new Array(123,45,6789,123,984,323,123,32);
   *
   *             var index:int = arr.indexOf(123);
   *             trace(index); // 0
   *
   *             var index2:int = arr.lastIndexOf(123);
   *             trace(index2); // 6
   *         }
   *     }
   * }
   * </listing>
   */
  public function lastIndexOf(searchElement:*, fromIndex:int = 0x7fffffff):int {
    // TODO: test!
    var len:uint = this.length;
    for (var i:int = ((fromIndex < 0) ? Math.max(len, len - fromIndex) : fromIndex || len) - 1; i >= 0; i--) {
      if (searchElement === this[i])
        return i;
    }
    return -1;
  }

  /**
   * Executes a function on each item in an array, and constructs a new array of items corresponding to the results of the function on each item in the original array.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. Suppose you create a function in a movie clip called <code>me</code>:</p>
   * <pre>
   * function myFunction(obj:Object):void {
   *     //your code here
   * }
   * </pre>
   * <p>Suppose you then use the <code>map()</code> method on an array called <code>myArray</code>:</p>
   * <pre>
   * myArray.map(myFunction, me);
   * </pre>
   * <p>Because <code>myFunction</code> is a member of the Timeline class, which cannot be overridden by <code>me</code>, the Flash runtime will throw an exception. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>
   * var myFunction:Function = function(obj:Object):void {
   *     //your code here
   * };
   * myArray.map(myFunction, me);
   * </pre>
   * @param callback The function to run on each item in the array. This function can contain a simple command (such as changing the case of an array of strings) or a more complex operation, and is invoked with three arguments; the value of an item, the index of an item, and the Array object:
   * <pre>function callback(item:*, index:int, array:Array):String;</pre>
   * @param thisObject An object to use as <code>this</code> for the function.
   *
   * @return A new array that contains the results of the function on each item in the original array.
   *
   * @see #filter()
   *
   * @example The following example changes all items in the array to use uppercase letters:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_map extends Sprite {
   *         public function Array_map() {
   *             var arr:Array = new Array("one", "two", "Three");
   *             trace(arr); // one,two,Three
   *
   *             var upperArr:Array = arr.map(toUpper);
   *             trace(upperArr); // ONE,TWO,THREE
   *         }
   *         private function toUpper(element:*, index:int, arr:Array):String {
   *             return String(element).toUpperCase();
   *         }
   *     }
   * }
   * </listing>
   */
  public function map(callback:Function, thisObject:* = null):Array {
    var results:Array = [];
    var i:uint = 0,
      j:uint = this.length;
    // for maximum performance, repeat for-loop code with different function invocations:
    if (thisObject) {
      for (; i < j; i++) {
        results[i] = callback.call(thisObject, this[i], i, this);
      }
    } else {
      for (; i < j; i++) {
        results[i] = callback(this[i], i, this);
      }
    }
    return results;
  }

  /**
   * Removes the last element from an array and returns the value of that element.
   * @return The value of the last element (of any data type) in the specified array.
   *
   * @see #push()
   * @see #shift()
   * @see #unshift()
   *
   * @example The following code creates an Array object <code>letters</code> with elements <code>a</code>, <code>b</code>, and <code>c</code>. The last element (<code>c</code>) is then removed from the array using the <code>pop()</code> method and assigned to the String object <code>letter</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c");
   * trace(letters); // a,b,c
   * var letter:String = letters.pop();
   * trace(letters); // a,b
   * trace(letter);     // c
   * </listing>
   */
  public native function pop():*;

  /**
   * Adds one or more elements to the end of an array and returns the new length of the array.
   * @param args One or more values to append to the array.
   *
   * @return An integer representing the length of the new array.
   *
   * @see #pop()
   * @see #shift()
   * @see #unshift()
   *
   * @example The following code creates an empty Array object <code>letters</code> and then populates the array with the elements <code>a</code>, <code>b</code>, and <code>c</code> using the <code>push()</code> method.
   * <listing>
   * var letters:Array = new Array();
   *
   * letters.push("a");
   * letters.push("b");
   * letters.push("c");
   *
   * trace(letters.toString()); // a,b,c
   * </listing>
   * <div>The following code creates an Array object <code>letters</code>, which is initially populated with the element <code>a</code>. The <code>push()</code> method is then used once to add the elements <code>b</code> and <code>c</code> to the end of the array, which is three elements after the push.
   * <listing>
   * var letters:Array = new Array("a");
   * var count:uint = letters.push("b", "c");
   *
   * trace(letters); // a,b,c
   * trace(count);   // 3
   * </listing></div>
   */
  public native function push(... args):uint;

  /**
   * Reverses the array in place.
   * @return The new array.
   *
   * @example The following code creates an Array object <code>letters</code> with elements <code>a</code>, <code>b</code>, and <code>c</code>. The order of the array elements is then reversed using the <code>reverse()</code> method to produce the array <code>[c,b,a]</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c");
   * trace(letters); // a,b,c
   * letters.reverse();
   * trace(letters); // c,b,a
   * </listing>
   */
  public native function reverse():Array;

  /**
   * Removes the first element from an array and returns that element. The remaining array elements are moved from their original position, i, to i-1.
   * @return The first element (of any data type) in an array.
   *
   * @see #pop()
   * @see #push()
   * @see #unshift()
   *
   * @example The following code creates the Array object <code>letters</code> with elements <code>a</code>, <code>b</code>, and <code>c</code>. The <code>shift()</code> method is then used to remove the first element (<code>a</code>) from <code>letters</code> and assign it to the string <code>firstLetter</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c");
   * var firstLetter:String = letters.shift();
   * trace(letters);     // b,c
   * trace(firstLetter); // a
   * </listing>
   */
  public native function shift():*;

  /**
   * Returns a new array that consists of a range of elements from the original array, without modifying the original array. The returned array includes the <code>startIndex</code> element and all elements up to, but not including, the <code>endIndex</code> element.
   * <p>If you don't pass any parameters, the new array is a duplicate (shallow clone) of the original array.</p>
   * @param startIndex A number specifying the index of the starting point for the slice. If <code>startIndex</code> is a negative number, the starting point begins at the end of the array, where -1 is the last element.
   * @param endIndex A number specifying the index of the ending point for the slice. If you omit this parameter, the slice includes all elements from the starting point to the end of the array. If <code>endIndex</code> is a negative number, the ending point is specified from the end of the array, where -1 is the last element.
   *
   * @return An array that consists of a range of elements from the original array.
   *
   * @example The following code creates an Array object <code>letters</code> with elements <code>[a,b,c,d,e,f]</code>. The array <code>someLetters</code> is then created by calling the <code>slice()</code> method on elements one (<code>b</code>) through three (<code>d</code>), resulting in an array with elements <code>b</code> and <code>c</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c", "d", "e", "f");
   * var someLetters:Array = letters.slice(1,3);
   *
   * trace(letters);     // a,b,c,d,e,f
   * trace(someLetters); // b,c
   * </listing>
   * <div>The following code creates an Array object <code>letters</code> with elements <code>[a,b,c,d,e,f]</code>.The array <code>someLetters</code> is then created by calling the <code>slice()</code> method on element two (<code>c</code>), resulting in an array with elements <code>[c,d,e,f]</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c", "d", "e", "f");
   * var someLetters:Array = letters.slice(2);
   *
   * trace(letters);     // a,b,c,d,e,f
   * trace(someLetters); // c,d,e,f
   * </listing></div>
   * <div>The following code creates an Array object <code>letters</code> with elements <code>[a,b,c,d,e,f]</code>. The array <code>someLetters</code> is then created by calling the <code>slice()</code> method on the second to last element from the end (<code>e</code>), resulting in an array with elements <code>e</code> and <code>f</code>.
   * <listing>
   * var letters:Array = new Array("a", "b", "c", "d", "e", "f");
   * var someLetters:Array = letters.slice(-2);
   *
   * trace(letters);     // a,b,c,d,e,f
   * trace(someLetters); // e,f
   * </listing></div>
   */
  public native function slice(startIndex:* = 0, endIndex:* = 0xffffffff):Array;

  /**
   * Executes a test function on each item in the array until an item is reached that returns <code>true</code>. Use this method to determine whether any items in an array meet a criterion, such as having a value less than a particular number.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. Suppose you create a function in a movie clip called <code>me</code>:</p>
   * <pre>
   * function myFunction(obj:Object):void {
   *     //your code here
   * }
   * </pre>
   * <p>Suppose you then use the <code>some()</code> method on an array called <code>myArray</code>:</p>
   * <pre>
   * myArray.some(myFunction, me);
   * </pre>
   * <p>Because <code>myFunction</code> is a member of the Timeline class, which cannot be overridden by <code>me</code>, the Flash runtime will throw an exception. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>
   * var myFunction:Function = function(obj:Object):void {
   *     //your code here
   * };
   myArray.some(myFunction, me);
   </pre>
   * @param callback The function to run on each item in the array. This function can contain a simple comparison (for example <code>item < 20</code>) or a more complex operation, and is invoked with three arguments; the value of an item, the index of an item, and the Array object:
   * <pre>function callback(item:*, index:int, array:Array):Boolean;</pre>
   * @param thisObject An object to use as <code>this</code> for the function.
   *
   * @return A Boolean value of <code>true</code> if any items in the array return <code>true</code> for the specified function; otherwise <code>false</code>.
   *
   * @see #every()
   *
   * @example The following example displays which values are undefined:
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     public class Array_some extends Sprite {
   *         public function Array_some() {
   *             var arr:Array = new Array();
   *             arr[0] = "one";
   *             arr[1] = "two";
   *             arr[3] = "four";
   *             var isUndef:Boolean = arr.some(isUndefined);
   *             if (isUndef) {
   *                 trace("array contains undefined values: " + arr);
   *             } else {
   *                 trace("array contains no undefined values.");
   *             }
   *         }
   *         private function isUndefined(element:*, index:int, arr:Array):Boolean {
   *             return (element == undefined);
   *         }
   *     }
   * }
   * </listing>
   */
  public function some(callback:Function, thisObject:* = null):Boolean {
    var i:uint = 0,
      j:uint = this.length;
    // for maximum performance, repeat for-loop code with different function invocations:
    if (thisObject) {
      for (; i < j; i++) {
        if (i in this) {
          if (callback.call(thisObject, this[i], i, this)) {
            return true;
          }
        }
      }
    } else {
      for (; i < j; i++) {
        if (i in this) {
          if (callback(this[i], i, this)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Sorts the elements in an array. This method sorts according to Unicode values. (ASCII is a subset of Unicode.)
   * <p>By default, <code>Array</code>.<code>sort()</code> works in the following way:</p>
   * <ul>
   * <li>Sorting is case-sensitive (<i>Z</i> precedes <i>a</i>).</li>
   * <li>Sorting is ascending (<i>a</i> precedes <i>b</i>).</li>
   * <li>The array is modified to reflect the sort order; multiple elements that have identical sort fields are placed consecutively in the sorted array in no particular order.</li>
   * <li>All elements, regardless of data type, are sorted as if they were strings, so 100 precedes 99, because "1" is a lower string value than "9".</li></ul>
   * <p>To sort an array by using settings that deviate from the default settings, you can either use one of the sorting options described in the <code>sortOptions</code> portion of the <code>...args</code> parameter description, or you can create your own custom function to do the sorting. If you create a custom function, you call the <code>sort()</code> method, and use the name of your custom function as the first argument (<code>compareFunction</code>)</p>
   * @param args The arguments specifying a comparison function and one or more values that determine the behavior of the sort.
   * <p>This method uses the syntax and argument order <code>Array.sort(compareFunction, sortOptions)</code> with the arguments defined as follows:</p>
   * <ul>
   * <li><code>compareFunction</code> - A comparison function used to determine the sorting order of elements in an array. This argument is optional. A comparison function should take two arguments to compare. Given the elements A and B, the result of <code>compareFunction</code> can have a negative, 0, or positive value:
   * <ul>
   * <li>A negative return value specifies that A appears before B in the sorted sequence.</li>
   * <li>A return value of 0 specifies that A and B have the same sort order.</li>
   * <li>A positive return value specifies that A appears after B in the sorted sequence.</li></ul></li>
   * <li><code>sortOptions</code> - One or more numbers or defined constants, separated by the <code>|</code> (bitwise OR) operator, that change the behavior of the sort from the default. This argument is optional. The following values are acceptable for <code>sortOptions</code>:
   * <ul>
   * <li>1 or <code>Array.CASEINSENSITIVE</code></li>
   * <li>2 or <code>Array.DESCENDING</code></li>
   * <li>4 or <code>Array.UNIQUESORT</code></li>
   * <li>8 or <code>Array.RETURNINDEXEDARRAY</code></li>
   * <li>16 or <code>Array.NUMERIC</code></li></ul>For more information, see the <code>Array.sortOn()</code> method.</li></ul>
   *
   * @return The return value depends on whether you pass any arguments, as described in the following list:
   * <ul>
   * <li>If you specify a value of 4 or <code>Array.UNIQUESORT</code> for the <code>sortOptions</code> argument of the <code>...args</code> parameter and two or more elements being sorted have identical sort fields, Flash returns a value of 0 and does not modify the array.</li>
   * <li>If you specify a value of 8 or <code>Array.RETURNINDEXEDARRAY</code> for the <code>sortOptions</code> argument of the <code>...args</code> parameter, Flash returns a sorted numeric array of the indices that reflects the results of the sort and does not modify the array.</li>
   * <li>Otherwise, Flash returns nothing and modifies the array to reflect the sort order.</li></ul>
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#bitwise_OR
   * @see #sortOn()
   *
   * @example The following code creates the Array object <code>vegetables</code> with elements <code>[spinach, green pepper, cilantro, onion, avocado]</code>. The array is then sorted by the <code>sort()</code> method, which is called with no parameters. The result is <code>vegetables</code> sorted in alphabetical order (<code>[avocado, cilantro, green pepper, onion, spinach]</code>).
   * <listing>
   * var vegetables:Array = new Array("spinach",
   *                  "green pepper",
   *                  "cilantro",
   *                  "onion",
   *                  "avocado");
   *
   * trace(vegetables); // spinach,green pepper,cilantro,onion,avocado
   * vegetables.sort();
   * trace(vegetables); // avocado,cilantro,green pepper,onion,spinach
   * </listing>
   * <div>The following code creates the Array object <code>vegetables</code> with elements <code>[spinach, green pepper, Cilantro, Onion, and Avocado]</code>. The array is then sorted by the <code>sort()</code> method, which is called with no parameters the first time; the result is <code>[Avocado,Cilantro,Onion,green pepper,spinach]</code>. Then <code>sort()</code> is called on <code>vegetables</code> again with the <code>CASEINSENSITIVE</code> constant as a parameter. The result is <code>vegetables</code> sorted in alphabetical order (<code>[Avocado, Cilantro, green pepper, Onion, spinach]</code>).
   * <listing>
   * var vegetables:Array = new Array("spinach",
   *                  "green pepper",
   *                  "Cilantro",
   *                  "Onion",
   *                  "Avocado");
   *
   * vegetables.sort();
   * trace(vegetables); // Avocado,Cilantro,Onion,green pepper,spinach
   * vegetables.sort(Array.CASEINSENSITIVE);
   * trace(vegetables); // Avocado,Cilantro,green pepper,Onion,spinach
   * </listing></div>
   * <div>The following code creates the empty Array object <code>vegetables</code>, which is then populated through five calls to <code>push()</code>. Each time <code>push()</code> is called, a new <code>Vegetable</code> object is created by a call to the <code>Vegetable()</code> constructor, which accepts a String (<code>name</code>) and Number (<code>price</code>) object. Calling <code>push()</code> five times with the values shown results in the following array: <code>[lettuce:1.49, spinach:1.89, asparagus:3.99, celery:1.29, squash:1.44]</code>. The <code>sort()</code> method is then used to sort the array, resulting in the array <code>[asparagus:3.99, celery:1.29, lettuce:1.49, spinach:1.89, squash:1.44]</code>.
   * <listing>
   * var vegetables:Array = new Array();
   * vegetables.push(new Vegetable("lettuce", 1.49));
   * vegetables.push(new Vegetable("spinach", 1.89));
   * vegetables.push(new Vegetable("asparagus", 3.99));
   * vegetables.push(new Vegetable("celery", 1.29));
   * vegetables.push(new Vegetable("squash", 1.44));
   *
   * trace(vegetables);
   * // lettuce:1.49, spinach:1.89, asparagus:3.99, celery:1.29, squash:1.44
   *
   * vegetables.sort();
   *
   * trace(vegetables);
   * // asparagus:3.99, celery:1.29, lettuce:1.49, spinach:1.89, squash:1.44
   *
   * //The following code defines the Vegetable class
   * class Vegetable {
   *     private var name:String;
   *     private var price:Number;
   *
   *     public function Vegetable(name:String, price:Number) {
   *         this.name = name;
   *         this.price = price;
   *     }
   *
   *     public function toString():String {
   *         return " " + name + ":" + price;
   *     }
   * }
   * </listing></div>
   * <div>The following example is exactly the same as the previous one, except that the <code>sort()</code> method is used with a custom sort function (<code>sortOnPrice</code>), which sorts according to price instead of alphabetically. Note that the new function <code>getPrice()</code> extracts the price.
   * <listing>
   * var vegetables:Array = new Array();
   * vegetables.push(new Vegetable("lettuce", 1.49));
   * vegetables.push(new Vegetable("spinach", 1.89));
   * vegetables.push(new Vegetable("asparagus", 3.99));
   * vegetables.push(new Vegetable("celery", 1.29));
   * vegetables.push(new Vegetable("squash", 1.44));
   *
   * trace(vegetables);
   * // lettuce:1.49, spinach:1.89, asparagus:3.99, celery:1.29, squash:1.44
   *
   * vegetables.sort(sortOnPrice);
   *
   * trace(vegetables);
   * // celery:1.29, squash:1.44, lettuce:1.49, spinach:1.89, asparagus:3.99
   *
   * function sortOnPrice(a:Vegetable, b:Vegetable):Number {
   *     var aPrice:Number = a.getPrice();
   *     var bPrice:Number = b.getPrice();
   *
   *     if(aPrice > bPrice) {
   *         return 1;
   *     } else if(aPrice < bPrice) {
   *         return -1;
   *     } else  {
   *         //aPrice == bPrice
   *         return 0;
   *     }
   * }
   *
   * // The following code defines the Vegetable class and should be in a separate package.
   * class Vegetable {
   *     private var name:String;
   *     private var price:Number;
   *
   *     public function Vegetable(name:String, price:Number) {
   *         this.name = name;
   *         this.price = price;
   *     }
   *
   *     public function getPrice():Number {
   *         return price;
   *     }
   *
   *     public function toString():String {
   *         return " " + name + ":" + price;
   *     }
   * }
   * </listing></div>
   * <div>The following code creates the Array object <code>numbers</code> with elements <code>[3,5,100,34,10]</code>. A call to <code>sort()</code> without any parameters sorts alphabetically, producing the undesired result <code>[10,100,3,34,5]</code>. To properly sort numeric values, you must pass the constant <code>NUMERIC</code> to the <code>sort()</code> method, which sorts <code>numbers</code> as follows: <code>[3,5,10,34,100]</code>.
   * <p><b>Note:</b> The default behavior of the <code>sort()</code> function is to handle each entity as a string. If you use the <code>Array.NUMERIC</code> argument, the Flash runtime attempts to convert any non-numeric values to integers for sorting purposes. If it fails, the runtime throws an error. For example, the runtime can successfully convert a String value of <code>"6"</code> to an integer, but will throw an error if it encounters a String value of <code>"six"</code>.</p>
   * <listing>
   * var numbers:Array = new Array(3,5,100,34,10);
   *
   * trace(numbers); // 3,5,100,34,10
   * numbers.sort();
   * trace(numbers); // 10,100,3,34,5
   * numbers.sort(Array.NUMERIC);
   * trace(numbers); // 3,5,10,34,100
   * </listing></div>
   */
  public native function sort(... args):*;

  /**
   * Sorts the elements in an array according to one or more fields in the array. The array should have the following characteristics:
   * <ul>
   * <li>The array is an indexed array, not an associative array.</li>
   * <li>Each element of the array holds an object with one or more properties.</li>
   * <li>All of the objects have at least one property in common, the values of which can be used to sort the array. Such a property is called a <i>field</i>.</li></ul>
   * <p>If you pass multiple <code>fieldName</code> parameters, the first field represents the primary sort field, the second represents the next sort field, and so on. Flash sorts according to Unicode values. (ASCII is a subset of Unicode.) If either of the elements being compared does not contain the field that is specified in the <code>fieldName</code> parameter, the field is assumed to be set to <code>undefined</code>, and the elements are placed consecutively in the sorted array in no particular order.</p>
   * <p>By default, <code>Array</code>.<code>sortOn()</code> works in the following way:</p>
   * <ul>
   * <li>Sorting is case-sensitive (<i>Z</i> precedes <i>a</i>).</li>
   * <li>Sorting is ascending (<i>a</i> precedes <i>b</i>).</li>
   * <li>The array is modified to reflect the sort order; multiple elements that have identical sort fields are placed consecutively in the sorted array in no particular order.</li>
   * <li>Numeric fields are sorted as if they were strings, so 100 precedes 99, because "1" is a lower string value than "9".</li></ul>
   * <p>Flash Player 7 added the <code>options</code> parameter, which you can use to override the default sort behavior. To sort a simple array (for example, an array with only one field), or to specify a sort order that the <code>options</code> parameter doesn't support, use <code>Array.sort()</code>.</p>
   * <p>To pass multiple flags, separate them with the bitwise OR (<code>|</code>) operator:</p>
   * <listing>
   *      my_array.sortOn(someFieldName, Array.DESCENDING | Array.NUMERIC);
   *     </listing>
   * <p>Flash Player 8 added the ability to specify a different sorting option for each field when you sort by more than one field. In Flash Player 8 and later, the <code>options</code> parameter accepts an array of sort options such that each sort option corresponds to a sort field in the <code>fieldName</code> parameter. The following example sorts the primary sort field, <code>a</code>, using a descending sort; the secondary sort field, <code>b</code>, using a numeric sort; and the tertiary sort field, <code>c</code>, using a case-insensitive sort:</p>
   * <listing>
   *      Array.sortOn (["a", "b", "c"], [Array.DESCENDING, Array.NUMERIC, Array.CASEINSENSITIVE]);
   *     </listing>
   * <p><b>Note:</b> The <code>fieldName</code> and <code>options</code> arrays must have the same number of elements; otherwise, the <code>options</code> array is ignored. Also, the <code>Array.UNIQUESORT</code> and <code>Array.RETURNINDEXEDARRAY</code> options can be used only as the first element in the array; otherwise, they are ignored.</p>
   * @param fieldName A string that identifies a field to be used as the sort value, or an array in which the first element represents the primary sort field, the second represents the secondary sort field, and so on.
   * @param options One or more numbers or names of defined constants, separated by the <code>bitwise OR (|)</code> operator, that change the sorting behavior. The following values are acceptable for the <code>options</code> parameter:
   * <ul>
   * <li><code>Array.CASEINSENSITIVE</code> or 1</li>
   * <li><code>Array.DESCENDING</code> or 2</li>
   * <li><code>Array.UNIQUESORT</code> or 4</li>
   * <li><code>Array.RETURNINDEXEDARRAY</code> or 8</li>
   * <li><code>Array.NUMERIC</code> or 16</li></ul>
   * <p>Code hinting is enabled if you use the string form of the flag (for example, <code>DESCENDING</code>) rather than the numeric form (2).</p>
   *
   * @return The return value depends on whether you pass any parameters:
   * <ul>
   * <li>If you specify a value of 4 or <code>Array.UNIQUESORT</code> for the <code>options</code> parameter, and two or more elements being sorted have identical sort fields, a value of 0 is returned and the array is not modified.</li>
   * <li>If you specify a value of 8 or <code>Array.RETURNINDEXEDARRAY</code> for the <code>options</code> parameter, an array is returned that reflects the results of the sort and the array is not modified.</li>
   * <li>Otherwise, nothing is returned and the array is modified to reflect the sort order.</li></ul>
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#bitwise_OR
   * @see #sort()
   *
   * @example The following code creates an empty Array object <code>vegetables</code> and the array is then populated using five calls to <code>push()</code>. Each time <code>push()</code> is called, a new <code>Vegetable</code> object is created by calling the <code>Vegetable()</code> constructor, which accepts a String (<code>name</code>) and Number (<code>price</code>) object. Calling <code>push()</code> five times with the values shown results in the following array: <code>[lettuce:1.49, spinach:1.89, asparagus:3.99, celery:1.29, squash:1.44]</code>. The <code>sortOn()</code> method is then used with the <code>name</code> parameter to produce the following array: <code>[asparagus:3.99, celery:1.29, lettuce:1.49, spinach:1.89, squash:1.44]</code>. The <code>sortOn()</code> method is then called again with the price parameter, and the NUMERIC and DESCENDING constants to produce an array sorted by numbers in descending order: <code>[asparagus:3.99, spinach:1.89, lettuce:1.49, squash:1.44, celery:1.29]</code>.
   * <listing>
   * var vegetables:Array = new Array();
   * vegetables.push(new Vegetable("lettuce", 1.49));
   * vegetables.push(new Vegetable("spinach", 1.89));
   * vegetables.push(new Vegetable("asparagus", 3.99));
   * vegetables.push(new Vegetable("celery", 1.29));
   * vegetables.push(new Vegetable("squash", 1.44));
   *
   * trace(vegetables);
   * // lettuce:1.49, spinach:1.89, asparagus:3.99, celery:1.29, squash:1.44
   *
   * vegetables.sortOn("name");
   * trace(vegetables);
   * // asparagus:3.99, celery:1.29, lettuce:1.49, spinach:1.89, squash:1.44
   *
   * vegetables.sortOn("price", Array.NUMERIC | Array.DESCENDING);
   * trace(vegetables);
   * // asparagus:3.99, spinach:1.89, lettuce:1.49, squash:1.44, celery:1.29
   *
   * class Vegetable {
   *     public var name:String;
   *     public var price:Number;
   *
   *     public function Vegetable(name:String, price:Number) {
   *         this.name = name;
   *         this.price = price;
   *     }
   *
   *     public function toString():String {
   *         return " " + name + ":" + price;
   *     }
   * }
   * </listing>
   * <div>The following code creates an empty Array object <code>records</code> and the array is then populated using three calls to <code>push()</code>. Each time <code>push()</code> is called, the strings <code>name</code> and <code>city</code> and a <code>zip</code> number are added to <code>records</code>. Three <code>for</code> loops are used to print the array elements. The first <code>for</code> loop prints the elements in the order in which they were added. The second <code>for</code> loop is run after <code>records</code> has been sorted by name and then city using the <code>sortOn()</code> method. The third <code>for</code> loop produces different output because <code>records</code> is re-sorted by city then by name.
   * <listing>
   *
   * var records:Array = new Array();
   * records.push({name:"john", city:"omaha", zip:68144});
   * records.push({name:"john", city:"kansas city", zip:72345});
   * records.push({name:"bob", city:"omaha", zip:94010});
   *
   * for(var i:uint = 0; i < records.length; i++) {
   *     trace(records[i].name + ", " + records[i].city);
   * }
   * // Results:
   * // john, omaha
   * // john, kansas city
   * // bob, omaha
   *
   * trace("records.sortOn('name', 'city');");
   * records.sortOn(["name", "city"]);
   * for(var i:uint = 0; i < records.length; i++) {
   *     trace(records[i].name + ", " + records[i].city);
   * }
   * // Results:
   * // bob, omaha
   * // john, kansas city
   * // john, omaha
   *
   * trace("records.sortOn('city', 'name');");
   * records.sortOn(["city", "name"]);
   * for(var i:uint = 0; i < records.length; i++) {
   *     trace(records[i].name + ", " + records[i].city);
   * }
   * // Results:
   * // john, kansas city
   * // bob, omaha
   * // john, omaha
   * </listing></div>
   * <div>The following code creates an empty Array object <code>users</code> and the array is then populated using four calls to <code>push()</code>. Each time <code>push()</code> is called, a User object is created with the <code>User()</code> constructor and a <code>name</code> string and <code>age</code> uint are added to users. The resulting array set is <code>[Bob:3,barb:35,abcd:3,catchy:4]</code>.
   * <p>The array is then sorted in the following ways:</p><ol>
   * <li>By name only, producing the array <code>[Bob:3,abcd:3,barb:35,catchy:4]</code></li>
   * <li>By name and using the <code>CASEINSENSITIVE</code> constant, producing the array <code>[abcd:3,barb:35,Bob:3,catchy:4]</code></li>
   * <li>By name and using the <code>CASEINSENSITIVE</code> and <code>DESCENDING</code> constants, producing the array <code>[catchy:4,Bob:3,barb:35,abcd:3]</code></li>
   * <li>By age only, producing the array <code>[abcd:3,Bob:3,barb:35,catchy:4]</code></li>
   * <li>By age and using the <code>NUMERIC</code> constant, producing the array <code>[Bob:3,abcd:3,catchy:4,barb:35]</code></li>
   * <li>By age and using the <code>DESCENDING</code> and <code>NUMERIC</code> constants, producing the array <code>[barb:35,catchy:4,Bob:3,abcd:3]</code></li></ol>
   * <p>An array called <code>indices</code> is then created and assigned the results of a sort by age and using the <code>NUMERIC</code> and <code>RETURNINDEXEDARRAY</code> constants, resulting in the array <code>[Bob:3,abcd:3,catchy:4,barb:35]</code>, which is then printed out using a <code>for</code> loop.</p>
   * <listing>
   * class User {
   *     public var name:String;
   *     public var age:Number;
   *     public function User(name:String, age:uint) {
   *         this.name = name;
   *         this.age = age;
   *     }
   *
   *     public function toString():String {
   *         return this.name + ":" + this.age;
   *     }
   * }
   *
   * var users:Array = new Array();
   * users.push(new User("Bob", 3));
   * users.push(new User("barb", 35));
   * users.push(new User("abcd", 3));
   * users.push(new User("catchy", 4));
   *
   * trace(users); // Bob:3,barb:35,abcd:3,catchy:4
   *
   * users.sortOn("name");
   * trace(users); // Bob:3,abcd:3,barb:35,catchy:4
   *
   * users.sortOn("name", Array.CASEINSENSITIVE);
   * trace(users); // abcd:3,barb:35,Bob:3,catchy:4
   *
   * users.sortOn("name", Array.CASEINSENSITIVE | Array.DESCENDING);
   * trace(users); // catchy:4,Bob:3,barb:35,abcd:3
   *
   * users.sortOn("age");
   * trace(users); // abcd:3,Bob:3,barb:35,catchy:4
   *
   * users.sortOn("age", Array.NUMERIC);
   * trace(users); // Bob:3,abcd:3,catchy:4,barb:35
   *
   * users.sortOn("age", Array.DESCENDING | Array.NUMERIC);
   * trace(users); // barb:35,catchy:4,Bob:3,abcd:3
   *
   * var indices:Array = users.sortOn("age", Array.NUMERIC | Array.RETURNINDEXEDARRAY);
   * var index:uint;
   * for(var i:uint = 0; i < indices.length; i++) {
   *     index = indices[i];
   *     trace(users[index].name, ": " + users[index].age);
   * }
   *
   * // Results:
   * // Bob : 3
   * // abcd : 3
   * // catchy : 4
   * // barb : 35
   * </listing></div>
   */
  public native function sortOn(fieldName:*, options:* = 0, ...rest):*; // TODO: avoid Array prototype pollution, but implement... how?

  /**
   * Adds elements to and removes elements from an array. This method modifies the array without making a copy.
   * <p><b>Note:</b> To override this method in a subclass of Array, use <code>...args</code> for the parameters, as this example shows:</p>
   * <pre>
   * public override function splice(...args) {
   *     // your statements here
   * }
   * </pre>
   * @param args the following parameters:
   * <ul>
   *   <li><code>startIndex</code> &mdash; An integer that specifies the index of the element in the array where the insertion or deletion begins. You can use a negative integer to specify a position relative to the end of the array (for example, -1 is the last element of the array)./<li>
   *   <li><code>deleteCount</code> &mdash; An integer that specifies the number of elements to be deleted. This number includes the element specified in the <code>startIndex</code> parameter. If you do not specify a value for the <code>deleteCount</code> parameter, the method deletes all of the values from the <code>startIndex</code> element to the last element in the array. If the value is 0, no elements are deleted.
   * <li><code>values</code> &mdash; one or more comma-separated values to insert into the array at the position specified in the <code>startIndex</code> parameter. If an inserted value is of type Array, the array is kept intact and inserted as a single element. For example, if you splice an existing array of length three with another array of length three, the resulting array will have only four elements. One of the elements, however, will be an array of length three.
   *
   * @return An array containing the elements that were removed from the original array.
   *
   * @example The following code creates the Array object <code>vegetables</code> with the elements <code>[spinach, green pepper, cilantro, onion, avocado]</code>. The <code>splice()</code> method is then called with the parameters 2 and 2, which assigns <code>cilantro</code> and <code>onion</code> to the <code>spliced</code> array. The <code>vegetables</code> array then contains <code>[spinach,green pepper,avocado]</code>. The <code>splice()</code> method is called a second time using the parameters 1, 0, and the <code>spliced</code> array to assign <code>[cilantro,onion]</code> as the second element in <code>vegetables</code>.
   * <listing>
   * var vegetables:Array = new Array("spinach",
   *                  "green pepper",
   *                  "cilantro",
   *                  "onion",
   *                  "avocado");
   *
   * var spliced:Array = vegetables.splice(2, 2);
   * trace(vegetables); // spinach,green pepper,avocado
   * trace(spliced);    // cilantro,onion
   *
   * vegetables.splice(1, 0, spliced);
   * trace(vegetables); // spinach,cilantro,onion,green pepper,avocado
   *
   * </listing>
   * <div>Notice that <code>cilantro</code> and <code>onion</code> trace out as if <code>vegetables</code> has 5 elements, even though it actually has four (and the second element is another array containing two elements). To add <code>cilantro</code> and <code>onion</code> individually, you would use:
   * <listing>
   *
   * var vegetables:Array = new Array("spinach",
   *                  "green pepper",
   *                  "cilantro",
   *                  "onion",
   *                  "avocado");
   *
   *  var spliced:Array = vegetables.splice(2, 2);
   *  trace(vegetables); // spinach,green pepper,avocado
   *  trace(spliced);    // cilantro,onion
   *
   *  vegetables.splice(1, 0, "cilantro", "onion");
   *  trace(vegetables); // spinach,cilantro,onion,green pepper,avocado
   * </listing></div>
   */
  public native function splice(...args):Array;

  /**
   * Returns a string that represents the elements in the specified array. Every element in the array, starting with index 0 and ending with the highest index, is converted to a concatenated string and separated by commas. In the ActionScript 3.0 implementation, this method returns the same value as the <code>Array.toString()</code> method.
   * @return A string of array elements.
   *
   * @see #toString()
   *
   */
  public native function toLocaleString():String;

  /**
   * Returns a string that represents the elements in the specified array. Every element in the array, starting with index 0 and ending with the highest index, is converted to a concatenated string and separated by commas. To specify a custom separator, use the <code>Array.join()</code> method.
   * @return A string of array elements.
   *
   * @see String#split()
   * @see #join()
   *
   * @example The following code creates an Array, converts the values to strings, and stores them in the <code>vegnums</code> variable of the String data type.
   * <listing>
   * var vegetables:Array = new Array();
   * vegetables.push(1);
   * vegetables.push(2);
   * vegetables.push(3);
   * vegetables.push(4);
   * vegetables.push(5);
   * var vegnums:String = vegetables.toString();
   * trace(vegnums+",6");
   * // 1,2,3,4,5,6
   * </listing>
   */
  public native function toString():String;

  /**
   * Adds one or more elements to the beginning of an array and returns the new length of the array. The other elements in the array are moved from their original position, i, to i+1.
   * @param args One or more numbers, elements, or variables to be inserted at the beginning of the array.
   *
   * @return An integer representing the new length of the array.
   *
   * @see #pop()
   * @see #push()
   * @see #shift()
   *
   * @example The following code creates the empty Array object <code>names</code>. The strings <code>Bill</code> and <code>Jeff</code> are added by the <code>push()</code> method, and then the strings <code>Alfred</code> and <code>Kyle</code> are added to the beginning of <code>names</code> by two calls to the <code>unshift()</code> method.
   * <listing>
   * var names:Array = new Array();
   * names.push("Bill");
   * names.push("Jeff");
   *
   * trace(names); // Bill,Jeff
   *
   * names.unshift("Alfred");
   * names.unshift("Kyle");
   *
   * trace(names); // Kyle,Alfred,Bill,Jeff
   * </listing>
   */
  public native function unshift(... args):uint;
  /**
   * Specifies case-insensitive sorting for the Array class sorting methods. You can use this constant for the <code>options</code> parameter in the <code>sort()</code> or <code>sortOn()</code> method.
   * <p>The value of this constant is 1.</p>
   * @see #sort()
   * @see #sortOn()
   *
   */
  public static const CASEINSENSITIVE:uint = 1;
  /**
   * Specifies descending sorting for the Array class sorting methods. You can use this constant for the <code>options</code> parameter in the <code>sort()</code> or <code>sortOn()</code> method.
   * <p>The value of this constant is 2.</p>
   * @see #sort()
   * @see #sortOn()
   *
   */
  public static const DESCENDING:uint = 2;
  /**
   * Specifies numeric (instead of character-string) sorting for the Array class sorting methods. Including this constant in the <code>options</code> parameter causes the <code>sort()</code> and <code>sortOn()</code> methods to sort numbers as numeric values, not as strings of numeric characters. Without the <code>NUMERIC</code> constant, sorting treats each array element as a character string and produces the results in Unicode order.
   * <p>For example, given the array of values <code>[2005, 7, 35]</code>, if the <code>NUMERIC</code> constant is <b>not</b> included in the <code>options</code> parameter, the sorted array is <code>[2005, 35, 7]</code>, but if the <code>NUMERIC</code> constant <b>is</b> included, the sorted array is <code>[7, 35, 2005]</code>.</p>
   * <p>This constant applies only to numbers in the array; it does not apply to strings that contain numeric data such as <code>["23", "5"]</code>.</p>
   * <p>The value of this constant is 16.</p>
   * @see #sort()
   * @see #sortOn()
   *
   */
  public static const NUMERIC:uint = 16;
  /**
   * Specifies that a sort returns an array that consists of array indices. You can use this constant for the <code>options</code> parameter in the <code>sort()</code> or <code>sortOn()</code> method, so you have access to multiple views of the array elements while the original array is unmodified.
   * <p>The value of this constant is 8.</p>
   * @see #sort()
   * @see #sortOn()
   *
   */
  public static const RETURNINDEXEDARRAY:uint = 8;
  /**
   * Specifies the unique sorting requirement for the Array class sorting methods. You can use this constant for the <code>options</code> parameter in the <code>sort()</code> or <code>sortOn()</code> method. The unique sorting option terminates the sort if any two elements or fields being sorted have identical values.
   * <p>The value of this constant is 4.</p>
   * @see #sort()
   * @see #sortOn()
   *
   */
  public static const UNIQUESORT:uint = 4;
}
}
