package net.jangaroo.exml.tools;

import net.jangaroo.exml.tools.ExtJsApi.ExtClass;
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
import net.jangaroo.jooc.model.NamedModel;
import net.jangaroo.jooc.model.NamespacedModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.model.TypedModel;
import net.jangaroo.jooc.mxml.MxmlToModelParser;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static net.jangaroo.exml.tools.ExtJsApi.Cfg;
import static net.jangaroo.exml.tools.ExtJsApi.Deprecation;
import static net.jangaroo.exml.tools.ExtJsApi.Event;
import static net.jangaroo.exml.tools.ExtJsApi.Member;
import static net.jangaroo.exml.tools.ExtJsApi.Method;
import static net.jangaroo.exml.tools.ExtJsApi.Param;
import static net.jangaroo.exml.tools.ExtJsApi.Property;
import static net.jangaroo.exml.tools.ExtJsApi.Tag;
import static net.jangaroo.exml.tools.ExtJsApi.Var;
import static net.jangaroo.exml.tools.ExtJsApi.isConst;
import static net.jangaroo.exml.tools.ExtJsApi.isSingleton;

/**
 * Generate ActionScript 3 APIs from a jsduck JSON export of the Ext JS 4.x API.
 */
public class ExtAsApiGenerator {

  private static ExtJsApi extJsApi;
  private static Set<ExtClass> extClasses;
  private static CompilationUnitModelRegistry compilationUnitModelRegistry;
  private static Set<String> interfaces;
  private static final List<String> NON_COMPILE_TIME_CONSTANT_INITIALIZERS = Arrays.asList("window", "document", "document.body", "new Date()", "this");
  private static ExtAsApi referenceApi;
  private static boolean generateEventClasses;
  private static boolean generateForMxml;
  private static Properties jsAsNameMappingProperties = new Properties();
  private static Properties jsConfigClassNameMappingProperties = new Properties();

  public static void main(String[] args) throws IOException {
    File srcDir = new File(args[0]);
    File outputDir = new File(args[1]);
    referenceApi = new ExtAsApi("2.0.14", "2.0.13");

    generateEventClasses = args.length <= 2 || Boolean.valueOf(args[2]);
    generateForMxml = args.length <= 3 ? false : Boolean.valueOf(args[3]);

    jsAsNameMappingProperties.load(ExtAsApiGenerator.class.getClassLoader().getResourceAsStream("net/jangaroo/exml/tools/js-as-name-mapping.properties"));
    jsConfigClassNameMappingProperties.load(ExtAsApiGenerator.class.getClassLoader().getResourceAsStream("net/jangaroo/exml/tools/js-config-name-mapping.properties"));

    File[] files = srcDir.listFiles();
    if (files != null) {
      compilationUnitModelRegistry = new CompilationUnitModelRegistry();
      interfaces = new HashSet<String>();
      extJsApi = new ExtJsApi(files);
      extClasses = new HashSet<ExtClass>(extJsApi.getExtClasses());
      removePrivateApiClasses();

      Set<ExtClass> mixins = extJsApi.getMixins();
      for (ExtClass mixin : mixins) {
        String mixinName = getActionScriptName(mixin);
        if (mixinName != null) {
          interfaces.add(mixinName);
        }
      }

      // correct wrong usage of util.Observable as a mixin:
      interfaces.remove("ext.util.Observable");
      // correct wrong usage of dom.Element as a mixin, not superclass, in dom.CompositeElementLite:
      interfaces.remove("ext.Element");
      // since every Ext object extends Base, there is no need to generate an interface for that:
      interfaces.remove("ext.Base");
      interfaces.remove("Object");
      interfaces.add("ext.EventObjectImpl");

      for (ExtClass extClass : extClasses) {
        CompilationUnitModel compilationUnitModel = generateClassModel(extClass);
        if (compilationUnitModel != null) {
          generateConfigClassModel(extClass, compilationUnitModel);
        }
      }
      compilationUnitModelRegistry.complementOverrides();
      compilationUnitModelRegistry.complementImports();

      adaptToReferenceApi();

      // again, to make corrections consistent (actually needed?)
//      compilationUnitModelRegistry.complementOverrides();
//      compilationUnitModelRegistry.complementImports();

      if (!outputDir.exists()) {
        System.err.println("No output directory specified, skipping code generation.");
        return;
      }
      for (CompilationUnitModel compilationUnitModel : compilationUnitModelRegistry.getCompilationUnitModels()) {
        generateActionScriptCode(compilationUnitModel, outputDir);
      }
    }
  }

