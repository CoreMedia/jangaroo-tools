package net.jangaroo.exml.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  private static Doxi readExtApiJson(File jsonFile) throws IOException {
    System.out.printf("Reading API from %s...\n", jsonFile.getPath());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    return objectMapper.readValue(jsonFile, Doxi.class);
  }

  public <T extends Member> List<T> filterByOwner(boolean isInterface, boolean isStatic, ExtClass owner, String membersTypeName, Class<T> memberType) {
    List<T> result = new ArrayList<T>();
    Optional<Members> membersWithType = owner.items.stream().filter(members -> membersTypeName.equals(members.$type)).findFirst();
    List<? extends Member> members = membersWithType.isPresent() ? membersWithType.get().items : Collections.emptyList();
    for (Member member : members) {
      if (memberType.isInstance(member) &&
              member.static_ == isStatic &&
              !"private".equals(member.access) &&
              (!isInterface || isPublicNonStaticMethodOrPropertyOrCfg(member))) {
        result.add(memberType.cast(member));
      }
    }
    return result;
  }

  private static <T extends Member> T resolve(ExtClass owner, String name, Class<T> memberType) {
    if (owner != null) {
      for (Members members : owner.items) {
        for (Member member : members.items) {
          if (memberType.isInstance(member) &&
                  name.equals(member.name) &&
                  !member.static_) {
            return memberType.cast(member);
          }
        }
      }
    }
    return null;
  }

  public boolean inheritsDoc(Member member) {
    if (member.overrides != null && !member.overrides.isEmpty()) {
      final Overrides override = member.overrides.get(0); // or the last element? Didn't find any example of more than one element.
      final Member superMember = resolve(getExtClass(override.owner), override.name, member.getClass());
      if (superMember != null && superMember.text != null) {
        return member.text.equals(superMember.text);
      }
    }
    return false;
  }

  public boolean isStatic(Member member) {
    return member.static_ || isConstantName(member.name);
  }

  private static boolean isConstantName(String name) {
    return CONSTANT_NAME_PATTERN.matcher(name).matches();
  }

  public boolean isReadOnly(Member member) {
    return member.readonly || isConstantName(member.name);
  }

  public boolean isProtected(Member member) {
    return "protected".equals(member.access);
  }

  public static boolean isPublicNonStaticMethodOrPropertyOrCfg(Member member) {
    return (member instanceof Method || member instanceof Property || member instanceof Cfg)
            && !member.static_ && !"private".equals(member.access) && !"protected".equals(member.access)
            && !"constructor".equals(member.name);
  }

  public static boolean isConst(Member member) {
    return member.readonly || (member.name.equals(member.name.toUpperCase()) && member.value != null);
  }

  // normalize / use alternate class name if it can be found in reference API:
  public ExtJsApi(File srcFile) throws IOException {
    extClassByName = new HashMap<String, ExtClass>();
    extClasses = new LinkedHashSet<ExtClass>();

    Doxi doxi = readExtApiJson(srcFile);
    for (Tag global : doxi.global.items) {
      if (global instanceof ExtClass) {
        ExtClass extClass = (ExtClass) global;
        extClasses.add(extClass);
        extClassByName.put(extClass.name, extClass);
        if (extClass.alternateClassNames != null) {
          for (String alternateClassName : extClass.alternateClassNames) {
            extClassByName.put(alternateClassName, extClass);
          }
        }
      }
    }
    Set<ExtClass> collectMixins = new HashSet<ExtClass>();
    for (Tag global : doxi.global.items) {
      if (global instanceof ExtClass) {
        ExtClass extClass = (ExtClass) global;
        for (String mixin : extClass.mixins) {
          final ExtClass mixinClass = getExtClass(mixin);
          if (mixinClass != null) {
            collectMixins.add(mixinClass);
          }
        }
      }
    }
    markTransitiveSupersAsMixins(collectMixins);
    mixins = Collections.unmodifiableSet(collectMixins);
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
  @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="$type", visible = true)
  @JsonSubTypes({
          @JsonSubTypes.Type(value = ExtClass.class, name = "class"),
          @JsonSubTypes.Type(value = Enum.class, name = "enum"),
          @JsonSubTypes.Type(value = Member.class, name = "member"),
  })
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Tag {
    public String $type;
    public String name;
    public String text = "";
    public String access;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Tag tag = (Tag)o;
      return name.equals(tag.name) && !($type != null ? !$type.equals(tag.$type) : tag.$type != null);

    }

    @Override
    public int hashCode() {
      int result = $type != null ? $type.hashCode() : 0;
      result = 31 * result + name.hashCode();
      return result;
    }
  }

  @JsonTypeName("doxi")
  private static class Doxi extends Tag {
    public List<String> files;
    public Namespace global;
  }

  @JsonTypeName("namespace")
  private static class Namespace extends Tag {
    public List<? extends Tag> items;
  }

  @SuppressWarnings("unused")
  public static class ExtClass extends Tag {
    public String deprecatedMessage;
    public String deprecatedVersion;
    @JsonProperty("extends")
    @JsonDeserialize(using = FixExtendsDeserializer.class)
    public String extends_;
    @JsonDeserialize(using = CommaSeparatedStringsDeserializer.class)
    public List<String> mixins = Collections.emptyList();
    @JsonDeserialize(using = CommaSeparatedStringsDeserializer.class)
    public List<String> alternateClassNames;
    public String alias;
    public boolean singleton;
    public List<Members> items = Collections.emptyList();
  }

  @SuppressWarnings("unused")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = Members.class, name = "methods"),
          @JsonSubTypes.Type(value = Members.class, name = "static-methods"),
          @JsonSubTypes.Type(value = Members.class, name = "properties"),
          @JsonSubTypes.Type(value = Members.class, name = "static-properties"),
          @JsonSubTypes.Type(value = Members.class, name = "configs"),
          @JsonSubTypes.Type(value = Members.class, name = "vars"),
          @JsonSubTypes.Type(value = Members.class, name = "events"),
          @JsonSubTypes.Type(value = Members.class, name = "sass-mixins"),
  })
  private static class Members extends Tag {
    public List<? extends Member> items;
  }

  @SuppressWarnings("unused")
  public static class FilenNameAndLineNumber {
    public String filename;
    public String linenr;
    public String href;
  }

  @JsonSubTypes({
          @JsonSubTypes.Type(value = Param.class, name = "param"),
          @JsonSubTypes.Type(value = Return.class, name = "return"),
  })
  public abstract static class Var extends Tag {
    public String type = "";
    public String value;
  }

  @SuppressWarnings("unused")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = Method.class, name = "method"),
          @JsonSubTypes.Type(value = Property.class, name = "property"),
          @JsonSubTypes.Type(value = Cfg.class, name = "cfg"),
          @JsonSubTypes.Type(value = Event.class, name = "event"),

  })
  public static class Member extends Var {
    public String deprecatedMessage;
    public String deprecatedVersion;
    public String since;
    @JsonProperty("static")
    public boolean static_;
    public boolean inheritable;
    public Object src;
    public Object accessor; // TRUE or "w"
    public boolean evented;
    public List<Overrides> overrides;
    public boolean readonly;
    public Map<String,Object> autodetected;
  }

  @SuppressWarnings("unused")
  public static class Enum extends Member {
  }
  
  @SuppressWarnings("unused")
  public static class Cfg extends Member {
    public boolean required;

    public Cfg() {
      System.out.println("FOO");
    }
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
  public static class Property extends Member {
    public boolean required;

    @Override
    public String toString() {
      return access + " var " + super.toString();
    }
  }

  @SuppressWarnings("unused")
  public static class Param extends Var {
    public boolean optional;
    public List<Property> items = Collections.emptyList();
  }

  @SuppressWarnings("unused")
  public static class Return extends Var {
  }

  @SuppressWarnings("unused")
  public static class Method extends Member {
    public List<Var> items = Collections.emptyList();
    public boolean chainable;
    @JsonProperty("abstract")
    public boolean abstract_;
  }

  @SuppressWarnings("unused")
  public static class Event extends Member {
    public List<Var> items = Collections.emptyList();
    public boolean preventable;
  }

  @SuppressWarnings("UnusedDeclaration")
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
    public String deprecatedMessage;
    public String deprecatedVersion;
    public String template;
    public List<String> author;
    public List<String> docauthor;
    public boolean required;
    public Map<String,String> removed;
    public String since;
  }

  private static class CommaSeparatedStringsDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      String string = jsonParser.readValueAs(String.class);
      return new ArrayList<>(Arrays.asList(string.split(",")));
    }
  }

  private static class FixExtendsDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      return jsonParser.readValueAs(String.class).split(",")[0];
    }
  }
}
