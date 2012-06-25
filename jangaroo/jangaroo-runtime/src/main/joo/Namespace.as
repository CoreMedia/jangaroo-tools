package {

/**
 * The Namespace class contains methods and properties for defining and working with namespaces. There are three scenarios for using namespaces:
 * <ul>
 * <li><b>Namespaces of XML objects</b> Namespaces associate a namespace prefix with a Uniform Resource Identifier (URI) that identifies the namespace. The prefix is a string used to reference the namespace within an XML object. If the prefix is undefined, when the XML is converted to a string, a prefix is automatically generated.</li>
 * <li><b>Namespace to differentiate methods</b> Namespaces can differentiate methods with the same name to perform different tasks. If two methods have the same name but separate namespaces, they can perform different tasks.</li>
 * <li><b>Namespaces for access control</b> Namespaces can be used to control access to a group of properties and methods in a class. If you place the properties and methods into a private namespace, they are inaccessible to any code that does not have access to that namespace. You can grant access to the group of properties and methods by passing the namespace to other classes, methods or functions.</li></ul>
 * <p>This class shows two forms of the constructor method because each form accepts different parameters.</p>
 * <p>This class (along with the XML, XMLList, and QName classes) implements powerful XML-handling standards defined in ECMAScript for XML (E4X) specification (ECMA-357 edition 2).</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/Namespace.html#includeExamplesSummary">View the examples</a></p>
 * <p>More examples</p>
 * <div><a href="http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e6c.html">Using XML namespaces</a><a href="http://help.adobe.com/en_US/as3/learn/WScd8be5d4d2d754b2213cb12813207207d3b-7ff2.html">Using XML namespaces</a></div>
 * <p>Learn more</p>
 * <div><a href="http://www.ecma-international.org/publications/standards/Ecma-357.htm">ECMAScript for XML (E4X) specification (ECMA-357 edition 2)</a></div>
 * <p>Related API Elements</p>
 * <div><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/XML.html">XML</a><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/XMLList.html">XMLList</a><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/QName.html">QName</a></div>
 */
[Native]
public final class Namespace {
  /**
   * The prefix of the namespace.
   */
  public native function get prefix():String;

  /**
   * @private
   */
  public native function set prefix(value:String):void;

  /**
   * The Uniform Resource Identifier (URI) of the namespace.
   */
  public native function get uri():String;

  /**
   * @private
   */
  public native function set uri(value:String):void;

  /**
   * Creates a Namespace object. The values assigned to the <code>uri</code> and <code>prefix</code> properties of the new Namespace object depend on the type of value passed for the <code>uriValue</code> parameter:
   * <ul>
   * <li>If no value is passed, the <code>prefix</code> and <code>uri</code> properties are set to an empty string.</li>
   * <li>If the value is a Namespace object, a copy of the object is created.</li>
   * <li>If the value is a QName object, the <code>uri</code> property is set to the <code>uri</code> property of the QName object.</li></ul>
   * <p><b>Note:</b> This class shows two constructor entries because each form accepts different parameters. The constructor behaves differently depending on the type and number of parameters passed, as detailed in each entry. ActionScript 3.0 does not support method or constructor overloading.</p>
   *
   * Creates a Namespace object according to the values of the <code>prefixValue</code> and <code>uriValue</code> parameters. This constructor requires both parameters.
   * <p>The value of the <code>prefixValue</code> parameter is assigned to the <code>prefix</code> property as follows:</p>
   * <ul>
   * <li>If <code>undefined</code> is passed, <code>prefix</code> is set to <code>undefined</code>.</li>
   * <li>If the value is a valid XML name, as determined by the <code>isXMLName()</code> function, it is converted to a string and assigned to the <code>prefix</code> property.</li>
   * <li>If the value is not a valid XML name, the <code>prefix</code> property is set to <code>undefined</code>.</li></ul>
   * <p>The value of the <code>uriValue</code> parameter is assigned to the <code>uri</code> property as follows:</p>
   * <ul>
   * <li>If a QName object is passed, the <code>uri</code> property is set to the value of the QName object's <code>uri</code> property.</li>
   * <li>Otherwise, the <code>uriValue</code> parameter is converted to a string and assigned to the <code>uri</code> property.</li></ul>
   * <p><b>Note:</b> This class shows two constructor method entries because each form accepts different parameters. The constructor behaves differently depending on the type and number of arguments passed, as detailed in each entry. ActionScript 3.0 does not support method or constructor overloading.</p>
   * @param prefixValue The prefix to use for the namespace.
   * @param uriValue The Uniform Resource Identifier (URI) of the namespace.
   *
   */
  public function Namespace(prefixValue:*, uriValue:*) {
  }

  /**
   * Equivalent to the <code>Namespace.uri</code> property.
   * @return The Uniform Resource Identifier (URI) of the namespace, as a string.
   *
   */
  public native function toString():String;

  /**
   * Returns the URI value of the specified object.
   * @return The Uniform Resource Identifier (URI) of the namespace, as a string.
   *
   */
  public native function valueOf():String;

}
}
