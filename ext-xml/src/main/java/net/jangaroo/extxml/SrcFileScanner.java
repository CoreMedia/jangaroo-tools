package net.jangaroo.extxml;

import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.IOException;

/**
 * The FileScanner scans a directory for all *.as and *.js files that contain Ext annotations and builds a
 * {@link net.jangaroo.extxml.ComponentSuite} from all Ext JS component classes.
 */
public class SrcFileScanner {

  public SrcFileScanner(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public void scan() throws IOException {
    scan(componentSuite.getRootDir());
    componentSuite.resolveSuperClasses();
  }

  private void scan(final File dir) throws IOException {
    FileSet srcFiles = new FileSet();
    srcFiles.setDirectory(dir.getAbsolutePath());
    srcFiles.addInclude("**/*.js");
    srcFiles.addInclude("**/*.as");
    for (String srcFileRelativePath : new FileSetManager().getIncludedFiles(srcFiles)) {
      ExtComponentSrcFileScanner.scan(componentSuite, new File(dir, srcFileRelativePath));
    }
  }

  private ComponentSuite componentSuite;
}
