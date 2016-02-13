package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import java.util.Map;

public class MetadataConfigurer implements Configurer {

  static final String NAME = "name";
  static final String VERSION = "version";
  static final String CREATOR = "creator";
  static final String SUMMARY = "summary";

  private MavenProject project;

  public MetadataConfigurer(MavenProject project) {
    this.project = project;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(NAME, SenchaUtils.getSenchaPackageNameForMavenProject(project));
    config.put(VERSION, SenchaUtils.getSenchaVersionForProject(project));
    config.put(CREATOR, StringUtils.defaultString(project.getOrganization() != null ? project.getOrganization().getName() : ""));
    config.put(SUMMARY, StringUtils.defaultString(project.getDescription()));
  }

}
