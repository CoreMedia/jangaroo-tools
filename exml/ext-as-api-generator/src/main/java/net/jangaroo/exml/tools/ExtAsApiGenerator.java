package net.jangaroo.exml.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.backend.ActionScriptCodeGeneratingModelVisitor;
import net.jangaroo.jooc.backend.JsCodeGenerator;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModelRegistry;
import net.jangaroo.jooc.model.CompilationUnitModel;
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
  private static boolean generateEventClasses;

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
    File[] files = srcDir.listFiles();
    if (files != null) {
      compilationUnitModelRegistry = new CompilationUnitModelRegistry();
      interfaces = new HashSet<String>();
      mixins = new HashSet<String>();
      Set<String> singletons = new HashSet<String>();
      readExtClasses(files);
      for (ExtClass extClass : extClasses) {
        for (String mixin : extClass.mixins) {
          String mixinName = getPreferredName(mixin);
          interfaces.add(mixinName);
          mixins.add(mixinName);
        }
        if (isSingleton(extClass)) {
          singletons.add(getPreferredName(extClass));
        }
      }

      markTransitiveSupersAsInterfaces(interfaces);
      //markTransitiveSupersAsInterfaces(singletons);
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
      String superclass = ExtAsApiGenerator.getExtClass(extClass).extends_;
      if (superclass != null) {
        String preferredName = getPreferredName(superclass);
        result.add(preferredName);
      }
    }
    return result;
  }

  private static ExtClass readExtApiJson(File jsonFile) throws IOException {
    System.out.printf("Reading API from %s...\n", jsonFile.getPath());
    ExtClass extClass = new ObjectMapper().readValue(jsonFile, ExtClass.class);
    if (JsCodeGenerator.PRIMITIVES.contains(extClass.name)) {
      System.err.println("ignoring built-in class " + extClass.name);
      return null;
    }
    return extClass;
  }

  private static void generateClassModel(ExtClass extClass) {
    String extClassName = getPreferredName(extClass);
    CompilationUnitModel extAsClassUnit = createClassModel(convertType(extClassName));
    ClassModel extAsClass = (ClassModel)extAsClassUnit.getPrimaryDeclaration();
    System.out.printf("Generating AS3 API model %s for %s...%n", extAsClassUnit.getQName(), extClassName);
    extAsClass.setAsdoc(toAsDoc(extClass.doc));
    CompilationUnitModel extAsInterfaceUnit = null;
    if (interfaces.contains(extClassName)) {
      extAsInterfaceUnit = createClassModel(convertToInterface(extClassName));
      System.out.printf("Generating AS3 API model %s for %s...%n", extAsInterfaceUnit.getQName(), extClassName);
      ClassModel extAsInterface = (ClassModel)extAsInterfaceUnit.getPrimaryDeclaration();
      extAsInterface.setInterface(true);
      extAsInterface.setAsdoc(toAsDoc(extClass.doc));
      addInterfaceForSuperclass(extClass, extAsInterface);
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
    if (extAsInterfaceUnit != null) {
      extAsClass.addInterface(extAsInterfaceUnit.getQName());
    }
    for (String mixin : extClass.mixins) {
      String superInterface = convertToInterface(mixin);
      extAsClass.addInterface(superInterface);
      if (extAsInterfaceUnit != null) {
        extAsInterfaceUnit.getClassModel().addInterface(superInterface);
      }
    }

    if (extAsInterfaceUnit != null) {
      addNonStaticMembers(extClass, extAsInterfaceUnit);
    }

    addFields(extAsClass, filterByOwner(false, extClass, extClass.statics.property));
    addMethods(extAsClass, filterByOwner(false, extClass, extClass.statics.method));

    addNonStaticMembers(extClass, extAsClassUnit);

    String configClassQName = getConfigClassQName(extClass);
    if (configClassQName != null) {
      CompilationUnitModel configClassUnit = createClassModel(configClassQName);
      ClassModel configClass = (ClassModel)configClassUnit.getPrimaryDeclaration();
      configClassUnit.getClassModel().setAsdoc(extAsClass.getAsdoc());
      configClassUnit.getClassModel().addAnnotation(new AnnotationModel("ExtConfig", new AnnotationPropertyModel("target", CompilerUtils.quote(extAsClassUnit.getQName())))); // TODO: add (x|p|gc)type!
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
      Map.Entry<String, List<String>> alias = getAlias(extClass);
      String typeProperty = alias == null ? null : "widget".equals(alias.getKey()) ? "xtype" : "type";
      if (typeProperty != null) {
        String typeValue = CompilerUtils.quote(getPreferredAlias(alias));
        configClass.addBodyCode(String.format("%n  %s['prototype'].%s = %s;%n", configClassQName, typeProperty, typeValue));

        PropertyModel typePropertyModel = (PropertyModel) extAsClass.getMember(typeProperty);
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
      if (generateEventClasses) {
        addEvents(configClass, extAsClassUnit, filterByOwner(false, extClass, extClass.members.event));
      }
      List<Member> configProperties = filterByOwner(false, extClass, extClass.members.cfg);
      addProperties(configClass, configProperties);

      // also add config option properties to target class:
      addProperties(extAsClass, configProperties);
      if (extAsInterfaceUnit != null) {
        addProperties(extAsInterfaceUnit.getClassModel(), configProperties);
      }
    }
  }

  private static String getConfigClassQName(ExtClass extClass) {
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
      if (hasOwnMember(extClass, extClass.members.cfg) || hasOwnMember(extClass, extClass.members.event)) {
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

  private static boolean hasOwnMember(ExtClass extClass, List<? extends Member> members) {
    for (Member member : members) {
      // suppress inherited config options
      // and config option "listeners", as we handle event listeners through [Event] annotations:
      if (extClass.name.equals(member.owner) && !"listeners".equals(member.name)) {
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
      nativeAnnotation.addProperty(new AnnotationPropertyModel("global", CompilerUtils.quote(nativeName)));
    }
    return nativeAnnotation;
  }

  private static void addInterfaceForSuperclass(ExtClass extClass, ClassModel extAsInterface) {
    if (extClass.extends_ != null) {
      extAsInterface.addInterface(convertToInterface(extClass.extends_));
    }
  }

  private static CompilationUnitModel createClassModel(String qName) {
    CompilationUnitModel compilationUnitModel = new CompilationUnitModel(null, new ClassModel());
    compilationUnitModel.setQName(qName);
    compilationUnitModelRegistry.register(compilationUnitModel);
    return compilationUnitModel;
  }

  private static void addNonStaticMembers(ExtClass extClass, CompilationUnitModel extAsClassUnit) {
    ClassModel extAsClass = extAsClassUnit.getClassModel();
    if (!extAsClass.isInterface()) {
      addEvents(extAsClassUnit.getClassModel(), extAsClassUnit, filterByOwner(false, extClass, extClass.members.event));
    }
    addProperties(extAsClass, filterByOwner(extAsClass.isInterface(), extClass, extClass.members.property));
    addMethods(extAsClass, filterByOwner(extAsClass.isInterface(), extClass, extClass.members.method));
  }

  private static void generateActionScriptCode(CompilationUnitModel extAsClass, File outputDir) throws IOException {
    File outputFile = CompilerUtils.fileFromQName(extAsClass.getQName(), outputDir, Jooc.AS_SUFFIX);
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR
    System.out.printf("Generating AS3 API for %s into %s...\n", extAsClass.getQName(), outputFile.getPath());
    extAsClass.visit(new ActionScriptCodeGeneratingModelVisitor(new FileWriter(outputFile)));
  }

  private static <T extends Member> List<T> filterByOwner(boolean isInterface, ExtClass owner, List<T> members) {
    List<T> result = new ArrayList<T>();
    for (T member : members) {
      if (member.meta.removed == null &&
              !"listeners".equals(member.name) &&
              member.owner.equals(owner.name) && (!isInterface || isPublicNonStaticMethodOrProperty(member))) {
        result.add(member);
      }
    }
    return result;
  }

  private static boolean isPublicNonStaticMethodOrProperty(Member member) {
    return (member instanceof Method || member instanceof Property)
            && !member.meta.static_ && !member.meta.private_ && !member.meta.protected_
            && !"constructor".equals(member.name);
  }

  private static boolean isConst(Member member) {
    return member.meta.readonly || (member.name.equals(member.name.toUpperCase()) && member.default_ != null);
  }

  private static void addEvents(ClassModel classModel, CompilationUnitModel compilationUnitModel, List<Event> events) {
    for (Event event : events) {
      AnnotationModel annotationModel = new AnnotationModel("Event",
              new AnnotationPropertyModel("name", "'on" + event.name + "'"));
      String asdoc = toAsDoc(event.doc);
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
      extAsClass.setAsdoc(String.format("%s%n@see %s", toAsDoc(event.doc), compilationUnitModel.getQName()));

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
        property.setAsdoc(toAsDoc(param.doc));
        extAsClass.addMember(property);
      }

      constructorModel.setBody("super(NAME);" + propertyAssignments.toString());

    }
    return eventTypeQName;
  }

  private static void addFields(ClassModel classModel, List<? extends Member> fields) {
    for (Member member : fields) {
      FieldModel fieldModel = new FieldModel(convertName(member.name), convertType(member.type), member.default_);
      fieldModel.setAsdoc(toAsDoc(member.doc));
      setStatic(fieldModel, member);
      fieldModel.setConst(isConst(member));
      classModel.addMember(fieldModel);
    }
  }

  private static void addProperties(ClassModel classModel, List<? extends Member> properties) {
    for (Member member : properties) {
      if (classModel.getMember(member.name) == null) {
        String type = convertType(member.type);
        if ("*".equals(type) || "Object".equals(type)) {
          // try to deduce a more specific type from the property name:
          type = "cls".equals(member.name) ? "String"
                  : "useBodyElement".equals(member.name) ? "Boolean"
                  : "items".equals(member.name) || "plugins".equals(member.name) ? "Array"
                  : type;
        }
        PropertyModel propertyModel = new PropertyModel(convertName(member.name), type);
        if ("items".equals(member.name)) {
          propertyModel.addAnnotation(new AnnotationModel(MxmlToModelParser.MXML_DEFAULT_PROPERTY_ANNOTATION));
        }
        propertyModel.setAsdoc(toAsDoc(member.doc));
        setStatic(propertyModel, member);
        propertyModel.addGetter();
        if (!member.meta.readonly) {
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
        MethodModel methodModel = isConstructor
                ? new MethodModel(classModel.getName(), null)
                : new MethodModel(convertName(methodName), convertType(method.return_.type));
        methodModel.setAsdoc(toAsDoc(method.doc));
        methodModel.getReturnModel().setAsdoc(toAsDoc(method.return_.doc));
        setStatic(methodModel, method);
        for (Param param : method.params) {
          ParamModel paramModel = new ParamModel(convertName(param.name), convertType(param.type));
          paramModel.setAsdoc(toAsDoc(param.doc));
          setDefaultValue(paramModel, param);
          paramModel.setRest(param == method.params.get(method.params.size() - 1) && param.type.endsWith("..."));
          methodModel.addParam(paramModel);
        }
        if (isConstructor && method.params.size() == 1) {
          Param theOnlyParam = method.params.get(0);
          if (!theOnlyParam.optional && "config".equals(theOnlyParam.name)) {
            ParamModel theOnlyParamModel = methodModel.getParams().get(0);
            theOnlyParamModel.setOptional(true);
            theOnlyParamModel.setType("Object");
          }
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
    memberModel.setStatic(extClass.singleton ? isStaticSingleton(extClass) : member.meta.static_);
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
    mixin = getPreferredName(mixin);
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
    if ("number".equals(extType) || "boolean".equals(extType)) {
      return Character.toUpperCase(extType.charAt(0)) + extType.substring(1);
    }
    // rename classes in package ext.selection:
    Matcher selectionClassMatcher = Pattern.compile("Ext[.]selection[.](.*)Model").matcher(extType);
    if (selectionClassMatcher.matches()) {
      return "ext.selection." + selectionClassMatcher.group(1) + "SelectionModel";
    }
    if ("HTMLElement".equals(extType) || "Event".equals(extType) || "XMLHttpRequest".equals(extType)) {
      return "js." + extType;
    }
    if ("google.maps.Map".equals(extType) || "CSSStyleSheet".equals(extType) || "CSSStyleRule".equals(extType)) {
      return "Object"; // no AS3 type yet
    }
    if (extType.endsWith("...")) {
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
      className = "I" + className;
    }
    if (JsCodeGenerator.PRIMITIVES.contains(className)) {
      if ("ext".equals(packageName)) {
        if (isStaticSingleton(extClass)) {
          // for most built-in classes, there is a static ...Util class:
          packageName = "ext.util";
          className += "Util";
        } else {
          // all others in package "ext" are prefixed with "Ext":
          className = "Ext" + className;
        }
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

    for (File jsonFile : files) {
      ExtClass extClass = readExtApiJson(jsonFile);
      if (extClass != null) {
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
          String packageName = CompilerUtils.packageName(extClassName);
          for (String alternateClassName : extClass.alternateClassNames) {
            if (!referenceApiClassNames.contains(extClassName)
                    && referenceApiClassNames.contains(alternateClassName)) {
              normalizeExtClassName.put(extClassName, alternateClassName);
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
    for (ExtClass extClass : extClasses) {
      String extClassName = extClass.name;
      if (!normalizeExtClassName.containsKey(extClassName)) {
        // new class, not contained in reference API: try to find an alternate class name with the same package mapping!
        String packageName = normalizedPackageName.get(CompilerUtils.packageName(extClassName));
        if (packageName != null) {
          for (String alternateClassName : extClass.alternateClassNames) {
            if (packageName.equals(CompilerUtils.packageName(alternateClassName))) {
              extClassName = alternateClassName;
              break;
            }
          }
        }
        normalizeExtClassName.put(extClass.name, extClassName);
      }
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
      throw new IllegalStateException("unmapped class " + extClass.name);
    }
    return normalizedClassName;
  }

  private static boolean isSingleton(ExtClass extClass) {
    return extClass != null && extClass.singleton && !isStaticSingleton(extClass);
  }

  private static boolean isStaticSingleton(ExtClass extClass) {
    return extClass != null && extClass.singleton && (extClass.extends_ == null || extClass.extends_.length() == 0)
      && extClass.mixins.isEmpty();
  }

  @SuppressWarnings("UnusedDeclaration")
  @JsonIgnoreProperties({"html_meta", "html_type", "linenr"})
  public static class Tag {
    public String tagname;
    public String name;
    public String doc;
    @JsonProperty("private")
    public String private_;
    public MemberReference inheritdoc;

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

  @SuppressWarnings("UnusedDeclaration")
  @JsonIgnoreProperties({"html_meta", "html_type", "linenr", "enum", "override"})
  public static class ExtClass extends Tag {
    @JsonProperty("extends")
    public String extends_;
    public List<String> mixins;
    public List<String> alternateClassNames;
    public Map<String,List<String>> aliases;
    public boolean singleton;
    public List<String> requires;
    public List<String> uses;
    public String code_type;
    public boolean inheritable;
    public Meta meta;
    public String id;
    public Members members;
    public Members statics;
    public List<Object> files;
    public boolean component;
    public List<String> superclasses;
    public List<String> subclasses;
    public List<String> mixedInto;
    public List<String> parentMixins;
    @JsonProperty("abstract")
    public boolean abstract_;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static class Members {
    public List<Member> cfg;
    public List<Property> property;
    public List<Method> method;
    public List<Event> event;
    public List<Member> css_var;
    public List<Member> css_mixin;
  }

  @JsonIgnoreProperties({"html_type", "html_meta", "linenr", "properties"})
  public abstract static class Var extends Tag {
    public String type;
    @JsonProperty("default")
    public String default_;
  }

  @SuppressWarnings("UnusedDeclaration")
  @JsonIgnoreProperties({"html_type", "html_meta", "linenr", "properties", "autodetected"})
  public static class Member extends Var {
    public String owner;
    public String shortDoc;
    public Meta meta;
    public boolean inheritable;
    public String id;
    public List<String> files;
    public boolean accessor;
    public boolean evented;
    public List<Overrides> overrides;
  }

  @SuppressWarnings("UnusedDeclaration")
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
  }

  public static class Property extends Member {
    @Override
    public String toString() {
      return meta + "var " + super.toString();
    }
  }

  public static class Param extends Var {
    public boolean optional;
  }

  @JsonIgnoreProperties({"html_type", "html_meta", "linenr", "properties", "autodetected", "throws"})
  public static class Method extends Member {
    public List<Param> params;
    @JsonProperty("return")
    public Param return_;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static class Event extends Member {
    public List<Param> params;
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
    public Map<String,String> deprecated;
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
