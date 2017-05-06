package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Ide;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Helper class to verify the standard XML header <code>&lt;?xml version="..."?&gt;</code>.
 *
 * https://www.w3.org/TR/REC-xml/#sec-prolog-dtd
 */
public class XmlHeader extends XmlTag {

  public XmlHeader(JooSymbol lt, Ide xmlIde, List<XmlAttribute> attributes, JooSymbol gt) {
    super(lt, xmlIde, attributes, gt);
    if(!"xml".equals(xmlIde.getName())) {
      throw JangarooParser.error(xmlIde, "XML header must start with 'xml'");
    }

    for (XmlAttribute attribute : attributes) {
      if (!asList("version", "encoding", "standalone").contains(attribute.getLocalName())) {
        throw JangarooParser.error(attribute, "unsupported XML header attribute");
      }
    }

  }

}
