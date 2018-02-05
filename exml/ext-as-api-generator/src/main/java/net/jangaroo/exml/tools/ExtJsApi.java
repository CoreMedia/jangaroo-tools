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
    return filterByOwner(false, isInterface, isStatic, owner, membersTypeName, memberType);
  }

  public <T extends Member> List<T> filterByOwner(boolean isMixin, boolean isInterface, boolean isStatic, ExtClass owner, String membersTypeName, Class<T> memberType) {
    List<T> result = new ArrayList<T>();
    Optional<Members> membersWithType = owner.items.stream().filter(members -> membersTypeName.equals(members.$type)).findFirst();
    List<? extends Member> members = membersWithType.isPresent() ? membersWithType.get().items : Collections.emptyList();
    for (Member member : members) {
      if (memberType.isInstance(member) &&
              member.static_ == isStatic &&
              !"private".equals(member.access) &&
              (!isInterface || isPublicNonStaticMethodOrPropertyOrCfg(member)) &&
              (!isMixin || !isPublicNonStaticMethodOrPropertyOrCfg(member))) {
        result.add(memberType.cast(member));
      }
    }
    return result;
  }

  public <T extends Tag> T resolve(String reference, String thisClassName, Class<T> memberType) {
    String[] parts = reference.split("[#-]");
    String className = parts[0].isEmpty() ? thisClassName : parts[0];
    ExtClass owner = getExtClass(className);
    if (parts.length == 1) {
      return memberType.cast(owner);
    }
    String name = parts[parts.length - 1];
    return resolve(owner, name, memberType);
  }

  private <T extends Tag> T resolve(ExtClass owner, String name, Class<T> memberType) {
    if (owner != null) {
      for (Members members : owner.items) {
        for (Member member : members.items) {
          if (name.equals(member.name) && memberType.isInstance(member)) {
            return memberType.cast(member);
          }
        }
      }
      if (owner.extends_ != null) {
        T result = resolve(getExtClass(owner.extends_), name, memberType);
        if (result != null) {
          return result;
        }
        for (String mixin : owner.mixins) {
          result = resolve(getExtClass(mixin), name, memberType);
          if (result != null) {
            return result;
          }
        }
      }
    }
    return null;
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

    Set<ExtClass> overrides = new LinkedHashSet<>();
    for (Tag global : doxi.global.items) {
      if (global instanceof ExtClass) {
        ExtClass extClass = (ExtClass) global;
        extClasses.add(extClass);
        if (extClass.override != null) {
          overrides.add(extClass);
          if (extClass.name.equals(extClass.override) && extClassByName.containsKey(extClass.name)) {
            continue; // do not let an override hide the original class!
          }
        }
        extClassByName.put(extClass.name, extClass);
        if (extClass.alternateClassNames != null) {
          for (String alternateClassName : extClass.alternateClassNames) {
            extClassByName.put(alternateClassName, extClass);
          }
        }
      }
    }
    // apply overrides:
    for (ExtClass override : overrides) {
      ExtClass overriddenClass = extClassByName.get(override.override);
      if (overriddenClass == override) {
        // ignore self-overrides!
        continue;
      }
      if (overriddenClass == null) {
        System.err.println("Overridden class not found: " + override.name + " wants to override " + override.override);
      } else {
        overriddenClass.text += "\n<p><b>From override " + override.name + ":</b> " + override.text;
        for (Members members : override.items) {
          // find members.type in overriddenClass:
          List<Member> overriddenMembers = findOrCreateMembers(overriddenClass, members.$type);
          overriddenMembers.addAll(members.items); // TODO: maybe members are actually replaced, not complemented? Currently not.
        }
      }
    }
    // mark classes as mixins:
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

  private List<Member> findOrCreateMembers(ExtClass extClass, String memberType) {
    for (Members members : extClass.items) {
      if (memberType.equals(members.$type)) {
        return (List<Member>) members.items;
      }
    }
    Members members = new Members();
    members.$type = memberType;
    extClass.items.add(members);
    return (List<Member>) members.items;
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

  @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="$type", visible = true)
  @JsonSubTypes({
          @JsonSubTypes.Type(value = ExtClass.class, name = "class"),
          @JsonSubTypes.Type(value = Enum.class, name = "enum"),
          @JsonSubTypes.Type(value = Member.class, name = "member"),
  })
  public static class Tag {
    public String $type;
    public String name;
    public String since;
    public boolean deprecated;
    public String deprecatedMessage;
    public String deprecatedVersion;
    public String text = "";
    public String inheritdoc;
    public String localdoc;
    public String access;
    public Object src;

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
  @JsonIgnoreProperties({"files", "version"})
  private static class Doxi extends Tag {
    public Namespace global;
  }

  @JsonTypeName("namespace")
  private static class Namespace extends Tag {
    public List<? extends Tag> items;
  }

  @JsonIgnoreProperties({"extended", "extenders", "package", "mixed", "mixers", "requires", "uses", "abstract", "Classic", "CT_Location", "disable"})
  public static class ExtClass extends Tag {
    @JsonProperty("extends")
    @JsonDeserialize(using = FixExtendsDeserializer.class)
    public String extends_;
    @JsonDeserialize(using = CommaSeparatedStringsDeserializer.class)
    public List<String> mixins = Collections.emptyList();
    public String override;
    @JsonDeserialize(using = CommaSeparatedStringsDeserializer.class)
    public List<String> alternateClassNames;
    public String alias;
    public boolean singleton;
    public List<Members> items = Collections.emptyList();
  }

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

  @JsonSubTypes({
          @JsonSubTypes.Type(value = Param.class, name = "param"),
          @JsonSubTypes.Type(value = Return.class, name = "return"),
          @JsonSubTypes.Type(value = Method.class, name = "method"),
          @JsonSubTypes.Type(value = Property.class, name = "property"),
          @JsonSubTypes.Type(value = Cfg.class, name = "cfg"),
          @JsonSubTypes.Type(value = Event.class, name = "event"),
  })
  public abstract static class Var extends Tag {
    public String type = "";
    public String value;
    public List<Var> items = Collections.emptyList();
  }

  @JsonSubTypes({
          @JsonSubTypes.Type(value = Method.class, name = "method"),
          @JsonSubTypes.Type(value = Property.class, name = "property"),
          @JsonSubTypes.Type(value = Cfg.class, name = "cfg"),
          @JsonSubTypes.Type(value = Event.class, name = "event"),

  })
  public static class Member extends Var {
    @JsonProperty("static")
    public boolean static_;
    public boolean inheritable;
    public Object accessor; // TRUE or "w"
    public boolean evented;
    public boolean readonly;
    public boolean hide;
    public boolean ignore;
    public boolean undocumented;
    public boolean locale;
  }

  @JsonIgnoreProperties({"aliasPrefix", "items"})
  public static class Enum extends Tag {
  }
  
  public static class Cfg extends Member {
    public boolean required;

    public Cfg() {
      System.out.println("FOO");
    }
  }

  @JsonIgnoreProperties({"removedMessage", "removedVersion"})
  public static class Property extends Member {
    public boolean required;
    public boolean optional;
    public boolean controllable;
    public boolean dynamic;

    @Override
    public String toString() {
      return access + " var " + super.toString();
    }
  }

  public static class Param extends Var {
    public boolean optional;

    @JsonProperty("optional")
    public void setOptional(boolean value) {
      optional = value;
    }

    @JsonProperty("Optional")
    public void setUpperCaseOptional(boolean value) {
      optional = value;
    }

    public List<Property> items = Collections.emptyList();
  }

  public static class Return extends Var {
  }

  @JsonIgnoreProperties({"disable", "fires", "removedMessage", "removedVersion"})
  public static class Method extends Member {
    public boolean template;
    public boolean constructor;
    public boolean chainable;
    @JsonProperty("abstract")
    public boolean abstract_;
  }

  public static class Event extends Member {
    public List<Var> items = Collections.emptyList();
    public boolean preventable;
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
      String value = jsonParser.readValueAs(String.class);
      return Arrays.stream(value.split(","))
              .filter(className -> !"Object".equals(className))
              .findFirst()
              .orElse("Object");
    }
  }
}
