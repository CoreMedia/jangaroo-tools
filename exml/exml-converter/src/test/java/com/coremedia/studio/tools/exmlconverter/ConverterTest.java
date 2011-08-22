package com.coremedia.studio.tools.exmlconverter;

import org.apache.commons.io.output.StringBuilderWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Test that the EXML converter does the right thing.
 */
public class ConverterTest {
  private class TestConverter extends Converter {
    private String input;
    private StringBuilder output = new StringBuilder();

    private TestConverter(String input) {
      this.input = input;
    }

    @Override
    protected Writer createWriter() throws UnsupportedEncodingException, FileNotFoundException {
      return new StringBuilderWriter(output);
    }

    @Override
    protected Reader createReader() throws UnsupportedEncodingException, FileNotFoundException {
      return new StringReader(input);
    }

    public String getOutput() {
      return output.toString();
    }
  }

  private void checkConversion(String input, String expected) throws IOException, ParseException {
    TestConverter converter = new TestConverter(input);
    converter.execute();
    String output = converter.getOutput();
    Assert.assertEquals(expected, output);
  }

  @Test
  public void testExmlNamespace() throws Exception {
    checkConversion("<exml:component xmlns:exml=\"http://net.jangaroo.com/extxml/0.1\"/>",
            "<exml:component xmlns:exml=\"http://www.jangaroo.net/exml/0.8\"/>");
  }

  @Test
  public void testExmlNamespaceWithSingleQuotes() throws Exception {
    checkConversion("<exml:component xmlns:exml='http://net.jangaroo.com/extxml/0.1'/>",
            "<exml:component xmlns:exml='http://www.jangaroo.net/exml/0.8'/>");
  }

  @Test
  public void testComponentToLower() throws Exception {
    checkConversion("<exml:component><prefix:WorkAreaBase/></exml:component>",
            "<exml:component><prefix:workAreaBase/></exml:component>");
  }

  @Test
  public void testSpecialComponents() throws Exception {
    checkConversion("<exml:component><prefix:binddisable/></exml:component>",
            "<exml:component><prefix:bindDisable/></exml:component>");
  }

  @Test
  public void testOneCharacterName() throws Exception {
    checkConversion("<exml:component><prefix:A/></exml:component>",
            "<exml:component><prefix:a/></exml:component>");
  }

  @Test
  public void testSpecialCharactersInName() throws Exception {
    checkConversion("<exml:component><TheSpecialComponänt_v01/></exml:component>",
            "<exml:component><theSpecialComponänt_v01/></exml:component>");
  }

  @Test
  public void testComponentToLowerNoNamespace() throws Exception {
    checkConversion("<exml:component><WorkAreaBase/></exml:component>",
            "<exml:component><workAreaBase/></exml:component>");
  }

  @Test
  public void testComment() throws Exception {
    checkConversion("<!-- a comment - with a hyphen --><exml:component/>",
            "<!-- a comment - with a hyphen --><exml:component/>");
  }

  @Test
  public void testHeader() throws Exception {
    checkConversion("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><exml:component/>",
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><exml:component/>");
  }

  @Test
  public void testCdata() throws Exception {
    checkConversion("<exml:component><exml:object><![CDATA[&&&\"]]></exml:object></exml:component>",
            "<exml:component><exml:object><![CDATA[&&&\"]]></exml:object></exml:component>");
  }

  @Test
  public void testEntity() throws Exception {
    checkConversion("<exml:component><exml:object>a&amp;b</exml:object></exml:component>",
            "<exml:component><exml:object>a&amp;b</exml:object></exml:component>");
  }

  @Test
  public void testDoctype() throws Exception {
    checkConversion("<!DOCTYPE FOO PUBLIC \"a\" \"b\"><exml:component/>",
            "<!DOCTYPE FOO PUBLIC \"a\" \"b\"><exml:component/>");
  }

  @Test
  public void testFormatting() throws Exception {
    checkConversion("\r\n<exml:component >\n\t\t<prefix:WorkAreaBase \n\t\ra =\t'b'\r/>    </exml:component>\t",
            "\r\n<exml:component >\n\t\t<prefix:workAreaBase \n\t\ra =\t'b'\r/>    </exml:component>\t");
  }

  private void expectParseException(String input) throws Exception {
    try {
      TestConverter converter = new TestConverter(input);
      converter.execute();
      Assert.fail("expected parse exception");
    } catch (ParseException e) {
      // this is expected
    }
  }

  @Test
  public void testUnterminatedTag() throws Exception {
    expectParseException("<exml:component");
  }

  @Test
  public void testIncompleteCdataStart() throws Exception {
    expectParseException("<exml:component><exml:object><![CDA");
  }

  @Test
  public void testUnterminatedCdata() throws Exception {
    expectParseException("<exml:component><exml:object><![CDATA[blabla");
  }

  @Test
  public void testIncompleteCommentStart() throws Exception {
    expectParseException("<exml:component><exml:object><!");
  }

  @Test
  public void testNotCdata() throws Exception {
    expectParseException("<exml:component><exml:object><![CDATÄ");
  }

  @Test
  public void testUnterminatedEntity() throws Exception {
    expectParseException("<exml:component><exml:object>&lt");
  }

  @Test
  public void testUnterminatedAttributeValue() throws Exception {
    expectParseException("<exml:component><exml:object a='");
  }

  @Test
  public void testUnquotedAttributeValue() throws Exception {
    expectParseException("<exml:component a=b />");
  }

  @Test
  public void testMissingAttributeValue() throws Exception {
    expectParseException("<exml:component><foo:bar baz=");
  }

  @Test
  public void testMissingEntityName() throws Exception {
    expectParseException("<");
  }

  @Test
  public void testUnterminatedElement() throws Exception {
    expectParseException("<exml:component ");
  }
}
