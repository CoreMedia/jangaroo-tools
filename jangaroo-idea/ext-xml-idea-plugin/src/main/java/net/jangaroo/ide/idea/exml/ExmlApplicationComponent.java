package net.jangaroo.ide.idea.exml;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.javaee.ExternalResourceManager;
import org.jetbrains.annotations.NotNull;

/**
 * The EXML plugin's global configuration.
 * Adds XML Schemata for EXML 0.1.
 */
public class ExmlApplicationComponent implements ApplicationComponent {

  @NotNull
  public String getComponentName() {
    return "exmlGlobals";
  }

  public void initComponent() {
    FileTypeManager ftm = FileTypeManager.getInstance();
    ftm.registerFileType(ftm.getFileTypeByExtension("xml"), "exml");
    ExternalResourceManager erm = ExternalResourceManager.getInstance();
    erm.addStdResource("http://net.jangaroo.com/extxml/0.1", "/net/jangaroo/extxml/schemas/extxml.xsd", getClass());
  }

  public void disposeComponent() {
    ExternalResourceManager erm = ExternalResourceManager.getInstance();
    erm.removeResource("http://net.jangaroo.com/extxml/0.1", "/net/jangaroo/extxml/schemas/extxml.xsd");
  }
}
