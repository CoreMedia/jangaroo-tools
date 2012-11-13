package net.jangaroo.exml.parser;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.config.ValidationMode;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Validates the configured EXML source files against the EXML schema and the generated component suite schemas
 * in the classpath.
 */
public class ExmlValidator {

  private final ExmlConfiguration config;
  private final Map<String, ExmlSchemaSource> exmlSchemaSourceByNamespace;

  public ExmlValidator(ExmlConfiguration config) {
    this.config = config;
    exmlSchemaSourceByNamespace = new HashMap<String, ExmlSchemaSource>();
    addClasspathXsdMappings(exmlSchemaSourceByNamespace);
    addXsdMappingsFromFilesInDirectory(exmlSchemaSourceByNamespace, this.config.getResourceOutputDirectory());
  }

  public void validateExmlFile(File exmlFile) throws IOException, SAXException {
    SAXParser parser = setupSAXParser();
    validateOneExmlfile(parser, exmlFile);
  }

  public void validateAllExmlFiles() throws IOException, SAXException {
    SAXParser parser = setupSAXParser();
    for (final File exmlFile : config.getSourceFiles()) {
      validateOneExmlfile(parser, exmlFile);
    }
  }

  private void validateOneExmlfile(SAXParser parser, final File exmlFile) throws SAXException, IOException {
    // System.out.println("validating " + exmlFile.getPath() + "...");
    try {
      parser.parse(exmlFile, new DefaultHandler() {
        @Override
        public void fatalError(SAXParseException e) throws SAXException {
          logSAXParseException(exmlFile, e, true);
        }

        @Override
        public void error(final SAXParseException e) throws SAXException {
          logSAXParseException(exmlFile, e, config.getValidationMode() == ValidationMode.ERROR);
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
          logSAXParseException(exmlFile, e, false);
        }
      });
    } catch (SAXParseException e) {
      // should not be thrown, but if so, log it nevertheless:
      logSAXParseException(exmlFile, e, true);
    }
  }

  private void logSAXParseException(File exmlFile, SAXParseException e, boolean isError) {
    SAXParseException2FilePositionAdapter filePosition = new SAXParseException2FilePositionAdapter(exmlFile, e);
    if (isError) {
      config.getLog().error(filePosition, e.getMessage());
    } else {
      config.getLog().warning(filePosition, e.getMessage());
    }
    //System.out.println("validation issue in " + exmlFile + " in line " + e.getLineNumber() + ": " + e.getMessage());
  }

