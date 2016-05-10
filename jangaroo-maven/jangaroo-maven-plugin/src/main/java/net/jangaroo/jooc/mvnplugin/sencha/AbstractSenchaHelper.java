package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaConfigBuilder;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * A base class for all Sencha helpers, storing Maven project, Sencha configuration, and log.
 */
abstract class AbstractSenchaHelper<T extends SenchaConfiguration, U extends SenchaConfigBuilder> {
  private final MavenProject project;
  private final T senchaConfiguration;
  private final Log log;

  public AbstractSenchaHelper(MavenProject project, T senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;
  }

  protected MavenProject getProject() {
    return project;
  }

  protected T getSenchaConfiguration() {
    return senchaConfiguration;
  }

  protected Log getLog() {
    return log;
  }

  protected Map<String, Object> getConfig() throws MojoExecutionException {
    U configBuilder = createSenchaConfigBuilder();
    String defaultsJsonFileName = getDefaultsJsonFileName();
    try {
      configBuilder.defaults(defaultsJsonFileName);
    } catch (IOException e) {
      throw new MojoExecutionException("could not read " + defaultsJsonFileName, e);
    }
    configure(configBuilder);
    return configBuilder.build();
  }

  protected void writeJson(File jsonFile) throws MojoExecutionException {
    writeJson(jsonFile, null);
  }

  protected void writeJson(File jsonFile, String comment) throws MojoExecutionException {
    try {
      PrintWriter pw = new PrintWriter(new FileWriter(jsonFile), false);
      if (comment != null) {
        pw.println("/**");
        pw.println(" * " + comment);
        pw.println(" */");
      }
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(pw, getConfig());
    } catch (IOException e) {
      throw new MojoExecutionException("Could not write " + jsonFile, e);
    }
  }

  protected abstract U createSenchaConfigBuilder();

  protected abstract void configure(U configBuilder) throws MojoExecutionException;

  protected abstract String getDefaultsJsonFileName();
}
