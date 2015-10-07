package net.jangaroo.ext {
/**
 * Create a target object from the given config class and object.
 * This function is mainly used by code generated from EXML source files.
 * <p>First, an instance of the config class is created with the given config parameter.
 * Then, the target class, retrieved from the <code>ExtConfig</code> annotation in the
 * config class, is instantiated with the config class instance.
 * </p>
 * @param configClass the config class
 * @param config the untyped config object
 * @return the created target object
 */
public native function create(configClass:Class,  config:Object):Object;

}
