package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.util.ArrayList;

public class PhantomJsTestRunner  {

  private final String testSuite;
  private final String args;
  private final File testOutputDirectory;
  private final File phantomjs;
  private final Log log;
  private final String testRunner;
  private final int timeout;

  /**
   * @param phantomjs the binary to execute
   * @param testOutputDirectory the directory containing the classes to test
   * @param testRunner the test runner script to be loaded in phantomjs
   * @param testSuite the test suite class to run
   * @param args additional arguments to be passed to the phantomjs runner script
   * @param timeout timeout in seconds
   * @param log
   */
  public PhantomJsTestRunner(File phantomjs, File testOutputDirectory, String testRunner, String testSuite, String args, int timeout, Log log) {
    this.phantomjs = phantomjs;
    this.testOutputDirectory = testOutputDirectory;
    this.testRunner = testRunner;
    this.testSuite = testSuite;
    this.log = log;
    this.timeout = timeout;
    this.args = args;
  }

  public boolean execute() throws CommandLineException {
    final Commandline cmd = new Commandline();
    cmd.setExecutable(phantomjs.getAbsolutePath());
    cmd.setWorkingDirectory(testOutputDirectory);
    final ArrayList<String> arguments = new ArrayList<String>();
    arguments.add(testRunner);
    final StringBuffer argString = new StringBuffer("(function(c){")
            .append("c['testSuiteName'] = '").append(testSuite).append("';")
            .append("c['timeout'] = ").append(timeout).append(';')
            .append("return c;})").append('(');
    if(args != null){
      argString.append(args.replace('\n',' ')); // phantomjs doesn't like new lines in config object argument
    } else {
      argString.append("{}");
    }
    argString.append(')');

    arguments.add(argString.toString());
    cmd.addArguments(arguments.toArray(new String[arguments.size()]));

    final StreamConsumer consumer = new StreamConsumer() {
      @Override
      public void consumeLine(String line) {
        log.info(line);
      }
    };
    log.info("executing phantomjs cmd: "+cmd.toString());
    return 0 == CommandLineUtils.executeCommandLine(cmd, consumer, consumer, timeout);
  }

  public boolean isTestAvailable() {
    if(testOutputDirectory != null && testSuite != null){
      final String fileName = testSuite.replace(".", "/");
      final File file = new File(testOutputDirectory, "/joo/classes/" + fileName + ".js");
      final boolean exists = file.exists();
      if(!exists){
        log.warn("cannot find test suite: "+ file.getAbsolutePath());
      }
      return exists;
    }
    return false;
  }

  public boolean canRun() {
    return
            testOutputDirectory != null && testOutputDirectory.exists() &&
            phantomjs != null && phantomjs.canExecute() &&
            testRunner != null && testSuite != null;
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
}
