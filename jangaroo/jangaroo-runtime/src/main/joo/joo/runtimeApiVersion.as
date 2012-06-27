package joo {

/**
 * The Jangaroo Runtime API version (increased only on incompatible compiler-runtime contract changes).
 * @return the Jangaroo Runtime API version.
 */
[Native]
public native function get runtimeApiVersion():String;

}
