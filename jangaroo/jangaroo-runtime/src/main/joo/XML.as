package {


/**
 * The XML class contains methods and properties for working with XML objects. The XML class (along with the XMLList, Namespace, and QName classes) implements the powerful XML-handling standards defined in ECMAScript for XML (E4X) specification (ECMA-357 edition 2).
 * <p>Use the <code>toXMLString()</code> method to return a string representation of the XML object regardless of whether the XML object has simple content or complex content.</p>
 * <p><b>Note</b>: The XML class (along with related classes) from ActionScript 2.0 has been renamed XMLDocument and moved into the flash.xml package. It is included in ActionScript 3.0 for backward compatibility.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./XML.html#includeExamplesSummary">View the examples</a></p>
 * @see Namespace
 * @see QName
 * @see XMLList
 * @see #toXMLString()
 * @see http://www.ecma-international.org/publications/standards/Ecma-357.htm ECMAScript for XML (E4X) specification (ECMA-357 edition 2)
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
 *
 */
[Native]
public final dynamic class XML {
  /**
   * Determines whether XML comments are ignored when XML objects parse the source XML data. By default, the comments are ignored (<code>true</code>). To include XML comments, set this property to <code>false</code>. The <code>ignoreComments</code> property is used only during the XML parsing, not during the call to any method such as <code>myXMLObject.child(*).toXMLString()</code>. If the source XML includes comment nodes, they are kept or discarded during the XML parsing.
   * @see #child()
   * @see #toXMLString()
   *
   * @example This example shows the effect of setting <code>XML.ignoreComments</code> to <code>false</code> and to <code>true</code>:
   * <listing>
   * XML.ignoreComments = false;
   * var xml1:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *         &lt;/foo>;
   * trace(xml1.toXMLString()); // &lt;foo>&lt;!-- comment -->&lt;/foo>
   *
   * XML.ignoreComments = true;
   * var xml2:XML =
   *         &lt;foo>
   *             &lt;!-- example -->
   *         &lt;/foo>;
   * trace(xml2.toXMLString()); // &lt;foo/>
   * </listing>
   */
  public static function get ignoreComments():Boolean {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * @private
   */
  public static function set ignoreComments(value:Boolean):void {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Determines whether XML processing instructions are ignored when XML objects parse the source XML data. By default, the processing instructions are ignored (<code>true</code>). To include XML processing instructions, set this property to <code>false</code>. The <code>ignoreProcessingInstructions</code> property is used only during the XML parsing, not during the call to any method such as <code>myXMLObject.child(*).toXMLString()</code>. If the source XML includes processing instructions nodes, they are kept or discarded during the XML parsing.
   * @see #child()
   * @see #toXMLString()
   *
   * @example This example shows the effect of setting <code>XML.ignoreProcessingInstructions</code> to <code>false</code> and to <code>true</code>:
   * <listing>
   * XML.ignoreProcessingInstructions = false;
   * var xml1:XML =
   *         &lt;foo>
   *             &lt;?exampleInstruction ?>
   *         &lt;/foo>;
   * trace(xml1.toXMLString()); // &lt;foo>&lt;?exampleInstruction ?>&lt;/foo>
   *
   * XML.ignoreProcessingInstructions = true;
   * var xml2:XML =
   *         &lt;foo>
   *             &lt;?exampleInstruction ?>
   *         &lt;/foo>;
   * trace(xml2.toXMLString()); // &lt;foo/>
   * </listing>
   */
  public static function get ignoreProcessingInstructions():Boolean {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * @private
   */
  public static function set ignoreProcessingInstructions(value:Boolean):void {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Determines whether white space characters at the beginning and end of text nodes are ignored during parsing. By default, white space is ignored (<code>true</code>). If a text node is 100% white space and the <code>ignoreWhitespace</code> property is set to <code>true</code>, then the node is not created. To show white space in a text node, set the <code>ignoreWhitespace</code> property to <code>false</code>.
   * <p>When you create an XML object, it caches the current value of the <code>ignoreWhitespace</code> property. Changing the <code>ignoreWhitespace</code> does not change the behavior of existing XML objects.</p>
   * @example This example shows the effect of setting <code>XML.ignoreWhitespace</code> to <code>false</code> and to <code>true</code>:
   * <listing>
   * XML.ignoreWhitespace = false;
   * var xml1:XML = &lt;foo>    &lt;/foo>;
   * trace(xml1.children().length()); // 1
   *
   * XML.ignoreWhitespace = true;
   * var xml2:XML = &lt;foo>    &lt;/foo>;
   * trace(xml2.children().length()); // 0
   * </listing>
   */
  public static function get ignoreWhitespace():Boolean {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * @private
   */
  public static function set ignoreWhitespace(value:Boolean):void {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Determines the amount of indentation applied by the <code>toString()</code> and <code>toXMLString()</code> methods when the <code>XML.prettyPrinting</code> property is set to <code>true</code>. Indentation is applied with the space character, not the tab character. The default value is <code>2</code>.
   * @see #prettyPrinting
   * @see #toString()
   * @see #toXMLString()
   *
   * @example This example shows the effect of setting the <code>XML.prettyIndent</code> static property:
   * <listing>
   * var xml:XML = &lt;foo>&lt;bar/>&lt;/foo>;
   * XML.prettyIndent = 0;
   * trace(xml.toXMLString());
   *
   * XML.prettyIndent = 1;
   * trace(xml.toXMLString());
   *
   * XML.prettyIndent = 2;
   * trace(xml.toXMLString());
   * </listing>
   */
  public static function get prettyIndent():int {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * @private
   */
  public static function set prettyIndent(value:int):void {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Determines whether the <code>toString()</code> and <code>toXMLString()</code> methods normalize white space characters between some tags. The default value is <code>true</code>.
   * @see #prettyIndent
   * @see #toString()
   * @see #toXMLString()
   *
   * @example This example shows the effect of setting <code>XML.prettyPrinting</code> static property:
   * <listing>
   * var xml:XML = &lt;foo>&lt;bar/>&lt;/foo>;
   * XML.prettyPrinting = false;
   * trace(xml.toXMLString());
   *
   * XML.prettyPrinting = true;
   * trace(xml.toXMLString());
   * </listing>
   */
  public static function get prettyPrinting():Boolean {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * @private
   */
  public static function set prettyPrinting(value:Boolean):void {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Creates a new XML object. You must use the constructor to create an XML object before you call any of the methods of the XML class.
   * <p>Use the <code>toXMLString()</code> method to return a string representation of the XML object regardless of whether the XML object has simple content or complex content.</p>
   * @param value Any object that can be converted to XML with the top-level <code>XML()</code> function.
   *
   * @see #XML()
   * @see #toXMLString()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows how you can load a remote XML document in ActionScript 3.0 using the URLLoader class in Flash Professional. Example provided by <a href="http://actionscriptexamples.com/2008/12/05/dynamically-loading-xml-files-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * //
   * // Requires:
   * //   - TextArea control UI component in the Flash Professional Library.
   * //
   * import fl.controls.TextArea;
   *
   * var xml:XML;
   *
   * var urlRequest:URLRequest = new URLRequest("http://www.helpexamples.com/flash/xml/menu.xml");
   *
   * var urlLoader:URLLoader = new URLLoader();
   * urlLoader.addEventListener(Event.COMPLETE, urlLoader_complete);
   * urlLoader.load(urlRequest);
   *
   * var textArea:TextArea = new TextArea();
   * textArea.move(5, 5);
   * textArea.setSize(stage.stageWidth - 10, stage.stageHeight - 10);
   * addChild(textArea);
   *
   * function urlLoader_complete(evt:Event):void {
   *     xml = new XML(evt.currentTarget.data);
   *     textArea.text = xml.toXMLString();
   * }
   * </listing>
   * <div>Here's another variation using all ActionScript. Example provided by <a href="http://actionscriptexamples.com/2008/12/05/dynamically-loading-xml-files-in-actionscript-30/">ActionScriptExamples.com</a>.
   * <listing>
   * var xml:XML;
   * var textArea:TextField = new TextField();
   * textArea.width = 300;
   *
   * var urlRequest:URLRequest = new URLRequest("http://www.helpexamples.com/flash/xml/menu.xml");
   * var urlLoader:URLLoader = new URLLoader();
   * urlLoader.dataFormat = URLLoaderDataFormat.TEXT;
   * urlLoader.addEventListener(Event.COMPLETE, urlLoader_complete);
   * urlLoader.load(urlRequest);
   *
   * function urlLoader_complete(evt:Event):void {
   *     xml = new XML(evt.target.data);
   *     textArea.text = xml.toXMLString();
   *     addChild(textArea);
   * }
   * </listing></div>
   */
  public function XML(value:Object) {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * Adds a namespace to the set of in-scope namespaces for the XML object. If the namespace already exists in the in-scope namespaces for the XML object (with a prefix matching that of the given parameter), then the prefix of the existing namespace is set to <code>undefined</code>. If the input parameter is a Namespace object, it's used directly. If it's a QName object, the input parameter's URI is used to create a new namespace; otherwise, it's converted to a String and a namespace is created from the String.
   * @param ns The namespace to add to the XML object.
   *
   * @return The new XML object, with the namespace added.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example uses a namespace defined in one XML object and applies it to another XML object:
   * <listing>
   * var xml1:XML = &lt;ns:foo xmlns:ns="www.example.com/ns" />;
   * var nsNamespace:Namespace = xml1.namespace();
   *
   * var xml2:XML = &lt;bar />;
   * xml2.addNamespace(nsNamespace);
   * trace(xml2.toXMLString()); // &lt;bar xmlns:ns="www.example.com/ns"/>
   * </listing>
   */
  public native function addNamespace(ns:Object):XML;

  /**
   * Appends the given child to the end of the XML object's properties. The <code>appendChild()</code> method takes an XML object, an XMLList object, or any other data type that is then converted to a String.
   * <p>Use the <code>delete</code> (XML) operator to remove XML nodes.</p>
   * @param child The XML object to append.
   *
   * @return The resulting XML object.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#delete_(XML)
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e72.html The E4X approach to XML processing
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e68.html Assembling and transforming XML objects
   *
   * @example This example appends a new element to the end of the child list of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;body>
   *             &lt;p>hello&lt;/p>
   *         &lt;/body>;
   *
   * xml.appendChild(&lt;p>world&lt;/p>);
   * trace(xml.p[0].toXMLString()); // &lt;p>hello&lt;/p>
   * trace(xml.p[1].toXMLString()); // &lt;p>world&lt;/p>
   * </listing>
   */
  public native function appendChild(child:Object):XML;

  /**
   * Returns the XML value of the attribute that has the name matching the <code>attributeName</code> parameter. Attributes are found within XML elements. In the following example, the element has an attribute named "<code>gender</code>" with the value "<code>boy</code>": <code><first gender="boy">John</first></code>.
   * <p>The <code>attributeName</code> parameter can be any data type; however, String is the most common data type to use. When passing any object other than a QName object, the <code>attributeName</code> parameter uses the <code>toString()</code> method to convert the parameter to a string.</p>
   * <p>If you need a qualified name reference, you can pass in a QName object. A QName object defines a namespace and the local name, which you can use to define the qualified name of an attribute. Therefore calling <code>attribute(qname)</code> is not the same as calling <code>attribute(qname.toString())</code>.</p>
   * @param attributeName The name of the attribute.
   *
   * @return An XMLList object or an empty XMLList object. Returns an empty XMLList object when an attribute value has not been defined.
   *
   * @see #attributes()
   * @see QName
   * @see Namespace
   * @see #elements()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#attribute_identifier
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6b.html Traversing XML structures
   *
   * @example This example shows a QName object passed into the <code>attribute()</code> method. The <code>localName</code> property is <code>attr</code> and the <code>namespace</code> property is <code>ns</code>.
   * <listing>
   *  var xml:XML = &lt;ns:node xmlns:ns = "http://uri" ns:attr = '7' />
   *  var qn:QName = new QName("http://uri", "attr");
   *  trace (xml.attribute(qn)); // 7
   * </listing>
   * <div>To return an attribute with a name that matches an ActionScript reserved word, use the <code>attribute()</code> method instead of the attribute identifier (@) operator, as in the following example:
   * <listing>
   *  var xml:XML = &lt;example class="first" />
   *  trace(xml.attribute("class"));
   * </listing>
   * </div>
   */
  public native function attribute(attributeName:*):XMLList;

  /**
   * Returns a list of attribute values for the given XML object. Use the <code>name()</code> method with the <code>attributes()</code> method to return the name of an attribute. Use of <code>xml.attributes()</code> is equivalent to <code>xml.@*</code>.
   * @return The list of attribute values.
   *
   * @see #attribute()
   * @see #name()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#attribute_identifier
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6b.html Traversing XML structures
   *
   * @example The following example returns the name of the attribute:
   * <listing>
   * var xml:XML=&lt;example id='123' color='blue'/>
   * trace(xml.attributes()[1].name()); //color
   * </listing>
   * <div>This example returns the names of all the attributes:
   * <listing>
   * var xml:XML = &lt;example id='123' color='blue'/>
   * var attNamesList:XMLList = xml.@*;
   *
   * trace (attNamesList is XMLList); // true
   * trace (attNamesList.length()); // 2
   *
   * for (var i:int = 0; i &lt; attNamesList.length(); i++)
   * {
   *     trace (typeof (attNamesList[i])); // xml
   *     trace (attNamesList[i].nodeKind()); // attribute
   *     trace (attNamesList[i].name()); // id and color
   * }
   * </listing>
   * </div>
   */
  public native function attributes():XMLList;

  /**
   * Lists the children of an XML object. An XML child is an XML element, text node, comment, or processing instruction.
   * <p>Use the <code>propertyName</code> parameter to list the contents of a specific XML child. For example, to return the contents of a child named <code><first></code>, use <code>child.name("first")</code>. You can generate the same result by using the child's index number. The index number identifies the child's position in the list of other XML children. For example, <code>name.child(0)</code> returns the first child in a list.</p>
   * <p>Use an asterisk (*) to output all the children in an XML document. For example, <code>doc.child("*")</code>.</p>
   * <p>Use the <code>length()</code> method with the asterisk (*) parameter of the <code>child()</code> method to output the total number of children. For example, <code>numChildren = doc.child("*").length()</code>.</p>
   * @param propertyName The element name or integer of the XML child.
   *
   * @return An XMLList object of child nodes that match the input parameter.
   *
   * @see #elements()
   * @see XMLList
   * @see #length()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6b.html Traversing XML structures
   *
   * @example This example shows the use of the <code>child()</code> method to identify child elements with a specified name:
   * <listing>
   * var xml:XML =
   *         &lt;foo>
   *             &lt;bar>text1&lt;/bar>
   *             &lt;bar>text2&lt;/bar>
   *         &lt;/foo>;
   * trace(xml.child("bar").length());  // 2
   * trace(xml.child("bar")[0].toXMLString()); // &lt;bar>text1&lt;/bar>
   * trace(xml.child("bar")[1].toXMLString()); // &lt;bar>text2&lt;/bar>
   * </listing>
   */
  public native function child(propertyName:Object):XMLList;

  /**
   * Identifies the zero-indexed position of this XML object within the context of its parent.
   * @return The position of the object. Returns -1 as well as positive integers.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example shows the use of the <code>childIndex()</code> method:
   * <listing>
   * var xml:XML =
   *             &lt;foo>
   *                 &lt;bar />
   *                 text
   *                 &lt;bob />
   *             &lt;/foo>;
   * trace(xml.bar.childIndex()); // 0
   * trace(xml.bob.childIndex()); // 2
   * </listing>
   */
  public native function childIndex():int;

  /**
   * Lists the children of the XML object in the sequence in which they appear. An XML child is an XML element, text node, comment, or processing instruction.
   * @return An XMLList object of the XML object's children.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example shows the use of the <code>children()</code> method:
   * <listing>
   * XML.ignoreComments = false;
   * XML.ignoreProcessingInstructions = false;
   * var xml:XML =
   *         &lt;foo id="22">
   *             &lt;bar>44&lt;/bar>
   *             text
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *         &lt;/foo>;
   * trace(xml.children().length()); // 4
   * trace(xml.children()[0].toXMLString()); // &lt;bar>44&lt;/bar>
   * trace(xml.children()[1].toXMLString()); // text
   * trace(xml.children()[2].toXMLString()); // &lt;!-- comment -->
   * trace(xml.children()[3].toXMLString()); // &lt;?instruction ?>
   * </listing>
   */
  public native function children():XMLList;

  /**
   * Lists the properties of the XML object that contain XML comments.
   * @return An XMLList object of the properties that contain comments.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example shows the use of the <code>comments()</code> method:
   * <listing>
   * XML.ignoreComments = false;
   * var xml:XML =
   *         &lt;foo>
   *             &lt;!-- example -->
   *             &lt;!-- example2 -->
   *         &lt;/foo>;
   * trace(xml.comments().length()); // 2
   * trace(xml.comments()[1].toXMLString()); // &lt;!-- example2 -->
   * </listing>
   */
  public native function comments():XMLList;

  /**
   * Compares the XML object against the given <code>value</code> parameter.
   * @param value A value to compare against the current XML object.
   *
   * @return If the XML object matches the <code>value</code> parameter, then <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example shows the use of the <code>contains()</code> method:
   * <listing>
   * var xml:XML =
   *         &lt;order>
   *             &lt;item>Rice&lt;/item>
   *             &lt;item>Kung Pao Shrimp&lt;/item>
   *         &lt;/order>;
   * trace(xml.item[0].contains(&lt;item>Rice&lt;/item>)); // true
   * trace(xml.item[1].contains(&lt;item>Kung Pao Shrimp&lt;/item>)); // true
   * trace(xml.item[1].contains(&lt;item>MSG&lt;/item>)); // false
   * </listing>
   */
  public native function contains(value:XML):Boolean;

  /**
   * Returns a copy of the given XML object. The copy is a duplicate of the entire tree of nodes. The copied XML object has no parent and returns <code>null</code> if you attempt to call the <code>parent()</code> method.
   * @return The copy of the object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example shows that the <code>copy()</code> method creates a new instance of an XML object. When you modify the copy, the original remains unchanged:
   * <listing>
   * var xml1:XML = &lt;foo />;
   * var xml2:XML = xml1.copy();
   * xml2.appendChild(&lt;bar />);
   * trace(xml1.bar.length()); // 0
   * trace(xml2.bar.length()); // 1
   * </listing>
   */
  public native function copy():XML;

  /**
   * Returns an object with the following properties set to the default values: <code>ignoreComments</code>, <code>ignoreProcessingInstructions</code>, <code>ignoreWhitespace</code>, <code>prettyIndent</code>, and <code>prettyPrinting</code>. The default values are as follows:
   * <ul>
   * <li><code>ignoreComments = true</code></li>
   * <li><code>ignoreProcessingInstructions = true</code></li>
   * <li><code>ignoreWhitespace = true</code></li>
   * <li><code>prettyIndent = 2</code></li>
   * <li><code>prettyPrinting = true</code></li></ul>
   * <p><b>Note:</b> You do not apply this method to an instance of the XML class; you apply it to <code>XML</code>, as in the following code: <code>var df:Object = XML.defaultSettings()</code>.</p>
   * @return An object with properties set to the default settings.
   *
   * @see #ignoreComments
   * @see #ignoreProcessingInstructions
   * @see #ignoreWhitespace
   * @see #prettyIndent
   * @see #prettyPrinting
   * @see #setSettings()
   * @see #settings()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows: how to apply some custom settings (for including comments and processing instructions) prior to setting an XML object; how to then revert back to the default settings before setting another XML object; and then how to set the custom settings again (for setting any more XML objects):
   * <listing>
   * XML.ignoreComments = false;
   * XML.ignoreProcessingInstructions = false;
   * var customSettings:Object = XML.settings();
   *
   * var xml1:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *         &lt;/foo>;
   * trace(xml1.toXMLString());
   * //    &lt;foo>
   * //        &lt;!-- comment -->
   * //         &lt;?instruction ?>
   * //    &lt;/foo>
   *
   * XML.setSettings(XML.defaultSettings());
   * var xml2:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *         &lt;/foo>;
   * trace(xml2.toXMLString());
   * </listing>
   */
  public native static function defaultSettings():Object;

  /**
   * Returns all descendants (children, grandchildren, great-grandchildren, and so on) of the XML object that have the given <code>name</code> parameter. The <code>name</code> parameter is optional. The <code>name</code> parameter can be a QName object, a String data type or any other data type that is then converted to a String data type.
   * <p>To return all descendants, use the "*" parameter. If no parameter is passed, the string "*" is passed and returns all descendants of the XML object.</p>
   * @param name The name of the element to match.
   *
   * @return An XMLList object of matching descendants. If there are no descendants, returns an empty XMLList object.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#descendant_accessor
   *
   * @example To return descendants with names that match ActionScript reserved words, use the <code>descendants()</code> method instead of the descendant (..) operator, as in the following example:
   * <listing>
   * var xml:XML =
   *   &lt;enrollees>
   *     &lt;student id="239">
   *         &lt;class name="Algebra" />
   *         &lt;class name="Spanish 2"/>
   *     &lt;/student>
   *     &lt;student id="206">
   *         &lt;class name="Trigonometry" />
   *         &lt;class name="Spanish 2" />
   *     &lt;/student>
   *   &lt;/enrollees>
   * trace(xml.descendants("class"));
   * </listing>
   * <div>The following example shows that the <code>descendants()</code> method returns an XMLList object that contains <i>all</i> descendant objects, including children, grandchildren, and so on:
   * <listing>
   * XML.ignoreComments = false;
   * var xml:XML =
   *         &lt;body>
   *             &lt;!-- comment -->
   *             text1
   *             &lt;a>
   *                 &lt;b>text2&lt;/b>
   *             &lt;/a>
   *         &lt;/body>;
   * trace(xml.descendants("*").length()); // 5
   * trace(xml.descendants("*")[0]); // // &lt;!-- comment -->
   * trace(xml.descendants("*")[1].toXMLString()); // text1
   * trace(xml.descendants("a").toXMLString()); // &lt;a>&lt;b>text2&lt;/b>&lt;/a>
   * trace(xml.descendants("b").toXMLString()); // &lt;b>text2&lt;/b>
   * </listing></div>
   */
  public native function descendants(name:Object = "*"):XMLList;

  /**
   * Lists the elements of an XML object. An element consists of a start and an end tag; for example <code><first></first></code>. The <code>name</code> parameter is optional. The <code>name</code> parameter can be a QName object, a String data type, or any other data type that is then converted to a String data type. Use the <code>name</code> parameter to list a specific element. For example, the element "<code>first</code>" returns "<code>John</code>" in this example: <code><first>John</first></code>.
   * <p>To list all elements, use the asterisk (*) as the parameter. The asterisk is also the default parameter.</p>
   * <p>Use the <code>length()</code> method with the asterisk parameter to output the total number of elements. For example, <code>numElement = addressbook.elements("*").length()</code>.</p>
   * @param name The name of the element. An element's name is surrounded by angle brackets. For example, "<code>first</code>" is the <code>name</code> in this example: <code><first></first></code>.
   *
   * @return An XMLList object of the element's content. The element's content falls between the start and end tags. If you use the asterisk (*) to call all elements, both the element's tags and content are returned.
   *
   * @see #child()
   * @see XMLList
   * @see #length()
   * @see #attribute()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#dot_(XML)
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows that the <code>elements()</code> method returns a list of elements only — not comments, text properties, or processing instructions:
   * <listing>
   * var xml:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *             text
   *             &lt;a>1&lt;/a>
   *             &lt;b>2&lt;/b>
   *         &lt;/foo>;
   * trace(xml.elements("*").length()); // 2
   * trace(xml.elements("*")[0].toXMLString()); // &lt;a>1&lt;/a>
   * trace(xml.elements("b").length()); // 1
   * trace(xml.elements("b")[0].toXMLString()); // &lt;b>2&lt;/b>
   * </listing>
   * <div>To return elements with names that match ActionScript reserved words, use the <code>elements()</code> method instead of the XML dot (.) operator, as in the following example:
   * <listing>
   * var xml:XML =
   *  &lt;student id="206">
   *     &lt;class name="Trigonometry" />
   *     &lt;class name="Spanish 2" />
   *  &lt;/student>
   * trace(xml.elements("class"));
   * </listing></div>
   */
  public native function elements(name:Object = "*"):XMLList;

  /**
   * Checks to see whether the XML object contains complex content. An XML object contains complex content if it has child elements. XML objects that representing attributes, comments, processing instructions, and text nodes do not have complex content. However, an object that <i>contains</i> these can still be considered to contain complex content (if the object has child elements).
   * @return If the XML object contains complex content, <code>true</code>; otherwise <code>false</code>.
   *
   * @see #hasSimpleContent()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows an XML object with one property named <code>a</code> that has simple content and one property named <code>a</code> that has complex content:
   * <listing>
   * var xml:XML =
   *         &lt;foo>
   *             &lt;a>
   *                 text
   *             &lt;/a>
   *             &lt;a>
   *                 &lt;b/>
   *             &lt;/a>
   *         &lt;/foo>;
   * trace(xml.a[0].hasComplexContent()); // false
   * trace(xml.a[1].hasComplexContent()); // true
   *
   * trace(xml.a[0].hasSimpleContent()); // true
   * trace(xml.a[1].hasSimpleContent()); // false
   * </listing>
   */
  public native function hasComplexContent():Boolean;

  /**
   * Checks to see whether the object has the property specified by the <code>p</code> parameter.
   * @param p The property to match.
   *
   * @return If the property exists, <code>true</code>; otherwise <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>hasOwnProperty()</code> method to ensure that a property (<code>b</code>) exists prior to evaluating an expression (<code>b == "11"</code>) that uses the property:
   * <listing>
   * var xml:XML =
   *         &lt;foo>
   *             &lt;a />
   *             &lt;a>
   *                 &lt;b>10&lt;/b>
   *             &lt;/a>
   *             &lt;a>
   *                 &lt;b>11&lt;/b>
   *             &lt;/a>
   *         &lt;/foo>;
   * trace(xml.a.(hasOwnProperty("b") && b == "11"));
   * </listing>If the last line in this example were the following, Flash Player would throw an exception since the first element named <code>a</code> does not have a property named <code>b</code>:
   * <pre>trace(xml.a.(b == "11"));</pre>
   * <div>The following example uses the <code>hasOwnProperty()</code> method to ensure that a property (<code>item</code>) exists prior to evaluating an expression (<code>item.contains("toothbrush")</code>) that uses the property:
   * <listing>
   * var xml:XML =
   *         &lt;orders>
   *             &lt;order id='1'>
   *                 &lt;item>toothbrush&lt;/item>
   *                 &lt;item>toothpaste&lt;/item>
   *             &lt;/order>
   *             &lt;order>
   *                 &lt;returnItem>shoe polish&lt;/returnItem>
   *             &lt;/order>
   *         &lt;/orders>;
   * trace(xml.order.(hasOwnProperty("item") && item.contains("toothbrush")));
   * </listing></div>
   */
  public native function hasOwnProperty(p:String):Boolean;

  /**
   * Checks to see whether the XML object contains simple content. An XML object contains simple content if it represents a text node, an attribute node, or an XML element that has no child elements. XML objects that represent comments and processing instructions do <i>not</i> contain simple content.
   * @return If the XML object contains simple content, <code>true</code>; otherwise <code>false</code>.
   *
   * @see #hasComplexContent()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows an XML object with one property named <code>a</code> that has simple content and one property named <code>a</code> that has complex content:
   * <listing>
   * var xml:XML =
   *         &lt;foo>
   *             &lt;a>
   *                 text
   *             &lt;/a>
   *             &lt;a>
   *                 &lt;b/>
   *             &lt;/a>
   *         &lt;/foo>;
   * trace(xml.a[0].hasComplexContent()); // false
   * trace(xml.a[1].hasComplexContent()); // true
   *
   * trace(xml.a[0].hasSimpleContent()); // true
   * trace(xml.a[1].hasSimpleContent()); // false
   * </listing>
   */
  public native function hasSimpleContent():Boolean;

  /**
   * Lists the namespaces for the XML object, based on the object's parent.
   * @return An array of Namespace objects.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   */
  public native function inScopeNamespaces():Array;

  /**
   * Inserts the given <code>child2</code> parameter after the <code>child1</code> parameter in this XML object and returns the resulting object. If the <code>child1</code> parameter is <code>null</code>, the method inserts the contents of <code>child2</code> <i>before</i> all children of the XML object (in other words, after <i>none</i>). If <code>child1</code> is provided, but it does not exist in the XML object, the XML object is not modified and <code>undefined</code> is returned.
   * <p>If you call this method on an XML child that is not an element (text, attributes, comments, pi, and so on) <code>undefined</code> is returned.</p>
   * <p>Use the <code>delete</code> (XML) operator to remove XML nodes.</p>
   * @param child1 The object in the source object that you insert before <code>child2</code>.
   * @param child2 The object to insert.
   *
   * @return The resulting XML object or <code>undefined</code>.
   *
   * @see #insertChildBefore()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#delete_(XML)
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e68.html Assembling and transforming XML objects
   *
   * @example The following example appends an element to the end of the child elements of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;menu>
   *             &lt;item>burger&lt;/item>
   *             &lt;item>soda&lt;/item>
   *         &lt;/menu>;
   * xml.insertChildAfter(xml.item[0], &lt;saleItem>fries&lt;/saleItem>);
   * trace(xml);
   * </listing>The <code>trace()</code> output is the following:
   * <pre>
   * &lt;code>&lt;menu>
   * &lt;item>burger&lt;/item>
   * &lt;saleItem>fries&lt;/saleItem>
   * &lt;item>soda&lt;/item>
   * &lt;/menu>&lt;/code>
   * </pre>
   */
  public native function insertChildAfter(child1:Object, child2:Object):*;

  /**
   * Inserts the given <code>child2</code> parameter before the <code>child1</code> parameter in this XML object and returns the resulting object. If the <code>child1</code> parameter is <code>null</code>, the method inserts the contents of <code>child2</code> <i>after</i> all children of the XML object (in other words, before <i>none</i>). If <code>child1</code> is provided, but it does not exist in the XML object, the XML object is not modified and <code>undefined</code> is returned.
   * <p>If you call this method on an XML child that is not an element (text, attributes, comments, pi, and so on) <code>undefined</code> is returned.</p>
   * <p>Use the <code>delete</code> (XML) operator to remove XML nodes.</p>
   * @param child1 The object in the source object that you insert after <code>child2</code>.
   * @param child2 The object to insert.
   *
   * @return The resulting XML object or <code>undefined</code>.
   *
   * @see #insertChildAfter()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#delete_(XML)
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e68.html Assembling and transforming XML objects
   *
   * @example The following example appends an element to the end of the child elements of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;menu>
   *             &lt;item>burger&lt;/item>
   *             &lt;item>soda&lt;/item>
   *         &lt;/menu>;
   * xml.insertChildBefore(xml.item[0], &lt;saleItem>fries&lt;/saleItem>);
   * trace(xml);
   * </listing>The <code>trace()</code> output is the following:
   * <pre>
   * <menu>
   * &lt;saleItem>fries&lt;/saleItem>
   * &lt;item>burger&lt;/item>
   * &lt;item>soda&lt;/item>
   * &lt;/menu>
   * </pre>
   */
  public native function insertChildBefore(child1:Object, child2:Object):*;

  /**
   * For XML objects, this method always returns the integer <code>1</code>. The <code>length()</code> method of the XMLList class returns a value of <code>1</code> for an XMLList object that contains only one value.
   * @return Always returns <code>1</code> for any XML object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   */
  public native function length():int;

  /**
   * Gives the local name portion of the qualified name of the XML object.
   * @return The local name as either a String or <code>null</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example illustrates the use of the <code>localName()</code> method:
   * <listing>
   * var xml:XML =
   *         &lt;soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope"
   *             soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
   *
   *                         &lt;soap:Body xmlns:wx = "http://example.com/weather">
   *                 &lt;wx:forecast>
   *                     &lt;wx:city>Quito&lt;/wx:city>
   *                 &lt;/wx:forecast>
   *             &lt;/soap:Body>
   *         &lt;/soap:Envelope>;
   *
   * trace(xml.localName()); // Envelope
   * </listing>
   */
  public native function localName():Object;

  /**
   * Gives the qualified name for the XML object.
   * @return The qualified name is either a QName or <code>null</code>.
   *
   * @see #attributes()
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#attribute_identifier
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example illustrates the use of the <code>name()</code> method to get the qualified name of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope"
   *             soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
   *
   *                         &lt;soap:Body xmlns:wx = "http://example.com/weather">
   *                 &lt;wx:forecast>
   *                     &lt;wx:city>Quito&lt;/wx:city>
   *                 &lt;/wx:forecast>
   *             &lt;/soap:Body>
   *         &lt;/soap:Envelope>;
   *
   * trace(xml.name().localName); // Envelope
   * trace(xml.name().uri); // "http://www.w3.org/2001/12/soap-envelope"
   * </listing>
   * <div>The following example illustrates the use of the <code>name()</code> method called on an XML property, on a text element, and on an attribute:
   * <listing>
   * var xml:XML =
   *         &lt;foo x="15" y="22">
   *             text
   *         &lt;/foo>;
   *
   * trace(xml.name().localName); // foo
   * trace(xml.name().uri == ""); // true
   * trace(xml.children()[0]); // text
   * trace(xml.children()[0].name()); // null
   * trace(xml.attributes()[0]); // 15
   * trace(xml.attributes()[0].name()); // x
   * </listing></div>
   */
  public native function name():Object;

  /**
   * If no parameter is provided, gives the namespace associated with the qualified name of this XML object. If a <code>prefix</code> parameter is specified, the method returns the namespace that matches the <code>prefix</code> parameter and that is in scope for the XML object. If there is no such namespace, the method returns <code>undefined</code>.
   * @param prefix The prefix you want to match.
   *
   * @return Returns <code>null</code>, <code>undefined</code>, or a namespace.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>namespace()</code> method to get the namespace of an XML object and assign it to a Namespace object named <code>soap</code> which is then used in identifying a property of the <code>xml</code> object (<code>xml.soap::Body[0]</code>):
   * <listing>
   * var xml:XML =
   *         &lt;soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope"
   *             soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
   *
   *                         &lt;soap:Body xmlns:wx = "http://example.com/weather">
   *                 &lt;wx:forecast>
   *                     &lt;wx:city>Quito&lt;/wx:city>
   *                 &lt;/wx:forecast>
   *             &lt;/soap:Body>
   *         &lt;/soap:Envelope>;
   *
   * var soap:Namespace = xml.namespace();
   * trace(soap.prefix); // soap
   * trace(soap.uri); // http://www.w3.org/2001/12/soap-envelope
   *
   * var body:XML = xml.soap::Body[0];
   * trace(body.namespace().prefix); // soap
   * trace(xml.namespace().uri); // http://www.w3.org/2001/12/soap-envelope
   * trace(body.namespace("wx").uri); // "http://example.com/weather"
   * </listing>
   * <div>The following example uses the <code>namespace()</code> method to get the default namespace for a node, as well as the namespace for a specific prefix (<code>"dc"</code>):
   * <listing>
   * var xml:XML =
   *         &lt;rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   *             xmlns:dc="http://purl.org/dc/elements/1.1/"
   *             xmlns="http://purl.org/rss/1.0/">
   *                 &lt;!-- ... -->
   *         &lt;/rdf:RDF>;
   *
   * trace(xml.namespace()); // http://www.w3.org/1999/02/22-rdf-syntax-ns#
   * trace(xml.namespace("dc")); // http://purl.org/dc/elements/1.1/
   * trace(xml.namespace("foo")); // undefined
   * </listing></div>
   */
  public native function namespace(prefix:String = null):*;

  /**
   * Lists namespace declarations associated with the XML object in the context of its parent.
   * @return An array of Namespace objects.
   *
   * @see #namespace()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example outputs the namespace declarations of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   *             xmlns:dc="http://purl.org/dc/elements/1.1/"
   *             xmlns="http://purl.org/rss/1.0/">
   *
   *             &lt;!-- ... -->
   *
   *         &lt;/rdf:RDF>;
   *
   * for (var i:uint = 0; i &lt; xml.namespaceDeclarations().length; i++) {
   *     var ns:Namespace = xml.namespaceDeclarations()[i];
   *     var prefix:String = ns.prefix;
   *     if (prefix == "") {
   *         prefix = "(default)";
   *     }
   *     trace(prefix + ":" , ns.uri);
   * }
   * </listing>
   * The <code>trace()</code> output is the following:
   * <pre>
   * &lt;code>rdf: http://www.w3.org/1999/02/22-rdf-syntax-ns#
   * dc: http://purl.org/dc/elements/1.1/
   * (default): http://purl.org/rss/1.0/&lt;/code>
   * </pre>
   */
  public native function namespaceDeclarations():Array;

  /**
   * Specifies the type of node: text, comment, processing-instruction, attribute, or element.
   * @return The node type used.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#attribute_identifier
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example This example traces all five node types:
   * <listing>
   * XML.ignoreComments = false;
   * XML.ignoreProcessingInstructions = false;
   *
   * var xml:XML =
   *     &lt;example id="10">
   *         &lt;!-- this is a comment -->
   *         &lt;?test this is a pi ?>
   *         and some text
   *     &lt;/example>;
   *
   * trace(xml.nodeKind()); // element
   * trace(xml.children()[0].nodeKind()); // comment
   * trace(xml.children()[1].nodeKind()); // processing-instruction
   * trace(xml.children()[2].nodeKind()); // text
   * trace(xml.@id[0].nodeKind()); // attribute
   * </listing>
   */
  public native function nodeKind():String;

  /**
   * For the XML object and all descendant XML objects, merges adjacent text nodes and eliminates empty text nodes.
   * @return The resulting normalized XML object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows the effect of calling the <code>normalize()</code> method:
   * <listing>
   * var xml:XML = &lt;body>&lt;/body>;
   * xml.appendChild("hello");
   * xml.appendChild(" world");
   * trace(xml.children().length()); // 2
   * xml.normalize();
   * trace(xml.children().length()); // 1
   * </listing>
   */
  public native function normalize():XML;

  /**
   * Returns the parent of the XML object. If the XML object has no parent, the method returns <code>undefined</code>.
   * @return Either an XML reference of the parent node, or <code>undefined</code> if the XML object has no parent.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6b.html Traversing XML structures
   *
   * @example The following example uses the <code>parent()</code> method to identify the parent element of a specific element in an XML structure:
   * <listing>
   * var xml:XML =
   *     &lt;body>
   *         &lt;p id="p1">Hello&lt;/p>
   *         &lt;p id="p2">Test:
   *             &lt;ul>
   *                 &lt;li>1&lt;/li>
   *                 &lt;li>2&lt;/li>
   *             &lt;/ul>
   *         &lt;/p>
   *     &lt;/body>;
   * var node:XML = xml.p.ul.(li.contains("1"))[0]; // == &lt;ul> ... &lt;/ul>
   * trace(node.parent().@id); // p2
   * </listing>
   */
  public native function parent():*;

  /**
   * Inserts a copy of the provided <code>child</code> object into the XML element before any existing XML properties for that element.
   * <p>Use the <code>delete</code> (XML) operator to remove XML nodes.</p>
   * @param value The object to insert.
   *
   * @return The resulting XML object.
   *
   * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/operators.html#delete_(XML)
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e68.html Assembling and transforming XML objects
   *
   * @example The following example uses the <code>prependChild()</code> method to add an element to the begining of a child list of an XML object:
   * <listing>
   * var xml:XML =
   *         &lt;body>
   *             &lt;p>hello&lt;/p>
   *         &lt;/body>;
   *
   * xml.prependChild(&lt;p>world&lt;/p>);
   * trace(xml.p[0].toXMLString()); // &lt;p>world&lt;/p>
   * trace(xml.p[1].toXMLString()); // &lt;p>hello&lt;/p>
   * </listing>
   */
  public native function prependChild(value:Object):XML;

  /**
   * If a <code>name</code> parameter is provided, lists all the children of the XML object that contain processing instructions with that <code>name</code>. With no parameters, the method lists all the children of the XML object that contain any processing instructions.
   * @param name The name of the processing instructions to match.
   *
   * @return A list of matching child objects.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>processingInstructions()</code> method to get an array of processing instructions for an XML object:
   * <listing>
   * XML.ignoreProcessingInstructions = false;
   * var xml:XML =
   *     &lt;body>
   *             foo
   *             &lt;?xml-stylesheet href="headlines.css" type="text/css" ?>
   *             &lt;?instructionX ?>
   *
   *     &lt;/body>;
   *
   * trace(xml.processingInstructions().length()); // 2
   * trace(xml.processingInstructions()[0].name()); // xml-stylesheet
   * </listing>
   */
  public native function processingInstructions(name:String = "*"):XMLList;

  /**
   * Checks whether the property <code>p</code> is in the set of properties that can be iterated in a <code>for..in</code> statement applied to the XML object. Returns <code>true</code> only if <code>toString(p) == "0"</code>.
   * @param p The property that you want to check.
   *
   * @return If the property can be iterated in a <code>for..in</code> statement, <code>true</code>; otherwise, <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows that, for an XML object, the <code>propertyNameIsEnumerable()</code> method returns a value of <code>true</code> only for the value <code>0</code>; whereas for an XMLList object, the return value is <code>true</code> for each valid index value for the XMLList object:
   * <listing>
   * var xml:XML =
   *         &lt;body>
   *              &lt;p>Hello&lt;/p>
   *                &lt;p>World&lt;/p>
   *         &lt;/body>;
   *
   * trace(xml.propertyIsEnumerable(0)); // true
   * trace(xml.propertyIsEnumerable(1)); // false
   *
   * for (var propertyName:String in xml) {
   *     trace(xml[propertyName]);
   * }
   *
   * var list:XMLList = xml.p;
   * trace(list.propertyIsEnumerable(0)); // true
   * trace(list.propertyIsEnumerable(1)); // true
   * trace(list.propertyIsEnumerable(2)); // false
   *
   * for (var propertyName:String in list) {
   *     trace(list[propertyName]);
   * }
   * </listing>
   */
  public native function propertyIsEnumerable(p:String):Boolean;

  /**
   * Removes the given namespace for this object and all descendants. The <code>removeNamespaces()</code> method does not remove a namespace if it is referenced by the object's qualified name or the qualified name of the object's attributes.
   * @param ns The namespace to remove.
   *
   * @return A copy of the resulting XML object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example shows how to remove a namespace declaration from an XML object:
   * <listing>
   * var xml:XML =
   *     &lt;rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   *         xmlns:dc="http://purl.org/dc/elements/1.1/"
   *         xmlns="http://purl.org/rss/1.0/">
   *
   *         &lt;!-- ... -->
   *
   *     &lt;/rdf:RDF>;
   *
   * trace(xml.namespaceDeclarations().length); // 3
   * trace(xml.namespaceDeclarations()[0] is String); //
   * var dc:Namespace = xml.namespace("dc");
   * xml.removeNamespace(dc);
   * trace(xml.namespaceDeclarations().length); // 2
   * </listing>
   */
  public native function removeNamespace(ns:Namespace):XML;

  /**
   * Replaces the properties specified by the <code>propertyName</code> parameter with the given <code>value</code> parameter. If no properties match <code>propertyName</code>, the XML object is left unmodified.
   * @param propertyName Can be a numeric value, an unqualified name for a set of XML elements, a qualified name for a set of XML elements, or the asterisk wildcard ("*"). Use an unqualified name to identify XML elements in the default namespace.
   * @param value The replacement value. This can be an XML object, an XMLList object, or any value that can be converted with <code>toString()</code>.
   *
   * @return The resulting XML object, with the matching properties replaced.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example illustrates calling the <code>replace()</code> method with an integer as the first parameter:
   * <listing>
   * var xml:XML =
   *     &lt;body>
   *         &lt;p>Hello&lt;/p>
   *         &lt;p>World&lt;/p>
   *         &lt;hr/>
   *     &lt;/body>;
   *
   * xml.replace(1, &lt;p>Bob&lt;/p>);
   * trace(xml);
   * </listing>
   * This results in the following <code>trace()</code> output:
   * <pre>
   * &lt;code>&lt;body>
   * &lt;p>Hello&lt;/p>
   * &lt;p>Bob&lt;/p>
   * &lt;hr/>
   * &lt;/body>
   * &lt;/code>
   * </pre>
   * <div>The following example calls <code>replace()</code> method with a string as the first parameter:
   * <listing>
   * var xml:XML =
   *     &lt;body>
   *         &lt;p>Hello&lt;/p>
   *         &lt;p>World&lt;/p>
   *         &lt;hr/>
   *     &lt;/body>;
   *
   * xml.replace("p", &lt;p>Hi&lt;/p>);
   * trace(xml);
   * </listing>
   * This results in the following <code>trace()</code> output:
   * <pre>
   * &lt;code>&lt;body>
   * &lt;p>Hi&lt;/p>
   * &lt;hr/>
   * &lt;/body>;
   * &lt;/code>
   * </pre>
   * </div>
   * <div>The following example illustrates calling the <code>replace()</code> method with a QName as the first parameter:
   * <listing>
   * var xml:XML =
   *     &lt;ns:body xmlns:ns = "myNS">
   *         &lt;ns:p>Hello&lt;/ns:p>
   *         &lt;ns:p>World&lt;/ns:p>
   *         &lt;hr/>
   *     &lt;/ns:body>;
   *
   * var qname:QName = new QName("myNS", "p");
   * xml.replace(qname, &lt;p>Bob&lt;/p>);
   * trace(xml);
   *
   * </listing>
   * This results in the following <code>trace()</code> output:
   * <pre>
   * &lt;code>&lt;ns:body xmlns:ns = "myNS">
   * &lt;p>Bob&lt;/p>
   * &lt;hr/>
   * &lt;/ns:body>
   * &lt;/code>
   * </pre>
   * </div>
   * <div>The following example illustrates calling the <code>replace()</code> method with the string <code>"*"</code> as the first parameter:
   * <listing>
   * var xml:XML =
   *     &lt;body>
   *         &lt;p>Hello&lt;/p>
   *         &lt;p>World&lt;/p>
   *         &lt;hr/>
   *     &lt;/body>;
   *
   * xml.replace("*", &lt;img src = "hello.jpg"/>);
   * trace(xml);
   * </listing>
   * This results in the following <code>trace()</code> output:
   * <pre>
   * &lt;code>&lt;body>
   * &lt;img src="hello.jpg"/>
   * &lt;/body>
   * &lt;/code>
   * </pre>
   * </div>
   */
  public native function replace(propertyName:Object, value:XML):XML;

  /**
   * Replaces the child properties of the XML object with the specified set of XML properties, provided in the <code>value</code> parameter.
   * @param value The replacement XML properties. Can be a single XML object or an XMLList object.
   *
   * @return The resulting XML object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example illustrates calling the <code>setChildren()</code> method, first using an XML object as the parameter, and then using an XMLList object as the parameter:
   * <listing>
   * var xml:XML =
   *     &lt;body>
   *         &lt;p>Hello&lt;/p>
   *         &lt;p>World&lt;/p>
   *     &lt;/body>;
   *
   * var list:XMLList = xml.p;
   *
   * xml.setChildren(&lt;p>hello&lt;/p>);
   * trace(xml);
   *
   * //    &lt;body>
   * //        &lt;p>hello&lt;/p>
   * //    &lt;/body>
   *
   * xml.setChildren(list);
   * trace(xml);
   *
   * //    &lt;body>
   * //        &lt;p>Hello&lt;/p>
   * //        &lt;p>World&lt;/p>
   * //    &lt;/body>
   * </listing>
   */
  public native function setChildren(value:Object):XML;

  /**
   * Changes the local name of the XML object to the given <code>name</code> parameter.
   * @param name The replacement name for the local name.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>setLocalName()</code> method to change the local name of an XML element:
   * <listing>
   * var xml:XML =
   *     &lt;ns:item xmlns:ns="http://example.com">
   *         toothbrush
   *     &lt;/ns:item>;
   *
   * xml.setLocalName("orderItem");
   * trace(xml.toXMLString()); // &lt;ns:orderItem xmlns:ns="http://example.com">toothbrush&lt;/ns:orderItem>
   * </listing>
   */
  public native function setLocalName(name:String):void;

  /**
   * Sets the name of the XML object to the given qualified name or attribute name.
   * @param name The new name for the object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>setName()</code> method to change the name of an XML element:
   * <listing>
   * var xml:XML =
   *     &lt;item>
   *         toothbrush
   *     &lt;/item>;
   *
   * xml.setName("orderItem");
   * trace(xml.toXMLString()); // &lt;orderItem>toothbrush&lt;/orderItem>
   * </listing>
   */
  public native function setName(name:String):void;

  /**
   * Sets the namespace associated with the XML object.
   * @param ns The new namespace.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   *
   * @example The following example uses the <code>soap</code> namespace defined in one XML object and applies it to the namespace of another XML object (<code>xml2</code>):
   * <listing>
   * var xml1:XML =
   *         &lt;soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope"
   *             soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
   *             &lt;!-- ... -->
   *         &lt;/soap:Envelope>;
   * var ns:Namespace = xml1.namespace("soap");
   *
   * var xml2:XML =
   *     &lt;Envelope>
   *         &lt;Body/>
   *     &lt;/Envelope>;
   *
   * xml2.setNamespace(ns);
   *
   * trace(xml2);
   * </listing>
   */
  public native function setNamespace(ns:Namespace):void;

  /**
   * Sets values for the following XML properties: <code>ignoreComments</code>, <code>ignoreProcessingInstructions</code>, <code>ignoreWhitespace</code>, <code>prettyIndent</code>, and <code>prettyPrinting</code>. The following are the default settings, which are applied if no <code>setObj</code> parameter is provided:
   * <ul>
   * <li><code>XML.ignoreComments = true</code></li>
   * <li><code>XML.ignoreProcessingInstructions = true</code></li>
   * <li><code>XML.ignoreWhitespace = true</code></li>
   * <li><code>XML.prettyIndent = 2</code></li>
   * <li><code>XML.prettyPrinting = true</code></li></ul>
   * <p><b>Note</b>: You do not apply this method to an instance of the XML class; you apply it to <code>XML</code>, as in the following code: <code>XML.setSettings()</code>.</p>
   * @param rest An object with each of the following properties:
   * <ul>
   * <li><code>ignoreComments</code></li>
   * <li><code>ignoreProcessingInstructions</code></li>
   * <li><code>ignoreWhitespace</code></li>
   * <li><code>prettyIndent</code></li>
   * <li><code>prettyPrinting</code></li></ul>
   *
   * @see #ignoreComments
   * @see #ignoreProcessingInstructions
   * @see #ignoreWhitespace
   * @see #prettyIndent
   * @see #prettyPrinting
   * @see #defaultSettings()
   * @see #settings()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7fd0.html Functions
   * @see http://help.adobe.com/en_US/as3/learn/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f56.html Function parameters
   *
   * @example The following example shows: how to apply some custom settings (for including comments and processing instructions) prior to setting an XML object; how to then revert back to the default settings before setting another XML object; and then how to set the custom settings again (for setting any more XML objects):
   * <listing>
   * XML.ignoreComments = false;
   * XML.ignoreProcessingInstructions = false;
   * var customSettings:Object = XML.settings();
   *
   * var xml1:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *         &lt;/foo>;
   * trace(xml1.toXMLString());
   * //    &lt;foo>
   * //        &lt;!-- comment -->
   * //         &lt;?instruction ?>
   * //    &lt;/foo>
   *
   * XML.setSettings(XML.defaultSettings());
   * var xml2:XML =
   *         &lt;foo>
   *             &lt;!-- comment -->
   *             &lt;?instruction ?>
   *         &lt;/foo>;
   * trace(xml2.toXMLString());
   * </listing>
   */

  public native static function setSettings(...rest):void;
/**
 * Retrieves the following properties: <code>ignoreComments</code>, <code>ignoreProcessingInstructions</code>, <code>ignoreWhitespace</code>, <code>prettyIndent</code>, and <code>prettyPrinting</code>.
 * @return An object with the following XML properties:
 * <ul>
 * <li><code>ignoreComments</code></li>
 * <li><code>ignoreProcessingInstructions</code></li>
 * <li><code>ignoreWhitespace</code></li>
 * <li><code>prettyIndent</code></li>
 * <li><code>prettyPrinting</code></li></ul>
 *
 * @see #ignoreComments
 * @see #ignoreProcessingInstructions
 * @see #ignoreWhitespace
 * @see #prettyIndent
 * @see #prettyPrinting
 * @see #defaultSettings()
 * @see #setSettings()
 *
 * @example The following example shows: how to apply some custom settings (for including comments and processing instructions) prior to setting an XML object; how to then revert back to the default settings before setting another XML object; and then how to set the custom settings again (for setting any more XML objects):
 * <listing>
 * XML.ignoreComments = false;
 * XML.ignoreProcessingInstructions = false;
 * var customSettings:Object = XML.settings();
 *
 * var xml1:XML =
 *         &lt;foo>
 *             &lt;!-- comment -->
 *             &lt;?instruction ?>
 *         &lt;/foo>;
 * trace(xml1.toXMLString());
 * //    &lt;foo>
 * //        &lt;!-- comment -->
 * //         &lt;?instruction ?>
 * //    &lt;/foo>
 *
 * XML.setSettings(XML.defaultSettings());
 * var xml2:XML =
 *         &lt;foo>
 *             &lt;!-- comment -->
 *             &lt;?instruction ?>
 *         &lt;/foo>;
 * trace(xml2.toXMLString());
 * </listing>
 */
public native static function settings():Object;

/**
 * Returns an XMLList object of all XML properties of the XML object that represent XML text nodes.
 * @return The list of properties.
 *
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
 *
 * @example The following example uses the <code>text()</code> method to get the text nodes of an XML object:
 * <listing>
 * var xml:XML =
 *         &lt;body>
 *             text1
 *             &lt;hr/>
 *             text2
 *         &lt;/body>;
 * trace(xml.text()[0]); // text1
 * trace(xml.text()[1]); // text2
 * </listing>
 */
[Native]
public native function text():XMLList;

/**
 * Returns a string representation of the XML object. The rules for this conversion depend on whether the XML object has simple content or complex content:
 * <ul>
 * <li>If the XML object has simple content, <code>toString()</code> returns the String contents of the XML object with the following stripped out: the start tag, attributes, namespace declarations, and end tag.</li></ul>
 * <ul>
 * <li>If the XML object has complex content, <code>toString()</code> returns an XML encoded String representing the entire XML object, including the start tag, attributes, namespace declarations, and end tag.</li></ul>
 * <p>To return the entire XML object every time, use <code>toXMLString()</code>.</p>
 * @return The string representation of the XML object.
 *
 * @see #hasSimpleContent()
 * @see #hasComplexContent()
 * @see #toXMLString()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6d.html XML type conversion
 *
 * @example The following example shows what the <code>toString()</code> method returns when the XML object has simple content:
 * <listing>
 * var test:XML = &lt;type name="Joe">example&lt;/type>;
 * trace(test.toString()); //example
 * </listing>
 * <div>The following example shows what the <code>toString()</code> method returns when the XML object has complex content:
 * <listing>
 * var test:XML =
 * &lt;type name="Joe">
 *     &lt;base name="Bob">&lt;/base>
 *     example
 * &lt;/type>;
 * trace(test.toString());
 *   // &lt;type name="Joe">
 *   // &lt;base name="Bob"/>
 *   // example
 *   // &lt;/type>
 * </listing></div>
 */
[Native]
public native function toString():String;

/**
 * Returns a string representation of the XML object. Unlike the <code>toString()</code> method, the <code>toXMLString()</code> method always returns the start tag, attributes, and end tag of the XML object, regardless of whether the XML object has simple content or complex content. (The <code>toString()</code> method strips out these items for XML objects that contain simple content.)
 * @return The string representation of the XML object.
 *
 * @see #toString()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6d.html XML type conversion
 *
 * @example The following example shows the difference between using the <code>toString()</code> method (which is applied to all parameters of a <code>trace()</code> method, by default) and using the <code>toXMLString()</code> method:
 * <listing>
 * var xml:XML =
 *         &lt;p>hello&lt;/p>;
 * trace(xml); // hello
 * trace(xml.toXMLString()); // &lt;p>hello&lt;/p>
 * </listing>
 */
[Native]
public native function toXMLString():String;

/**
 * Returns the XML object.
 * @return The primitive value of an XML instance.
 *
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e71.html XML objects
 *
 * @example The following example shows that the value returned by the <code>valueOf()</code> method is the same as the source XML object:
 * <listing>
 * var xml:XML = &lt;p>hello&lt;/p>;
 * trace(xml.valueOf() === xml); // true
 * </listing>
 */
[Native]
public native function valueOf():XML;
}
}
