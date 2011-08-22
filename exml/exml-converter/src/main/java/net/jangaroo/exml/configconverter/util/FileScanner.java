package net.jangaroo.exml.configconverter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * An awk-like file processor that scans a text file line by line and dispatches to
 * processing functions based on regular expressions.
 */
public class FileScanner<State> {

  public FileScanner<State> add(Rule<State> rule) {
    rules.add(rule);
    return this;
  }

  public State scan(File file, State state) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")));
      String line;
      while ((line = reader.readLine()) != null) {
        for (Rule<State> rule : rules) {
          Matcher matcher = rule.createMatcher(line);
          if (matcher.find()) {
            List<String> groups = new ArrayList<String>(matcher.groupCount());
            for (int i = 1; i <= matcher.groupCount(); ++i) {
              groups.add(matcher.group(i));
            }
            rule.matched(state, groups);
            break;
          }
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return state;
  }

  private List<Rule<State>> rules = new ArrayList<Rule<State>>();
}
