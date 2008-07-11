package com.coremedia.tools.jscdoc;

import com.sun.javadoc.Doc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;
import net.jangaroo.jooc.NodeImplBase;
import net.jangaroo.jooc.IdeDeclaration;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Array;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 13:55:38
 * To change this template use File | Settings | File Templates.
 */
public class DocImpl implements Doc {
  NodeImplBase declaration;
  private Tag[] allTags = null;
  private Tag[] firstSentenceTags = null;
  private Map/*String->Tag[]*/ tagsByName = null;
  private String rawCommentText = "rawComment";
  public static final Tag[] NO_TAGS = new Tag[0];

  public DocImpl(NodeImplBase node) {
    this.declaration = node;
  }

  private void initTags() {
    allTags = NO_TAGS;
    firstSentenceTags = NO_TAGS;
    this.tagsByName = new HashMap();
    if (((IdeDeclaration)declaration).getSymModifiers().length>0) {
      String text = Util.tidy(((IdeDeclaration)declaration).getSymModifiers()[0].getWhitespace());
      if (text.length()>0) {
        allTags = Util.extractTags(this,text);
        firstSentenceTags = Util.extractInlineTags(this,Util.firstSentence(text));
        Map/*String->List<Tag>*/ tagsByName = new HashMap();
        for (int i = 0; i < allTags.length; i++) {
          Tag tag = allTags[i];
          List tags = (List)tagsByName.get(tag.name());
          if (tags==null) {
            tags = new ArrayList();
            tagsByName.put(tag.name(),tags);
          }
          tags.add(tag);
        }
        for (Iterator iterator = tagsByName.entrySet().iterator(); iterator.hasNext();) {
          Map.Entry entry = (Map.Entry)iterator.next();
          List/*Tag*/ tagList = (List)entry.getValue();
          Object tagName = entry.getKey();
          this.tagsByName.put(tagName,tagList.toArray(new Tag[tagList.size()]));
        }
      }
    }
  }

  private Tag[] allTags() {
    if (allTags==null)
      initTags();
    return allTags;
  }

  private Map tagsByName() {
    if (tagsByName==null)
      initTags();
    return tagsByName;
  }

  public boolean isClass() {
    return false;
  }

  public boolean isConstructor() {
    return false;
  }

  public boolean isError() {
    return false;
  }

  public boolean isException() {
    return false;
  }

  public boolean isField() {
    return false;
  }

  public boolean isIncluded() {
    return true;
  }

  public boolean isInterface() {
    return false;
  }

  public boolean isMethod() {
    return false;
  }

  public boolean isOrdinaryClass() {
    return false;
  }

  public Tag[] typedTags(String tag, Class tagSubType) {
    Tag[] tags = tags(tag);
    Tag[] typedTags = (Tag[])Array.newInstance(tagSubType,tags.length);
    System.arraycopy(tags,0,typedTags,0,tags.length);
    return typedTags;
  }

  public SeeTag[] seeTags() {
    return (SeeTag[])typedTags("see",SeeTag.class);
  }

  public SourcePosition position() {
    return null;
  }

  public Tag[] firstSentenceTags() {
    if (firstSentenceTags==null)
      initTags();
    return firstSentenceTags;
  }

  public Tag[] inlineTags() {
    Tag[] allTags = allTags();
    return allTags.length>0 ? allTags[0].inlineTags() : NO_TAGS;
  }

  public Tag[] tags() {
    return allTags();
  }

  public int compareTo(Object obj) {
    return name().compareTo(((Doc)obj).name());
  }

  public String name() {
    return null;
  }

  public String getRawCommentText() {
    return rawCommentText;
  }

  public void setRawCommentText(String rawCommentText) {
    this.rawCommentText = rawCommentText;
  }

  public String commentText() {
    return rawCommentText;
  }

  public Tag[] tags(String s) {
    Tag[] tags = (Tag[])tagsByName().get("@"+s);
    return tags==null ? NO_TAGS : tags;
  }

  public boolean isEnumConstant() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isAnnotationTypeElement() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isEnum() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isAnnotationType() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
