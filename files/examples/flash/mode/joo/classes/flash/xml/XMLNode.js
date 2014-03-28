joo.classLoader.prepare("package flash.xml",/* {*/


/**
 * The XMLNode class represents the legacy XML object that was present in ActionScript 2.0 and that was renamed in ActionScript 3.0. In ActionScript 3.0, consider using the new top-level <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/XML.html">XML</a> class and related classes instead, which support E4X (ECMAScript for XML). The XMLNode class is present for backward compatibility.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/xml/XMLNode.html#includeExamplesSummary">View the examples</a></p>
 * @see XML
 * @see XMLDocument
 *
 */
"public class XMLNode",1,function($$private){;return[ 
  /**
   * An object containing all of the attributes of the specified XMLNode instance. The XMLNode.attributes object contains one variable for each attribute of the XMLNode instance. Because these variables are defined as part of the object, they are generally referred to as properties of the object. The value of each attribute is stored in the corresponding property as a string. For example, if you have an attribute named <code>color</code>, you would retrieve that attribute's value by specifying <code>color</code> as the property name, as the following code shows:
   * <pre>     var myColor:String = doc.firstChild.attributes.color
   </pre>
   */
  "public function get attributes",function attributes$get()/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set attributes",function attributes$set(value/*:Object*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An array of the specified XMLNode object's children. Each element in the array is a reference to an XMLNode object that represents a child node. This is a read-only property and cannot be used to manipulate child nodes. Use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * <p>This property is undefined for text nodes (<code>nodeType == 3</code>).</p>
   * @see #nodeType
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public function get childNodes",function childNodes$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Evaluates the specified XMLDocument object and references the first child in the parent node's child list. This property is <code>null</code> if the node does not have children. This property is <code>undefined</code> if the node is a text node. This is a read-only property and cannot be used to manipulate child nodes; use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public var",{ firstChild/*:XMLNode*/:null},
  /**
   * An XMLNode value that references the last child in the node's child list. The <code>XMLNode.lastChild</code> property is <code>null</code> if the node does not have children. This property cannot be used to manipulate child nodes; use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public var",{ lastChild/*:XMLNode*/:null},

  /**
   * The local name portion of the XML node's name. This is the element name without the namespace prefix. For example, the node <code>&lt;contact:mailbox/>bob&#64;example.com&lt;/contact:mailbox></code> has the local name "mailbox", and the prefix "contact", which comprise the full element name "contact.mailbox".
   * <p>You can access the namespace prefix through the <code>prefix</code> property of the XML node object. The <code>nodeName</code> property returns the full name (including the prefix and the local name).</p>
   */
  "public function get localName",function localName$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * If the XML node has a prefix, <code>namespaceURI</code> is the value of the <code>xmlns</code> declaration for that prefix (the URI), which is typically called the namespace URI. The <code>xmlns</code> declaration is in the current node or in a node higher in the XML hierarchy.
   * <p>If the XML node does not have a prefix, the value of the <code>namespaceURI</code> property depends on whether there is a default namespace defined (as in <code>xmlns="http://www.example.com/"</code>). If there is a default namespace, the value of the <code>namespaceURI</code> property is the value of the default namespace. If there is no default namespace, the <code>namespaceURI</code> property for that node is an empty string (<code>""</code>).</p>
   * <p>You can use the <code>getNamespaceForPrefix()</code> method to identify the namespace associated with a specific prefix. The <code>namespaceURI</code> property returns the prefix associated with the node name.</p>
   * @see #getNamespaceForPrefix()
   * @see #getPrefixForNamespace()
   *
   */
  "public function get namespaceURI",function namespaceURI$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An XMLNode value that references the next sibling in the parent node's child list. This property is <code>null</code> if the node does not have a next sibling node. This property cannot be used to manipulate child nodes; use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * @see #firstChild
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public var",{ nextSibling/*:XMLNode*/:null},
  /**
   * A string representing the node name of the XMLNode object. If the XMLNode object is an XML element (<code>nodeType == 1</code>), <code>nodeName</code> is the name of the tag that represents the node in the XML file. For example, <code>TITLE</code> is the <code>nodeName</code> of an HTML <code>TITLE</code> tag. If the XMLNode object is a text node (<code>nodeType == 3</code>), nodeName is <code>null</code>.
   * @see #nodeType
   *
   */
  "public var",{ nodeName/*:String*/:null},
  /**
   * A <code>nodeType</code> constant value, either <code>XMLNodeType.ELEMENT_NODE</code> for an XML element or <code>XMLNodeType.TEXT_NODE</code> for a text node.
   * <p>The <code>nodeType</code> is a numeric value from the NodeType enumeration in the W3C DOM Level 1 recommendation: <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core.html">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core.html</a>. The following table lists the values:</p>
   * <table>
   * <tr><th>Integer value</th><th>Defined constant</th></tr>
   * <tr>
   * <td>1</td>
   * <td>ELEMENT_NODE</td></tr>
   * <tr>
   * <td>3</td>
   * <td>TEXT_NODE</td></tr>
   * <tr>
   * <td>5</td>
   * <td>ENTITY_REFERENCE_NODE</td></tr>
   * <tr>
   * <td>7</td>
   * <td>PROCESSING_INSTRUCTION_NODE</td></tr>
   * <tr>
   * <td>9</td>
   * <td>DOCUMENT_NODE</td></tr>
   * <tr>
   * <td>11</td>
   * <td>DOCUMENT_FRAGMENT_NODE</td></tr></table>
   * <p>In Flash Player, the built-in XMLNode class only supports <code>XMLNodeType.ELEMENT_NODE</code> and <code>XMLNodeType.TEXT_NODE</code>.</p>
   * @see XMLNodeType#TEXT_NODE
   * @see XMLNodeType#ELEMENT_NODE
   *
   */
  "public var",{ nodeType/*:uint*/:0},
  /**
   * The node value of the XMLDocument object. If the XMLDocument object is a text node, the <code>nodeType</code> is 3, and the <code>nodeValue</code> is the text of the node. If the XMLDocument object is an XML element (<code>nodeType</code> is 1), <code>nodeValue</code> is <code>null</code> and read-only.
   * @see #nodeType
   *
   */
  "public var",{ nodeValue/*:String*/:null},
  /**
   * An XMLNode value that references the parent node of the specified XML object, or returns <code>null</code> if the node has no parent. This is a read-only property and cannot be used to manipulate child nodes; use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public var",{ parentNode/*:XMLNode*/:null},

  /**
   * The prefix portion of the XML node name. For example, the node <code>&lt;contact:mailbox/>bob&#64;example.com&lt;/contact:mailbox></code> prefix "contact" and the local name "mailbox", which comprise the full element name "contact.mailbox".
   * <p>The <code>nodeName</code> property of an XML node object returns the full name (including the prefix and the local name). You can access the local name portion of the element's name via the <code>localName</code> property.</p>
   */
  "public function get prefix",function prefix$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An XMLNode value that references the previous sibling in the parent node's child list. The property has a value of null if the node does not have a previous sibling node. This property cannot be used to manipulate child nodes; use the <code>appendChild()</code>, <code>insertBefore()</code>, and <code>removeNode()</code> methods to manipulate child nodes.
   * @see #lastChild
   * @see #appendChild()
   * @see #insertBefore()
   * @see #removeNode()
   *
   */
  "public var",{ previousSibling/*:XMLNode*/:null},

  /**
   * Creates a new XMLNode object. You must use the constructor to create an XMLNode object before you call any of the methods of the XMLNode class.
   * <p><b>Note:</b> Use the <code>createElement()</code> and <code>createTextNode()</code> methods to add elements and text nodes to an XML document tree.</p>
   * @param type The node type: either 1 for an XML element or 3 for a text node.
   * @param value The XML text parsed to create the new XMLNode object.
   *
   * @see XMLDocument#createElement()
   * @see XMLDocument#createTextNode()
   *
   */
  "public function XMLNode",function XMLNode$(type/*:uint*/, value/*:String*/) {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Appends the specified node to the XML object's child list. This method operates directly on the node referenced by the <code>childNode</code> parameter; it does not append a copy of the node. If the node to be appended already exists in another tree structure, appending the node to the new location will remove it from its current location. If the <code>childNode</code> parameter refers to a node that already exists in another XML tree structure, the appended child node is placed in the new tree structure after it is removed from its existing parent node.
   * @param node An XMLNode that represents the node to be moved from its current location to the child list of the <code>my_xml</code> object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e72.html The E4X approach to XML processing
   *
   */
  "public function appendChild",function appendChild(node/*:XMLNode*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Constructs and returns a new XML node of the same type, name, value, and attributes as the specified XML object. If <code>deep</code> is set to <code>true</code>, all child nodes are recursively cloned, resulting in an exact copy of the original object's document tree.
   * <p>The clone of the node that is returned is no longer associated with the tree of the cloned item. Consequently, <code>nextSibling</code>, <code>parentNode</code>, and <code>previousSibling</code> all have a value of <code>null</code>. If the <code>deep</code> parameter is set to <code>false</code>, or the <code>my_xml</code> node has no child nodes, <code>firstChild</code> and <code>lastChild</code> are also null.</p>
   * @param deep A Boolean value; if set to <code>true</code>, the children of the specified XML object will be recursively cloned.
   *
   * @return An XMLNode Object.
   *
   */
  "public function cloneNode",function cloneNode(deep/*:Boolean*/)/*:XMLNode*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the namespace URI that is associated with the specified prefix for the node. To determine the URI, <code>getPrefixForNamespace()</code> searches up the XML hierarchy from the node, as necessary, and returns the namespace URI of the first <code>xmlns</code> declaration for the given <code>prefix</code>.
   * <p>If no namespace is defined for the specified prefix, the method returns <code>null</code>.</p>
   * <p>If you specify an empty string (<code>""</code>) as the <code>prefix</code> and there is a default namespace defined for the node (as in <code>xmlns="http://www.example.com/"</code>), the method returns that default namespace URI.</p>
   * @param prefix The prefix for which the method returns the associated namespace.
   *
   * @return The namespace that is associated with the specified prefix.
   *
   * @see #getPrefixForNamespace()
   * @see #namespaceURI
   *
   */
  "public function getNamespaceForPrefix",function getNamespaceForPrefix(prefix/*:String*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the prefix that is associated with the specified namespace URI for the node. To determine the prefix, <code>getPrefixForNamespace()</code> searches up the XML hierarchy from the node, as necessary, and returns the prefix of the first <code>xmlns</code> declaration with a namespace URI that matches <code>ns</code>.
   * <p>If there is no <code>xmlns</code> assignment for the given URI, the method returns <code>null</code>. If there is an <code>xmlns</code> assignment for the given URI but no prefix is associated with the assignment, the method returns an empty string (<code>""</code>).</p>
   * @param ns The namespace URI for which the method returns the associated prefix.
   *
   * @return The prefix associated with the specified namespace.
   *
   * @see #getNamespaceForPrefix()
   * @see #namespaceURI
   *
   */
  "public function getPrefixForNamespace",function getPrefixForNamespace(ns/*:String*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Indicates whether the specified XMLNode object has child nodes. This property is <code>true</code> if the specified XMLNode object has child nodes; otherwise, it is <code>false</code>.
   * @return Returns <code>true</code> if the specified XMLNode object has child nodes; otherwise, <code>false</code>.
   *
   */
  "public function hasChildNodes",function hasChildNodes()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Inserts a new child node into the XML object's child list, before the <code>beforeNode</code> node. If the <code>beforeNode</code> parameter is undefined or null, the node is added using the <code>appendChild()</code> method. If <code>beforeNode</code> is not a child of <code>my_xml</code>, the insertion fails.
   * @param node The XMLNode object to be inserted.
   * @param before The XMLNode object before the insertion point for the <code>childNode</code>.
   *
   * @see #cloneNode()
   *
   */
  "public function insertBefore",function insertBefore(node/*:XMLNode*/, before/*:XMLNode*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Removes the specified XML object from its parent. Also deletes all descendants of the node.
   */
  "public function removeNode",function removeNode()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Evaluates the specified XMLNode object, constructs a textual representation of the XML structure, including the node, children, and attributes, and returns the result as a string.
   * <p>For top-level XMLDocument objects (those created with the constructor), the <code>XMLDocument.toString()</code> method outputs the document's XML declaration (stored in the <code>XMLDocument.xmlDecl</code> property), followed by the document's <code>DOCTYPE</code> declaration (stored in the <code>XMLDocument.docTypeDecl</code> property), followed by the text representation of all XML nodes in the object. The XML declaration is not output if the <code>XMLDocument.xmlDecl</code> property is <code>null</code>. The <code>DOCTYPE</code> declaration is not output if the <code>XMLDocument.docTypeDecl</code> property is <code>null</code>.</p>
   * @return The string representing the XMLNode object.
   *
   * @see XMLDocument#docTypeDecl
   * @see XMLDocument#xmlDecl
   *
   */
  "public function toString",function toString()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);