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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    assertValidationErrorContains(testExmlFilename, 4,
      words("cvc-datatype-valid.1.2.3", "wrongType", "Number"),
      validationErrorIterator.next());
    assertValidationErrorContains(testExmlFilename, 4,
      words("cvc-attribute.3", "wrongType", "x", "panel", "Number"),
      validationErrorIterator.next());
    assertValidationErrorContains(testExmlFilename, 4,
      words("cvc-complex-type.3.2.2", "wrongAttribute", "panel"),
      validationErrorIterator.next());
    assertValidationErrorContains(testExmlFilename, 5,
      words("cvc-complex-type.3.2.2", "anotherWrongAttribute", "baseAction"),
      validationErrorIterator.next());
  }

  private void assertValidationErrorContains(String expectedFilename, int expectedLine, Set<String> expectedWords, Map.Entry<FilePosition, String> validationError) {
    Assert.assertEquals("Expected file name", expectedFilename, validationError.getKey().getFileName());
    Assert.assertEquals("Expected line number", expectedLine, validationError.getKey().getLine());
    for (String expectedWord : expectedWords) {
      Assert.assertTrue("Message should contain '" + expectedWord + "'", validationError.getValue().contains(expectedWord));
    }
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlValidatorTest.class.getResource("/test-module" + path).toURI());
  }

  private Set<String> words(String... words) {
    return new HashSet<String>(Arrays.asList(words));
  }
}