  private SAXParser setupSAXParser() throws IOException {
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      ExmlSchemaResolver exmlSchemaResolver = new ExmlSchemaResolver();
      schemaFactory.setResourceResolver(exmlSchemaResolver);
      List<Source> schemas = new ArrayList<Source>();
      schemas.add(new StreamSource(getClass().getResourceAsStream(Exmlc.EXML_SCHEMA_LOCATION), "exml"));
      schemas.add(new StreamSource(getClass().getResourceAsStream(Exmlc.EXML_UNTYPED_SCHEMA_LOCATION), "untyped"));
      Collection<ExmlSchemaSource> exmlSchemaSources = exmlSchemaSourceByNamespace.values();
      for (ExmlSchemaSource exmlSchemaSource : exmlSchemaSources) {
        schemas.add(exmlSchemaSource.newStreamSource());
      }
      Schema exmlSchema = schemaFactory.newSchema(schemas.toArray(new Source[schemas.size()]));
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      saxFactory.setSchema(exmlSchema);
      SAXParser saxParser = saxFactory.newSAXParser();
      saxParser.getXMLReader().setEntityResolver(new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
          //System.err.println("### I am asked to resolve entity " + publicId + " / " + systemId);
          return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
      });
      return saxParser;
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("A default dom builder should be provided.", e);
    } catch (SAXParseException e) {
      logSAXParseException(null, e, true);
      throw new IllegalStateException("SAX parser error while parsing EXML schemas.", e);
    } catch (SAXException e) {
      throw new IllegalStateException("SAX parser does not support validation.", e);
    }
  }

  private void addClasspathXsdMappings(Map<String, ExmlSchemaSource> schemas) {
    for (File classPathEntry : config.getClassPath()) {
      if (!addXsdMappingsFromFilesInDirectory(schemas, classPathEntry)
              && !addXsdMappingsFromEntriesInJar(schemas, classPathEntry)) {
        System.err.println("[WARN] ignoring invalid classpath entry " + classPathEntry.getPath());
      }
    }
  }

  private boolean addXsdMappingsFromEntriesInJar(Map<String, ExmlSchemaSource> schemas, File jarFile) {
    if (jarFile == null || !jarFile.exists() || !jarFile.getName().endsWith(".jar")) {
      return false;
    }
    try {
      ZipFile jarZipFile = new ZipFile(jarFile);
      Set<ZipEntry> xsdZipEntries = ExmlUtils.findXsdJarEntries(jarZipFile);
      for (ZipEntry xsdZipEntry : xsdZipEntries) {
        String xsdZipEntryName = xsdZipEntry.getName();
        String namespace = Exmlc.EXML_CONFIG_URI_PREFIX + CompilerUtils.removeExtension(xsdZipEntryName);
        //System.out.println("### adding EXML schema " + namespace + " from " + jarZipFile);
        schemas.put(namespace, new ExmlSchemaZipEntrySource(jarFile, xsdZipEntry));
      }
      return true;
    } catch (IOException e) {
      System.err.println("Cannot read classpath JAR file '" + jarFile.getPath() + "'.");
      e.printStackTrace();
      return false;
    }
  }

  private boolean addXsdMappingsFromFilesInDirectory(Map<String, ExmlSchemaSource> schemas, File xsdsDirectory) {
    if (xsdsDirectory != null && xsdsDirectory.exists() && xsdsDirectory.isDirectory()) {
      File[] xsds = xsdsDirectory.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".xsd");
        }
      });
      for (final File xsd : xsds) {
        String namespace = Exmlc.EXML_CONFIG_URI_PREFIX + CompilerUtils.removeExtension(xsd.getName());
        // System.out.println("### adding EXML schema " + namespace + " from " + xsd.getPath());
        schemas.put(namespace, new ExmlSchemaFileSource(xsd));
      }
      return true;
    }
    return false;
  }

  private class ExmlSchemaResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      // System.err.println("*** I am asked for resource " + namespaceURI);
      if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(type)) {
        ExmlSchemaSource exmlSchemaSource = exmlSchemaSourceByNamespace.get(namespaceURI);
        if (exmlSchemaSource != null) {
          return new StreamSourceLSInput(exmlSchemaSource);
        } else {
          System.err.println("[WARN] ExmlValidator has been asked for unregistered resource " + namespaceURI);
        }
      }
      return null;
    }

  }

  private static class SAXParseException2FilePositionAdapter implements FilePosition {
    private final File exmlFile;
    private final SAXParseException e;

    public SAXParseException2FilePositionAdapter(File exmlFile, SAXParseException e) {
      this.exmlFile = exmlFile;
      this.e = e;
    }

    @Override
    public String getFileName() {
      return exmlFile == null ? "-unknown-" : exmlFile.getAbsolutePath();
    }

    @Override
    public int getLine() {
      return e.getLineNumber();
    }

    @Override
    public int getColumn() {
      return e.getColumnNumber();
    }
  }

  private abstract class ExmlSchemaSource {
    private String systemId;

    protected ExmlSchemaSource(String systemId) {
      this.systemId = systemId;
    }

    public String getSystemId() {
      return systemId;
    }

    public abstract InputStream newInputStream();

    @Override
    public String toString() {
      return "[ExmlSchemaZipEntrySource " + getSystemId() +"]";
    }

    public StreamSource newStreamSource() {
      return new StreamSource(newInputStream(), getSystemId());
    }
  }

  private class ExmlSchemaZipEntrySource extends ExmlSchemaSource {
    private File jarFile;
    private ZipEntry xsdEntry;

    private ExmlSchemaZipEntrySource(File jarFile, ZipEntry xsdEntry) {
      super(constructUrl(jarFile, xsdEntry));
      this.jarFile = jarFile;
      this.xsdEntry = xsdEntry;
    }

    @Override
    public InputStream newInputStream() {
      try {
        return new ZipFile(jarFile).getInputStream(xsdEntry);
      } catch (IOException e) {
        throw new RuntimeException("cannot create input stream of "+ getSystemId());
      }
    }
  }

  private class ExmlSchemaFileSource extends ExmlSchemaSource {
    private final File xsd;

    public ExmlSchemaFileSource(File xsd) {
      super(xsd.getPath());
      this.xsd = xsd;
    }

    @Override
    public InputStream newInputStream() {
      try {
        return new FileInputStream(xsd);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static String constructUrl(File jarFile, ZipEntry xsdEntry) {
    try {
      return "jar:" + jarFile.toURI().toURL() + "!/" + xsdEntry.getName();
    } catch (MalformedURLException e) {
      throw new RuntimeException("cannot happen");
    }
  }

  /**
   * Minimum LSInput impl based on a ExmlSchemaSource.
   */
  private static final class StreamSourceLSInput implements LSInput {
    private ExmlSchemaSource exmlSchemaSource;

    /**
     * @param exmlSchemaSource the InputStream to return for {@link #getByteStream()}
     */
    private StreamSourceLSInput(ExmlSchemaSource exmlSchemaSource) {
      this.exmlSchemaSource = exmlSchemaSource;
    }

    public InputStream getByteStream() {
      return exmlSchemaSource.newInputStream();
    }

    // --- dummy methods to fulfil the interface ------------------
    public Reader getCharacterStream() {return null;}
    public String getStringData() {return null;}
    public String getSystemId() {return null;}
    public String getPublicId() {return null;}

    public String getBaseURI() {return null;}
    public String getEncoding() {return null;}
    public boolean getCertifiedText() {return false;}
    public void setCharacterStream(Reader characterStream) {}
    public void setByteStream(InputStream byteStream) {}
    public void setStringData(String stringData) {}
    public void setSystemId(String systemId) {}
    public void setPublicId(String publicId) {}
    public void setBaseURI(String baseURI) {}
    public void setEncoding(String encoding) {}
    public void setCertifiedText(boolean certifiedText) {}
  }

}
