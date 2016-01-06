package net.jangaroo.exml.generator;

import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ConfigClassType;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MxmlLibraryManifestGeneratorTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void testCreateManifestFile() throws Exception {
    ExmlConfiguration exmlConfiguration = mock(ExmlConfiguration.class);
    when(exmlConfiguration.getOutputDirectory()).thenReturn(temporaryFolder.newFolder("out"));
    ConfigClass configClass1 = mock(ConfigClass.class);
    when(configClass1.getFullName()).thenReturn("foo");
    when(configClass1.getType()).thenReturn(ConfigClassType.CLASS);
    when(configClass1.getComponentClassName()).thenReturn("Foo");
    ConfigClass configClass2 = mock(ConfigClass.class);
    when(configClass2.getFullName()).thenReturn("bar");
    when(configClass2.getType()).thenReturn(ConfigClassType.CLASS);
    when(configClass2.getComponentClassName()).thenReturn("Bar");
    ConfigClass configClass3 = mock(ConfigClass.class);
    when(configClass3.getFullName()).thenReturn("comp");
    when(configClass3.getType()).thenReturn(ConfigClassType.COMPONENT);
    when(configClass3.getComponentClassName()).thenReturn("Comp");
    ConfigClassRegistry configClassRegistry = mock(ConfigClassRegistry.class);
    when(configClassRegistry.getConfig()).thenReturn(exmlConfiguration);
    when(configClassRegistry.getSourceConfigClasses()).thenReturn(asList(configClass1, configClass2, configClass3));

    File manifest = new MxmlLibraryManifestGenerator(configClassRegistry).createManifestFile();

    assertEquals(IOUtils.toString(getClass().getResourceAsStream("/manifest.xml")), readFileToString(manifest));
  }
}
