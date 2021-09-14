package joo {

/**
 * Return a qualified object, given by its dot-separated qualified name.
 * Usually used to retrieve a package, class, interface, or package-scoped member by its fully qualified name.
 * @param qualifiedName the qualified name for which to retrieve the qualified object
 * @return Any object found under the qualified name or undefined.
 * @see joo.getOrCreatePackage
 */
[Native]
public native function getQualifiedObject(qualifiedName : String) : *;


}
