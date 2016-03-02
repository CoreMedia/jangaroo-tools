package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.ast.Ide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Helper class to verify the standard XML header <code>&lt;?xml version="..."?></code>.
 *
 * https://www.w3.org/TR/REC-xml/#sec-prolog-dtd
 */
public class XmlHeader {

  public static void verifyXmlHeader(@Nonnull Ide xmlIde, @Nullable List<XmlAttribute> attributes) {
    if(!"xml".equals(xmlIde.getName())) {
      throw JangarooParser.error(xmlIde, "XML header must start with 'xml'");
    }

    XmlAttribute version;
    if(null == attributes || null == (version = Iterables.getFirst(Iterables.consumingIterable(attributes), null))) {
      throw JangarooParser.error(xmlIde, "XML header must contain version info");
    }

    if(!"version".equals(version.getLocalName())) {
      throw JangarooParser.error(version, "XML header must start with version info");
    }

    for(XmlAttribute attribute : attributes) {
      if(!asList("encoding", "standalone").contains(attribute.getLocalName())) {
        throw JangarooParser.error(attribute, "unsupported XML header attribute");
      }
    }

  }

}
