package net.jangaroo.utils;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses file arguments, separated by the system dependant path separator character (e.g. ':' on Unix systems, ';' on Windows)
 */
public class PathHandler extends OptionHandler<List<File>> {

  public PathHandler(CmdLineParser parser, OptionDef option, Setter<? super List<File>> setter) {
    super(parser, option, setter);
  }

  @Override
  public int parseArguments(Parameters parameters) throws CmdLineException {
    List<File> sourcePathFiles = null;
    String sourcePathString = parameters.getParameter(0).trim();
    if (!sourcePathString.isEmpty()) {
      final String[] sourceDirs = sourcePathString.split("\\Q" + File.pathSeparatorChar + "\\E");
      sourcePathFiles = new ArrayList<File>(sourceDirs.length);
      for (String sourceDirPath : sourceDirs) {
        // be tolerant, accept also '/' as file separator
        File sourceDir = new File(sourceDirPath.replace('/', File.separatorChar));
        if (!sourceDir.exists()) {
          throw new CmdLineException(owner, "directory or file does not exist: " + sourceDir.getAbsolutePath());
        }
        sourcePathFiles.add(sourceDir);
      }
    }
    if(sourcePathFiles == null) {
      sourcePathFiles = new ArrayList<File>();
    }
    setter.addValue(sourcePathFiles);
    return 1;
  }

  @Override
  public String getDefaultMetaVariable() {
    return "PATH";  
  }
}
