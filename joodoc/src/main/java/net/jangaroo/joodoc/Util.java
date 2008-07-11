package net.jangaroo.joodoc;

import com.sun.javadoc.Tag;
import com.sun.javadoc.Doc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 21.07.2004
 * Time: 12:17:29
 * To change this template use File | Settings | File Templates.
 */
public class Util {

  private static final Pattern SENTENCE_SEPERATOR_PATTERN = Pattern.compile("\\.($|\\s)");
  private static final Pattern TAG_PATTERN = Pattern.compile("@[a-z]*");
  private static final Pattern INLINE_TAG_PATTERN = Pattern.compile("\\{@[a-z]* [^\\}]*\\}");
  private static final Pattern TOP_LEVEL_TAG_PATTERN = Pattern.compile("(^|[^\\{])@[a-z]*");
  private static final Pattern IDENT_PATTERN = Pattern.compile("\\A[\\p{Alpha}_$][\\p{Alnum}_$]*(\\.[\\p{Alpha}$_][\\p{Alnum}$_]*)*");

  public static String tidy(String str) {
    str=str.trim();
    str=str.replaceAll("/\\x2a","");
    str=str.replaceAll("\\x2a/","");
    str=str.replaceAll("\\x2a","");
    //str=str.replaceAll("@","<br/>@");
    str=str.trim();
    return str;
  }

  public static String firstSentence(String str) {
    Matcher sentenceSeperatorMatcher = SENTENCE_SEPERATOR_PATTERN.matcher(str);
    if (sentenceSeperatorMatcher.find()) {
      int dotPos = sentenceSeperatorMatcher.start();
      // check that the "firstSentence" is not in the top-level tags:
      Matcher tagMatcher = TOP_LEVEL_TAG_PATTERN.matcher(str);
      if (!tagMatcher.find() || tagMatcher.start()>dotPos) {
        return str.substring(0,dotPos+1);
      }
    }
    return "";
  }

  public static void main(String[] args) {
   // System.out.println(firstSentence("hallo * @.hallo"));
    Tag[] x = extractTags((Doc)null,"hallo {@link test} eins @see zwei");
    for (int i = 0; i < x.length; i++) {
      System.out.println(((TagImpl)x[i]).getDescription());
    }
  }

  public static String[] extractTags(String ws, String tagName) {
    ArrayList al= new ArrayList();
    int seeTagStart=ws.indexOf(tagName);
    int seeTagEnd=0;
    while (seeTagStart>=0) {
      ws=ws.substring(seeTagStart);
      seeTagEnd=ws.indexOf("@",tagName.length());
      if (ws.indexOf("\n",tagName.length())<seeTagEnd)
        seeTagEnd=ws.indexOf("\n",tagName.length());
      if (seeTagEnd<0) seeTagEnd=ws.length();
      al.add(tidy(ws.substring(tagName.length(),seeTagEnd)));
      seeTagStart=ws.indexOf(tagName,seeTagEnd);
    }
    String[] seeTags = (String[])al.toArray(new String[al.size()]);
    return seeTags;
  }

  public static Tag[] extractInlineTags(Doc holder, String text) {
    Matcher inlineTagMatcher = INLINE_TAG_PATTERN.matcher(text);
    List tags = new ArrayList();
    boolean found;
    int start=0;
    do {
      found = inlineTagMatcher.find();
      int end = found ? inlineTagMatcher.start() : text.length();
      if (end>start) {
        // add plain text tag:
        Tag tag = new TagImpl(holder,text.substring(start,end));
        tags.add(tag);
        start = end;
      }
      if (found) {
        // add inline tag:
        Tag tag = createTag(holder,text.substring(inlineTagMatcher.start()+1,inlineTagMatcher.end()-1));
        tags.add(tag);
        start = inlineTagMatcher.end();
      }
    } while (found);
    return (Tag[])tags.toArray(new Tag[tags.size()]);
  }

  public static Tag[] extractTags(Doc holder, String text) {
    List tags = new ArrayList();
    Matcher tagMatcher = TOP_LEVEL_TAG_PATTERN.matcher(text);
    boolean found;
    int start = 0;
    do {
      found = tagMatcher.find();
      int end = found ? tagMatcher.start() : text.length();
      Tag tag = createTag(holder,text.substring(start,end));
      tags.add(tag);
      start = end;
    } while (found);
    return (Tag[])tags.toArray(new Tag[tags.size()]);
  }

  private static Tag createTag(Doc holder, String text) {
    Matcher tagMatcher = TAG_PATTERN.matcher(text);
    if (tagMatcher.find() && tagMatcher.start()<=1) {
      String tag = text.substring(tagMatcher.start(),tagMatcher.end());
      String content = text.substring(tagMatcher.end()).trim();
      if ("@see".equals(tag)) {
        return new SeeTagImpl(holder, content);
      } else if ("@link".equals(tag)) {
        return new SeeTagImpl(holder, "@link", content);
      } else if ("@override".equals(tag)) {
        return new SeeTagImpl(holder, "@override", content);
      } else if ("@param".equals(tag)) {
        return new ParamTagImpl(holder,content);
      } else {
        return new TagImpl(holder,tag,tag,content);
      }
    }
    return new TagImpl(holder,text);
  }

  public static String getQualifiedName(String[] qualifiedNamePath) {
    StringBuffer buf=new StringBuffer();
    for (int i = 0; i < qualifiedNamePath.length; i++) {
      if (i>0)
       buf.append('.');
      buf.append(qualifiedNamePath[i]);
    }
    return buf.toString();
  }

  public static int getIdentifierLength(String text) {
    Matcher matcher = IDENT_PATTERN.matcher(text);
    return matcher.find() ? matcher.end() : 0;
  }

}
