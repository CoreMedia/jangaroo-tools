package net.jangaroo.jooc.util;

import net.jangaroo.jooc.input.InputSource;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class for joo.flex that parses an "include" directive
 * and returns a Reader for the included file.
 * It can handle relative paths and line ranges.
 */
public class IncludeEvaluator {

  private static final int BEGIN_INDEX = "include \"".length();
  private static final Pattern FILENAME_WITH_LINE_RANGE_PATTERN = Pattern.compile("^(.*):([0-9]+),([0-9]+)$");

  public static Reader createReader(String includeDirective, InputSource source) throws IOException {
    String filename = includeDirective.substring(BEGIN_INDEX, includeDirective.length() - 1);
    Matcher matcher = FILENAME_WITH_LINE_RANGE_PATTERN.matcher(filename);
    boolean hasLineRange = matcher.matches();
    if (hasLineRange) {
      filename = matcher.group(1);
    }
    File file = new File(filename);
    InputStream in = null;
    if (!file.exists() && !file.isAbsolute()) {
      InputSource parent = source.getParent();
      InputSource input = parent.getChild(filename);
      if (input == null) {
        throw new IOException("cannot find input file " + source.getPath() + "/" + filename);
      }
      in = input.getInputStream();
    }
    if (in == null) {
      in = new FileInputStream(file);
    }
    Reader result = new InputStreamReader(in, "UTF-8");
    if (hasLineRange) {
      int startLine = Integer.parseInt(matcher.group(2)) + 1;
      int endLine = Integer.parseInt(matcher.group(3));
      result = new LineRangeReader(result, startLine, endLine);
    }
    return result;
  }
}
