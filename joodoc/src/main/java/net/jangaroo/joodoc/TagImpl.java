package net.jangaroo.joodoc;

import com.sun.javadoc.Tag;
import com.sun.javadoc.Doc;
import com.sun.javadoc.SourcePosition;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 21.07.2004
 * Time: 08:31:31
 * To change this template use File | Settings | File Templates.
 */
public class TagImpl implements Tag {
  String kind;
  String name;
  String text;
  Doc doc;
  Tag[] inlineTags;
  Tag[] firstSentenceTags;

  public TagImpl(Doc doc, String text) {
    this(doc,"Text","Text",text);
  }

  protected TagImpl(Doc doc, String kind, String name, String text) {
    this.doc = doc;
    this.kind = kind;
    this.name = name;
    this.text = text;
  }

  public Doc holder() {
    return doc;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SourcePosition position() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Tag[] firstSentenceTags() {
    if (firstSentenceTags==null) {
      firstSentenceTags = Util.extractInlineTags(doc,Util.firstSentence(getInlineText()));
    }
    return firstSentenceTags;
  }

  public Tag[] inlineTags() {
    if (inlineTags==null) {
      inlineTags = Util.extractInlineTags(doc,getInlineText());
    }
    return inlineTags;
  }

  protected String getInlineText() {
    return text;
  }

  public String kind() {
    return kind;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public String name() {
    return name;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public String text() {
    return text;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public String toString() {
    return kind+"/"+name+"/'"+text+"'";
  }

  public String getDescription() {
    return toString()+" "+Arrays.asList(inlineTags());
  }

}