  private static void adaptToReferenceApi() {
    System.err.printf("Class\tremoved\tchanged\tsame%n");
    for (CompilationUnitModel compilationUnitModel : compilationUnitModelRegistry.getCompilationUnitModels()) {
      String qName = compilationUnitModel.getQName();
      NamedModel primaryDeclaration = compilationUnitModel.getPrimaryDeclaration();
      if (primaryDeclaration instanceof FieldModel) {
        // use compilation units that define type of singleton, not the singleton CUs themselves:
        String singletonType = ((FieldModel) primaryDeclaration).getType(); // already fully qualified!
        compilationUnitModel = compilationUnitModelRegistry.resolveCompilationUnit(singletonType);

        CompilationUnitModel singletonReference = getReferenceDeclaration(qName);
        if (singletonReference == null || !(singletonReference.getPrimaryDeclaration() instanceof FieldModel)) {
          continue;
        }
        qName = CompilerUtils.qName(CompilerUtils.packageName(qName), ((FieldModel) singletonReference.getPrimaryDeclaration()).getType());
      }
      if (primaryDeclaration instanceof ClassModel) {
        for (CompilationUnitModel referenceClass : getReferenceDeclarations(qName)) {
          if (referenceClass.getPrimaryDeclaration() instanceof ClassModel) {
            adaptToReferenceApi(compilationUnitModel, referenceClass);
          }
        }
      }
    }
  }

  private static void adaptToReferenceApi(CompilationUnitModel compilationUnitModel, CompilationUnitModel referenceCompilationUnitModel) {
    // System.err.println("### Adapting " + compilationUnitModel.getQName() + " to " + referenceCompilationUnitModel.getQName());
    ClassModel referenceClassModel = referenceCompilationUnitModel.getClassModel();
    int removedCount = 0;
    int changedCount = 0;
    for (MemberModel member : referenceClassModel.getMembers()) {
      MemberModel newMember = findMemberModel(compilationUnitModel, member);
      if (newMember == null) {
        if (!member.equals(referenceClassModel.getConstructor())) {
          //System.err.printf("*** member %s.%s not found%n", classModel.getName(), member.getName());
          ++removedCount;
        }
      } else {
        adaptNamespaceToReferenceApi(newMember, member);
        boolean changed = adaptTypeToReferenceApi(referenceCompilationUnitModel, newMember, member);
        if (member instanceof MethodModel) {
          Iterator<ParamModel> iterator = ((MethodModel) member).getParams().iterator(),
                         newIterator = ((MethodModel) newMember).getParams().iterator();
          while (iterator.hasNext() && newIterator.hasNext()) {
            ParamModel param = iterator.next();
            ParamModel newParam = newIterator.next();
            changed |= adaptTypeToReferenceApi(referenceCompilationUnitModel, newParam, param);
          }
          if (iterator.hasNext() || newIterator.hasNext()) {
            changed = true;
          }
        }
        if (changed) {
          ++changedCount;
        }
      }
    }
    int sameCount = referenceClassModel.getMembers().size() - changedCount - removedCount;
    //System.err.printf("=== Adapted %s: removed: %d, changed: %d, same: %d%n", compilationUnitModel.getQName(), removedCount, changedCount, sameCount);
    System.err.printf("%s\t%d\t%d\t%d%n", compilationUnitModel.getQName(), removedCount, changedCount, sameCount);
  }


