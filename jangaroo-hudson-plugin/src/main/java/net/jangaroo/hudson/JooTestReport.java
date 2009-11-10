package net.jangaroo.hudson;

import hudson.maven.AggregatableAction;
import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.BuildListener;
import hudson.tasks.junit.TestResult;
import hudson.tasks.junit.TestResultAction;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public class JooTestReport extends TestResultAction implements AggregatableAction {
  JooTestReport(MavenBuild build, TestResult result, BuildListener listener) {
    super(build, result, listener);
  }

  public MavenAggregatedReport createAggregatedAction(MavenModuleSetBuild build, Map<MavenModule, List<MavenBuild>> moduleBuilds) {
    return new JooTestAggregatedReport(build);
  }
}
