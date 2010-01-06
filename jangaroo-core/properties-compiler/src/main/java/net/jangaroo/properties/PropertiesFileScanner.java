/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties;

import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.properties.model.PropertiesClass;
import net.jangaroo.properties.model.ResourceBundleClass;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

public final class PropertiesFileScanner {

  private LocalizationSuite suite;
  private static Pattern LOCALE_PATTERN  = Pattern.compile("^(\\p{Lower}{2})(_(\\p{Upper}{2}))?");

  public PropertiesFileScanner(LocalizationSuite suite) {
    this.suite = suite;
  }

  public LocalizationSuite getSuite() {
    return suite;
  }

  public void scan() throws IOException {
    FileSet srcFiles = new FileSet();
    srcFiles.setDirectory(suite.getRootDir().getAbsolutePath());
    srcFiles.addInclude("**/*.properties");
    for (String srcFileRelativePath : new FileSetManager().getIncludedFiles(srcFiles)) {

      File srcFile = new File(suite.getRootDir(), srcFileRelativePath);

      String className = FileUtils.removeExtension(srcFile.getName());

      Locale locale = null;
      if (className.indexOf("_") != -1) {
        String localeString = className.substring(className.indexOf("_") + 1, className.length());
        if(localeString.indexOf("_") != -1) {
          String lang = localeString.substring(0, localeString.indexOf("_"));
          String countr = localeString.substring(lang.length()+1, localeString.length());
          if(countr.indexOf("_") != -1) {
            String var = countr.substring(countr.indexOf("_") + 1, countr.length());
            countr = countr.substring(0, countr.indexOf("_"));
            locale = new Locale(lang, countr, var);
          } else {
            locale = new Locale(lang,countr);
          }
        } else {
          locale = new Locale(localeString);
        }
        className = className.substring(0, className.indexOf("_"));
      } else {
        locale = Locale.ENGLISH;
      }

      String packageName = FileUtils.dirname(srcFileRelativePath.substring(0)).replaceAll("[\\\\/]", ".");

      String fullName;
      if (packageName != null && !"".equals(packageName)) {
        fullName = packageName + "." + className;
      } else {
        fullName = className;
      }

      ResourceBundleClass bundle = suite.getClassByFullName(fullName);
      if (bundle == null) {
        bundle = new ResourceBundleClass(fullName);
        suite.addResourceBundleClass(bundle);
      }

      Properties p = new Properties();
      Reader r = new FileReader(srcFile);
      try {
        p.load(r);
      } finally {
        r.close();
      }
      PropertiesClass propertiesCl = new PropertiesClass(bundle, locale, p, srcFile);
    }
  }
}
