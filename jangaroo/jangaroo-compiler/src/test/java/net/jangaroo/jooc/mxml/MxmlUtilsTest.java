package net.jangaroo.jooc.mxml;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test MxmlUtils.
 */
public class MxmlUtilsTest {

  @Test
  public void testIsBindingExpression() {
    Assert.assertTrue(MxmlUtils.isBindingExpression("{foo}"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("{{ foo: 'bar'}}"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("{foo.bar.baz}"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("{foo.bar}boo{baz}"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("bla{foo.bar}boo{baz}"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("{foo.bar}boo{baz}blub"));
    Assert.assertTrue(MxmlUtils.isBindingExpression("bla{foo.bar}boo{baz}blub"));
    Assert.assertFalse(MxmlUtils.isBindingExpression("{}"));
    Assert.assertFalse(MxmlUtils.isBindingExpression("\\{foo}"));
    Assert.assertFalse(MxmlUtils.isBindingExpression("{foo\\}"));
  }

  @Test
  public void testGetBindingExpression() {
    Assert.assertEquals("foo", MxmlUtils.getBindingExpression("{foo}"));
    Assert.assertEquals("{ foo: 'bar'}", MxmlUtils.getBindingExpression("{{ foo: 'bar'}}"));
    Assert.assertEquals("foo.bar.baz", MxmlUtils.getBindingExpression("{foo.bar.baz}"));
    Assert.assertEquals("foo.bar + \"boo\" + baz", MxmlUtils.getBindingExpression("{foo.bar}boo{baz}"));
    Assert.assertEquals("\"bla\" + foo.bar + \"boo\" + baz", MxmlUtils.getBindingExpression("bla{foo.bar}boo{baz}"));
    Assert.assertEquals("foo.bar + \"boo\" + baz + \"blub\"", MxmlUtils.getBindingExpression("{foo.bar}boo{baz}blub"));
    Assert.assertEquals("\"bla\" + foo.bar + \"boo\" + baz + \"blub\"", MxmlUtils.getBindingExpression("bla{foo.bar}boo{baz}blub"));
    Assert.assertEquals("function() { return {foo:2};}", MxmlUtils.getBindingExpression("{function() { return {foo:2};}}"));
    Assert.assertEquals("foo + \"bar\" + \"{baz\"", MxmlUtils.getBindingExpression("{foo}bar{baz"));
    Assert.assertEquals("\"foo}bar\" + baz", MxmlUtils.getBindingExpression("foo}bar{baz}"));
  }

  @Test
  public void testToASDoc() {
    Assert.assertEquals("/* this text and two empty lines \n\n */\n" +
                    "/** unescaped: $1 *//* single star *//**\n" +
                    " *\n" +
                    " * escaped: \\$2\n" +
                    " *\n" +
                    " */",
            MxmlUtils.toASDoc("<!-- this text and two empty lines \n\n-->\n" +
                    "<!--- unescaped: $1 --><!-- single star --><!---\n" +
                    "\n" +
                    " escaped: \\$2\n" +
                    "\n" +
                    "-->"));
  }

  @Test
  public void testToASDocClass() {
    Assert.assertEquals(
            "//Some comment\n/**\n" +
                    " * This is a form panel which combines several form elements to an editor for local settings to configure\n" +
                    " * the fixed index behaviour. A combination of integer field and reset button.\n" +
                    " */\n",
            MxmlUtils.toASDoc(
                    "//Some comment\n<!---\n" +
                    " This is a form panel which combines several form elements to an editor for local settings to configure\n" +
                    " the fixed index behaviour. A combination of integer field and reset button.\n" +
                    "-->\n"));
  }

  @Test
  public void testToASDocClassStartOfFile() {
    Assert.assertEquals(
            "/**\n" +
                    " * This is a form panel which combines several form elements to an editor for local settings to configure\n" +
                    " * the fixed index behaviour. A combination of integer field and reset button.\n" +
                    " */\n",
            MxmlUtils.toASDoc(
                    "<!---\n" +
                    " This is a form panel which combines several form elements to an editor for local settings to configure\n" +
                    " the fixed index behaviour. A combination of integer field and reset button.\n" +
                    "-->\n"));
  }

  @Test
  public void testToASDocProperty() {
    Assert.assertEquals(
            "    /**\n" +
                    "     * The name of the sting property of the Bean to bind in this field.\n" +
                    "     * The string property holds the id of the catalog product\n" +
                    "     */\n",
            MxmlUtils.toASDoc(
                    "    <!---\n" +
                    "     The name of the sting property of the Bean to bind in this field.\n" +
                    "     The string property holds the id of the catalog product\n" +
                    "    -->\n"));
  }
}
