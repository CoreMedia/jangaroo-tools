package net.jangaroo.webjars.mvnplugin.util;

import net.jangaroo.webjars.mvnplugin.UnpackJarResourcesMojo;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.AbstractZipUnArchiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * A ZipUnArchiver for JARs with META-INF/resources/, using only the sub-path below META-INF/resources
 * when extracting files.
*/
public class ResourcesUnArchiver extends AbstractZipUnArchiver {
  @Override
  protected void extractFile(File srcF, File dir, InputStream compressedInputStream, String entryName, Date entryDate, boolean isDirectory, Integer mode) throws IOException, ArchiverException {
    String subEntryName = entryName.substring(UnpackJarResourcesMojo.META_INF_RESOURCES.length());
    super.extractFile(srcF, dir, compressedInputStream, subEntryName, entryDate, isDirectory, mode);
  }
}
