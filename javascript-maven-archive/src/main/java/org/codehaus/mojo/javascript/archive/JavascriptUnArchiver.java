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

import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;

/**
 * Custom archiver for javascript dependencies, packaged as "jsar" (JavaScript
 * ARchive), that are simply a jar of scripts and resources.
 * 
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 * @plexus.component role="org.codehaus.plexus.archiver.UnArchiver"
 * role-hint="javascript"
 */
public class JavascriptUnArchiver
    extends ZipUnArchiver
{
    /**
     *
     */
    public JavascriptUnArchiver()
    {
        super();
    }
	
    /**
     * overwrite the super.extract() to set the default filter.
     */
	public void extract() throws ArchiverException, IOException 
	{
        setArchiveFilters( Collections.singletonList( new ArchiveFileFilter()
        {
            public boolean include( InputStream dataStream, String entryName )
                throws ArchiveFilterException
            {
                return !entryName.startsWith( "META-INF" );
            }
        } ) );
		super.extract();
	}
}
