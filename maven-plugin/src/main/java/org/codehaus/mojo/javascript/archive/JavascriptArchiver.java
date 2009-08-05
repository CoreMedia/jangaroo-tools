package org.codehaus.mojo.javascript.archive;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

/**
 * Custom archiver for javascript dependencies, packaged as "jsar" (JavaScript
 * ARchive), that are simply a jar of scripts and resources.
 * 
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 * @plexus.component role="org.codehaus.plexus.archiver.Archiver"
 * role-hint="javascript" instantiation-strategy="per-lookup"
 */
public class JavascriptArchiver
    extends JarArchiver
{
    /**
     *
     */
    public JavascriptArchiver()
    {
        super();
        archiveType = Types.JAVASCRIPT_EXTENSION;
    }

    public void createDefaultManifest( MavenProject project )
        throws ManifestException, IOException, ArchiverException
    {
        Manifest manifest = new Manifest();
        Manifest.Attribute attr = new Manifest.Attribute( "Created-By", "Apache Maven" );
        manifest.addConfiguredAttribute( attr );
        attr = new Manifest.Attribute( "Implementation-Title", project.getName() );
        manifest.addConfiguredAttribute( attr );
        attr = new Manifest.Attribute( "Implementation-Version", project.getVersion() );
        manifest.addConfiguredAttribute( attr );
        attr = new Manifest.Attribute( "Implementation-Vendor-Id", project.getGroupId() );
        manifest.addConfiguredAttribute( attr );
        if ( project.getOrganization() != null )
        {
            String vendor = project.getOrganization().getName();
            attr = new Manifest.Attribute( "Implementation-Vendor", vendor );
            manifest.addConfiguredAttribute( attr );
        }
        attr = new Manifest.Attribute( "Built-By", System.getProperty( "user.name" ) );
        manifest.addConfiguredAttribute( attr );

        File mf = File.createTempFile( "maven", ".mf" );
        mf.deleteOnExit();
        PrintWriter writer = new PrintWriter( new FileWriter( mf ) );
        manifest.write( writer );
        writer.close();
        setManifest( mf );
    }
}
