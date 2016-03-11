package net.jangaroo.jooc.mvnplugin.sencha.executor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class SenchaCmdExecutorTest {

  @Mock
  File workingDirectory;

  @Mock
  Log log;

  @Test
  public void testExecuteSuccess() throws Exception {
    SenchaCmdExecutor senchaCmdExecutor = new TestableSenchaCmdExecutor(workingDirectory, "version", log);
    try {
      senchaCmdExecutor.execute();
    } catch (MojoExecutionException e) {
      Assert.fail("Should not happen!");
    }
  }

  private class TestableSenchaCmdExecutor extends SenchaCmdExecutor {

    public TestableSenchaCmdExecutor(File workingDirectory, String arguments, Log log) {
      super(workingDirectory, arguments, log);
    }

    protected CommandLine getCommandLine(String line) {
      CommandLine mock = Mockito.mock(CommandLine.class);
      Mockito.when(mock.toString()).thenReturn(line);
      return mock;
    }

    protected Executor getExecutor() {
      return Mockito.mock(Executor.class);
    }

    protected ExecuteWatchdog getExecuteWatchdog() {
      return new ExecuteWatchdog(MAX_EXECUTION_TIME);
    }

    protected SenchaCmdLogOutputStream getSenchaCmdLogOutputStream(ExecuteWatchdog watchdog) {
      return new SenchaCmdLogOutputStream(watchdog, log);
    }

    protected PumpStreamHandler getPumpStreamHandler(SenchaCmdLogOutputStream outputStream) {
      return new PumpStreamHandler(outputStream);
    }
  }
}