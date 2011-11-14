package net.jangaroo.exml.schemas;


import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ValidateSchemas {

  @Test
  public void validateExmlSchema() throws IOException, SAXException, URISyntaxException {
    String schemaLang = "http://www.w3.org/2001/XMLSchema";

    // get validation driver:
    SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

    // create schema by reading it from an XSD file:
    factory.newSchema(new StreamSource(getFile("/net/jangaroo/exml/schemas/exml.xsd")));
  }

  @Test
  public void validateUntypedSchema() throws IOException, SAXException, URISyntaxException {
    String schemaLang = "http://www.w3.org/2001/XMLSchema";

    // get validation driver:
    SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

    // create schema by reading it from an XSD file:
    factory.newSchema(new StreamSource(getFile("/net/jangaroo/exml/schemas/untyped.xsd")));
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ValidateSchemas.class.getResource(path).toURI());
  }
}
