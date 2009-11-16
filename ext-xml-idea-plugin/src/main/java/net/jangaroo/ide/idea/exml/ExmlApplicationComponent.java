package net.jangaroo.ide.idea.exml;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.javaee.ExternalResourceManager;
import org.jetbrains.annotations.NotNull;

/**
 * The EXML plugin's global configuration.
 * Adds all required resources: XML Schemata for EXML 0.1 and for the standard Ext JS 3 components.
 */
public class ExmlApplicationComponent implements ApplicationComponent {
  public static final String EXT3_XSD_URI = "http://extjs.com/ext3";

  @NotNull
  public String getComponentName() {
    return "exmlGlobals";
  }

  public void initComponent() {
    FileTypeManager ftm = FileTypeManager.getInstance();
    ftm.registerFileType(ftm.getFileTypeByExtension("xml"), "exml");
    ExternalResourceManager erm = ExternalResourceManager.getInstance();
    erm.addStdResource("http://net.jangaroo.com/extxml/0.1", "/net/jangaroo/extxml/schemas/extxml.xsd", getClass());
    erm.addStdResource(EXT3_XSD_URI, "/ext3.xsd", getClass());
  }

  public void disposeComponent() {
    ExternalResourceManager erm = ExternalResourceManager.getInstance();
    erm.removeResource("http://net.jangaroo.com/extxml/0.1", "/net/jangaroo/extxml/schemas/extxml.xsd");
    erm.removeResource(EXT3_XSD_URI, "/ext3.xsd");
  }
}
