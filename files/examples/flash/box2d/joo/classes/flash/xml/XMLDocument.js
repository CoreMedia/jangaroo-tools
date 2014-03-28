joo.classLoader.prepare("package flash.xml",/* {*/

/**
 * The XMLDocument class represents the legacy XML object that was present in ActionScript 2.0. It was renamed in ActionScript 3.0 to XMLDocument to avoid name conflicts with the new XML class in ActionScript 3.0. In ActionScript 3.0, it is recommended that you use the new <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/XML.html">XML</a> class and related classes, which support E4X (ECMAScript for XML).
 * <p>The XMLDocument class, as well as XMLNode and XMLNodeType, are present for backward compatibility. The functionality for loading XML documents can now be found in the URLLoader class.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/xml/XMLDocument.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.net.URLLoader
 * @see XML
 *
 */
"public class XMLDocument extends flash.xml.XMLNode",2,function($$private){;return[function(){joo.classLoader.init(flash.xml.XMLNodeType);}, 
  /**
   * Specifies information about the XML document's <code>DOCTYPE</code> declaration. After the XML text has been parsed into an XMLDocument object, the <code>XMLDocument.docTypeDecl</code> property of the XMLDocument object is set to the text of the XML document's <code>DOCTYPE</code> declaration (for example, <code><!DOCTYPE</code> <code>greeting SYSTEM "hello.dtd"></code>). This property is set using a string representation of the <code>DOCTYPE</code> declaration, not an XMLNode object.
   * <p>The legacy ActionScript XML parser is not a validating parser. The <code>DOCTYPE</code> declaration is read by the parser and stored in the <code>XMLDocument.docTypeDecl</code> property, but no DTD validation is performed.</p>
   * <p>If no <code>DOCTYPE</code> declaration was encountered during a parse operation, the <code>XMLDocument.docTypeDecl</code> property is set to <code>null</code>. The <code>XML.toString()</code> method outputs the contents of <code>XML.docTypeDecl</code> immediately after the XML declaration stored in <code>XML.xmlDecl</code>, and before any other text in the XML object. If <code>XMLDocument.docTypeDecl</code> is null, no <code>DOCTYPE</code> declaration is output.</p>
   */
  "public var",{ docTypeDecl/*:Object*/ : null},
  /**
   * An Object containing the nodes of the XML that have an <code>id</code> attribute assigned. The names of the properties of the object (each containing a node) match the values of the <code>id</code> attributes.
   * <p>Consider the following XMLDocument object:</p>
   * <listing>
   *      <employee id='41'>
   *          <name>
   *              John Doe
   *          </name>
   *          <address>
   *              601 Townsend St.
   *          </address>
   *      </employee>
   *
   *      <employee id='42'>
   *          <name>
   *              Jane Q. Public
   *          </name>
   *      </employee>
   *      <department id="IT">
   *          Information Technology
   *      </department>
   *     </listing>
   * <p>In this example, the <code>idMap</code> property for this XMLDocument object is an Object with three properties: <code>41</code>, <code>42</code>, and <code>IT</code>. Each of these properties is an XMLNode that has the matching <code>id</code> value. For example, the <code>IT</code> property of the <code>idMap</code> object is this node:</p>
   * <listing>
   *      <department id="IT">
   *          Information Technology
   *      </department>
   *     </listing>
   * <p>You must use the <code>parseXML()</code> method on the XMLDocument object for the <code>idMap</code> property to be instantiated.</p>
   * <p>If there is more than one XMLNode with the same <code>id</code> value, the matching property of the <code>idNode</code> object is that of the last node parsed. For example:</p>
   * <listing>
   *      var x1:XML = new XMLDocument("<a id='1'><b id='2' /><c id='1' /></a>");
   *      x2 = new XMLDocument();
   *      x2.parseXML(x1);
   *      trace(x2.idMap['1']);
   *     </listing>This will output the <code><c></code> node:
   * <listing>
   *      <c id='1' />
   *     </listing>
   */
  "public var",{ idMap/*:Object*/:null},
  /**
   * When set to <code>true</code>, text nodes that contain only white space are discarded during the parsing process. Text nodes with leading or trailing white space are unaffected. The default setting is <code>false</code>.
   * <p>You can set the <code>ignoreWhite</code> property for individual XMLDocument objects, as the following code shows:</p>
   * <listing>
   *      my_xml.ignoreWhite = true;
   *     </listing>
   */
  "public var",{ ignoreWhite/*:Boolean*/ : false},
  /**
   * A string that specifies information about a document's XML declaration. After the XML document is parsed into an XMLDocument object, this property is set to the text of the document's XML declaration. This property is set using a string representation of the XML declaration, not an XMLNode object. If no XML declaration is encountered during a parse operation, the property is set to <code>null</code>. The <code>XMLDocument.toString()</code> method outputs the contents of the <code>XML.xmlDecl</code> property before any other text in the XML object. If the <code>XML.xmlDecl</code> property contains <code>null</code>, no XML declaration is output.
   */
  "public var",{ xmlDecl/*:Object*/ : null},

  /**
   * Creates a new XMLDocument object. You must use the constructor to create an XMLDocument object before you call any of the methods of the XMLDocument class.
   * <p><b>Note:</b> Use the <code>createElement()</code> and <code>createTextNode()</code> methods to add elements and text nodes to an XML document tree.</p>
   * @param source The XML text parsed to create the new XMLDocument object.
   *
   * @see #createElement()
   * @see #createTextNode()
   *
   */
  "public function XMLDocument",function XMLDocument$(source/*:String = null*/) {if(arguments.length<1){source = null;}
    this.super$2(flash.xml.XMLNodeType.DOCUMENT_NODE, source);
  },

  /**
   * Creates a new XMLNode object with the name specified in the parameter. The new node initially has no parent, no children, and no siblings. The method returns a reference to the newly created XMLNode object that represents the element. This method and the <code>XMLDocument.createTextNode()</code> method are the constructor methods for creating nodes for an XMLDocument object.
   * @param name The tag name of the XMLDocument element being created.
   *
   * @return An XMLNode object.
   *
   * @see #createTextNode()
   *
   */
  "public function createElement",function createElement(name/*:String*/)/*:XMLNode*/ {
    return new flash.xml.XMLNode(flash.xml.XMLNodeType.ELEMENT_NODE, name);
  },

  /**
   * Creates a new XML text node with the specified text. The new node initially has no parent, and text nodes cannot have children or siblings. This method returns a reference to the XMLDocument object that represents the new text node. This method and the <code>XMLDocument.createElement()</code> method are the constructor methods for creating nodes for an XMLDocument object.
   * @param text The text used to create the new text node.
   *
   * @return An XMLNode object.
   *
   * @see #createElement()
   *
   */
  "public function createTextNode",function createTextNode(text/*:String*/)/*:XMLNode*/ {
    return new flash.xml.XMLNode(flash.xml.XMLNodeType.ELEMENT_NODE, text);
  },

  /**
   * Parses the XML text specified in the <code>value</code> parameter and populates the specified XMLDocument object with the resulting XML tree. Any existing trees in the XMLDocument object are discarded.
   * @param source The XML text to be parsed and passed to the specified XMLDocument object.
   *
   */
  "public function parseXML",function parseXML(source/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns a string representation of the XML object.
   * @return A string representation of the XML object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.xml.XMLNode","flash.xml.XMLNodeType","Error"], "0.8.0", "0.8.1"
);