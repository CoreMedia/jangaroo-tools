package net.jangaroo.jooc.mvnplugin.test;

import org.apache.commons.lang.ArrayUtils;
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

  public PhantomJsTestRunner(File phantomjs, File testOutputDirectory, String testRunner, String testSuite, String args, Log log) {
    this.phantomjs = phantomjs;
    this.testOutputDirectory = testOutputDirectory;
    this.testRunner = testRunner;
    this.testSuite = testSuite;
    this.log = log;
    this.args = args;
  }

  public boolean execute() throws CommandLineException {
    Commandline cmd = new Commandline();
    cmd.setExecutable(phantomjs.getAbsolutePath());
    cmd.setWorkingDirectory(testOutputDirectory);
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add(testRunner);
    arguments.add("test="+testSuite);
    if(args != null){
      arguments.add(args);
    }
    cmd.addArguments(arguments.toArray(new String[arguments.size()]));

    final StreamConsumer consumer = new StreamConsumer() {
      @Override
      public void consumeLine(String line) {
        log.info(line);
      }
    };
    return 0 == CommandLineUtils.executeCommandLine(cmd, consumer, consumer);
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
