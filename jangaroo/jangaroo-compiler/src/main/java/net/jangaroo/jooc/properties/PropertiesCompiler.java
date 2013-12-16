package net.jangaroo.jooc.properties;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.utils.CompilerUtils;
import net.jangaroo.utils.FileLocations;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;

/**
 * A "compiler" to translate properties files into JSON objects that 
 */
public class PropertiesCompiler {

  private FileLocations locations;

  public PropertiesCompiler(FileLocations locations) {
    this.locations = locations;
  }

  public FileLocations getConfig() {
    return locations;
  }

  public void setConfig(FileLocations config) {
    this.locations = config;
  }

  public File generate(File propertiesFile) throws IOException {
    PropertiesConfiguration p = loadProperties(propertiesFile);
    return generateJson(p, propertiesFile);
  }

  public static File computeGeneratedPropertiesJsonFile(FileLocations locations, String bundleName, Locale locale) {
    String jsonFileName = String.format("locale/%s/%s.js", locale, bundleName);
    return new File(locations.getOutputDirectory(), jsonFileName); 
  }
  
  public static Locale computeLocale(File propertiesFile) {
    String localeStr = propertiesFile.getParentFile().getName();
    String[] parts = localeStr.split("_", 3);
    switch (parts.length) {
      case 3: return new Locale(parts[0], parts[1], parts[2]);
      case 2: return new Locale(parts[0], parts[1]);
      case 1: return new Locale(parts[0]);
    }
    return null;
  }

  private File generateJson(PropertiesConfiguration p, File propertiesFile) throws IOException {
    Locale locale = computeLocale(propertiesFile);
    String bundleName = CompilerUtils.removeExtension(propertiesFile.getName());
    File file = computeGeneratedPropertiesJsonFile(getConfig(), bundleName, locale);
    // fill JSON from properties:
    JsonObject json = new JsonObject();
    Iterator keyIterator = p.getKeys();
    while (keyIterator.hasNext()) {
      String key = (String) keyIterator.next();
      String value = p.getString(key);
      json.set(key, value);
    }
    //noinspection ResultOfMethodCallIgnored
    file.getParentFile().mkdirs();
    //noinspection ResultOfMethodCallIgnored
    file.createNewFile();
    if (!file.exists()) {
      throw Jooc.error("cannot create properties output file", file);
    }
    writeJson(json, file);

    // generate master properties file for every locale-specific one (a bit wasteful, but well):
    JsonObject master = new JsonObject();
    File localeParentDir = propertiesFile.getParentFile().getParentFile();
    for (File localeDirectory : localeParentDir.listFiles(DirectoryFileFilter.INSTANCE)) {
      if (new File(localeDirectory, bundleName + ".properties").exists()) {
        master.set(localeDirectory.getName(), true);
      }
    }
    writeJson(master, new File(getConfig().getOutputDirectory(), "locale/" + bundleName + ".js"));

    return file;
  }

  private void writeJson(JsonObject json, File file) throws IOException {
    String code = "define(" + json.toString(2) + ");";
    Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
    try {
      writer.write(code);
    } finally {
      writer.close();
    }
  }

  private PropertiesConfiguration loadProperties(File propertiesFile) {
    PropertiesConfiguration p = new PropertiesConfiguration();
    p.setDelimiterParsingDisabled(true);
    Reader r = null;
    try {
      r = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8"));
      p.load(r);
    } catch (IOException e) {
      throw Jooc.error("Error while parsing properties file", propertiesFile, e);
    } catch (ConfigurationException e) {
      throw Jooc.error("Error while parsing properties file", propertiesFile, e);
    } finally {
      try {
        if (r != null) {
          r.close();
        }
      } catch (IOException e) {
        //not really
      }
    }
    return p;
  }

  private static class DirectoryFileFilter implements FileFilter {
    public static final DirectoryFileFilter INSTANCE = new DirectoryFileFilter();
    @Override
    public boolean accept(File file) {
      return file.isDirectory();
    }
  }
}
