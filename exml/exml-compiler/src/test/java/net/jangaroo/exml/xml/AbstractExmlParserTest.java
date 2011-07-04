package net.jangaroo.exml.xml;

import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.jooc.input.FileInputSource;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: okummer
 * Date: 04.07.11
 * Time: 09:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractExmlParserTest {
  private FileInputSource sourcePathInputSource;
  private FileInputSource classpathInputSource;
  protected ConfigClassRegistry registry;

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  protected void setUp(String configClassPackage) throws Exception {
    sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, configClassPackage, outputFolder.getRoot());
  }
}
