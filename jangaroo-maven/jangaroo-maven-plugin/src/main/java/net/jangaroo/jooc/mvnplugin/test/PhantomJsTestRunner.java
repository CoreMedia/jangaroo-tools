package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhantomJsTestRunner {

  public static final Pattern LOG_LEVEL_PATTERN = Pattern.compile("^\\[([A-Z]+)\\]\\s*(.*)$");
  private final String testPageUrl;
  private String testResultFilename;
  private final String phantomjs;
  private final Log log;
  private final String testRunner;
  private final int timeout;

  /**
   * @param phantomjs           the binary to execute
   * @param testPageUrl         the URL of the test page to load
   * @param testResultFilename  the file to write the test result into
   * @param testRunner          the test bootstrap script to be loaded in phantomjs
   * @param timeout             timeout in seconds
   * @param log                 the maven log
   */
  public PhantomJsTestRunner(String phantomjs, String testPageUrl, String testResultFilename, String testRunner, int timeout, Log log) {
    this.phantomjs = phantomjs;
    this.testPageUrl = testPageUrl;
    this.testResultFilename = testResultFilename;
    this.testRunner = testRunner;
    this.timeout = timeout;
    this.log = log;
  }

  public boolean execute() throws CommandLineException {
    final Commandline cmd = createCommandLine();
    final ArrayList<String> arguments = new ArrayList<String>();
    arguments.add(testRunner);

    arguments.add(testPageUrl);
    arguments.add(testResultFilename);
    arguments.add(String.valueOf(timeout));
    cmd.addArguments(arguments.toArray(new String[arguments.size()]));

    final StreamConsumer outConsumer = new StreamConsumer() {
      @Override
      public void consumeLine(String line) {
        Matcher matcher = LOG_LEVEL_PATTERN.matcher(line);
        String logLevel;
        String msg;
        if (matcher.matches()) {
          logLevel = matcher.group(1);
          msg = matcher.group(2);
        } else {
          logLevel = "DEBUG";
          msg = line;
        }
        if ("ERROR".equals(logLevel)) {
          log.error(msg);
        } else if ("WARN".equals(logLevel)) {
          log.warn(msg);
        } else if ("INFO".equals(logLevel)) {
          log.info(msg);
        } else {
          log.debug(msg);
        }
      }
    };
    final StreamConsumer errConsumer = new StreamConsumer() {
      @Override
      public void consumeLine(String line) {
        log.warn(line);
      }
    };
    log.info("executing phantomjs cmd: " + cmd.toString());
    int returnCode = CommandLineUtils.executeCommandLine(cmd, outConsumer, errConsumer, timeout);
    return returnCode == 0;
  }

  public boolean canRun() {
    return
            testResultFilename != null &&
                    testRunner != null &&
                    phantomjs != null &&
                    (new File(phantomjs).canExecute() || canExecutePhantomJs());
  }

  private boolean canExecutePhantomJs() {
    final Commandline commandline = createCommandLine();
    commandline.addArguments(new String[]{"--version"});
    final StringBuilder buffer = new StringBuilder();
    final StreamConsumer consumer = new StreamConsumer() {
      @Override
      public void consumeLine(String line) {
        buffer.append(line);

      }
    };
    boolean canExecute = false;
    try {
      canExecute = CommandLineUtils.executeCommandLine(commandline, consumer, consumer, 1) == 0;
      if (canExecute) {
        log.info("Found phantomjs version: " + buffer.toString());
      } else {
        log.info("Cannot determine phantomjs version: " + buffer.toString());
      }
    } catch (CommandLineException e) {
      log.error("cannot execute phantomjs", e);
    }
    return canExecute;
  }

  private Commandline createCommandLine() {
    final Commandline commandline = new Commandline();
    commandline.setExecutable(phantomjs);
    return commandline;
  }

  @Override
  public String toString() {
    return "PhantomJsTestRunner{" +
            "phantomjs=" + phantomjs +
            ", testRunner='" + testRunner + '\'' +
            ", testPageUrl='" + testPageUrl + '\'' +
            ", testResultFilename=" + testResultFilename +
            '}';
  }

}
