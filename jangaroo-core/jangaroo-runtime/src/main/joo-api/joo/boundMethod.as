/*
 * Copyright 2009 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */
package joo {

/**
 * Bind a method of an object to this object.
 * <p>Methods of a class are slightly different than Function objects. Unlike an ordinary function object, a method is
 * tightly linked to its associated class object. Therefore, a method or property has a definition that is shared among
 * all instances of the same class. Methods can be extracted from an instance and treated as "bound" methods (retaining
 * the link to the original instance). For a bound method, the this keyword points to the original object that
 * implemented the method. For a function, this points to the associated object at the time the function is invoked.</p>
 * <p>If a method is retrieved from its object using <code>this</code>, Jangaroo takes care of binding the method to
 * its object to achieve the AS3 semantics. Only if you retrieve a method through another variable, you currently have
 * to use <code>boundMethod</code> explicitly.</p>
 * <p>A boundMethod is cached, i.e. for the same object and method name, a new Function object is created on first
 * invocation only. This improves performance and memory consumption, and is important for keeping method object
 * identity, for example to successfully remove an event listener.</p>
 *
 * @example
 * In the following example, a method is attached as an event listener. Note that when using a method of
 * <code>this</code>, no further action is required, but when accessing an object through any other variable,
 * you have to take care of method binding yourself.
 * <pre>
 * public function attach(source:EventDispatcher, listener:MyListener):void {
 *   // If using 'this', method is bound automatically by Jangaroo:
 *   source.addEventListener("changed", this.changed);
 *
 *   // If using a variable, Jangaroo does not detect method access, so you must bind explicitly:
 *   source.addEventListener("updated", joo.boundMethod(listener, 'changed')));
 * }
 * private function changed(e:Event):void {
 *   // handle event...
 *   this.field = e.data; // accessing 'this' works only if method is bound!
 * }
 * </pre>
 *
 * @param object the object whose method to bind
 * @param methodName the name of the method to bind.
 * @return Function the bound method
 * @see Function
 */
public native function boundMethod(object:Object, methodName:String):Function;


}
