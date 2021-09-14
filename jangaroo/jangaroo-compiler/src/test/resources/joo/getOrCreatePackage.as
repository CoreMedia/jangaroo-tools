package joo {

/**
 * Retrieve or create the package with the given fully qualified name.
 * Although in AS3, packages do not have a runtime representation, in Jangaroo, they have.
 * @param packageName the fully qualified name of the package to retrieve or create
 * @return * the package object retrieved or created.
 * @see joo.getQualifiedObject()
 */
[Native]
public native function getOrCreatePackage(packageName : String) : *;


}
