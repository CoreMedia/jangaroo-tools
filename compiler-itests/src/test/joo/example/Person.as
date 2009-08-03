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

package example {

/**
 * This class models a person.
 */
public class Person {

  /**
   * Create a new person with a given name.
   * @param name the name of the new person.
   */
  public function Person(name: String) {
    this.setName(name ? name : example.Person.DEFAULT_NAME);
  }

  /**
   * Get the name of this person.
   * @return the name of this person.
   * @see #setName
   */
  public function getName(): String {
    return this.name;
  }

  /**
   * Set the name of this person.
   * @param name the new name of this person.
   */
  public function setName(name: String): void {
    this.name = name;
  }

  /**
   * Render this person as HTML into the current document.
   */
  public function render(): void {
    window.document.write("<div>"+this.renderInner()+"</div>");
  }

  /**
   * Get the inner HTML presenting this person.
   * Subclasses should add a presentation of their additional properties.
   */
  protected function renderInner(): String {
    this.privateTest("foo");
    return "<p>name: "+this.getName()+"</p>";
  }

  private function privateTest(param :String) :Person {
    return this;
  }

  private var name: String;
  private static const DEFAULT_NAME: String = "Hugo";
}
}