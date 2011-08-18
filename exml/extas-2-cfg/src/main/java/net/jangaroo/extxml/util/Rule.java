package net.jangaroo.extxml.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;

/**
 * A Rule defines a regular expression ({@link java.util.regex.Pattern}) and code to be executed
 * when a line matches that pattern. The code will be invoked with the current State.
 */
public abstract class Rule<State> {

  public Rule(String pattern) {
    this.pattern = Pattern.compile(pattern,0);
  }

  public Matcher createMatcher(String line) {
    return pattern.matcher(line);
  }

  public abstract void matched(State state, List<String> groups);

  private Pattern pattern;
}
