package net.jangaroo.exml.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.backend.ActionScriptCodeGeneratingModelVisitor;
import net.jangaroo.jooc.backend.JsCodeGenerator;
import net.jangaroo.jooc.model.AbstractAnnotatedModel;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.CompilationUnitModelRegistry;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.mxml.MxmlToModelParser;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate ActionScript 3 APIs from a jsduck JSON export of the Ext JS 4.x API.
 */
public class ExtAsApiGenerator {

  private static Set<ExtClass> extClasses;
  private static Map<String,ExtClass> extClassByName;
  private static CompilationUnitModelRegistry compilationUnitModelRegistry;
  private static Set<String> mixins;
  private static Set<String> interfaces;
  private static final List<String> NON_COMPILE_TIME_CONSTANT_INITIALIZERS = Arrays.asList("window", "document", "document.body", "new Date()", "this");
  private static String extPackageName;
  private static String extAmdModuleName;
  private static List<String> referenceApiClassNames;
  private static Map<String,String> normalizeExtClassName = new HashMap<String, String>();
  private static Map<String,String> normalizeExtClassNamePattern = new HashMap<String, String>();
  private static boolean generateEventClasses;
  private static boolean generateForMxml;

  public static void main(String[] args) throws IOException {
    File srcDir = new File(args[0]);
    File outputDir = new File(args[1]);
    extPackageName = args[2];
    extAmdModuleName = args[3];
    referenceApiClassNames = new ArrayList<String>();
    File referenceApiDir;
    if (args.length >= 5) {
      referenceApiDir = new File(args[4]);
      File[] referenceApiFiles = referenceApiDir.listFiles();
      if (referenceApiFiles != null) {
        for (File referenceApiFile : referenceApiFiles) {
          ExtClass referenceApiClass = readExtApiJson(referenceApiFile);
          if (referenceApiClass != null) {
            System.out.println("Remembering " + referenceApiClass.name + " for reference.");
            referenceApiClassNames.add(referenceApiClass.name);
          }
        }
      }
    }

    generateEventClasses = args.length < 6 || Boolean.valueOf(args[5]);
    generateForMxml = args.length < 7 ? false : Boolean.valueOf(args[6]);
    File[] files = srcDir.listFiles();
    if (files != null) {
      compilationUnitModelRegistry = new CompilationUnitModelRegistry();
      interfaces = new HashSet<String>();
      mixins = new HashSet<String>();
      Set<String> singletons = new HashSet<String>();
      readExtClasses(files);
      for (ExtClass extClass : extClasses) {
        for (String mixin : extClass.mixins) {
          final ExtClass mixinClass = getExtClass(mixin);
          if (mixinClass != null) {
            String mixinName = getPreferredName(mixinClass);
            interfaces.add(mixinName);
            mixins.add(mixinName);
          }
        }
        if (isSingleton(extClass)) {
          singletons.add(getPreferredName(extClass));
        }
      }

      markTransitiveSupersAsInterfaces(interfaces);
      //markTransitiveSupersAsInterfaces(singletons);

      // correct wrong usage of util.Observable as a mixin:
      interfaces.remove("Ext.util.Observable");
      // correct wrong usage of dom.Element as a mixin, not superclass, in dom.CompositeElementLite:
      interfaces.remove("Ext.Element");
      // since every Ext object extends Base, there is no need to generate an interface for that:
      interfaces.remove("Ext.Base");

      for (ExtClass extClass : extClasses) {
        generateClassModel(extClass);
      }
      complementMixinConfigClasses();
      compilationUnitModelRegistry.complementOverrides();
      compilationUnitModelRegistry.complementImports();
      for (CompilationUnitModel compilationUnitModel : compilationUnitModelRegistry.getCompilationUnitModels()) {
        generateActionScriptCode(compilationUnitModel, outputDir);
      }
    }
  }

