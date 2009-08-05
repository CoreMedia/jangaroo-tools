package net.jangaroo.jooc.mvnplugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

/**
 * Goal that prepares scripts for packaging as a web application.
 *
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase compile
 */
public class WarPackageMojo
        extends AbstractMojo {


  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  /**
   * The directory where the webapp is built.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;

  /**
   * The folder in webapp for javascripts
   *
   * @parameter expression="${scripts}" default-value="scripts"
   */
  private String scriptsDirectory;

  /**
   * The folder for javascripts dependencies
   *
   * @parameter expression="${scripts}" default-value="lib"
   */
  private String libsDirectory;

  /**
   * Use the artifactId as folder
   *
   * @parameter default-value="true"
   */
  private boolean useArtifactId;

  /**
   * @component
   */
  private JavascriptArtifactManager javascriptArtifactManager;

  /**
   * {@inheritDoc}
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute()
          throws MojoExecutionException, MojoFailureException {
    File outputDirectory = new File(webappDirectory, scriptsDirectory);

    try {
      getLog().error("before exclude");
      excludeFromWarPackaging();
      getLog().error("before unpack");
      javascriptArtifactManager.unpack(project, DefaultArtifact.SCOPE_RUNTIME,
              new File(webappDirectory, scriptsDirectory + "/" + libsDirectory), useArtifactId);
      getLog().error("after unpack");
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    }
  }


  /**
   * Exclude all artifacts that have been depended with type 'jangaroo'. Since IntelliJ IDEA cannot
   * import jangaroo artifacts we need to import them twice. (a) with type jangaroo and (b) with no type
   * (defaulting to jar). Everything is fine except that these dependencies are included into WEB-INF/lib.
   * By manipulating the configuration of the war plugin we add these artifacts to the packagingExclude
   * property. !!! BAD BAD HACK !!!
   */
  private void excludeFromWarPackaging() {
    getLog().info("excludeFromWarPackaging");
    String pluginGroupId = "org.apache.maven.plugins";
    String pluginArtifactId = "maven-war-plugin";
    if (project.getBuildPlugins() != null) {
      for (Object o : project.getBuildPlugins()) {
        Plugin plugin = (Plugin) o;

        if (pluginGroupId.equals(plugin.getGroupId()) && pluginArtifactId.equals(plugin.getArtifactId())) {
          Xpp3Dom dom = (Xpp3Dom) plugin.getConfiguration();
          if (dom == null) {
            dom = new Xpp3Dom("configuration");
            plugin.setConfiguration(dom);
          }
          Xpp3Dom excludes = dom.getChild("packagingExcludes");
          if (excludes == null) {
            excludes = new Xpp3Dom("packagingExcludes");
            dom.addChild(excludes);
            excludes.setValue("");
          } else if (excludes.getValue().trim().length() > 0) {
            excludes.setValue(excludes.getValue() + ",");
          }

          Set dependencies = project.getArtifacts();
          getLog().debug("Size of getArtifacts: " + dependencies.size());
          String additionalExcludes = "";
          for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
            Artifact dependency = (Artifact) iterator.next();
            getLog().error("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
            if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
              getLog().info("Excluding jangaroo dependency form war plugin [" + dependency.toString() + "]");
              // Add two excludes. The first one is effective when no nameclash occcurs
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
              // the second when a nameclash occurs (artifact will hav groupId prepended before copying it into the lib dir)
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getGroupId() + "-" + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
            }
          }
          excludes.setValue(excludes.getValue() + additionalExcludes);
        }
      }
    }
  }
}
