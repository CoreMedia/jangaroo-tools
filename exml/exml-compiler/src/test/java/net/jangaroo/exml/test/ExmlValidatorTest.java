package net.jangaroo.exml.test;

import junit.framework.Assert;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.config.ValidationMode;
import net.jangaroo.exml.parser.ExmlValidator;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class ExmlValidatorTest extends AbstractExmlTest {

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  @Test
  public void testValidateExmlFile() throws Exception {
    setUp("testNamespace.config");
    ExmlConfiguration exmlConfiguration = getExmlc().getConfig();
    File testExmlFile = getFile("/exmlparser/TestValidation.exml");
    exmlConfiguration.setSourceFiles(Collections.singletonList(testExmlFile));
    exmlConfiguration.setResourceOutputDirectory(getFile("/"));

    exmlConfiguration.setValidationMode(ValidationMode.ERROR);
    final Map<FilePosition, String> validationErrors = new LinkedHashMap<FilePosition, String>();
    exmlConfiguration.setLog(new CompileLog() {
      @Override
      public void error(FilePosition position, String msg) {
        System.out.printf("%s(%d): %s%n", position.getFileName(), position.getLine(), msg);
        validationErrors.put(position, msg);
      }

      @Override
      public void error(String msg) {
        Assert.fail("should never be called");
      }

      @Override
      public void warning(FilePosition position, String msg) {
        Assert.fail("should never be called");
      }

      @Override
      public void warning(String msg) {
        Assert.fail("should never be called");
      }

      @Override
      public boolean hasErrors() {
        return !validationErrors.isEmpty();
      }
    });
    new ExmlValidator(exmlConfiguration).validateExmlFile(testExmlFile);
    String testExmlFilename = testExmlFile.getAbsolutePath();

    Assert.assertEquals(4, validationErrors.size());
    Iterator<Map.Entry<FilePosition,String>> validationErrorIterator = validationErrors.entrySet().iterator();
    assertValidationErrorEquals(testExmlFilename, 4,
            "cvc-datatype-valid.1.2.3: 'wrongType' is not a valid value of union type 'Number'.",
            validationErrorIterator.next());
    assertValidationErrorEquals(testExmlFilename, 4,
            "cvc-attribute.3: The value 'wrongType' of attribute 'x' on element 'panel' is not valid with respect to its type, 'Number'.",
            validationErrorIterator.next());
    assertValidationErrorEquals(testExmlFilename, 4,
            "cvc-complex-type.3.2.2: Attribute 'wrongAttribute' is not allowed to appear in element 'panel'.",
            validationErrorIterator.next());
    assertValidationErrorEquals(testExmlFilename, 5,
            "cvc-complex-type.3.2.2: Attribute 'anotherWrongAttribute' is not allowed to appear in element 'baseAction'.",
            validationErrorIterator.next());
  }

  private void assertValidationErrorEquals(String expectedFilename, int expectedLine, String expectedMessage, Map.Entry<FilePosition, String> validationError) {
    Assert.assertEquals(expectedFilename, validationError.getKey().getFileName());
    Assert.assertEquals(expectedMessage, validationError.getValue());
    Assert.assertEquals(expectedLine, validationError.getKey().getLine());
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlValidatorTest.class.getResource(path).toURI());
  }
}