  private static void complementMixinConfigClasses() {
    for (ExtClass extClass : extClasses) {
      String configClassQName = getConfigClassQName(extClass);
      if (configClassQName != null) {
        ClassModel configClassModel = compilationUnitModelRegistry.resolveCompilationUnit(configClassQName).getClassModel();
        if (configClassModel != null) {
          for (String mixin : extClass.mixins) {
            ExtClass extMixinClass = getExtClass(mixin);
            if (extMixinClass != null) {
              String mixinConfigClassQName = getConfigClassQName(extMixinClass);
              if (mixinConfigClassQName != null) {
                // System.err.println("*#*#*# found config mixin in " + extClass.name + ": " + mixinConfigClassQName + " (derived from mixin " + mixin + ")");
                ClassModel mixinConfigClassModel = compilationUnitModelRegistry.resolveCompilationUnit(convertType(mixinConfigClassQName)).getClassModel();
                MethodModel mixinConstructor = mixinConfigClassModel.getConstructor();
                for (MemberModel memberModel : mixinConfigClassModel.getMembers()) {
                  if (memberModel instanceof MethodModel && memberModel != mixinConstructor) {
                    if (configClassModel.getMember(memberModel.getName()) != null) {
                      System.err.println("*** [WARN] ignoring config option " + memberModel.getName() + " of config mixin " + mixinConfigClassQName + " in  config class " + configClassQName);
                    } else {
                      configClassModel.addMember(((MethodModel) memberModel).duplicate());
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private static void markTransitiveSupersAsInterfaces(Set<String> extClasses) {
    Set<String> supers = supers(extClasses);
    while (!supers.isEmpty()) {
      interfaces.addAll(supers);
      supers = supers(supers);
    }
  }

  private static Set<String> supers(Set<String> extClasses) {
    Set<String> result = new HashSet<String>();
    for (String extClass : extClasses) {
      String superclass = getExtClass(extClass).extends_;
      if (superclass != null) {
        String preferredName = getPreferredName(superclass);
        result.add(preferredName);
      }
    }
    return result;
  }

  private static ExtClass readExtApiJson(File jsonFile) throws IOException {
    System.out.printf("Reading API from %s...\n", jsonFile.getPath());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerSubtypes(Property.class, Cfg.class, Method.class, Event.class);
    ExtClass extClass = objectMapper.readValue(jsonFile, ExtClass.class);
    if (JsCodeGenerator.PRIMITIVES.contains(extClass.name)) {
      System.err.println("ignoring built-in class " + extClass.name);
      return null;
    }
    return extClass;
  }

  private static void generateClassModel(ExtClass extClass) {
    String extClassName = getPreferredName(extClass);
    CompilationUnitModel extAsClassUnit = createClassModel(convertType(extClass.name));
    ClassModel extAsClass = (ClassModel)extAsClassUnit.getPrimaryDeclaration();
    System.out.printf("Generating AS3 API model %s for %s...%n", extAsClassUnit.getQName(), extClassName);
    extAsClass.setAsdoc(toAsDoc(extClass));
    addDeprecation(extClass.deprecated, extAsClass);
    if (interfaces.contains(extClassName)) {
      extAsClass.setInterface(true);
    }
    AnnotationModel nativeAnnotation = createNativeAnnotation(extClass.name);
    if (isSingleton(extClass)) {
      extAsClass.addAnnotation(createNativeAnnotation(null));
      FieldModel singleton = new FieldModel(CompilerUtils.className(extAsClassUnit.getClassModel().getName().substring(1)), extAsClassUnit.getQName());
      singleton.setConst(true);
      singleton.setValue("new " + extAsClassUnit.getQName());
      singleton.addAnnotation(nativeAnnotation);
      singleton.setAsdoc(extAsClass.getAsdoc());
      CompilationUnitModel singletonUnit = new CompilationUnitModel(extAsClassUnit.getPackage(), singleton);
      compilationUnitModelRegistry.register(singletonUnit);

      extAsClass.setAsdoc(String.format("%s%n<p>Type of singleton %s.</p>%n@see %s %s",
        extAsClass.getAsdoc(),
        singleton.getName(),
        CompilerUtils.qName(extAsClassUnit.getPackage(), "#" + singleton.getName()),
        singletonUnit.getQName()));
    } else {
      extAsClass.addAnnotation(nativeAnnotation);
    }
    extAsClass.setSuperclass(convertType(extClass.extends_));
    for (String mixin : extClass.mixins) {
      String superInterface = convertToInterface(mixin);
      if (superInterface != null) {
        extAsClass.addInterface(superInterface);
      }
    }

    if (!interfaces.contains(extClassName)) {
      addFields(extAsClass, filterByOwner(false, true, extClass, extClass.members, Property.class));
      addMethods(extAsClass, filterByOwner(false, true, extClass, extClass.members, Method.class));
    }

    addNonStaticMembers(extClass, extAsClassUnit);

    String configClassQName = getConfigClassQName(extClass);
    if (configClassQName != null) {
      CompilationUnitModel configClassUnit = createClassModel(configClassQName);
      ClassModel configClass = (ClassModel)configClassUnit.getPrimaryDeclaration();
      configClassUnit.getClassModel().setAsdoc(extAsClass.getAsdoc());
      String superConfigClassQName = "joo.JavaScriptObject";
      for (ExtClass extSuperClass = getExtClass(extClass.extends_);
           extSuperClass != null;
           extSuperClass = getExtClass(extSuperClass.extends_)) {
        String checkSuperConfigClassQName = getConfigClassQName(extSuperClass);
        if (checkSuperConfigClassQName != null) {
          superConfigClassQName = checkSuperConfigClassQName;
          break;
        }
      }
      configClass.setSuperclass(superConfigClassQName);
      MethodModel constructor = configClass.createConstructor();
      constructor.addParam(new ParamModel("config", "Object", "null"));
      constructor.setBody("super(config);");

      AnnotationModel extConfigAnnotation = new AnnotationModel("ExtConfig", new AnnotationPropertyModel("target", CompilerUtils.quote(extAsClassUnit.getQName())));
      Map.Entry<String, List<String>> alias = getAlias(extClass);
      String typeProperty = alias == null ? null : "widget".equals(alias.getKey()) ? "xtype" : "type";
      if (typeProperty != null) {
        String typeValue = CompilerUtils.quote(getPreferredAlias(alias));
        // if explicit alias, add (x)type:
        extConfigAnnotation.addProperty(new AnnotationPropertyModel(typeProperty, typeValue));
        if (generateForMxml) {
          configClass.addBodyCode(String.format("%n  %s['prototype'].%s = %s;%n", configClassQName, typeProperty, typeValue));

          MemberModel member = extAsClass.getMember(typeProperty);
          if (member != null && !(member instanceof PropertyModel)) {
            System.err.println("[WARN]: Member " + typeProperty + " in class " + extAsClass.getName() + " should be a PropertyModel, but is a " + member.getClass().getSimpleName());
          } else {
            PropertyModel typePropertyModel = (PropertyModel) member;
            if (typePropertyModel == null) {
              typePropertyModel = new PropertyModel(typeProperty, "String");
              typePropertyModel.setAsdoc("@inheritDoc");
              typePropertyModel.addGetter();
              typePropertyModel.addSetter();
              extAsClass.addMember(typePropertyModel);
            }
            typePropertyModel.addAnnotation(new AnnotationModel(MxmlToModelParser.CONSTRUCTOR_PARAMETER_ANNOTATION,
                    new AnnotationPropertyModel(MxmlToModelParser.CONSTRUCTOR_PARAMETER_ANNOTATION_VALUE, typeValue)));
          }
        }
      }
      configClassUnit.getClassModel().addAnnotation(extConfigAnnotation);
      if (generateEventClasses) {
        addEvents(configClass, extAsClassUnit, filterByOwner(false, false, extClass, extClass.members, Event.class));
      }
      List<Cfg> configProperties = filterByOwner(false, false, extClass, extClass.members, Cfg.class);
      addProperties(configClass, configProperties);

      if (!interfaces.contains(extClassName)) {
        // adjust constructor of target class:
        MethodModel targetClassConstructor = extAsClass.getConstructor();
        if (targetClassConstructor == null) {
          targetClassConstructor = extAsClass.createConstructor();
          targetClassConstructor.addParam(new ParamModel("config", generateForMxml ? "Object" : configClassQName, "null", "@inheritDoc"));
        } else {
          final List<ParamModel> params = targetClassConstructor.getParams();
          for (ParamModel param : params) {
            if ("config".equals(param.getName())) {
              if (!generateForMxml) {
                param.setType(configClassQName);
              }
              param.setOptional(true);
              break;
            }
          }
        }
      }

      // also add config option properties to target class:
      addProperties(extAsClass, configProperties, true);
    }
  }

  private static String getConfigClassQName(ExtClass extClass) {
    // Singletons never have a config class:
    if (extClass.singleton) {
      return null;
    }
    String extClassName = getPreferredName(extClass);
    Map.Entry<String, List<String>> aliases = getAlias(extClass);
    String alias = null;
    // An Ext class needs a config class if
    if (aliases != null) {
      // a) it has an "alias" (xtype and the like)
      alias = getPreferredAlias(aliases);
    } else {
      // b) it defines some config options (members.cfg)
      // c) it defines any event
      if (hasOwnMember(extClass, extClass.members, Cfg.class) || hasOwnMember(extClass, extClass.members, Event.class)) {
        // try to make invented alias unique:
        alias = AliasFactory.INSTANCE.getAlias(extClassName);
      }
    }
    if (alias == null) {
      return null;
    }
    String configClassName = replaceSeparatorByCamelCase(alias, '-');
    configClassName = replaceSeparatorByCamelCase(configClassName, '.'); // some aliases contain dots!
    configClassName = configClassName.toLowerCase();
    String packageName = extPackageName + ".config";
    if (aliases != null) {
      String aliasCategory = aliases.getKey();
      if (!"widget".equals(aliasCategory) && !configClassName.contains(aliasCategory)) {
        configClassName += aliasCategory;
      }
    }
    String configClassQName = packageName + "." + configClassName;
    System.err.println("********* derived config class name "  + configClassQName + " from Ext class " + extClassName + " with alias " + alias);
    return configClassQName;
  }

  private static <T extends Member> boolean hasOwnMember(ExtClass extClass, List<Member> members, Class<T> memberType) {
    for (Member member : members) {
      // suppress inherited config options
      // and config option "listeners", as we handle event listeners through [Event] annotations:
      if (extClass.name.equals(member.owner) && memberType.isInstance(member) && !"listeners".equals(member.name)) {
        return true;
      }
    }
    return false;
  }

  private static String getPreferredAlias(Map.Entry<String, List<String>> aliases) {
    String alias;
    alias = aliases.getValue().get(aliases.getValue().size() - 1);
    if (aliases.getValue().size() > 1) {
      // for the following exceptions, use the first alias:
      if ("box".equals(alias)) {
        alias = aliases.getValue().get(0);
      }
      System.err.println("***### multiple aliases: " + aliases.getValue() + ", choosing " + alias);
    }
    return alias;
  }

  private static Map.Entry<String, List<String>> getAlias(ExtClass extClass) {
    Iterator<Map.Entry<String,List<String>>> iterator = extClass.aliases.entrySet().iterator();
    if (iterator.hasNext()) {
      Map.Entry<String, List<String>> firstEntry = iterator.next();
      if (firstEntry.getValue().size() > 0) {
        return firstEntry;
      }
    }
    return null;
  }

  private static AnnotationModel createNativeAnnotation(String nativeName) {
    AnnotationModel nativeAnnotation = new AnnotationModel(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeName != null) {
      nativeAnnotation.addProperty(new AnnotationPropertyModel("amd", CompilerUtils.quote(extAmdModuleName)));
      nativeAnnotation.addProperty(new AnnotationPropertyModel(null, CompilerUtils.quote(nativeName)));
    }
    return nativeAnnotation;
  }

  private static CompilationUnitModel createClassModel(String qName) {
    CompilationUnitModel oldCompilationUnitModel = compilationUnitModelRegistry.resolveCompilationUnit(qName);
    if (oldCompilationUnitModel != null) {
      System.err.println("[WARN] Redefining class " + qName);
      return oldCompilationUnitModel;
    }
    CompilationUnitModel compilationUnitModel = new CompilationUnitModel(null, new ClassModel());
    compilationUnitModel.setQName(qName);
    compilationUnitModelRegistry.register(compilationUnitModel);
    return compilationUnitModel;
  }

  private static void addNonStaticMembers(ExtClass extClass, CompilationUnitModel extAsClassUnit) {
    ClassModel extAsClass = extAsClassUnit.getClassModel();
    if (!extAsClass.isInterface()) {
      addEvents(extAsClassUnit.getClassModel(), extAsClassUnit, filterByOwner(false, false, extClass, extClass.members, Event.class));
    }
    addProperties(extAsClass, filterByOwner(extAsClass.isInterface(), false, extClass, extClass.members, Property.class));
    addMethods(extAsClass, filterByOwner(extAsClass.isInterface(), false, extClass, extClass.members, Method.class));
  }

  private static void generateActionScriptCode(CompilationUnitModel extAsClass, File outputDir) throws IOException {
    File outputFile = CompilerUtils.fileFromQName(extAsClass.getQName(), outputDir, Jooc.AS_SUFFIX);
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR
    System.out.printf("Generating AS3 API for %s into %s...\n", extAsClass.getQName(), outputFile.getPath());
    extAsClass.visit(new ActionScriptCodeGeneratingModelVisitor(new FileWriter(outputFile)));
  }

  private static <T extends Member> List<T> filterByOwner(boolean isInterface, boolean isStatic, ExtClass owner, List<Member> members, Class<T> memberType) {
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

  private static boolean inheritsDoc(Member member) {
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

  private static boolean isPublicNonStaticMethodOrProperty(Member member) {
    return (member instanceof Method || member instanceof Property)
            && !member.meta.static_ && !member.meta.private_ && !member.meta.protected_
            && !"constructor".equals(member.name);
  }

  private static boolean isConst(Member member) {
    return member.meta.readonly || (member.name.equals(member.name.toUpperCase()) && member.default_ != null);
  }

  private static void addDeprecation(Deprecation deprecation, AbstractAnnotatedModel model) {
    if (deprecation != null) {
      final AnnotationModel deprecated = new AnnotationModel("Deprecated");
      if (deprecation.text != null && !deprecation.text.matches("\\s*")) {
        deprecated.addProperty(new AnnotationPropertyModel(
                "message",
                CompilerUtils.quote(deprecation.text.replace("<p>", "").replace("</p>", ""))));
      }
      if (deprecation.version != null && !deprecation.version.matches("\\s*")) {
        deprecated.addProperty(new AnnotationPropertyModel(
                "since",
                deprecation.version.startsWith("\"") ? deprecation.version : CompilerUtils.quote(deprecation.version)));
      }
      model.addAnnotation(deprecated);
    }
  }

  private static void addEvents(ClassModel classModel, CompilationUnitModel compilationUnitModel, List<Event> events) {
    for (Event event : events) {
      AnnotationModel annotationModel = new AnnotationModel("Event",
              new AnnotationPropertyModel("name", "'on" + event.name + "'"));
      String asdoc = toAsDoc(event);
      if (generateEventClasses) {
        String eventTypeQName = generateEventClass(compilationUnitModel, event);
        annotationModel.addProperty(new AnnotationPropertyModel("type", "'" + eventTypeQName + "'"));
        asdoc +=  String.format("%n@eventType %s.NAME", eventTypeQName);
      }
      annotationModel.setAsdoc(asdoc);
      classModel.addAnnotation(annotationModel);
      System.err.println("*** adding event " + event.name + " to class " + classModel.getName());
    }
  }

  public static String capitalize(String name) {
    return name == null || name.length() == 0 ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
  }

  private static String generateEventClass(CompilationUnitModel compilationUnitModel, Event event) {
    ClassModel classModel = compilationUnitModel.getClassModel();
    String eventTypeQName = CompilerUtils.qName(compilationUnitModel.getPackage(),
            "events." + classModel.getName() + capitalize(event.name) + "Event");
    CompilationUnitModel extAsClassUnit = compilationUnitModelRegistry.resolveCompilationUnit(eventTypeQName);
    if (extAsClassUnit == null) {
      extAsClassUnit = createClassModel(eventTypeQName);
      ClassModel extAsClass = (ClassModel)extAsClassUnit.getPrimaryDeclaration();
      extAsClass.setSuperclass("flash.events.Event");
      extAsClass.setAsdoc(String.format("%s%n@see %s", toAsDoc(event), compilationUnitModel.getQName()));

      FieldModel eventNameConstant = new FieldModel("NAME", "String", CompilerUtils.quote("on" + event.name));
      eventNameConstant.setStatic(true);
      eventNameConstant.setConst(true);
      eventNameConstant.setAsdoc(String.format("This constant defines the value of the <code>type</code> property of the event object%nfor a <code>%s</code> event.%n@eventType %s", "on" + event.name, "on" + event.name));
      extAsClass.addMember(eventNameConstant);

      MethodModel constructorModel = extAsClass.createConstructor();
      constructorModel.addParam(new ParamModel("arguments", "Array"));
      StringBuilder propertyAssignments = new StringBuilder();
      for (int i = 0; i < event.params.size(); i++) {
        Param param = event.params.get(i);

        // add assignment to constructor body:
        propertyAssignments.append(String.format("%n    this['%s'] = arguments[%d];", convertName(param.name), i));

        // add getter method:
        MethodModel property = new MethodModel(MethodType.GET, convertName(param.name), convertType(param.type));
        property.setAsdoc(toAsDoc(param));
        extAsClass.addMember(property);
      }

      constructorModel.setBody("super(NAME);" + propertyAssignments.toString());

    }
    return eventTypeQName;
  }

  private static void addFields(ClassModel classModel, List<? extends Member> fields) {
    for (Member member : fields) {
      PropertyModel fieldModel = new PropertyModel(convertName(member.name), convertType(member.type));
      setStatic(fieldModel, member);
      fieldModel.addGetter().setAsdoc(toAsDoc(member));
      if (!isConst(member)) {
        fieldModel.addSetter().setAsdoc("@private");
      }
      addDeprecation(member.deprecated, fieldModel);
      classModel.addMember(fieldModel);
    }
  }

  private static void addProperties(ClassModel classModel, List<? extends Member> properties) {
    addProperties(classModel, properties, false);
  }

  private static void addProperties(ClassModel classModel, List<? extends Member> properties,
                                    boolean forceReadOnly) {
    for (Member member : properties) {
      if (classModel.getMember(member.name) == null) {
        if (inheritsDoc(member)) {
           // suppress overridden properties with the same JSDoc!
           continue;
         }
        String type = convertType(member.type);
        if ("*".equals(type) || "Object".equals(type)) {
          // try to deduce a more specific type from the property name:
          type = "cls".equals(member.name) ? "String"
                  : "useBodyElement".equals(member.name) ? "Boolean"
                  : "items".equals(member.name) || "plugins".equals(member.name) ? "Array"
                  : type;
        }
        PropertyModel propertyModel = new PropertyModel(convertName(member.name), type);
        if (generateForMxml && "items".equals(member.name)) {
          propertyModel.addAnnotation(new AnnotationModel(MxmlToModelParser.MXML_DEFAULT_PROPERTY_ANNOTATION));
        }
        propertyModel.setAsdoc(toAsDoc(member));
        addDeprecation(member.deprecated, propertyModel);
        setStatic(propertyModel, member);
        propertyModel.addGetter();
        if (!forceReadOnly && !member.meta.readonly) {
          propertyModel.addSetter();
        }
        classModel.addMember(propertyModel);
      }
    }
  }

  private static void addMethods(ClassModel classModel, List<Method> methods) {
    for (Method method : methods) {
      String methodName = method.name;
      if (classModel.getMember(methodName) == null) {
        boolean isConstructor = methodName.equals("constructor");
        if (!isConstructor && inheritsDoc(method)) {
          // suppress overridden methods with the same JSDoc!
          continue;
        }
        MethodModel methodModel = isConstructor
                ? new MethodModel(classModel.getName(), null)
                : new MethodModel(convertName(methodName), method.return_ == null ? null : convertType(method.return_.type));
        methodModel.setAsdoc(toAsDoc(method));
        if (method.return_ != null) {
          methodModel.getReturnModel().setAsdoc(toAsDoc(method.return_));
        }
        setStatic(methodModel, method);
        addDeprecation(method.deprecated, methodModel);
        for (Param param : method.params) {
          ParamModel paramModel = new ParamModel(convertName(param.name), convertType(param.type));
          paramModel.setAsdoc(toAsDoc(param));
          setDefaultValue(paramModel, param);
          paramModel.setRest(param == method.params.get(method.params.size() - 1) && param.type.endsWith("..."));
          methodModel.addParam(paramModel);
        }
        try {
          classModel.addMember(methodModel);
        } catch (IllegalArgumentException e) {
          System.err.println("while adding method " + methodModel + ": " + e);
        }
      }
    }
  }

  private static void setStatic(MemberModel memberModel, Member member) {
    ExtClass extClass = getExtClass(member.owner);
    memberModel.setStatic(!extClass.singleton && member.meta.static_);
  }

  private static String toAsDoc(Tag tag) {
    StringBuilder asDoc = new StringBuilder(toAsDoc(tag.doc));
    if (tag instanceof Var && ((Var)tag).default_ != null) {
      asDoc.append("\n@default ").append(((Var)tag).default_);
    }
    if (tag instanceof Member && ((Member)tag).since != null) {
      asDoc.append("\n@since ").append(((Member)tag).since);
    }
    return asDoc.toString();
  }

  private static String toAsDoc(String doc) {
    // remove <locale> and </locale>:
    String asDoc = doc.replaceAll("</?locale>", "");
    asDoc = asDoc.trim();
    if (asDoc.startsWith("<p>")) {
      // remove <p>...</p> around first paragraph:
      asDoc = asDoc.substring(3);
      int endTagPos = asDoc.indexOf("</p>");
      if (endTagPos != -1) {
        asDoc = asDoc.substring(0, endTagPos) + asDoc.substring(endTagPos + 4);
      }
    }
    if (asDoc.startsWith("{")) {
      int closingBracePos = asDoc.indexOf("} ");
      if (closingBracePos != -1) {
        asDoc = asDoc.substring(closingBracePos + 2);
      }
    }
    // add closing "/" on <img> elements:
    asDoc = asDoc.replaceAll("(<img[^>]*[^/])>", "$1/>");
    return asDoc;
  }

  private static void setDefaultValue(ParamModel paramModel, Param param) {
    String defaultValue = param.default_;
    if (defaultValue != null) {
      if (NON_COMPILE_TIME_CONSTANT_INITIALIZERS.contains(defaultValue)) {
        paramModel.setAsdoc("(Default " + defaultValue + ") " + paramModel.getAsdoc());
        defaultValue = null;
        param.optional = true; // only in case it is set inconsistently...
      }
    }
    if (defaultValue == null && param.optional) {
      defaultValue = AS3Type.getDefaultValue(paramModel.getType());
    }
    if (defaultValue != null && "String".equals(param.type) &&
            !(defaultValue.equals("null") || defaultValue.startsWith("'") || defaultValue.startsWith("\""))) {
      defaultValue = CompilerUtils.quote(defaultValue);
    }
    paramModel.setValue(defaultValue);
  }

  private static String convertName(String name) {
    name = replaceSeparatorByCamelCase(name, '-');
    return "is".equals(name) ? "matches" :
            "class".equals(name) ? "cls" :
                    "this".equals(name) ? "source" :
                            "new".equals(name) ? "new_" :
                                    "default".equals(name) ? "default_" :
                                            name;
  }

  private static String replaceSeparatorByCamelCase(String string, char separator) {
    while (true) {
      int separatorPos = string.indexOf(separator);
      if (separatorPos == -1) {
        break;
      }
      string = string.substring(0, separatorPos) + string.substring(separatorPos + 1, separatorPos + 2).toUpperCase() + string.substring(separatorPos + 2);
    }
    return string;
  }

  private static String convertToInterface(String mixin) {
    final ExtClass mixinClass = getExtClass(mixin);
    if (mixinClass == null) {
      return null;
    }
    mixin = getPreferredName(mixinClass);
    String packageName = CompilerUtils.packageName(mixin).toLowerCase();
    String className = "I" + CompilerUtils.className(mixin);
    if (packageName.startsWith("ext")) {
      packageName = extPackageName + packageName.substring(3);
    }
    return CompilerUtils.qName(packageName, className);
  }

  private static String convertType(String extType) {
    if (extType == null) {
      return null;
    }
    if ("undefined".equals(extType) || "null".equals(extType)) {
      return "void";
    }
    if ("number".equals(extType) || "boolean".equals(extType) || "string".equals(extType)) {
      return Character.toUpperCase(extType.charAt(0)) + extType.substring(1);
    }
    // rename classes in package ext.selection:
    Matcher selectionClassMatcher = Pattern.compile("Ext[.]selection[.](.*)Model").matcher(extType);
    if (selectionClassMatcher.matches()) {
      return "ext.selection." + selectionClassMatcher.group(1) + "SelectionModel";
    }
    // TODO: missing in "alternateClassNames" because jsduck failed to detect "alternateClassName":
    if ("Ext.tip.QuickTipManager".equals(extType)) {
      return "ext.IQuickTips";
    }
    if ("HTMLElement".equals(extType) || "Event".equals(extType) || "XMLHttpRequest".equals(extType)) {
      return "js." + extType;
    }
    if ("google.maps.Map".equals(extType) || "CSSStyleSheet".equals(extType) || "CSSStyleRule".equals(extType)) {
      return "Object"; // no AS3 type yet
    }
    if (extType.startsWith("Ext.enums.")) {
      return "String";
    }
    if (extType.endsWith("...") || extType.matches("[a-zA-Z0-9._$<>]+\\[\\]")) {
      return "Array";
    }
    if (!extType.matches("[a-zA-Z0-9._$<>]+") || "Mixed".equals(extType)) {
      return "*"; // TODO: join types? rather use Object? simulate overloading by splitting into several methods?
    }
    ExtClass extClass = getExtClass(extType);
    if (extClass != null) {
      extType = getPreferredName(extClass);
    }
    if ("Ext".equals(extType)) {
      // special case: move singleton "Ext" into package "ext":
      extType = "ext.Ext";
    }
    String packageName = CompilerUtils.packageName(extType).toLowerCase();
    String className = CompilerUtils.className(extType);
    if (isSingleton(extClass)) {
      className = "S" + className;
    } else if (interfaces.contains(extType)) {
      className = "I" + className;
    }
    if (JsCodeGenerator.PRIMITIVES.contains(className)) {
      if ("ext".equals(packageName)) {
        // all in package "ext" are prefixed with "Ext":
        className = "Ext" + className;
      } else {
        // all in other packages are postfixed with the upper-cased last package segment:
        className += ExmlUtils.createComponentClassName(packageName.substring(packageName.lastIndexOf('.') + 1));
      }
    } else if ("is".equals(className)) {
      // special case lower-case "is" class:
      className = "Is";
    }
    if (packageName.startsWith("ext")) {
      packageName = extPackageName + packageName.substring(3);
    }
    return CompilerUtils.qName(packageName, className);
  }

  // normalize / use alternate class name if it can be found in reference API:
  private static void readExtClasses(File[] files) throws IOException {
    extClassByName = new HashMap<String, ExtClass>();
    extClasses = new LinkedHashSet<ExtClass>();
    Set<ExtClass> privateClasses = new HashSet<ExtClass>();

    for (File jsonFile : files) {
      ExtClass extClass = readExtApiJson(jsonFile);
      if (extClass != null && !extClass.name.startsWith("Ext.enums.")) {
        // correct wrong usage of Ext.util.Observable as a mixin:
        int observableMixinIndex = extClass.mixins.indexOf("Ext.util.Observable");
        if (observableMixinIndex != -1) {
          //extClass.mixins = new ArrayList<String>(extClass.mixins);
          extClass.mixins.set(observableMixinIndex, "Ext.mixin.Observable");
        }
        if ("Ext.Base".equals(extClass.extends_)) {
          // correct inheritance / mixin API errors:
          if (extClass.mixins.contains("Ext.mixin.Observable")) {
            //extClass.mixins = new ArrayList<String>(extClass.mixins);
            extClass.mixins.remove("Ext.mixin.Observable");
            extClass.extends_ = "Ext.util.Observable";
          } else if (extClass.mixins.contains("Ext.dom.Element")) {
            //extClass.mixins = new ArrayList<String>(extClass.mixins);
            extClass.mixins.remove("Ext.dom.Element");
            extClass.extends_ = "Ext.dom.Element";
          }
        }
        if (extClass.private_) {
          privateClasses.add(extClass);
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
    Map<String,String> normalizedPackageName = new HashMap<String, String>();
    if (!referenceApiClassNames.isEmpty()) {
      // find the alternate class name that is used in reference API:
      for (ExtClass extClass : extClasses) {
        if (extClass.alternateClassNames != null) {
          String extClassName = extClass.name;
          if (referenceApiClassNames.contains(extClassName)) {
            privateClasses.remove(extClass);
          } else {
            String packageName = CompilerUtils.packageName(extClassName);
            for (String alternateClassName : extClass.alternateClassNames) {
              if (referenceApiClassNames.contains(alternateClassName)) {
                normalizeExtClassName.put(extClassName, alternateClassName);
                privateClasses.remove(extClass);
                // also record package mapping for new classes:
                String alternatePackageName = CompilerUtils.packageName(alternateClassName);
                if (!packageName.equals(alternatePackageName)) { // only record actual differences!
                  normalizedPackageName.put(packageName, alternatePackageName);
                }
                break;
              }
            }
          }
        }
      }
    }

    for (ExtClass extClass : extClasses) {
      if (!privateClasses.contains(extClass)) {
        markPublic(privateClasses, extClass.name);
      }
    }
    extClasses.removeAll(privateClasses);

    // some special cases to keep compatibility with Ext AS 3.4:
    normalizeExtClassName.put("Ext.Date", "ext.util.DateUtil");
    normalizeExtClassName.put("Ext.String", "ext.util.StringUtil");
    normalizeExtClassName.put("Ext.Array", "ext.util.ArrayUtil");
    normalizeExtClassName.put("Ext.Function", "ext.util.Functions"); // exceptional name in Ext AS 3.4 :-(
    normalizeExtClassName.put("Ext.fx.target.Target", "ext.fx.target.Target");
    normalizeExtClassName.put("Ext.grid.header.Container", "ext.grid.HeaderContainer");

    normalizeExtClassNamePattern.put("Ext\\.fx\\.target\\.(.+)", "ext.fx.target.$1Target");
    normalizeExtClassNamePattern.put("Ext\\.plugin\\.(.+)", "ext.plugin.$1Plugin");
    normalizeExtClassNamePattern.put("Ext\\.grid\\.plugin\\.(.+)", "ext.grid.plugin.$1Plugin");
    normalizeExtClassNamePattern.put("Ext\\.grid\\.selection\\.(.+)", "ext.grid.selection.$1Selection");

    for (ExtClass extClass : extClasses) {
      String extClassName = extClass.name;
      if (!normalizeExtClassName.containsKey(extClassName)) {
        // new class, not contained in reference API
        // try to match a pattern:
        for (Map.Entry<String, String> patternReplacementEntry : normalizeExtClassNamePattern.entrySet()) {
          final String pattern = patternReplacementEntry.getKey();
          final Matcher matcher = Pattern.compile(pattern).matcher(extClassName);
          if (matcher.matches()) {
            extClassName = extClassName.replaceAll(pattern, patternReplacementEntry.getValue());
            break;
          }
        }
        if (extClassName.equals(extClass.name)) {  // no matching pattern found...
          // try to find an alternate class name with the same package mapping!
          String packageName = normalizedPackageName.get(CompilerUtils.packageName(extClassName));
          if (packageName != null) {
            for (String alternateClassName : extClass.alternateClassNames) {
              if (packageName.equals(CompilerUtils.packageName(alternateClassName))) {
                extClassName = alternateClassName;
                break;
              }
            }
          }
        }
        normalizeExtClassName.put(extClass.name, extClassName);
      }
    }
  }

  private static void markPublic(Set<ExtClass> privateClasses, String extClassName) {
    ExtClass extClass = getExtClass(extClassName);
    privateClasses.remove(extClass);
    if (extClass.extends_ != null) {
      markPublic(privateClasses, extClass.extends_);
    }
    for (String mixin : extClass.mixins) {
      markPublic(privateClasses, mixin);
    }
  }

  private static ExtClass getExtClass(String name) {
    return extClassByName.get(name);
  }

  // normalize / use alternate class name if it can be found in reference API:
  private static String getPreferredName(String extClassName) {
    return getPreferredName(getExtClass(extClassName));
  }

  private static String getPreferredName(ExtClass extClass) {
    String normalizedClassName = normalizeExtClassName.get(extClass.name);
    if (normalizedClassName == null) {
      System.err.println(String.format("Private API %s leaks into public API.", extClass.name));
      // throw new IllegalStateException("unmapped class " + extClass.name);
      return "Object";
    }
    return normalizedClassName;
  }

  private static boolean isSingleton(ExtClass extClass) {
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

  @JsonTypeName("cfg")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "params", "fires", "throws", "return"})
  public static class Cfg extends Member {
    public boolean readonly;
    public boolean required;
  }
  
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

  @JsonTypeName("property")
  @JsonIgnoreProperties({"html_type", "html_meta", "short_doc", "localdoc", "linenr", "properties", "params", "return", "throws", "chainable"})
  public static class Property extends Member {
    public boolean readonly;
    @Override
    public String toString() {
      return meta + "var " + super.toString();
    }
  }

  @JsonTypeName("params")
  public static class Param extends Var {
    public boolean optional;
    public Object ext4_auto_param;
  }

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

  private static class AliasFactory {
    private static final AliasFactory INSTANCE = new AliasFactory();

    private final Map<String, String> UNIQUE_ALIAS_MAPPING = new LinkedHashMap<String, String>();

    private void add(String constant, String replacement) {
      addPattern(constant.replaceAll("\\.", "\\."), replacement);
    }

    private void addPattern(String pattern, String replacement) {
      UNIQUE_ALIAS_MAPPING.put(pattern, replacement);
    }

    private AliasFactory() {
      add("Ext.data.Field", "datafield");
      add("Ext.slider.Tip", "slidertip");
      add("Ext.form.Action", "formaction");
      add("Ext.Class", "extclass");
      add("Ext.state.Provider", "stateprovider");
      addPattern("Ext\\.list\\.(.*)Column", "lv$1column");
      add("Ext.grid.Column", "gridcolumn");
      addPattern("Ext\\.grid\\.(.+)Column", "$1column");
      add("Ext.store.Store", "store");
      addPattern("Ext\\.(.+)\\.Store", "$1store");
      addPattern("Ext\\.chart\\.(.+)", "chart$1");
      add("Ext.data.proxy.Proxy", "dataproxy");
      add("Ext.panel.Proxy", "panelproxy");
      add("Ext.view.DragZone", "viewdragzone");
      addPattern("Ext\\.(.+)\\.Controller", "$1controller");
      add("Ext.form.field.Field", "fieldmixin");
      add("Ext.app.domain.Direct", "appdomaindirect");
      add("Ext.layout.boxOverflow.Menu", "boxoverflowmenu");
    }

    public String getAlias(String extClassName) {
      for (Map.Entry<String, String> patternStringEntry : UNIQUE_ALIAS_MAPPING.entrySet()) {
        String pattern = patternStringEntry.getKey();
        Matcher matcher = Pattern.compile(pattern).matcher(extClassName);
        if (matcher.matches()) {
          return extClassName.replaceAll(pattern, patternStringEntry.getValue()).toLowerCase();
        }
      }
      return CompilerUtils.className(extClassName).toLowerCase();
    }
  }
}
