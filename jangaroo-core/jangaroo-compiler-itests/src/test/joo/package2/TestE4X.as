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

package package2 {

public class TestE4X {

  public static function testParseUnparse(xmlString:String):String {
    var xml:XML = new XML(xmlString);
    return xml.toXMLString();
  }

  public static function testDotDot(testString:String):String {
    var xml:XML = new XML("<root><bla><foo>" + testString + "</foo></bla></root>");
    return xml..foo.toString();
  }

  public static function testDotAsterisk(xmlString:String):String {
    var xml:XML = new XML(xmlString);
    return xml.*.toXMLString();
  }

  public static function testXMLList(xml1:String, xml2:String):String {
    var xml:XMLList = <></>;
    xml += new XML(xml1);
    xml += new XML(xml2);
    return xml.toXMLString();
  }

  public static function testDotParens(xmlString:String, selectId:String):String {
    var xml:XML = new XML(xmlString);
    return (xml.*).(@id == selectId).toString();
  }

}
}
