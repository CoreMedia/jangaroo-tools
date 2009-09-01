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
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.mappers.FileNameMapper;
import org.apache.maven.shared.model.fileset.mappers.GlobPatternMapper;
import org.apache.maven.shared.model.fileset.mappers.MapperException;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.xml.sax.SAXException;
import org.codehaus.plexus.util.FileUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JooClassGenerator {

  private static FileNameMapper XML_TO_JS_MAPPER = new GlobPatternMapper();
  static {
    XML_TO_JS_MAPPER.setFrom("*.xml");
    XML_TO_JS_MAPPER.setTo("*.as");
  }

  Processor processor;
  XsltCompiler compiler;
  XsltExecutable executable;
  ComponentSuites componentSuites;

  public JooClassGenerator(ComponentSuites componentSuites) throws SaxonApiException {
    InputStream inputStream = getClass().getResourceAsStream("/net/jangaroo/extxml/templates/ExtXML2JSON.xsl");
    processor = new Processor(false);
    compiler = processor.newXsltCompiler();
    executable = compiler.compile(new StreamSource(inputStream));

    this.componentSuites = componentSuites;
  }


  public String transform(InputStream inputStream) throws SaxonApiException {
    StringWriter stringWriter = new StringWriter();

    XdmNode source = processor.newDocumentBuilder().build(new StreamSource(inputStream));
    Serializer out = new Serializer();
    out.setOutputProperty(Serializer.Property.METHOD, "text");
    out.setOutputProperty(Serializer.Property.INDENT, "yes");
    out.setOutputWriter(stringWriter);

    XsltTransformer trans = executable.load();
    trans.setInitialContextNode(source);
    trans.setDestination(out);
    trans.transform();

    return stringWriter.toString();
  }


  protected List<String> getImports(String json) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException {
    Set<String> xtypes = collectXTypesFromJSON(json);
    List<String> imports = new ArrayList<String>(xtypes.size());
    for (String xtype : xtypes) {
      ComponentClass componentClass = componentSuites.getComponentClassByXtype(xtype);
      if (componentClass==null) {
        System.err.println(MessageFormat.format("xtype ''{0}'' not found in any imported component schema.", xtype));
      } else {
        imports.add(componentClass.getFullClassName());
      }
    }
    return imports;
  }


  private static Pattern XTYPE_PATTERN = Pattern.compile("\\bxtype\\s*:\\s*['\"]([^'\"]+)['\"]");

  private String nextXtype(Matcher xtypeMatcher) {
    return xtypeMatcher.find() ? xtypeMatcher.group(1) : null;
  }

  Set<String> collectXTypesFromJSON(String json) {
    Set<String> set = new TreeSet<String>();
    Matcher matcher = XTYPE_PATTERN.matcher(json);
    String xtype;
    while ((xtype = nextXtype(matcher)) != null) {
      set.add(xtype);
    }
    return set;
  }


  public void transformFile(File inputDir, String inputFileRelativePath,  File outputDir, String outputFileRelativePath) throws IOException, TemplateException, ParserConfigurationException, XPathExpressionException, SAXException, SaxonApiException {
    File outputFile = new File(outputDir, outputFileRelativePath);
    FileInputStream inputStream = new FileInputStream(new File(inputDir, inputFileRelativePath));
    String json = transform(inputStream);
    String className = FileUtils.basename(inputFileRelativePath,".xml");
    String packageName = FileUtils.normalize(FileUtils.dirname(inputFileRelativePath)).replaceAll("[\\\\/]", ".");

    String xtype = nextXtype(XTYPE_PATTERN.matcher(json));
    String extendsClass = componentSuites.getComponentClassByXtype(xtype).getFullClassName();

    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs();
    FileWriter writer = new FileWriter(outputFile);
    generateJangarooClass(
      new ComponentClass(getImports(json), packageName+"."+className, extendsClass, json), writer);
  }

  public void generateJangarooClass(ComponentClass jooClass, Writer output) throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ComponentClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());

    Template template = cfg.getTemplate("/net/jangaroo/extxml/templates/jangaroo_class.ftl");

    template.process(jooClass, output);
    output.close();
  }


  public void transformFiles(File inputDir, File outputDir) throws IOException, MapperException, SaxonApiException, SAXException, XPathExpressionException, TemplateException, ParserConfigurationException {
    FileSet fileSet = new FileSet();
    fileSet.setDirectory(inputDir.getAbsolutePath());
    fileSet.setOutputDirectory(outputDir.getAbsolutePath());
    fileSet.addInclude("**/*.xml");
    for (String inputFile : new FileSetManager().getIncludedFiles(fileSet)) {
      transformFile(
        inputDir, inputFile,
        outputDir, XML_TO_JS_MAPPER.mapFileName(inputFile));
    }
  }

}