  private static boolean adaptTypeToReferenceApi(CompilationUnitModel referenceCompilationUnitModel, TypedModel newMember, TypedModel member) {
    String oldType = referenceApi.resolveQualifiedName(referenceCompilationUnitModel, member.getType());
    String newType = newMember.getType(); // already fully-qualified!
    if (oldType != null && !oldType.equals(newType)) {
      //System.err.printf("*** found %s member %s type change: from %s to %s%n", classModel.getName(), member.getName(), oldType, newType);
      if (shouldCorrect(oldType, newType)) {
        newMember.setType(oldType);
        //System.err.printf("!!! corrected type of %s.%s from %s back to %s%n", classModel.getName(), member.getName(), newType, oldType);
      } else {
        return true;
      }
    }
    return false;
  }

  private static void adaptNamespaceToReferenceApi(MemberModel newMember, MemberModel member) {
    String oldNamespace = member.getNamespace();
    String newNamespace = newMember.getNamespace();
    if (oldNamespace != null && !oldNamespace.equals(newNamespace)) {
      newMember.setNamespace(oldNamespace);
    }
  }

  private static boolean shouldCorrect(String oldType, String newType) {
    return newType == null ||
            "void".equals(newType) ||
            "*".equals(newType) ||
            "void".equals(oldType) ||
            "Object".equals(newType) && "*".equals(oldType) ||
            "ext.IEventObject".equals(oldType) && newType.contains("Event") ||
            "Function".equals(newType) && "Class".equals(oldType);
  }

  private static MemberModel findMemberModel(CompilationUnitModel compilationUnitModel, MemberModel referenceMemberModel) {
    ClassModel classModel = compilationUnitModel.getClassModel();
    String referenceMemberName = referenceApi.getMappedMemberName(compilationUnitModel, referenceMemberModel.getName());
    MemberModel memberModel = referenceMemberModel instanceof MethodModel
            ? classModel.getMethod(referenceMemberModel.isStatic(), ((MethodModel) referenceMemberModel).getMethodType(), referenceMemberName)
            : classModel.getMember(referenceMemberModel.isStatic(), referenceMemberName);
    if (memberModel == null) {
      CompilationUnitModel superclass = compilationUnitModelRegistry.getSuperclassCompilationUnit(classModel);
      if (superclass != null) {
        return findMemberModel(superclass, referenceMemberModel);
      }
    }
    return memberModel;
  }

  private static CompilationUnitModel generateClassModel(ExtClass extClass) {
    String extClassName = getActionScriptName(extClass);
    if (extClassName == null) {
      return null;
    }
    CompilationUnitModel extAsClassUnit = createClassModel(convertType(extClass.name));
    ClassModel extAsClass = extAsClassUnit.getClassModel();
    System.out.printf("Generating AS3 API model %s for %s...%n", extAsClassUnit.getQName(), extClassName);
    extAsClass.setAsdoc(toAsDoc(extClass));
    addDeprecation(extClass.deprecated, extAsClass);
    CompilationUnitModel extAsInterfaceUnit = null;
    if (interfaces.contains(extClassName)) {
      extAsInterfaceUnit = createClassModel(convertToInterface(extClassName));
      System.out.printf("Generating AS3 API model %s for %s...%n", extAsInterfaceUnit.getQName(), extClassName);
      ClassModel extAsInterface = (ClassModel)extAsInterfaceUnit.getPrimaryDeclaration();
      extAsInterface.setInterface(true);
      extAsInterface.setAsdoc(toAsDoc(extClass.doc));
      if (extClass.extends_ != null) {
        String superInterface = convertToInterface(getActionScriptName(extClass.extends_));
        if (superInterface != null) {
          extAsInterface.addInterface(superInterface);
        }
      }
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
      String superInterface = convertToInterface(getActionScriptName(mixin));
      if (superInterface != null) {
        extAsClass.addInterface(superInterface);
        if (extAsInterfaceUnit != null) {
          extAsInterfaceUnit.getClassModel().addInterface(superInterface);
        }
      }
    }

    if (extAsInterfaceUnit != null) {
      addNonStaticMembers(extClass, extAsInterfaceUnit);
    }

    if (!interfaces.contains(extClassName)) {
      addFields(extAsClass, extJsApi.filterByOwner(false, true, extClass, extClass.members, Property.class));
      addMethods(extAsClass, extJsApi.filterByOwner(false, true, extClass, extClass.members, Method.class));
    }

    addNonStaticMembers(extClass, extAsClassUnit);
    return extAsClassUnit;
  }

