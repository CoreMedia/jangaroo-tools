package net.jangaroo.extxml;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.mappers.MapperException;
import org.apache.maven.shared.model.fileset.mappers.FileNameMapper;
import org.apache.maven.shared.model.fileset.mappers.GlobPatternMapper;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.text.MessageFormat;


public class JooClassGenerator {

  private static FileNameMapper XML_TO_JS_MAPPER = new GlobPatternMapper();
  static {
    XML_TO_JS_MAPPER.setFrom("*.xml");
    XML_TO_JS_MAPPER.setTo("*.as");
  }

  Processor processor;
  XsltCompiler compiler;
  XsltExecutable excutable;
  ComponentSuites componentSuites;

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
      ComponentClass componentClass = componentSuites.getComponentClassByXtype(xtype);
      if (componentClass==null) {
        System.err.println(MessageFormat.format("xtype ''{0}'' not found in any imported component schema.", xtype));
      } else {
        imports.add(componentClass.getClassName());
      }
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
        parseJSON((JSONObject)jsonArray.get(i), set);
      }
    }
    return set;
  }


  public void transformFile(File inputDir, String inputFileRelativePath,  File outputDir, String outputFileRelativePath) throws IOException, TemplateException, JSONException, ParserConfigurationException, XPathExpressionException, SAXException, SaxonApiException {
    File outputFile = new File(outputDir, outputFileRelativePath);
    FileInputStream inputStream = new FileInputStream(new File(inputDir, inputFileRelativePath));
    String json = transform(inputStream);
    String[] tokens = inputFileRelativePath.split("/");

    String packageName = StringUtils.join(tokens, ".", 0, tokens.length - 1);
    String className = tokens[tokens.length - 1];
    className = className.substring(0, className.lastIndexOf("."));

    JSONObject jsonObject = new JSONObject(json);
    String extendsClass = componentSuites.getComponentClassByXtype(jsonObject.getString("xtype")).getClassName();

    generateJangarooClass(
      new JooClass(packageName, getImports(json), className, extendsClass, json), outputFile
    );
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


  public void transformFiles(File inputDir, File outputDir) throws IOException, MapperException, SaxonApiException, SAXException, XPathExpressionException, TemplateException, JSONException, ParserConfigurationException {
    FileSet fileSet = new FileSet();
    fileSet.setDirectory(inputDir.getAbsolutePath());
    fileSet.setOutputDirectory(outputDir.getAbsolutePath());
    for (String inputFile : new FileSetManager().getIncludedFiles(fileSet)) {
      transformFile(
        inputDir, inputFile,
        outputDir, XML_TO_JS_MAPPER.mapFileName(inputFile));
    }
  }

}
