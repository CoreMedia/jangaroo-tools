package net.jangaroo.ide.idea.exml;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.javaee.ExternalResourceManager;
import org.jetbrains.annotations.NotNull;

/**
 * The EXML plugin's global configuration.
 * Adds *.exml file config to be treated as XML.
 */
public class ExmlApplicationComponent implements ApplicationComponent {

  @NotNull
  public String getComponentName() {
    return "exmlGlobals";
  }

  public void initComponent() {
    FileTypeManager ftm = FileTypeManager.getInstance();
    ftm.registerFileType(ftm.getFileTypeByExtension("xml"), "exml");
//import com.intellij.psi.filters.AndFilter;
//import com.intellij.psi.filters.ClassFilter;
//import com.intellij.psi.filters.position.TargetNamespaceFilter;
//import com.intellij.psi.meta.MetaDataRegistrar;
//import com.intellij.psi.xml.XmlDocument;
//    MetaDataRegistrar.getInstance().registerMetaData(
//      new AndFilter(
//        new ClassFilter(XmlDocument.class),
//        new TargetNamespaceFilter(EXML_NAMESPACE_URI)
//      ),
//      XmlNSDescriptor.class
//    );
  }

  public void disposeComponent() {
  }
}
