package {


/**
 * QName objects represent qualified names of XML elements and attributes. Each QName object has a local name and a namespace Uniform Resource Identifier (URI). When the value of the namespace URI is <code>null</code>, the QName object matches any namespace. Use the QName constructor to create a new QName object that is either a copy of another QName object or a new QName object with a <code>uri</code> from a Namespace object and a <code>localName</code> from a QName object.
 * <p>Methods specific to E4X can use QName objects interchangeably with strings. E4X methods are in the QName, Namespace, XML, and XMLList classes. These E4X methods, which take a string, can also take a QName object. This interchangeability is how namespace support works with, for example, the <code>XML.child()</code> method.</p>
 * <p>The QName class (along with the XML, XMLList, and Namespace classes) implements powerful XML-handling standards defined in ECMAScript for XML (E4X) specification (ECMA-357 edition 2).</p>
 * <p>A qualified identifier evaluates to a QName object. If the QName object of an XML element is specified without identifying a namespace, the <code>uri</code> property of the associated QName object is set to the global default namespace. If the QName object of an XML attribute is specified without identifying a namespace, the <code>uri</code> property is set to an empty string.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./QName.html#includeExamplesSummary">View the examples</a></p>
 * @see XML
 * @see XMLList
 * @see Namespace
 * @see http://www.ecma-international.org/publications/standards/Ecma-357.htm ECMAScript for XML (E4X) specification (ECMA-357 edition 2)
 *
 */
[Native]
public final class QName {
  /**
   * The local name of the QName object.
   */
  public function get localName():String {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * The Uniform Resource Identifier (URI) of the QName object.
   */
  public function get uri():String {
    throw new Error('not implemented'); // TODO: implement!
  }

  /**
   * a) Creates a QName object that is a copy of another QName object. If the parameter passed to the constructor is a QName object, a copy of the QName object is created. If the parameter is not a QName object, the parameter is converted to a string and assigned to the <code>localName</code> property of the new QName instance. If the parameter is <code>undefined</code> or unspecified, a new QName object is created with the <code>localName</code> property set to the empty string.
   * b) Creates a QName object with a URI object from a Namespace object and a <code>localName</code> from a QName object. If either parameter is not the expected data type, the parameter is converted to a string and assigned to the corresponding property of the new QName object. For example, if both parameters are strings, a new QName object is returned with a <code>uri</code> property set to the first parameter and a <code>localName</code> property set to the second parameter. In other words, the following permutations, along with many others, are valid forms of the constructor:
   * <pre>
   * QName (uri:Namespace, localName:String);
   * QName (uri:String, localName: QName);
   * QName (uri:String, localName: String);
   * </pre>
   * <p>If you pass <code>null</code> for the <code>qnameOrUri</code> parameter, the <code>uri</code> property of the new QName object is set to <code>null</code>.</p>
   * <p><b>Note:</b> This class shows two constructor entries because each form accepts different parameters. The constructor behaves differently depending on the type and number of parameters passed, as detailed in each entry. ActionSript 3.0 does not support method or constructor overloading.</p>
   * @param qnameOrUri a) The QName object to be copied. Objects of any other type are converted to a string that is assigned to the <code>localName</code> property of the new QName object.
   * b) A Namespace object from which to copy the <code>uri</code> value. A parameter of any other type is converted to a string.
   * @param localName A QName object from which to copy the <code>localName</code> value. A parameter of any other type is converted to a string.
   *
   */
  public native function QName(qnameOrUri:Object/*QName or Namespace*/ = null, localName:Object/*String or QName*/ = null);

  /**
   * Returns a string composed of the URI, and the local name for the QName object, separated by "::".
   * <p>The format depends on the <code>uri</code> property of the QName object:</p>
   * <pre>If <code>uri</code> == ""
   <code>toString</code> returns <code>localName</code>
   else if <code>uri</code> == null
   <code>toString</code> returns *::<code>localName</code>
   else
   <code>toString</code> returns <code>uri</code>::<code>localName</code></pre>
   * @return The qualified name, as a string.
   *
   */
  public native function toString():String;

  /**
   * Returns the QName object.
   * @return The primitive value of a QName instance.
   *
   */
  public native function valueOf():QName;
}
}
