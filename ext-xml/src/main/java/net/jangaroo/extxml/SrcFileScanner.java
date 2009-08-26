package net.jangaroo.extxml;

import org.w3c.tidy.Tidy;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The FileScanner scans a directory for all *.as and *.js files that contain Ext annotations and builds a
 * {@link ComponentSuite} from all Ext JS component classes.
 */
public class SrcFileScanner {

  private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {
    public boolean accept(File pathname) {
      return pathname.isDirectory();
    }
  };
  private static final FilenameFilter SRC_FILE_FILTER = new FilenameFilter() {
    public boolean accept(File dir, String name) {
      return name.endsWith(".js") || name.endsWith(".as");
    }
  };

  private enum LineType {
    CLASS, EXTENDS, XTYPE, REG, CFG, END_DESCRIPTION
  }

  private static final Map<Pattern, LineType> PATTERN_TO_LINE_TYPE_MAP = new LinkedHashMap<Pattern, LineType>();

  static {
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile(".*@class ([a-zA-Z0-9$_.]+)"), LineType.CLASS);
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile(".*@extends ([a-zA-Z0-9$_.]+)"), LineType.EXTENDS);
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile(".*@xtype ([a-zA-Z.]+)"), LineType.XTYPE);
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile("\\s*Ext.reg\\('([a-zA-Z.]+)', ([a-zA-Z0-9$_.]+)\\);\\s*"), LineType.REG);
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile(".*@cfg [{]?([a-zA-Z0-9$_./]+)[}]? ([a-zA-Z0-9$_]+)(.*)"), LineType.CFG);
    PATTERN_TO_LINE_TYPE_MAP.put(Pattern.compile(".*(\\*/|@constructor).*"), LineType.END_DESCRIPTION);
  }

  private static final List<Map.Entry<Pattern, LineType>> PATTERN_TO_LINE_TYPE = new ArrayList<Map.Entry<Pattern, LineType>>(PATTERN_TO_LINE_TYPE_MAP.entrySet());

  private static final Pattern COMMENT_LINE = Pattern.compile("\\s*\\* ?(.*)");

  public SrcFileScanner(ComponentSuite componentSuite) {
    this.componentSuite = componentSuite;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public void scan() {
    scan("");
  }

  private void scan(String relativeDirPath) {
    File dir = new File(componentSuite.getRootDir(), relativeDirPath);
    for (String srcFileName : dir.list(SRC_FILE_FILTER)) {
      scanFile(relativeDirPath + File.separator + srcFileName);
    }
    int dirPathLength = dir.getPath().length();
    for (File subdir : dir.listFiles(DIRECTORY_FILE_FILTER)) {
      String realtiveSubdirPath = subdir.getPath().substring(dirPathLength);
      scan(realtiveSubdirPath);
    }
  }

  private void scanFile(String relativeSrcFilePath) {
    try {
      File srcFile = new File(componentSuite.getRootDir(), relativeSrcFilePath);
      BufferedReader reader = new BufferedReader(new FileReader(srcFile));
      ComponentClass cc = null;
      String line;
      StringBuilder description = new StringBuilder();
      DescriptionHolder descriptionHolder = null;
      DescriptionHolder nextDescriptionHolder = null;
      try {
        while ((line = reader.readLine()) != null) {
          String descriptionLine = line;
          for (Map.Entry<Pattern, LineType> pattern2lineTypeEntry : PATTERN_TO_LINE_TYPE) {
            Matcher matcher = pattern2lineTypeEntry.getKey().matcher(line);
            if (matcher.matches()) {
              switch (pattern2lineTypeEntry.getValue()) {
                case CLASS:
                  addIfHasXtype(cc);
                  cc = new ComponentClass(relativeSrcFilePath);
                  cc.setClassName(jsType2asType(matcher.group(1)));
                  nextDescriptionHolder = cc;
                  descriptionLine = null;
                  break;
                case EXTENDS:
                  cc.setSuperClassName(jsType2asType(matcher.group(1)));
                  descriptionLine = null;
                  break;
                case XTYPE:
                  cc.setXtype(matcher.group(1));
                  descriptionLine = null;
                  break;
                case REG:
                  // old-style xtype registration, still used e.g. in Ext.Component.js:
                  if (cc.getXtype() == null && jsType2asType(matcher.group(2)).equals(cc.getClassName())) {
                    cc.setXtype(matcher.group(1));
                  }
                  descriptionLine = null;
                  break;
                case CFG:
                  // skip optional type if missing:
                  int nameGroupIndex = matcher.groupCount() == 3 ? 2 : 1;
                  String type = nameGroupIndex == 2 ? matcher.group(1) : "*";
                  String name = matcher.group(nameGroupIndex);
                  descriptionLine = matcher.group(nameGroupIndex + 1);
                  if (descriptionLine.length() == 0) {
                    descriptionLine = null; // suppress first empty line
                  }
                  ConfigAttribute configAttribute = new ConfigAttribute(name, type);
                  cc.addCfg(configAttribute);
                  nextDescriptionHolder = configAttribute;
                  break;
                case END_DESCRIPTION:
                  nextDescriptionHolder = null;
              }
              if (nextDescriptionHolder != descriptionHolder) {
                if (descriptionHolder != null) {
                  descriptionHolder.setDescription(tidy(description.toString()));
                  description = new StringBuilder();
                }
                descriptionHolder = nextDescriptionHolder;
              }
              break;
            }
          }
          if (descriptionHolder != null && descriptionLine != null) {
            // Skip leading white space before first '*':
            Matcher matcher = COMMENT_LINE.matcher(descriptionLine);
            if (matcher.matches()) {
              descriptionLine = matcher.group(1);
            }
            description.append(descriptionLine).append('\n');
          }
        }
      } catch (IOException e) {
        // should not happen:
        throw new RuntimeException(e);
      }
      addIfHasXtype(cc);
    } catch (FileNotFoundException e) {
      // cannot happen:
      throw new RuntimeException(e);
    }
  }

  private String jsType2asType(String jsType) {
    int lastDotPos = jsType.lastIndexOf('.');
    return lastDotPos == -1
      ? jsType
      : jsType.substring(0, lastDotPos).toLowerCase() + jsType.substring(lastDotPos);
  }

  private String tidy(String dirtyHtml) {
    Tidy tidy = new Tidy();
    tidy.setDropEmptyParas(true);
    tidy.setDropFontTags(true);
    tidy.setFixComments(true);
    tidy.setHideEndTags(false);
    tidy.setIndentAttributes(true);
    tidy.setMakeClean(true);
    tidy.setQuiet(true);
    tidy.setQuoteAmpersand(true);
    tidy.setShowWarnings(false);
    tidy.setXHTML(true);
    tidy.setXmlOut(true);
    tidy.setXmlSpace(false);
    tidy.setXmlPi(false);
    dirtyHtml = "<html><body>"+dirtyHtml+"</body></html>";
    StringWriter result = new StringWriter();
    try {
      Document document = tidy.parseDOM(new ByteArrayInputStream(dirtyHtml.getBytes("ISO-8859-1")), null);
      DOMSource domSource = new DOMSource(document.getDocumentElement());
      StreamResult streamResult = new StreamResult(result);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer serializer = tf.newTransformer();
      serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
      serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
      serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
      serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
      serializer.transform(domSource, streamResult);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      // should not happen for ISO-8859-1:
      throw new RuntimeException(e);
    }
    String xml = result.toString();
    if (xml.indexOf("<body/>")!=-1) {
      return "";
    }
    int bodyStart = xml.indexOf("<body>");
    int bodyEnd = xml.indexOf("</body>");
    if (bodyStart==-1 || bodyEnd==-1) {
      // should not happen:
      throw new RuntimeException("No body element found in "+xml);
    }
    return xml.substring(bodyStart+"<body>".length(),bodyEnd);
  }

  private void addIfHasXtype(ComponentClass cc) {
    if (cc != null && cc.getXtype() != null) {
      componentSuite.addComponentClass(cc);
    }
  }

  private ComponentSuite componentSuite;
}
