package net.jangaroo.extxml.file;

import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentType;
import net.jangaroo.extxml.Log;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * The FileScanner scans a directory for all *.as, *.js and *.exml files that contain Ext annotations and builds a
 * {@link net.jangaroo.extxml.ComponentSuite} from all Ext JS component classes.
 */
public final class SrcFileScanner {

  private ComponentSuite componentSuite;

  public SrcFileScanner(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public void scan() throws IOException {
    scan(componentSuite.getRootDir());

    //resolving all super classes
   componentSuite.resolveSuperClasses();
  }

  private void scan(final File dir) throws IOException {
    FileSet srcFiles = new FileSet();
    srcFiles.setDirectory(dir.getAbsolutePath());
    srcFiles.addInclude("**/*." + ComponentType.JavaScript.getExtension());
    srcFiles.addInclude("**/*." + ComponentType.ActionScript.getExtension());
    srcFiles.addInclude("**/*." + ComponentType.EXML.getExtension());
    for (String srcFileRelativePath : new FileSetManager().getIncludedFiles(srcFiles)) {
      File srcFile = new File(dir, srcFileRelativePath);
      Log.getErrorHandler().setCurrentFile(srcFile);
      
      ComponentType type = ComponentType.from(FileUtils.extension(srcFile.getName()));

      if (ComponentType.EXML.equals(type)) {
        ExmlComponentSrcFileScanner.scan(componentSuite, srcFile, type);
      } else {
        //parse AS3 or JS files
        ExtComponentSrcFileScanner.scan(componentSuite, srcFile, type);
      }
    }
  }
}
