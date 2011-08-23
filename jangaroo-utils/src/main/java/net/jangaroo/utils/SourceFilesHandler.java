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
 *
 */
public class SourceFilesHandler extends OptionHandler<List<File>> {

  public SourceFilesHandler(CmdLineParser parser, OptionDef option, Setter<? super List<File>> setter) {
    super(parser, option, setter);
  }

  @Override
  public int parseArguments(Parameters parameters) throws CmdLineException {
    List<File> sourceFiles = new ArrayList<File>(parameters.size());
    for(int i = 0; i<parameters.size(); i++) {
      String sourcePath = parameters.getParameter(i);
       // be tolerant, accept also '/' as file separator
        File sourceFile = new File(sourcePath.replace('/', File.separatorChar));
        if (!sourceFile.exists()) {
          throw new IllegalArgumentException("Source file does not exist: " + sourceFile.getAbsolutePath());
        }
      sourceFiles.add(sourceFile);

    }
    setter.addValue(sourceFiles);
    return parameters.size();
  }

  @Override
  public String getDefaultMetaVariable() {
    return "SOURCE FILES";
  }
}
