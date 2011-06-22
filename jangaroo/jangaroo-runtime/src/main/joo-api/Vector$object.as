package {


/**
 * The Vector class lets you access and manipulate a vector - an array whose elements all have the same data type. The data type of a Vector's elements is known as the Vector's <i>base type</i>. The base type can be any class, including built in classes and custom classes. The base type is specified when declaring a Vector variable as well as when creating an instance by calling the class constructor.
 * <p>As with an Array, you can use the array access operator (<code>[]</code>) to set or retrieve the value of a Vector element. Several Vector methods also provide mechanisms for setting and retrieving element values. These include <code>push()</code>, <code>pop()</code>, <code>shift()</code>, <code>unshift()</code>, and others. The properties and methods of a Vector object are similar - in most cases identical - to the properties and methods of an Array. In most cases where you would use an Array in which all the elements have the same data type, a Vector instance is preferable. However, Vector instances are dense arrays, meaning it must have a value (or <code>null</code>) in each index. Array instances don't have this same restriction.</p>
 * <p>The Vector's base type is specified using postfix type parameter syntax. Type parameter syntax is a sequence consisting of a dot (<code>.</code>), left angle bracket (<code>&lt;</code>), class name, then a right angle bracket (<code>&gt;</code>), as shown in this example:</p>
 * <p>In the first line of the example, the variable <code>v</code> is declared as a Vector.&lt;String&gt; instance. In other words, it represents a Vector (an array) that can only hold String instances and from which only String instances can be retrieved. The second line constructs an instance of the same Vector type (that is, a Vector whose elements are all String objects) and assigns it to <code>v</code>.</p>
 * <listing>
 *  var v:Vector.&lt;String&gt;;
 *  v = new Vector.&lt;String&gt;();
 * </listing>
 * <p>A variable declared with the Vector.&lt;T&gt; data type can only store a Vector instance that is constructed with the same base type <code>T</code>. For example, a Vector that's constructed by calling <code>new Vector.&lt;String&gt;()</code> can't be assigned to a variable that's declared with the Vector.<int> data type. The base types must match exactly. For example, the following code doesn't compile because the object's base type isn't the same as the variable's declared base type (even though Sprite is a subclass of DisplayObject):</p>
 * <listing>
 *  // This code doesn't compile even though Sprite is a DisplayObject subclass
 *  var v:Vector.&lt;DisplayObject&gt; = new Vector.&lt;Sprite&gt;();
 * </listing>
 * <p>To convert a Vector with base type <code>T</code> to a Vector of a superclass of <code>T</code>, use the <code>Vector()</code> global function.</p>
 * <p>In addition to the data type restriction, the Vector class has other restrictions that distinguish it from the Array class:</p>
 * <ul>
 * <li>A Vector is a dense array. Unlike an Array, which may have values in indices 0 and 7 even if there are no values in positions 1 through 6, a Vector must have a value (or <code>null</code>) in each index.</li>
 * <li>A Vector can optionally be fixed-length, meaning the number of elements it contains can't change.</li>
 * <li>Access to a Vector's elements is bounds-checked. You can never read a value from an index greater than the final element (<code>length - 1</code>). You can never set a value with an index more than one beyond the current final index (in other words, you can only set a value at an existing index or at index <code>[length]</code>).</li></ul>
 * <p>As a result of its restrictions, a Vector has three primary benefits over an Array instance whose elements are all instances of a single class:</p>
 * <ul>
 * <li>Performance: array element access and iteration are much faster when using a Vector instance than they are when using an Array.</li>
 * <li>Type safety: in strict mode the compiler can identify data type errors. Examples of data type errors include assigning a value of the incorrect data type to a Vector or expecting the wrong data type when reading a value from a Vector. Note, however, that when using the <code>push()</code> method or <code>unshift()</code> method to add values to a Vector, the arguments' data types are not checked at compile time. Instead, they are checked at run time.</li>
 * <li>Reliability: runtime range checking (or fixed-length checking) increases reliability significantly over Arrays.</li></ul>
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#array_access
 * @see Vector()
 * @see Array
 *
 */
public final dynamic class Vector$object {

  /**
   * Indicates whether the <code>length</code> property of the Vector can be changed. If the value is <code>true</code>, the <code>length</code> property can't be changed. This means the following operations are not allowed when <code>fixed</code> is <code>true</code>:
   * <ul>
   * <li>setting the <code>length</code> property directly</li>
   * <li>assigning a value to index position <code>length</code></li>
   * <li>calling a method that changes the <code>length</code> property, including:
   * <ul>
   * <li><code>pop()</code></li>
   * <li><code>push()</code></li>
   * <li><code>shift()</code></li>
   * <li><code>unshift()</code></li>
   * <li><code>splice()</code> (if the <code>splice()</code> call changes the <code>length</code> of the Vector).</li></ul></li></ul>
   */
  public native function get fixed():Boolean;

  /**
   * @private
   */
  public native function set fixed(value:Boolean):void;

  /**
   * The range of valid indices available in the Vector. A Vector instance has index positions up to but not including the <code>length</code> value.
   * <p>Every Vector element always has a value that is either an instance of the base type or <code>null</code>. When the <code>length</code> property is set to a value that's larger than its previous value, additional elements are created and populated with the default value appropriate to the base type (<code>null</code> for reference types).</p>
   * <p>When the <code>length</code> property is set to a value that's smaller than its previous value, all the elements at index positions greater than or equal to the new <code>length</code> value are removed from the Vector.</p>
   * @throws RangeError If this property is changed while <code>fixed</code> is <code>true</code>.
   * @throws RangeError If this property is set to a value larger than the maximum allowable index (2<sup>32</sup>).
   *
   */
  public native function get length():uint;

  /**
   * @private
   */
  public native function set length(value:uint):void;

  /**
   * Creates a Vector with the specified base type.
   * <p>When calling the <code>Vector.&lt;T&gt;()</code> constructor, specify the base type using type parameter syntax. Type parameter syntax is a sequence consisting of a dot (<code>.</code>), left angle bracket (<code><</code>), class name, then a right angle bracket (<code>></code>), as shown in this example:</p>
   * <listing>
   *      var v:Vector.&lt;String&gt; = new Vector.&lt;String&gt;();
   *     </listing>
   * <p>To create a Vector instance from an Array or another Vector (such as one with a different base type), use the <code>Vector()</code> global function.</p>
   * <p>To create a pre-populated Vector instance, use the following syntax instead of using the parameters specified below:</p>
   * <listing>
   *      // var v:Vector.&lt;T&gt; = new &lt;T&gt;[E0, ..., En-1 ,];
   *      // For example:
   *      var v:Vector.&lt;int&gt; = new &lt;int&gt;[0,1,2,];
   *     </listing>
   * <p>The following information applies to this syntax:</p>
   * <ul>
   * <li>It is supported in Flash Professional CS5 and later, Flash Builder 4 and later, and Flex 4 and later.</li>
   * <li>The trailing comma is optional.</li>
   * <li>Empty items in the array are not supported; a statement such as <code>var v:Vector.&lt;int&gt; = new &lt;int&gt;[0,,2,]</code> throws a compiler error.</li>
   * <li>You can't specify a default length for the Vector instance. Instead, the length is the same as the number of elements in the initialization list.</li>
   * <li>You can't specify whether the Vector instance has a fixed length. Instead, use the <code>fixed</code> property.</li>
   * <li>Data loss or errors can occur if items passed as values don't match the specified type. For example:</li>
   * <li>
   * <listing>
   *      var v:Vector.&lt;int&gt; = new &lt;int&gt;[4.2]; // compiler error when running in strict mode
   *      trace(v[0]); //returns 4 when not running in strict mode
   *     </listing></li></ul>
   * @param length The initial length (number of elements) of the Vector. If this parameter is greater than zero, the specified number of Vector elements are created and populated with the default value appropriate to the base type (<code>null</code> for reference types).
   * @param fixed Whether the Vector's length is fixed (<code>true</code>) or can be changed (<code>false</code>). This value can also be set using the <code>fixed</code> property.
   *
   * @see #fixed
   * @see Vector()
   *
   */
  public native function Vector$object(length:uint = 0, fixed:Boolean = false);

  /**
   * Concatenates the elements specified in the parameters with the elements in the Vector and creates a new Vector. If the parameters specify a Vector, the elements of that Vector are concatenated. If you don't pass any parameters, the new Vector is a duplicate (shallow clone) of the original Vector.
   * @param args One or more values of the base type of this Vector to be concatenated in a new Vector.
   *
   * @return A Vector with the same base type as this Vector that contains the elements from this Vector followed by elements from the parameters.
   *
   * @throws TypeError If any argument is not an instance of the base type and can't be converted to the base type.
   *
   */
  public native function concat(... rest):Vector$object;

  /**
   * Executes a test function on each item in the Vector until an item is reached that returns <code>false</code> for the specified function. You use this method to determine whether all items in a Vector meet a criterion, such as having values less than a particular number.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. That is the most common way of using this method.</p>
   * <p>However, suppose you create a function on a frame on the main timeline using Flash Professional, but you want it to be called in a different <code>this</code> context:</p>
   * <pre>    function myFunction(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   // your code here
   }
   </pre>
   * <p>Suppose you then use the <code>every()</code> method on a Vector called <code>myVector</code>:</p>
   * <pre>    myVector.every(myFunction, someObject);
   </pre>
   * <p>Because <code>myFunction</code> is a member of the main class of the SWF file, it cannot be executed in a different <code>this</code> context. Flash runtimes throw an exception when this code runs. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>    var myFunction:Function = function(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   //your code here
   };
   myVector.every(myFunction, someObject);
   </pre>
   * @param callback The function to run on each item in the Vector. This function is invoked with three arguments: the current item from the Vector, the index of the item, and the Vector object:
   * <pre>function callback(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   // your code here
   }
   </pre>
   * <p>The callback function should return a Boolean value.</p>
   * @param thisObject The object that the identifer <code>this</code> in the callback function refers to when the function is called.
   *
   * @return A Boolean value of <code>true</code> if the specified function returns <code>true</code> when called on all items in the Vector; otherwise, <code>false</code>.
   *
   * @see #some()
   *
   */
  public native function every(checker:Function, thisObj:Object = null):Boolean;

  /**
   * Executes a test function on each item in the Vector and returns a new Vector containing all items that return <code>true</code> for the specified function. If an item returns <code>false</code>, it is not included in the result Vector. The base type of the return Vector matches the base type of the Vector on which the method is called.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. That is the most common way of using this method.</p>
   * <p>However, suppose you create a function on a frame on the main timeline using Flash Professional, but you want it to be called in a different <code>this</code> context:</p>
   * <pre>     function myFunction(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   // your code here
   }
   </pre>
   * <p>Suppose you then use the <code>filter()</code> method on a Vector called <code>myVector</code>:</p>
   * <pre>     var result:Vector.&lt;T&gt; = myVector.filter(myFunction, someObject);
   </pre>
   * <p>Because <code>myFunction</code> is a member of the main class of the SWF file, it cannot be executed in a different <code>this</code> context. Flash runtimes throw an exception when this code runs. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>     var myFunction:Function = function(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   //your code here
   };
   myVector.filter(myFunction, someObject);
   </pre>
   * @param callback The function to run on each item in the Vector. This function is invoked with three arguments: the current item from the Vector, the index of the item, and the Vector object:
   * <pre>function callback(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean;</pre>
   * @param thisObject The object that the identifer <code>this</code> in the callback function refers to when the function is called.
   *
   * @return A new Vector that contains all items from the original Vector for which the <code>callback</code> function returned <code>true</code>.
   *
   * @see #map()
   *
   */
  public native function filter(callback:Function, thisObject:Object = null):Vector$object;

  /**
   * Executes a function on each item in the Vector.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. That is the most common way of using this method.</p>
   * <p>However, suppose you create a function on a frame on the main timeline using Flash Professional, but you want it to be called in a different <code>this</code> context:</p>
   * <pre>     function myFunction(item:T, index:int, vector:Vector.&lt;T&gt;):void {
   // your code here
   }
   </pre>
   * <p>Suppose you then use the <code>forEach()</code> method on a Vector called <code>myVector</code>:</p>
   * <pre>     myVector.forEach(myFunction, someObject);
   </pre>
   * <p>Because <code>myFunction</code> is a member of the main class of the SWF file, it cannot be executed in a different <code>this</code> context. Flash runtimes throw an exception when this code runs. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>     var myFunction:Function = function(item:T, index:int, vector:Vector.&lt;T&gt;):void {
   //your code here
   };
   myVector.forEach(myFunction, someObject);
   </pre>
   * @param callback The function to run on each item in the Vector. This function is invoked with three arguments: the current item from the Vector, the index of the item, and the Vector object:
   * <pre>function callback(item:T, index:int, vector:Vector.&lt;T&gt;):void;</pre>
   * <p>Any return value from the function call is discarded.</p>
   * @param thisObject The object that the identifer <code>this</code> in the callback function refers to when the function is called.
   *
   */
  public native function forEach(callback:Function, thisObject:Object = null):void;

  /**
   * Searches for an item in the Vector and returns the index position of the item. The item is compared to the Vector elements using strict equality (<code>===</code>).
   * @param searchElement The item to find in the Vector.
   * @param fromIndex The location in the Vector from which to start searching for the item. If this parameter is negative, it is treated as <code>length + fromIndex</code>, meaning the search starts <code>-fromIndex</code> items from the end and searches from that position forward to the end of the Vector.
   *
   * @return A zero-based index position of the item in the Vector. If the <code>searchElement</code> argument is not found, the return value is -1.
   *
   * @see #lastIndexOf()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#strict_equality
   *
   */
  public native function indexOf(searchElement:Object, fromIndex:int = 0):int;

  /**
   * Converts the elements in the Vector to strings, inserts the specified separator between the elements, concatenates them, and returns the resulting string. A nested Vector is always separated by a comma (,), not by the separator passed to the <code>join()</code> method.
   * @param sep A character or string that separates Vector elements in the returned string. If you omit this parameter, a comma is used as the default separator.
   *
   * @return A string consisting of the elements of the Vector converted to strings and separated by the specified string.
   *
   * @see String#split()
   *
   */
  public native function join(sep:String = ","):String;

  /**
   * Searches for an item in the Vector, working backward from the specified index position, and returns the index position of the matching item. The item is compared to the Vector elements using strict equality (<code>===</code>).
   * @param searchElement The item to find in the Vector.
   * @param fromIndex The location in the Vector from which to start searching for the item. The default is the maximum allowable index value, meaning that the search starts at the last item in the Vector.
   * <p>If this parameter is negative, it is treated as <code>length + fromIndex</code>, meaning the search starts <code>-fromIndex</code> items from the end and searches from that position backward to index 0.</p>
   *
   * @return A zero-based index position of the item in the Vector. If the <code>searchElement</code> argument is not found, the return value is -1.
   *
   * @see #indexOf()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#strict_equality
   *
   */
  public native function lastIndexOf(searchElement:Object, fromIndex:int = 0x7fffffff):int;

  /**
   * Executes a function on each item in the Vector, and returns a new Vector of items corresponding to the results of calling the function on each item in this Vector. The result Vector has the same base type and <code>length</code> as the original Vector. The element at index <code>i</code> in the result Vector is the result of the call on the element at index <code>i</code> in the original Vector.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. That is the most common way of using this method.</p>
   * <p>However, suppose you create a function on a frame on the main timeline, using Flash Professional but you want it to be called in a different <code>this</code> context:</p>
   * <pre>     function myFunction(item:Object, index:int, vector:Vector.&lt;T&gt;):T {
   // your code here
   }
   </pre>
   * <p>Suppose you then use the <code>map()</code> method on a Vector called <code>myVector</code>:</p>
   * <pre>     myVector.map(myFunction, someObject);
   </pre>
   * <p>Because <code>myFunction</code> is a member of the main class of the SWF file, it cannot be executed in a different <code>this</code> context. Flash runtimes throw an exception when this code runs. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>     var myFunction:Function = function(item:T, index:int, vector:Vector.&lt;T&gt;):void {
   //your code here
   };
   myVector.map(myFunction, someObject);
   </pre>
   * @param callback The function to run on each item in the Vector. This function is invoked with three arguments: the current item from the Vector, the index of the item, and the Vector object:
   * <pre>function callback(item:T, index:int, vector:Vector.&lt;T&gt;):T;</pre>
   * @param thisObject The object that the identifer <code>this</code> in the callback function refers to when the function is called.
   *
   * @return A new Vector that contains the results of calling the function on each item in this Vector. The result Vector has the same base type and <code>length</code> as the original.
   *
   * @see #filter()
   *
   */
  public native function map(callback:Function, thisObject:Object = null):Vector$object;

  /**
   * Removes the last element from the Vector and returns that element. The <code>length</code> property of the Vector is decreased by one when this function is called.
   * @return The value of the last element in the specified Vector.
   *
   * @throws RangeError If this method is called while <code>fixed</code> is <code>true</code>.
   *
   * @see #push()
   * @see #shift()
   * @see #unshift()
   *
   */
  public native function pop():Object;

  /**
   * Adds one or more elements to the end of the Vector and returns the new length of the Vector.
   * <p>Because this function can accept multiple arguments, the data type of the arguments is not checked at compile time even in strict mode. However, if an argument is passed that is not an instance of the base type, an exception occurs at run time.</p>
   * @param args One or more values to append to the Vector.
   *
   * @return The length of the Vector after the new elements are added.
   *
   * @throws TypeError If any argument is not an instance of the base type <code>T</code> of the Vector.
   * @throws RangeError If this method is called while <code>fixed</code> is <code>true</code>.
   *
   * @see #pop()
   * @see #shift()
   * @see #unshift()
   *
   */
  public native function push(...args):uint;

  /**
   * Reverses the order of the elements in the Vector. This method alters the Vector on which it is called.
   * @return The Vector with the elements in reverse order.
   *
   */
  public native function reverse():Vector$object;

  /**
   * Removes the first element from the Vector and returns that element. The remaining Vector elements are moved from their original position, i, to i - 1.
   * @return The first element in the Vector.
   *
   * @throws RangeError If <code>fixed</code> is <code>true</code>.
   *
   * @see #pop()
   * @see #push()
   * @see #unshift()
   *
   */
  public native function shift():Object;

  /**
   * Returns a new Vector that consists of a range of elements from the original Vector, without modifying the original Vector. The returned Vector includes the <code>startIndex</code> element and all elements up to, but not including, the <code>endIndex</code> element.
   * <p>If you don't pass any parameters, the new Vector is a duplicate (shallow clone) of the original Vector. If you pass a value of 0 for both parameters, a new, empty Vector is created of the same type as the original Vector.</p>
   * @param startIndex A number specifying the index of the starting point for the slice. If <code>startIndex</code> is a negative number, the starting point begins at the end of the Vector, where -1 is the last element.
   * @param endIndex A number specifying the index of the ending point for the slice. If you omit this parameter, the slice includes all elements from the starting point to the end of the Vector. If <code>endIndex</code> is a negative number, the ending point is specified from the end of the Vector, where -1 is the last element.
   *
   * @return a Vector that consists of a range of elements from the original Vector.
   *
   */
  public native function slice(startIndex:int = 0, endIndex:Number = 16777215):Vector$object;

  /**
   * Executes a test function on each item in the Vector until an item is reached that returns <code>true</code>. Use this method to determine whether any items in a Vector meet a criterion, such as having a value less than a particular number.
   * <p>For this method, the second parameter, <code>thisObject</code>, must be <code>null</code> if the first parameter, <code>callback</code>, is a method closure. That is the most common way of using this method.</p>
   * <p>However, suppose you create a function on a frame on the main timeline, but you want it to be called in a different <code>this</code> context:</p>
   * <pre>     function myFunction(item:Object, index:int, vector:Vector.&lt;T&gt;):Boolean {
   // your code here
   }
   </pre>
   * <p>Suppose you then use the <code>some()</code> method on a Vector called <code>myVector</code>:</p>
   * <pre>     myVector.some(myFunction, someObject);
   </pre>
   * <p>Because <code>myFunction</code> is a member of the main class of the SWF file, it cannot be executed in a different <code>this</code> context. Flash runtimes throw an exception when this code runs. You can avoid this runtime error by assigning the function to a variable, as follows:</p>
   * <pre>     var myFunction:Function = function(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean {
   //your code here
   };
   myVector.some(myFunction, someObject);
   </pre>
   * @param callback The function to run on each item in the Vector. This function is invoked with three arguments: the current item from the Vector, the index of the item, and the Vector object:
   * <pre>function callback(item:T, index:int, vector:Vector.&lt;T&gt;):Boolean</pre>
   * <p>The callback function should return a Boolean value.</p>
   * @param thisObject The object that the identifer <code>this</code> in the callback function refers to when the function is called.
   *
   * @return A Boolean value of <code>true</code> if any items in the Vector return <code>true</code> for the specified function; otherwise, <code>false</code>.
   *
   * @see #every()
   *
   */
  public native function some(callback:Function, thisObject:Object = null):Boolean;

  /**
   * Sorts the elements in the Vector. This method sorts according to the function provided as the <code>compareFunction</code> parameter.
   * @param compareFunction A comparison method that determines the behavior of the sort.
   * <p>The specified method must take two arguments of the base type (<code>T</code>) of the Vector and return a Number:</p>
   * <listing>
   * function compare(x:T, y:T):Number {}</listing>
   * <p>The logic of the <code>compareFunction</code> function is that, given two elements <code>x</code> and <code>y</code>, the function returns one of the following three values:</p>
   * <ul>
   * <li>a negative number, if <code>x</code> should appear before <code>y</code> in the sorted sequence</li>
   * <li>0, if <code>x</code> equals <code>y</code></li>
   * <li>a positive number, if <code>x</code> should appear after <code>y</code> in the sorted sequence</li></ul>
   *
   * @return This Vector, with elements in the new order.
   *
   */
  public native function sort(compareFunction:Function):Vector$object;

  /**
   * Adds elements to and removes elements from the Vector. This method modifies the Vector without making a copy.
   * <p><b>Note:</b> To override this method in a subclass of Vector, use <code>...args</code> for the parameters, as this example shows:</p>
   * <pre>     public override function splice(...args) {
   // your statements here
   }
   </pre>
   * @param startIndex An integer that specifies the index of the element in the Vector where the insertion or deletion begins. You can use a negative integer to specify a position relative to the end of the Vector (for example, -1 for the last element of the Vector).
   * @param deleteCount An integer that specifies the number of elements to be deleted. This number includes the element specified in the <code>startIndex</code> parameter. If you do not specify a value for the <code>deleteCount</code> parameter, the method deletes all of the values from the <code>startIndex</code> element to the last element in the Vector. (The default value is <code>uint.MAX_VALUE</code>.) If the value is 0, no elements are deleted.
   * @param items An optional list of one or more comma-separated values to insert into the Vector at the position specified in the <code>startIndex</code> parameter.
   *
   * @return a Vector containing the elements that were removed from the original Vector.
   *
   * @throws RangeError If the <code>startIndex</code> and <code>deleteCount</code> arguments specify an index to be deleted that's outside the Vector's bounds.
   * @throws RangeError If this method is called while <code>fixed</code> is <code>true</code> and the <code>splice()</code> operation changes the <code>length</code> of the Vector.
   *
   */
  public native function splice(startIndex:int, deleteCount:uint = 4294967295, ...items):Vector$object;

  /**
   * Returns a string that represents the elements in the specified Vector. Every element in the Vector, starting with index 0 and ending with the highest index, is converted to a concatenated string and separated by commas. In the ActionScript 3.0 implementation, this method returns the same value as the <code>Vector.toString()</code> method.
   * @return A string of Vector elements.
   *
   * @see #toString()
   *
   */
  public native function toLocaleString():String;

  /**
   * Returns a string that represents the elements in the Vector. Every element in the Vector, starting with index 0 and ending with the highest index, is converted to a concatenated string and separated by commas. To specify a custom separator, use the <code>Vector.join()</code> method.
   * @return A string of Vector elements.
   *
   * @see String#split()
   * @see #join()
   *
   */
  public native function toString():String;

  /**
   * Adds one or more elements to the beginning of the Vector and returns the new length of the Vector. The other elements in the Vector are moved from their original position, i, to i + the number of new elements.
   * <p>Because this function can accept multiple arguments, the data type of the arguments is not checked at compile time even in strict mode. However, if an argument is passed that is not an instance of the base type, an exception occurs at run time.</p>
   * @param args One or more instances of the base type of the Vector to be inserted at the beginning of the Vector.
   *
   * @return An integer representing the new length of the Vector.
   *
   * @throws TypeError If any argument is not an instance of the base type <code>T</code> of the Vector.
   * @throws RangeError If this method is called while <code>fixed</code> is <code>true</code>.
   *
   * @see #pop()
   * @see #push()
   * @see #shift()
   *
   */
  public native function unshift(...args):uint;
}
}