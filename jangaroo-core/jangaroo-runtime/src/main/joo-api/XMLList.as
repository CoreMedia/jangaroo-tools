package {

/**
 * The XMLList class contains methods for working with one or more XML elements. An XMLList object can represent one or more XML objects or elements (including multiple nodes or attributes), so you can call methods on the elements as a group or on the individual elements in the collection.
 * <p>If an XMLList object has only one XML element, you can use the XML class methods on the XMLList object directly. In the following example, <code>example.two</code> is an XMLList object of length 1, so you can call any XML method on it.</p>
 * <listing>
 *  var example2 = &lt;example>&lt;two>2&lt;/two>&lt;/example>;
 * </listing>
 * <p>If you attempt to use XML class methods with an XMLList object containing more than one XML object, an exception is thrown; instead, iterate over the XMLList collection (using a <code>for each..in</code> statement, for example) and apply the methods to each XML object in the collection.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./XMLList.html#includeExamplesSummary">View the examples</a></p>
 * @see XML
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#for_each..in for each..in
 * @see Namespace
 * @see QName
 *
 */
public final dynamic class XMLList {
  /**
   * Creates a new XMLList object.
   * @param value Any object that can be converted to an XMLList object by using the top-level <code>XMLList()</code> function.
   *
   * @see #XMLList()
   *
   */
  public native function XMLList(value:Object = null);

  /**
   * Calls the <code>attribute()</code> method of each XML object and returns an XMLList object of the results. The results match the given <code>attributeName</code> parameter. If there is no match, the <code>attribute()</code> method returns an empty XMLList object.
   * @param attributeName The name of the attribute that you want to include in an XMLList object.
   *
   * @return An XMLList object of matching XML objects or an empty XMLList object.
   *
   * @see XML#attribute()
   * @see XML#attributes()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function attribute(attributeName:*):XMLList;

  /**
   * Calls the <code>attributes()</code> method of each XML object and returns an XMLList object of attributes for each XML object.
   * @return An XMLList object of attributes for each XML object.
   *
   * @see XML#attribute()
   * @see XML#attributes()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function attributes():XMLList;

  /**
   * Calls the <code>child()</code> method of each XML object and returns an XMLList object that contains the results in order.
   * @param propertyName The element name or integer of the XML child.
   *
   * @return An XMLList object of child nodes that match the input parameter.
   *
   * @see XML#child()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function child(propertyName:Object):XMLList;

  /**
   * Calls the <code>children()</code> method of each XML object and returns an XMLList object that contains the results.
   * @return An XMLList object of the children in the XML objects.
   *
   * @see XML#children()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function children():XMLList;

  /**
   * Calls the <code>comments()</code> method of each XML object and returns an XMLList of comments.
   * @return An XMLList of the comments in the XML objects.
   *
   * @see XML#comments()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function comments():XMLList;

  /**
   * Checks whether the XMLList object contains an XML object that is equal to the given <code>value</code> parameter.
   * @param value An XML object to compare against the current XMLList object.
   *
   * @return If the XMLList contains the XML object declared in the <code>value</code> parameter, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function contains(value:XML):Boolean;

  /**
   * Returns a copy of the given XMLList object. The copy is a duplicate of the entire tree of nodes. The copied XML object has no parent and returns <code>null</code> if you attempt to call the <code>parent()</code> method.
   * @return The copy of the XMLList object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function copy():XMLList;

  /**
   * Returns all descendants (children, grandchildren, great-grandchildren, and so on) of the XML object that have the given <code>name</code> parameter. The <code>name</code> parameter can be a QName object, a String data type, or any other data type that is then converted to a String data type.
   * <p>To return all descendants, use the asterisk (*) parameter. If no parameter is passed, the string "*" is passed and returns all descendants of the XML object.</p>
   * @param name The name of the element to match.
   *
   * @return An XMLList object of the matching descendants (children, grandchildren, and so on) of the XML objects in the original list. If there are no descendants, returns an empty XMLList object.
   *
   * @see XML#descendants()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function descendants(name:Object = "*"):XMLList;

  /**
   * Calls the <code>elements()</code> method of each XML object. The <code>name</code> parameter is passed to the <code>descendants()</code> method. If no parameter is passed, the string "*" is passed to the <code>descendants()</code> method.
   * @param name The name of the elements to match.
   *
   * @return An XMLList object of the matching child elements of the XML objects.
   *
   * @see XML#elements()
   *
   */
  public native function elements(name:Object = "*"):XMLList;

  /**
   * Checks whether the XMLList object contains complex content. An XMLList object is considered to contain complex content if it is not empty and either of the following conditions is true:
   * <ul>
   * <li>The XMLList object contains a single XML item with complex content.</li>
   * <li>The XMLList object contains elements.</li></ul>
   * @return If the XMLList object contains complex content, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see #hasSimpleContent()
   * @see XML#hasComplexContent()
   * @see XML#hasSimpleContent()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function hasComplexContent():Boolean;

  /**
   * Checks for the property specified by <code>p</code>.
   * @param p The property to match.
   *
   * @return If the parameter exists, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function hasOwnProperty(p:String):Boolean;

  /**
   * Checks whether the XMLList object contains simple content. An XMLList object is considered to contain simple content if one or more of the following conditions is true:
   * <ul>
   * <li>The XMLList object is empty</li>
   * <li>The XMLList object contains a single XML item with simple content</li>
   * <li>The XMLList object contains no elements</li></ul>
   * @return If the XMLList contains simple content, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see #hasComplexContent()
   * @see XML#hasComplexContent()
   * @see XML#hasSimpleContent()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function hasSimpleContent():Boolean;

  /**
   * Returns the number of properties in the XMLList object.
   * @return The number of properties in the XMLList object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function length():int;

  /**
   * Merges adjacent text nodes and eliminates empty text nodes for each of the following: all text nodes in the XMLList, all the XML objects contained in the XMLList, and the descendants of all the XML objects in the XMLList.
   * @return The normalized XMLList object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function normalize():XMLList;

  /**
   * Returns the parent of the XMLList object if all items in the XMLList object have the same parent. If the XMLList object has no parent or different parents, the method returns <code>undefined</code>.
   * @return Returns the parent XML object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function parent():Object;

  /**
   * If a <code>name</code> parameter is provided, lists all the children of the XMLList object that contain processing instructions with that name. With no parameters, the method lists all the children of the XMLList object that contain any processing instructions.
   * @param name The name of the processing instructions to match.
   *
   * @return An XMLList object that contains the processing instructions for each XML object.
   *
   * @see XML#processingInstructions()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function processingInstructions(name:String = "*"):XMLList;

  /**
   * Checks whether the property <code>p</code> is in the set of properties that can be iterated in a <code>for..in</code> statement applied to the XMLList object. This is <code>true</code> only if <code>toNumber(p)</code> is greater than or equal to 0 and less than the length of the XMLList object.
   * @param p The index of a property to check.
   *
   * @return If the property can be iterated in a <code>for..in</code> statement, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function propertyIsEnumerable(p:String):Boolean;

  /**
   * Calls the <code>text()</code> method of each XML object and returns an XMLList object that contains the results.
   * @return An XMLList object of all XML properties of the XMLList object that represent XML text nodes.
   *
   * @see XML#text()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function text():XMLList;

  /**
   * Returns a string representation of all the XML objects in an XMLList object. The rules for this conversion depend on whether the XML object has simple content or complex content:
   * <ul>
   * <li>If the XML object has simple content, <code>toString()</code> returns the string contents of the XML object with the following stripped out: the start tag, attributes, namespace declarations, and end tag.</li></ul>
   * <ul>
   * <li>If the XML object has complex content, <code>toString()</code> returns an XML encoded string representing the entire XML object, including the start tag, attributes, namespace declarations, and end tag.</li></ul>
   * <p>To return the entire XML object every time, use the <code>toXMLString()</code> method.</p>
   * @return The string representation of the XML object.
   *
   * @see #hasComplexContent()
   * @see #hasSimpleContent()
   * @see #toXMLString()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6d.html XML type conversion
   *
   * @example The following example shows what the <code>toString()</code> method returns when the XML object has simple content:
   * <listing>
   * var test:XML = <type name="Joe">example</type>;
   * trace(test.toString()); //example
   * </listing>
   * <div>The following example shows what the <code>toString()</code> method returns when the XML object has complex content:
   * <listing>
   * var test:XML =
   * <type name="Joe">
   *     <base name="Bob"></base>
   *     example
   * </type>;
   * trace(test.toString());
   *   // <type name="Joe">
   *   // <base name="Bob"/>
   *   // example
   *   // </type>
   * </listing></div>
   */
  public native function toString():String;

  /**
   * Returns a string representation of all the XML objects in an XMLList object. Unlike the <code>toString()</code> method, the <code>toXMLString()</code> method always returns the start tag, attributes, and end tag of the XML object, regardless of whether the XML object has simple content or complex content. (The <code>toString()</code> method strips out these items for XML objects that contain simple content.)
   * @return The string representation of the XML object.
   *
   * @see #toString()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6d.html XML type conversion
   *
   */
  public native function toXMLString():String;

  /**
   * Returns the XMLList object.
   * @return Returns the current XMLList object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e70.html XMLList objects
   *
   */
  public native function valueOf():XMLList;
}
}
