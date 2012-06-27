package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.util.ArrayList;

public class PhantomJsTestRunner {

  private static final String JANGAROO_MAVEN_PLUGIN = "jangaroo-maven-plugin";
  private final String testSuite;
  private final String args;
  private final File testOutputDirectory;
  private final String phantomjs;
  private final Log log;
  private final String testRunner;
  private final int timeout;

  private final StringBuffer result = new StringBuffer();

  /**
   * @param phantomjs           the binary to execute
   * @param testSourceDirectory the directory containing the classes to test
   * @param testRunner          the test runner script to be loaded in phantomjs
   * @param testSuite           the test suite class to run
   * @param phantomArgs         additional arguments to be passed to the phantomjs runner script
   * @param timeout             timeout in seconds
   * @param log                 the maven log
   */
  public PhantomJsTestRunner(String phantomjs, File testSourceDirectory, String testRunner, String testSuite, String phantomArgs, int timeout, Log log) {
    this.phantomjs = phantomjs;
    this.testOutputDirectory = testSourceDirectory;
    this.testRunner = testRunner;
    this.testSuite = testSuite;
    this.args = phantomArgs;
    this.timeout = timeout;
    this.log = log;
  }

  public boolean execute() throws CommandLineException {
    final Commandline cmd = createCommandLine();
    final ArrayList<String> arguments = new ArrayList<String>();
    arguments.add(testRunner);
    final String startString = '<' + JANGAROO_MAVEN_PLUGIN + '>';
    final String endString = "</" + JANGAROO_MAVEN_PLUGIN + '>';
    final StringBuilder argString = new StringBuilder("(function(c){")
            .append("if(!c['testSuiteName']){ c['testSuiteName'] = '").append(testSuite).append("';}")
            .append("if(!c['timeout']){ c['timeout'] = ").append(timeout).append(";}")
            .append("if(!c['outputTestResult']){ c['outputTestResult'] = function(s){")
            .append(" if(!joo._outputTestResult){ joo._outputTestResult = function(s){console.error(s)};}")
            .append(" joo._outputTestResult('").append(startString).append("'+s+'").append(endString).append("');}")
            .append("};")
            .append("return c;})").append('(');
    if (args != null) {
      argString.append(args.replace('\n', ' ')); // phantomjs doesn't like new lines in config object argument
    } else {
      argString.append("{}");
    }
    argString.append(')');

    //TODO:this is very ugly, we should rethink this javascript cmdline stuff.
    String argsFormated;
    if (Os.isFamily(Os.FAMILY_UNIX)) {
      argsFormated = argString.toString().replace("'", "\"");
    } else {
      argsFormated = argString.toString();
    }

    arguments.add(argsFormated);
    cmd.addArguments(arguments.toArray(new String[arguments.size()]));

    final StreamConsumer consumer = new StreamConsumer() {
      int state = 0;

      @Override
      public void consumeLine(String line) {
        if (state == 0) { // not started
          final int index = line.indexOf(startString);
          if (index > -1) {
            result.append(line.substring(index + startString.length())).append('\n');
            state++;
          } else {
            log.info(line);
          }
        } else if (state == 1) {
          final int index = line.indexOf(endString);
          if (index > -1) {
            result.append(line.substring(0, index));
            state++;
          } else {
            result.append(line);
          }
          result.append('\n');
        } else {
          log.info(line);
        }
      }
    };
    log.info("executing phantomjs cmd: " + cmd.toString());
    int returnCode = CommandLineUtils.executeCommandLine(cmd, consumer, consumer, timeout);
    return 0 == returnCode && result.length() > 0;
  }

  public boolean isTestAvailable() {
    return testOutputDirectory != null && (testSuiteExists() || testsHtmlExists());
  }

  private boolean testsHtmlExists() {
    final File file = new File(testOutputDirectory, "tests.html");
    final boolean exists = file.exists();
    if (!exists) {
      log.warn("cannot find " + file.getAbsolutePath());
    }
    return exists;
  }

  private boolean testSuiteExists() {
    boolean exists = false;
    if (testSuite != null) {
      final String fileName = testSuite.replace(".", "/");
      final File file = new File(testOutputDirectory, "/joo/classes/" + fileName + ".js");
      exists = file.exists();
      if (!exists) {
        log.warn("cannot find test suite: " + file.getAbsolutePath());
      }
    }
    return exists;
  }

  public boolean canRun() {
    return
            testOutputDirectory != null && testOutputDirectory.exists() &&
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
    commandline.setWorkingDirectory(testOutputDirectory);
    return commandline;
  }

  @Override
  public String toString() {
    return "PhantomJsTestRunner{" +
            "phantomjs=" + phantomjs +
            ", testRunner='" + testRunner + '\'' +
            ", testSuite='" + testSuite + '\'' +
            ", args='" + args + '\'' +
            ", testOutputDirectory=" + testOutputDirectory +
            '}';
  }

  public String getTestResult() {
    return result.toString();
  }
}
