package net.jangaroo.exml.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Load API model from a jsduck JSON export of the Ext JS API.
 */
public class ExtJsApiParser {

  private static Map<String,ExtClass> extClassByName;

  private static ExtClass readExtApiJson(File jsonFile) throws IOException {
    System.out.printf("Reading API from %s...\n", jsonFile.getPath());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerSubtypes(Property.class, Cfg.class, Method.class, Event.class);
    return objectMapper.readValue(jsonFile, ExtClass.class);
  }

  public static <T extends Member> List<T> filterByOwner(boolean isInterface, boolean isStatic, ExtClass owner, List<Member> members, Class<T> memberType) {
    List<T> result = new ArrayList<T>();
    for (Member member : members) {
      if (memberType.isInstance(member) &&
              member.meta.removed == null &&
              member.static_ == isStatic &&
              (member.autodetected == null || !member.autodetected.containsKey("tagname")) &&
              !"listeners".equals(member.name) &&
              member.owner.equals(owner.name) && (!isInterface || isPublicNonStaticMethodOrProperty(member))) {
        result.add(memberType.cast(member));
      }
    }
    return result;
  }

  private static <T extends Member> T resolve(ExtClass owner, String name, Class<T> memberType) {
    if (owner != null) {
      for (Member member : owner.members) {
        if (memberType.isInstance(member) &&
                member.owner.equals(owner.name) &&
                name.equals(member.name) &&
                !member.meta.static_) {
          return memberType.cast(member);
        }
      }
    }
    return null;
  }

  public static boolean inheritsDoc(Member member) {
    if (member.overrides != null && !member.overrides.isEmpty()) {
      final Overrides override = member.overrides.get(0); // or the last element? Didn't find any example of more than one element.
      final Member superMember = resolve(getExtClass(override.owner), override.name, member.getClass());
      if (superMember != null && superMember.doc != null) {
        final String normalizedDoc = member.doc.replace(member.owner, superMember.owner);
        return normalizedDoc.equals(superMember.doc);
      }
    }
    return false;
  }

  public static boolean isPublicNonStaticMethodOrProperty(Member member) {
    return (member instanceof Method || member instanceof Property)
            && !member.meta.static_ && !member.meta.private_ && !member.meta.protected_
            && !"constructor".equals(member.name);
  }

  public static boolean isConst(Member member) {
    return member.meta.readonly || (member.name.equals(member.name.toUpperCase()) && member.default_ != null);
  }

  // normalize / use alternate class name if it can be found in reference API:
  public static Set<ExtClass> readExtClasses(File[] files) throws IOException {
    extClassByName = new HashMap<String, ExtClass>();
    Set<ExtClass> extClasses = new LinkedHashSet<ExtClass>();

    for (File jsonFile : files) {
      ExtClass extClass = readExtApiJson(jsonFile);
      if (extClass != null && !extClass.name.startsWith("Ext.enums.")) {
        // correct wrong usage of Ext.util.Observable as a mixin:
        int observableMixinIndex = extClass.mixins.indexOf("Ext.util.Observable");
        if (observableMixinIndex != -1) {
          extClass.mixins.set(observableMixinIndex, "Ext.mixin.Observable");
        }
        if ("Ext.Base".equals(extClass.extends_)) {
          // correct inheritance / mixin API errors:
          if (extClass.mixins.contains("Ext.mixin.Observable")) {
            extClass.mixins.remove("Ext.mixin.Observable");
            extClass.extends_ = "Ext.util.Observable";
          } else if (extClass.mixins.contains("Ext.dom.Element")) {
            extClass.mixins.remove("Ext.dom.Element");
            extClass.extends_ = "Ext.dom.Element";
          }
        }
        extClasses.add(extClass);
        extClassByName.put(extClass.name, extClass);
        if (extClass.alternateClassNames != null) {
          for (String alternateClassName : extClass.alternateClassNames) {
            extClassByName.put(alternateClassName, extClass);
          }
        }
      }
    }
    return extClasses;
  }

  private static ExtClass getExtClass(String name) {
    return extClassByName.get(name);
  }

  public static boolean isSingleton(ExtClass extClass) {
    return extClass != null && extClass.singleton;
  }

