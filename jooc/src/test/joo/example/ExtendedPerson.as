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

public class ExtendedPerson extends example.Person {

  public function ExtendedPerson(name: String, birthday: Date) {
    super(name);
    this.setBirthday(birthday);
  }

  public function getBirthday(): Date {
    return this.birthday;
  }

  public function setBirthday(birthday: Date): void {
    this.birthday = birthday;
  }

  override protected function renderInner(): void {
    var test = "foo";
    return super.renderInner()+"<p>birthday: "+this.getBirthday().toLocaleString()+"</p>";
  }

  private var birthday: Date = new Date();
}
}