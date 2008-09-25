/*
 * Copyright 2008 CoreMedia AG
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

package net.jangaroo.example {

  /**
   * A simple example demonstrating how to receive and return typed arguments.
   */
  public class HelloWorld {

    /**
     * Returns a personalized greeting.
     * @param name name of the person to greet
     * @return String a personalized greeting
     */
    public function greet(name : String) : String {
      return "Hello, " + name + "!";
    }

    /**
     * Returns a personalized and HTML-escaped greeting.
     * @param name name of the person to greet
     * @return String a personalized and HTML-escaped greeting
     */
    public function greetHtml(name : String) : String {
      return this.greet(name).replace(/&/g, "&amp;").replace(/</g, "&lt;");
    }

  }
}