  @SuppressWarnings("UnusedDeclaration")
  @JsonIgnoreProperties({"html_meta", "html_type", "short_doc", "localdoc", "linenr"})
  public static class Tag {
    public String tagname;
    public String name;
    public String doc = "";
    @JsonProperty("private")
    public boolean private_;
    public MemberReference inheritdoc;
    public Object author;
    public Object docauthor;
    public Object removed;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Tag tag = (Tag)o;
      return name.equals(tag.name) && !(tagname != null ? !tagname.equals(tag.tagname) : tag.tagname != null);

    }

    @Override
    public int hashCode() {
      int result = tagname != null ? tagname.hashCode() : 0;
      result = 31 * result + name.hashCode();
      return result;
    }
  }

  @SuppressWarnings("unused")
  @JsonIgnoreProperties({"html_meta", "html_type", "short_doc", "localdoc", "linenr", "enum", "override", "autodetected", "params"})
  public static class ExtClass extends Tag {
    public Deprecation deprecated;
    public String since;
    @JsonProperty("extends")
    public String extends_;
    public List<String> mixins = Collections.emptyList();
    public List<String> alternateClassNames;
    public Map<String,List<String>> aliases;
    public boolean singleton;
    public List<String> requires;
    public List<String> uses;
    public String code_type;
    public boolean inheritable;
    public Meta meta;
    public String id;
    public List<Member> members;
    public List<FilenNameAndLineNumber> files;
    public boolean component;
    public List<String> superclasses;
    public List<String> subclasses;
    public List<String> mixedInto;
    public List<String> parentMixins;
    @JsonProperty("abstract")
    public boolean abstract_;
    @JsonProperty("protected")
    public boolean protected_;
  }

  @SuppressWarnings("unused")
  public static class FilenNameAndLineNumber {
    public String filename;
    public String linenr;
  }

  public static class Deprecation {
    public String text;
    public String version;
  }

  @JsonIgnoreProperties({"html_type", "html_meta", "linenr", "properties"})
  public abstract static class Var extends Tag {
    public String type;
    @JsonProperty("default")
    public String default_;
  }

  @SuppressWarnings("unused")
  @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="tagname")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties"})
  public static class Member extends Var {
    public String owner;
    public Deprecation deprecated;
    public String since;
    @JsonProperty("static")
    public boolean static_;
    public Meta meta = new Meta();
    public boolean inheritable;
    public String id;
    public List<FilenNameAndLineNumber> files;
    public boolean accessor;
    public boolean evented;
    public List<Overrides> overrides;
    @JsonProperty("protected")
    public boolean protected_;
    public Map<String,Object> autodetected;
  }

  @SuppressWarnings("unused")
  @JsonTypeName("cfg")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "params", "fires", "throws", "return"})
  public static class Cfg extends Member {
    public boolean readonly;
    public boolean required;
  }
  
  @SuppressWarnings("unused")
  public static class MemberReference extends Tag {
    public String cls;
    public String member;
    public String type;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static class Overrides {
    public String name;
    public String owner;
    public String id;
    public String link;
  }

  @SuppressWarnings("unused")
  @JsonTypeName("property")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "params", "return", "throws", "chainable"})
  public static class Property extends Member {
    public boolean readonly;
    @Override
    public String toString() {
      return meta + "var " + super.toString();
    }
  }

  @SuppressWarnings("unused")
  @JsonTypeName("params")
  public static class Param extends Var {
    public boolean optional;
    public Object ext4_auto_param;
  }

  @SuppressWarnings("unused")
  @JsonTypeName("method")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "throws", "fires", "method_calls", "template", "readonly", "required"})
  public static class Method extends Member {
    public List<Param> params;
    @JsonProperty("return")
    public Param return_;
    public boolean chainable;
    @JsonProperty("abstract")
    public boolean abstract_;
  }

  @SuppressWarnings("unused")
  @JsonTypeName("event")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "return", "throws"})
  public static class Event extends Member {
    public List<Param> params;
    public boolean preventable;
  }

  @SuppressWarnings("UnusedDeclaration")
  @JsonIgnoreProperties({"aside", "chainable", "preventable"})
  public static class Meta {
    @JsonProperty("protected")
    public boolean protected_;
    @JsonProperty("private")
    public boolean private_;
    public boolean readonly;
    @JsonProperty("static")
    public boolean static_;
    @JsonProperty("abstract")
    public boolean abstract_;
    public boolean markdown;
    public Deprecation deprecated;
    public String template;
    public List<String> author;
    public List<String> docauthor;
    public boolean required;
    public Map<String,String> removed;
    public String since;
  }

}