  private static String getActionScriptName(String extClassName) {
    return getActionScriptName(extJsApi.getExtClass(extClassName));
  }

  private static void generateConfigClassModel(ExtClass extClass, CompilationUnitModel extAsClassUnit) {
    ClassModel extAsClass = extAsClassUnit.getClassModel();
    String extClassName = extAsClassUnit.getQName();
    System.out.printf("Generating AS3 config API model %s for %s...%n", extAsClassUnit.getQName(), extClassName);
    String configClassQName = getConfigClassQName(extClass);
    if (configClassQName != null) {
      CompilationUnitModel configClassUnit = createClassModel(configClassQName);
      ClassModel configClass = (ClassModel)configClassUnit.getPrimaryDeclaration();
      configClassUnit.getClassModel().setAsdoc(extAsClass.getAsdoc());
      String superConfigClassQName = "joo.JavaScriptObject";
      for (ExtClass extSuperClass = extJsApi.getExtClass(extClass.extends_);
           extSuperClass != null;
           extSuperClass = extJsApi.getExtClass(extSuperClass.extends_)) {
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
            if (generateForMxml) {
              typePropertyModel.addAnnotation(new AnnotationModel(MxmlToModelParser.CONSTRUCTOR_PARAMETER_ANNOTATION,
                      new AnnotationPropertyModel(MxmlToModelParser.CONSTRUCTOR_PARAMETER_ANNOTATION_VALUE, typeValue)));
            }
          }
        }
      }
      configClassUnit.getClassModel().addAnnotation(extConfigAnnotation);
      if (generateEventClasses) {
        addEvents(configClass, extAsClassUnit, extJsApi.filterByOwner(false, false, extClass, extClass.members, Event.class));
      }
      List<Cfg> configProperties = extJsApi.filterByOwner(false, false, extClass, extClass.members, Cfg.class);
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
    String extClassName = extClass.name;
    String alias = jsConfigClassNameMappingProperties.getProperty(extClassName);
    if (alias == null) {
      return null;
    }
    String configClassQName = CompilerUtils.qName("ext.config", alias);
    System.err.println("********* derived config class name "  + configClassQName + " from Ext class " + extClassName + " with alias " + alias);
    return configClassQName;
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
      addEvents(extAsClassUnit.getClassModel(), extAsClassUnit, extJsApi.filterByOwner(false, false, extClass, extClass.members, Event.class));
    }
    addProperties(extAsClass, extJsApi.filterByOwner(extAsClass.isInterface(), false, extClass, extClass.members, Property.class));
    addMethods(extAsClass, extJsApi.filterByOwner(extAsClass.isInterface(), false, extClass, extClass.members, Method.class));
  }

  private static void generateActionScriptCode(CompilationUnitModel extAsClass, File outputDir) throws IOException {
    File outputFile = CompilerUtils.fileFromQName(extAsClass.getQName(), outputDir, Jooc.AS_SUFFIX);
    //noinspection ResultOfMethodCallIgnored
    outputFile.getParentFile().mkdirs(); // NOSONAR
    System.out.printf("Generating AS3 API for %s into %s...\n", extAsClass.getQName(), outputFile.getPath());
    extAsClass.visit(new ActionScriptCodeGeneratingModelVisitor(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8")));
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
      setVisibility(fieldModel, member);
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
        if (extJsApi.inheritsDoc(member)) {
           // suppress overridden properties with the same JSDoc!
           continue;
         }
        String type = convertType(member.type);
        if (type == null || "*".equals(type) || "Object".equals(type)) {
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
        setVisibility(propertyModel, member);
        setStatic(propertyModel, member);
        propertyModel.addGetter();
        if (!forceReadOnly && !(member.meta.readonly || member.readonly)) {
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
        if (!isConstructor && extJsApi.inheritsDoc(method)) {
          // suppress overridden methods with the same JSDoc!
          continue;
        }
        MethodModel methodModel = isConstructor
                ? new MethodModel(classModel.getName(), null)
                : new MethodModel(convertName(methodName), method.return_ == null ? "void" : convertType(method.return_.type));
        methodModel.setAsdoc(toAsDoc(method));
        if (method.return_ != null) {
          methodModel.getReturnModel().setAsdoc(toAsDoc(method.return_));
        }
        setVisibility(methodModel, method);
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

  private static void setVisibility(MemberModel memberModel, Member member) {
    memberModel.setNamespace(member.protected_ || member.meta.protected_ ? NamespacedModel.PROTECTED : NamespacedModel.PUBLIC);
  }

  private static void setStatic(MemberModel memberModel, Member member) {
    ExtClass extClass = extJsApi.getExtClass(member.owner);
    memberModel.setStatic(!extClass.singleton && (member.static_ || member.meta.static_));
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

  private static String convertToInterface(String className) {
    if (className == null || !interfaces.contains(className)) {
      return null;
    }
    String interfaceName = "I" + CompilerUtils.className(className);
    if (interfaceName.endsWith("Impl")) {
      interfaceName = interfaceName.substring(0, interfaceName.length() - 4);
    }
    return CompilerUtils.qName(CompilerUtils.packageName(className), interfaceName);
  }

  private static String convertType(String extType) {
    if (extType == null) {
      return null;
    }
    if ("undefined".equals(extType) || "null".equals(extType)) {
      return "void";
    }
    if ("number".equals(extType) || "boolean".equals(extType) || "string".equals(extType)) {
      return capitalize(extType);
    }
    if ("HTMLElement".equals(extType) || "Event".equals(extType) || "XMLHttpRequest".equals(extType)) {
      return "js." + extType;
    }
    if ("google.maps.Map".equals(extType) || "CSSStyleSheet".equals(extType) || "CSSStyleRule".equals(extType)) {
      return "Object"; // no AS3 type yet
    }
    // enums and ad-hoc enums:
    if (extType.startsWith("Ext.enums.") || extType.matches("(['\"].*['\"]/)*['\"].*['\"]")) {
      return "String";
    }
    // array / vararg syntax:
    if (extType.endsWith("...") || extType.matches("[a-zA-Z0-9._$<>]+\\[\\]")) {
      return "Array";
    }
    if (!extType.matches("[a-zA-Z0-9._$<>]+") || "Mixed".equals(extType)) {
      return "*"; // TODO: join types? rather use Object? simulate overloading by splitting into several methods?
    }
    if (JsCodeGenerator.PRIMITIVES.contains(extType)) {
      return extType;
    }
    ExtClass extClass = extJsApi.getExtClass(extType);
    if (extClass == null) {
      //throw new RuntimeException("Fatal: No Ext class '" + extType + "' found.");
      System.err.println("Warning: No Ext class '" + extType + "' found, falling back to Object");
      return "Object";
    }
    String qName = getActionScriptName(extClass);
    if (qName == null) {
      // try with super class:
      return convertType(extClass.extends_);
    }
    if (isSingleton(extClass)) {
      qName = CompilerUtils.qName(CompilerUtils.packageName(qName), "S" + CompilerUtils.className(qName));
    }
    return qName;
  }

  // normalize / use alternate class name if it can be found in reference API:
  private static void removePrivateApiClasses() {
    // collect all non-public classes:
    Set<ExtClass> privateClasses = new HashSet<ExtClass>();

    for (ExtClass extClass: extClasses) {
      // correct wrong usage of Ext.util.Observable as a mixin:
      replaceMixin(extClass, "Ext.util.Observable", "Ext.mixin.Observable");
      // simplify "extends Base mixins Mixin-that-extends-Base" to "extends Mixin-that-extends-Base":
      replaceMixinByExtends(extClass, "Ext.dom.Element");

      // Classes to remove from public API:
      // explicitly marked private OR
      // a built-in type / class OR
      // Ext enums - they are just for documentation, so treat them as private API, too.
      if (extClass.private_ || JsCodeGenerator.PRIMITIVES.contains(extClass.name) || extClass.name.startsWith("Ext.enums.")) {
        privateClasses.add(extClass);
      }
    }

    if (referenceApi != null) {
      // all classes that already exist in the reference API must stay public API:
      for (ExtClass extClass : extClasses) {
        String jooClassName = getActionScriptName(extClass);
        if (jooClassName != null) {
          List<CompilationUnitModel> referenceDeclarations = getReferenceDeclarations(jooClassName);
          if (referenceDeclarations != null && !referenceDeclarations.isEmpty()) {
            System.out.printf(" (added 'private' API class %s because it appears in Ext 3.4 reference API as %s.)%n", extClass.name, referenceDeclarations.get(0).getQName());
            privateClasses.remove(extClass);
          }
        }
      }
    }

    // all super classes of public classes must be public:
    for (ExtClass extClass : extClasses) {
      if (!privateClasses.contains(extClass)) {
        markPublic(privateClasses, extClass.name);
      }
    }

    extClasses.removeAll(privateClasses);

    System.out.println("*****ADD TO JS-AS-NAME-MAPPING:");
    for (ExtClass extClass : extClasses) {
      if (getActionScriptName(extClass) == null) {
        System.out.println(extClass.name + " = " + extClass.name.substring(0, 1).toLowerCase() + extClass.name.substring(1));
      }
    }
    System.out.println("*****END ADD TO JS-AS-NAME-MAPPING:");
  }

  private static CompilationUnitModel getReferenceDeclaration(String jooClassName) {
    List<CompilationUnitModel> referenceDeclarations = getReferenceDeclarations(jooClassName);
    return referenceDeclarations.isEmpty() ? null : referenceDeclarations.get(0);
  }

  private static List<CompilationUnitModel> getReferenceDeclarations(String jooClassName) {
    return referenceApi.getCompilationUnitModels(jooClassName);
  }

  private static void replaceMixin(ExtClass extClass, String mixinImpl, String mixin) {
    // instead of implementing mixinImpl, a class has to implement its interface:
    int mixinImplIndex = extClass.mixins.indexOf(mixinImpl);
    if (mixinImplIndex != -1) {
      extClass.mixins.set(mixinImplIndex, mixin);
    }
    replaceMixinByExtends(extClass, mixin);
  }

  private static void replaceMixinByExtends(ExtClass extClass, String mixin) {
    // instead of extending Ext.Base and implementing the mixin, it is simpler to extend the mixin
    if ("Ext.Base".equals(extClass.extends_) && extClass.mixins.contains(mixin)) {
      extClass.mixins.remove(mixin);
      extClass.extends_ = mixin;
    }
  }

  private static void markPublic(Set<ExtClass> privateClasses, String extClassName) {
    ExtClass extClass = extJsApi.getExtClass(extClassName);
    if (privateClasses.remove(extClass)) {
      //System.err.println("*** marked public because it is a super class: " + extClass.name);
    }
    if (extClass.extends_ != null) {
      markPublic(privateClasses, extClass.extends_);
    }
    for (String mixin : extClass.mixins) {
      markPublic(privateClasses, mixin);
    }
  }

  // normalize / use alternate class name if it can be found in reference API:
  private static String getActionScriptName(ExtClass extClass) {
    String normalizedClassName = jsAsNameMappingProperties.getProperty(extClass.name);
    if (normalizedClassName == null) {
      // System.err.println(String.format("Ext JS class name %s not mapped to AS.", extClass.name));
      // throw new IllegalStateException("unmapped class " + extClass.name);
      return null;
    }
    return normalizedClassName;
  }

}
