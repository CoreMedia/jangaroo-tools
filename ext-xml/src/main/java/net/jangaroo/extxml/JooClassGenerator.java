package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.saxon.s9api.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.DirectoryWalker;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.*;


public class JooClassGenerator {

  Processor processor;
  XsltCompiler compiler;
  XsltExecutable excutable;
  ComponentSuites componentSuites;

  File inputDirectory;
  File outputDirectory;


  private static final FileFilter XML_FILE_FILTER = new FileFilter() {
    public boolean accept(File file) {
      return file.getName().endsWith(".xml");
    }
  };


  public JooClassGenerator(ComponentSuites componentSuites) throws SaxonApiException {
    InputStream inputStream = getClass().getResourceAsStream("/net/jangaroo/extxml/templates/ExtXML2JSON.xsl");
    processor = new Processor(false);
    compiler = processor.newXsltCompiler();
    excutable = compiler.compile(new StreamSource(inputStream));

    this.componentSuites = componentSuites;
  }


  public String transform(InputStream inputStream) throws SaxonApiException {
    StringWriter stringWriter = new StringWriter();

    XdmNode source = processor.newDocumentBuilder().build(new StreamSource(inputStream));
    Serializer out = new Serializer();
    out.setOutputProperty(Serializer.Property.METHOD, "text");
    out.setOutputProperty(Serializer.Property.INDENT, "yes");
    out.setOutputWriter(stringWriter);

    XsltTransformer trans = excutable.load();
    trans.setInitialContextNode(source);
    trans.setDestination(out);
    trans.transform();

    return stringWriter.toString();
  }


  protected List<String> getImports(String json) throws JSONException, IOException, ParserConfigurationException, XPathExpressionException, SAXException {
    Set<String> xtypes = collectXTypesFromJSON(json);
    List<String> imports = new ArrayList<String>(xtypes.size());
    for (String xtype : xtypes) {
      imports.add(componentSuites.getComponentClassByXtype(xtype).getClassName());
    }
    return imports;
  }


  protected Set<String> collectXTypesFromJSON(String json) throws JSONException {
    JSONObject jsonObject = new JSONObject(json);
    Set<String> set = new TreeSet<String>();
    parseJSON(jsonObject, set);
    return set;
  }


  private Set<String> parseJSON(JSONObject jsonObject, Set<String> set) throws JSONException {
    if (jsonObject.has("xtype")) {
      set.add(jsonObject.getString("xtype"));
    }
    if (jsonObject.has("items")) {
      JSONArray jsonArray = jsonObject.getJSONArray("items");
      for (int i = 0; i < jsonArray.length(); i++) {
        parseJSON((JSONObject) jsonArray.get(i), set);
      }

    }
    return set;
  }


  public void transformFile(File inputFile, File outputFile) throws IOException, TemplateException, JSONException, ParserConfigurationException, XPathExpressionException, SAXException, SaxonApiException {

    FileInputStream inputStream = new FileInputStream(inputFile);
    String relativePath = getRelativePath(inputFile, inputDirectory);
    String json = transform(inputStream);
    String[] tokens = relativePath.split("/");

    String packageName = StringUtils.join(tokens, ".", 0, tokens.length - 1);
    String className = tokens[tokens.length - 1];
    className = className.substring(0, className.lastIndexOf("."));

    JSONObject jsonObject = new JSONObject(json);
    String extendsClass = componentSuites.getComponentClassByXtype(jsonObject.getString("xtype")).getClassName();

    generateJangarooClass(
      new JooClass(packageName, getImports(json), className, extendsClass, json), outputFile
    );
  }


  private String getRelativePath(File file, File relativeTo) {
    return file.getAbsolutePath().substring(relativeTo.getAbsolutePath().length(), file.getAbsolutePath().length());
  }

  public void generateJangarooClass(JooClass jooClass, File outputFile) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(JooClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());

    Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/jangaroo_class.ftl");


    FileWriter fileWriter = new FileWriter(outputFile);
    template.process(jooClass, fileWriter);
    fileWriter.close();
  }


  public void transformFiles(File inputDir, File outputDir) throws IOException {
    inputDirectory = inputDir;
    outputDirectory = outputDir;

    DirectoryWalkerImpl directoryWalker = new DirectoryWalkerImpl();
    directoryWalker.start();
  }


  class DirectoryWalkerImpl extends DirectoryWalker {

    DirectoryWalkerImpl() {
      super(XML_FILE_FILTER, -1);

    }

    public void start() throws IOException {
      walk(inputDirectory, null);
    }

    @Override
    protected void handleFile(File file, int i, Collection collection) throws IOException {


      String relativePath = getRelativePath(file, inputDirectory);
      relativePath = relativePath.substring(0, relativePath.lastIndexOf(".xml")) + ".js";

      File outputFile = new File(outputDirectory.getAbsolutePath() + relativePath);

      try {
        transformFile(file, outputFile);
      } catch (TemplateException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (XPathExpressionException e) {
        e.printStackTrace();  
      } catch (SAXException e) {
        e.printStackTrace();
      } catch (SaxonApiException e) {
        e.printStackTrace();
      }

    }


  }

}
