package {
/**
 * Determines whether the specified string is a valid name for an XML element or attribute.
 * @param str A string to evaluate.
 *
 * @return Returns <code>true</code> if the <code>str</code> argument is a valid XML name; <code>false</code> otherwise.
 *
 */
[Native]
public native function isXMLName(str:String):Boolean;

}
