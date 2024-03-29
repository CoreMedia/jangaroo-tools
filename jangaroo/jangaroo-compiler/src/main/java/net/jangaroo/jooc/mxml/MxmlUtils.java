package net.jangaroo.jooc.mxml;

import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some useful utility functions for MXML handling.
 */
public class MxmlUtils {

  public static final String MXML_NAMESPACE_URI = "http://ns.adobe.com/mxml/2009";
  public static final String EXML_UNTYPED_NAMESPACE = "exml:untyped";
  public static final String MXML_DECLARATIONS = "Declarations";
  public static final String MXML_SCRIPT = "Script";
  public static final String MXML_METADATA = "Metadata";
  public static final String MXML_ID_ATTRIBUTE = "id";
  public static final String MXML_DEFAULT_PROPERTY_ANNOTATION = "DefaultProperty";
  public static final String EXML_MIXINS_PROPERTY_NAME = "__mixins__";

  public static final String EVENT_DISPATCHER_INTERFACE = "ext.mixin.IObservable";
  public static final String ADD_EVENT_LISTENER_METHOD_NAME = "addEventListener";

  private static final Pattern IS_BINDING_EXPRESSION_PATTERN = Pattern.compile("(^|[^\\\\])\\{([^}]*[^\\\\])\\}");
  private static final Pattern BINDING_EXPRESSION_START_OR_END_PATTERN = Pattern.compile("[{}]");

  private static final Pattern MXML_COMMENT = Pattern.compile("(^|\n)?( *)<!--(-?)([^-]*(?:-[^-]+)*)-->", Pattern.DOTALL);
  public static final String CONFIG = "config";
  private static final List<AS3Type> AS3_TYPES_THAT_ALLOW_EMPTY_STRING = Arrays.asList(AS3Type.ANY, AS3Type.OBJECT, AS3Type.STRING, AS3Type.ARRAY);

  public static boolean isMxmlNamespace(String uri) {
    return MXML_NAMESPACE_URI.equals(uri);
  }

  public static String createBindingExpression(String code) {
    return String.format("{%s}", code);
  }

  public static boolean isBindingExpression(String attributeValue) {
    return IS_BINDING_EXPRESSION_PATTERN.matcher(attributeValue).find();
  }

  public static String getBindingExpression(String attributeValue) {
    Matcher matcher = BINDING_EXPRESSION_START_OR_END_PATTERN.matcher(attributeValue);
    StringBuilder bindingExpression = new StringBuilder();
    // since we have to quote literals, we cannot use matcher.appendReplacement() / appendTail() :-(
    int startPos = 0;
    int curlyNesting = 0;
    while (matcher.find()) {
      int curlyPos = matcher.start();
      if (curlyPos == 0 || attributeValue.charAt(curlyPos - 1) != '\\') { // skip escaped curly braces
        String curly = matcher.group();
        if ("{".equals(curly)) {
          if (curlyNesting == 0) {
            // add the previous term as a literal: 
            startPos = addTerm(bindingExpression, attributeValue, startPos, curlyPos, true);
          }
          ++curlyNesting;
        } else { assert "}".equals(curly);
          if (curlyNesting > 0) { // interpret additional closing curly braces as literal
            --curlyNesting;
            if (curlyNesting == 0) {
              // add the previous term as an expression:
              startPos = addTerm(bindingExpression, attributeValue, startPos, curlyPos, false);
            }
          }
        }
      }
    }

    if (startPos < attributeValue.length()) {
      // interprete unclosed curly bracket as literal:
      if (curlyNesting > 0) {
        --startPos;
      }
      // add the remains as a literal:
      addTerm(bindingExpression, attributeValue, startPos, attributeValue.length(), true);
    }
    return bindingExpression.toString();
  }

  private static int addTerm(StringBuilder bindingExpression, String attributeValue, int startPos, int endPos, boolean quote) {
    if (startPos < endPos) {
      if (bindingExpression.length() > 0) {
        bindingExpression.append(" + ");
      }
      String term = attributeValue.substring(startPos, endPos);
      bindingExpression.append(quote ? CompilerUtils.quote(term) : term);
    }
    return endPos + 1;
  }

  @Nonnull
  public static String mxmlValueToActionScriptExpr(String attributeValue, String type) {
    String trimmedAttributeValue = attributeValue.trim();
    if (MxmlUtils.isBindingExpression(trimmedAttributeValue)) {
      return MxmlUtils.getBindingExpression(trimmedAttributeValue);
    } else {
      AS3Type as3Type = type == null ? AS3Type.ANY : AS3Type.typeByName(type);
      attributeValue = attributeValue.trim();
      if (attributeValue.isEmpty() && !AS3_TYPES_THAT_ALLOW_EMPTY_STRING.contains(as3Type)) {
        // empty string is only kept if type is compatible with "String" or "Array", otherwise, we use null:
        return "null";
      }
      if (AS3Type.ANY.equals(as3Type)) {
        as3Type = CompilerUtils.guessType(attributeValue);
      }
      if (as3Type != null) {
        switch (as3Type) {
          case BOOLEAN:
          case NUMBER:
          case UINT:
          case INT:
            return attributeValue;
          case ARRAY:
            return trimmedAttributeValue.isEmpty()
                    ? "[]"
                    : String.format("[%s]", mxmlValueToActionScriptExpr(attributeValue, AS3Type.ANY.name));
        }
      }
      // quote and restore MXML-binding-expression-escaped opening curly brace:
      return CompilerUtils.quote(attributeValue.replaceAll("\\\\\\{", "{"));
    }
  }

  public static String capitalize(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    return name.substring(0,1).toUpperCase() + name.substring(1);
  }

  public static String toASDoc(String xmlWhitespace) {
    // convert MXML comments to ASDoc comments
    Matcher matcher = MXML_COMMENT.matcher(xmlWhitespace);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      String indent = matcher.group(2);
      boolean isASDoc = "-".equals(matcher.group(3));
      String prefix = (matcher.group(1) == null ? "" : matcher.group(1))
              + indent
              + (isASDoc ? "/**" : "/*");
      String content = Matcher.quoteReplacement(matcher.group(4));
      if (isASDoc) {
        String[] lines = content.split("\n", -1);
        for (int i = 1; i < lines.length; i++) {
          String line = lines[i];
          if (line.isEmpty() || line.startsWith(indent)) {
            lines[i] = indent + " *" + (line.isEmpty() ? "" : line.substring(indent.length()));
          }
        }
        content = String.join("\n", lines);
      }
      String lastReplacement = prefix + content;
      if (!isASDoc && content.contains("\n")) {
        lastReplacement += " ";
      }
      if (!lastReplacement.endsWith("*")) {
        lastReplacement += "*";
      }
      lastReplacement += "/";
      matcher.appendReplacement(sb, lastReplacement);
    }
    return matcher.appendTail(sb).toString();
  }

}
