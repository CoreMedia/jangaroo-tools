package net.jangaroo.jooc.util;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A helper class for joo.flex that parses an "include" directive
 * and returns a Reader for the included file.
 * It can handle relative paths and line ranges.
 */
public class IncludeEvaluator {

  private static final int BEGIN_INDEX = "include \"".length();
  private static final Pattern FILENAME_WITH_LINE_RANGE_PATTERN = Pattern.compile("^(.*):([0-9]+),([0-9]+)$");

  public static Reader createReader(String includeDirective, String sourceFilename) throws IOException {
    String filename = includeDirective.substring(BEGIN_INDEX, includeDirective.length() - 1);
    Matcher matcher = FILENAME_WITH_LINE_RANGE_PATTERN.matcher(filename);
    boolean hasLineRange = matcher.matches();
    if (hasLineRange) {
      filename = matcher.group(1);
    }
    File file = new File(filename);
    if (!file.isAbsolute()) {
      File sourceDir = new File(sourceFilename).getAbsoluteFile().getParentFile();
      file = new File(sourceDir,filename);
    }
    Reader result = new InputStreamReader(new FileInputStream(file), "UTF-8");
    if (hasLineRange) {
      int startLine = Integer.parseInt(matcher.group(2))+1;
      int endLine = Integer.parseInt(matcher.group(3));
      result = new LineRangeReader(result, startLine, endLine);
    }
    return result;
  }
}
