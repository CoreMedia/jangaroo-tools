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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Load API model from a jsduck JSON export of the Ext JS API.
 */
public class ExtJsApi {

  private static final Pattern CONSTANT_NAME_PATTERN = Pattern.compile("[A-Z][A-Z0-9_]*");
  private Set<ExtClass> extClasses;
  private Map<String,ExtClass> extClassByName;
  private Set<ExtClass> mixins;

  private static ExtClass readExtApiJson(File jsonFile) throws IOException {
    System.out.printf("Reading API from %s...\n", jsonFile.getPath());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerSubtypes(Property.class, Cfg.class, Method.class, Event.class);
    return objectMapper.readValue(jsonFile, ExtClass.class);
  }

  public <T extends Member> List<T> filterByOwner(boolean isInterface, boolean isStatic, ExtClass owner, List<Member> members, Class<T> memberType) {
    Set<String> superclassNames = new HashSet<String>();
    for (ExtClass superclass : computeTransitiveSupersAndMixins(getSuperClass(owner))) {
      superclassNames.add(superclass.name);
    }
    List<T> result = new ArrayList<T>();
    for (Member member : members) {
      if (memberType.isInstance(member) &&
              member.meta.removed == null &&
              member.static_ == isStatic &&
              !member.private_ &&
              (member.autodetected == null || !member.autodetected.containsKey("tagname")) &&
              !superclassNames.contains(member.owner) && (!isInterface || isPublicNonStaticMethodOrPropertyOrCfg(member))) {
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

  public boolean inheritsDoc(Member member) {
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

  public boolean isStatic(Member member) {
    ExtClass extClass = getExtClass(member.owner);
    return !extClass.singleton && (member.static_ || member.meta.static_ || isConstantName(member.name));
  }

  private static boolean isConstantName(String name) {
    return CONSTANT_NAME_PATTERN.matcher(name).matches();
  }

  public boolean isReadOnly(Member member) {
    return member.meta.readonly || member.readonly || isConstantName(member.name);
  }

  public boolean isProtected(Member member) {
    return member.protected_ || member.meta.protected_;
  }

  public static boolean isPublicNonStaticMethodOrPropertyOrCfg(Member member) {
    return (member instanceof Method || member instanceof Property || member instanceof Cfg)
            && !member.meta.static_ && !(member.meta.private_ || member.private_) && !(member.meta.protected_ || member.protected_)
            && !"constructor".equals(member.name);
  }

  public static boolean isConst(Member member) {
    return member.meta.readonly || member.readonly || (member.name.equals(member.name.toUpperCase()) && member.default_ != null);
  }

  // normalize / use alternate class name if it can be found in reference API:
  public ExtJsApi(File[] files) throws IOException {
    extClassByName = new HashMap<String, ExtClass>();
    extClasses = new LinkedHashSet<ExtClass>();

    for (File jsonFile : files) {
      ExtClass extClass = readExtApiJson(jsonFile);
      extClasses.add(extClass);
      extClassByName.put(extClass.name, extClass);
      if (extClass.alternateClassNames != null) {
        for (String alternateClassName : extClass.alternateClassNames) {
          extClassByName.put(alternateClassName, extClass);
        }
      }
    }
    Set<ExtClass> collectMixins = new HashSet<ExtClass>();
    for (ExtClass extClass : extClasses) {
      for (String mixin : extClass.mixins) {
        final ExtClass mixinClass = getExtClass(mixin);
        if (mixinClass != null) {
          collectMixins.add(mixinClass);
        }
      }
    }
    markTransitiveSupersAsMixins(collectMixins);
    mixins = Collections.unmodifiableSet(collectMixins);
    extClasses = Collections.unmodifiableSet(extClasses);
  }

  private void markTransitiveSupersAsMixins(Set<ExtClass> extClasses) {
    Set<ExtClass> supers = supers(extClasses);
    while (!supers.isEmpty()) {
      extClasses.addAll(supers);
      supers = supers(supers);
    }
  }

  private Set<ExtClass> computeTransitiveSupersAndMixins(ExtClass extClass) {
    Set<ExtClass> result = new HashSet<ExtClass>();
    addTransitiveSupersAndMixins(result, extClass);
    return result;
  }

  private boolean addTransitiveSupersAndMixins(Set<ExtClass> supersAndMixins, ExtClass extClass) {
    if (extClass != null && supersAndMixins.add(extClass)) {
      addTransitiveSupersAndMixins(supersAndMixins, getSuperClass(extClass));
      for (String mixin : extClass.mixins) {
        addTransitiveSupersAndMixins(supersAndMixins, getExtClass(mixin));
      }
      return true;
    }
    return false;
  }
  
  private Set<ExtClass> supers(Set<ExtClass> extClasses) {
    Set<ExtClass> result = new HashSet<ExtClass>();
    for (ExtClass extClass : extClasses) {
      ExtClass superclass = getSuperClass(extClass);
      if (superclass != null) {
        result.add(superclass);
      }
    }
    return result;
  }

  public Set<ExtClass> getExtClasses() {
    return extClasses;
  }

  public ExtClass getExtClass(String name) {
    return extClassByName.get(name);
  }

  public ExtClass getSuperClass(ExtClass extClass) {
    return getExtClass(extClass.extends_);
  }

  public Set<ExtClass> getMixins() {
    return mixins;
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
    public List<Param> properties;

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
    public String href;
  }

  public static class Deprecation {
    public String text;
    public String version;
  }

  @JsonIgnoreProperties({"html_type", "html_meta", "linenr"})
  public abstract static class Var extends Tag {
    public String type;
    @JsonProperty("default")
    public String default_;
  }

  @SuppressWarnings("unused")
  @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="tagname")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr"})
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
    public boolean readonly;
    public Map<String,Object> autodetected;
  }

  @SuppressWarnings("unused")
  @JsonTypeName("cfg")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "params", "fires", "throws", "return"})
  public static class Cfg extends Member {
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
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "params", "return", "throws", "chainable"})
  public static class Property extends Member {
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
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "throws", "fires", "method_calls", "template", "required"})
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
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "return", "throws"})
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
