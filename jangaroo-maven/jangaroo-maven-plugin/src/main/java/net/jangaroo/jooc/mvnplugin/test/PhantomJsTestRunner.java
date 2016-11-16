package net.jangaroo.jooc.mvnplugin.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.codehaus.plexus.util.StringUtils.isBlank;

public class PhantomJsTestRunner {

  public static final Pattern LOG_LEVEL_PATTERN = Pattern.compile("^\\[([A-Z]+)\\]\\s*(.*)$");

  private static volatile Boolean phantomjsExecutableFound;

  private final String testPageUrl;
  private String testResultFilename;
  private final String phantomjs;
  private final Log log;
  private final String testRunner;
  private final int timeout;
  private final int maxRetriesOnCrashes;

  /**
   * @param phantomjs           the binary to execute
   * @param testPageUrl         the URL of the test page to load
   * @param testResultFilename  the file to write the test result into
   * @param testRunner          the test bootstrap script to be loaded in phantomjs
   * @param timeout             timeout in seconds
   * @param maxRetriesOnCrashes number of retries when receiving unexpected result from phantomjs (crash?)
   * @param log                 the maven log
   */
  public PhantomJsTestRunner(String phantomjs, String testPageUrl, String testResultFilename, String testRunner, int timeout, int maxRetriesOnCrashes, Log log) {
    this.phantomjs = makeOsSpecific(phantomjs);
    this.testPageUrl = testPageUrl;
    this.testResultFilename = testResultFilename;
    this.testRunner = testRunner;
    this.timeout = timeout;
    this.maxRetriesOnCrashes = maxRetriesOnCrashes;
    this.log = log;
  }

  private static String makeOsSpecific(String phantomjs) {
    if(SystemUtils.IS_OS_WINDOWS) {
      if(!isBlank(phantomjs) && phantomjs.charAt(phantomjs.length() - 4) != '.') {
        return phantomjs +  ".exe";
      }
    }
    return phantomjs;
  }

  public boolean execute() throws CommandLineException {
    final Commandline cmd = createCommandLine();
    final ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("--debug=true");
    arguments.add("--web-security=false");
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
    for (int tryCount = 0; tryCount <= maxRetriesOnCrashes; ++tryCount) {
      int returnCode = CommandLineUtils.executeCommandLine(cmd, outConsumer, errConsumer, timeout);
      if (returnCode >= 0 && returnCode <= 4) { // valid phantomjs-joounit-page-runner return codes!
        return returnCode == 0;
      }
      log.warn(String.format("unexpected result %d from phantomjs run #%d", returnCode, tryCount + 1));
    }
    log.error(String.format("Got %d unexpected results from phantomjs, giving up.", maxRetriesOnCrashes + 1));
    return false;
  }

  public boolean canRun() {
    return
            testResultFilename != null &&
                    testRunner != null &&
                    phantomjs != null &&
                    isExecutableAvailable();
  }

  private boolean isExecutableAvailable() {
    if(phantomjsExecutableFound == null) {
      synchronized (PhantomJsTestRunner.class) {
        phantomjsExecutableFound = (new File(phantomjs).canExecute() || canExecutePhantomJs());
      }
    }
    return phantomjsExecutableFound;
  }

  private boolean canExecutePhantomJs() {
    final String path = System.getenv("PATH");
    final String[] directoryNames = StringUtils.split(path, File.pathSeparatorChar);
    for (String directoryName : directoryNames) {
      final File[] files = new File(directoryName).listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return phantomjs.equals(name);
        }
      });
      if(!(files == null || files.length == 0)) {
        return true;
      }
    }
    log.warn(phantomjs + " not found in " + path);
    return false;
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
