package net.jangaroo.extxml;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * The FileScanner scans a directory for all *.as and *.js files that contain Ext annotations and builds a
 * {@link net.jangaroo.extxml.ComponentSuite} from all Ext JS component classes.
 */
public class SrcFileScanner {

  private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {
    public boolean accept(File pathname) {
      return pathname.isDirectory();
    }
  };
  private static final FilenameFilter SRC_FILE_FILTER = new FilenameFilter() {
    public boolean accept(File dir, String name) {
      return name.endsWith(".js") || name.endsWith(".as");
    }
  };

  public SrcFileScanner(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public void scan() {
    scan(componentSuite.getRootDir());
    componentSuite.resolveSuperClasses();
  }

  private void scan(File dir) {
    for (File srcFile : dir.listFiles(SRC_FILE_FILTER)) {
      ExtComponentSrcFileScanner.scan(componentSuite, srcFile);
    }
    for (File subdir : dir.listFiles(DIRECTORY_FILE_FILTER)) {
      scan(subdir);
    }
  }

  private ComponentSuite componentSuite;
}
