package net.jangaroo.jooc.model;

import net.jangaroo.jooc.Jooc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A model of an ActionScript class or interface.
 */
public class ClassModel extends AbstractAnnotatedModel implements NamespacedModel {
  private boolean isInterface = false;
  private boolean isFinal = false;
  private boolean isDynamic = false;
  private String namespace = PUBLIC;
  private String superclass = null;
  private String annotationCode = "";
  private String bodyCode = "";
  private List<String> interfaces = new ArrayList<String>();
  private List<MemberModel> members = new ArrayList<MemberModel>();

  public ClassModel() {
  }

  /**
   * @param name (unqualified) class name
   */
  public ClassModel(String name) {
    super(name);
  }

  /**
   * @param name (unqualified) class name
   * @param superclass fully qualified name of super class
   */
  public ClassModel(String name, String superclass) {
    super(name);
    this.superclass = superclass;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public boolean isFinal() {
    return isFinal;
  }

  public void setFinal(boolean aFinal) {
    isFinal = aFinal;
  }

  public boolean isDynamic() {
    return isDynamic;
  }

  public void setDynamic(boolean dynamic) {
    isDynamic = dynamic;
  }

  public boolean isInterface() {
    return isInterface;
  }

  public void setInterface(boolean value) {
    this.isInterface = value;
  }

  public String getSuperclass() {
    return superclass;
  }

  public void setSuperclass(String superclass) {
    this.superclass = superclass;
  }

  public List<String> getInterfaces() {
    return Collections.unmodifiableList(interfaces);
  }

  public void setInterfaces(List<String> interfaces) {
    this.interfaces = interfaces;
  }

  public void addInterface(String interfaceName) {
    interfaces.add(interfaceName);
  }

  public String getAnnotationCode() {
    return annotationCode;
  }

  public void setAnnotationCode(String annotationCode) {
    this.annotationCode = annotationCode;
  }

  public void addAnnotationCode(String code) {
    this.annotationCode += code;
  }

  public String getBodyCode() {
    return bodyCode;
  }

  public void setBodyCode(String bodyCode) {
    this.bodyCode = bodyCode;
  }

  public void addBodyCode(String code) {
    this.bodyCode += code;
  }

  public List<AnnotationModel> getEvents() {
    return getAnnotations(Jooc.EVENT_ANNOTATION_NAME);
  }

  public AnnotationModel getEvent(String name) {
    for (AnnotationModel event : getEvents()) {
      AnnotationPropertyModel eventName = event.getPropertiesByName().get(Jooc.EVENT_ANNOTATION_NAME_ATTRIBUTE_NAME);
      if (eventName == null) {
        eventName = event.getPropertiesByName().get(null);
        if (eventName == null) {
          System.out.println("*** no event value found: " + event.getProperties());
        }
      }
      if (eventName != null && name.equals(eventName.getStringValue())) {
        return event;
      }
    }
    return null;
  }

  public List<MemberModel> getMembers() {
    return Collections.unmodifiableList(members);
  }

  public void setMembers(List<MemberModel> members) {
    this.members = members;
  }

  /**
   * Adds a member to this class model and returns the member that is replaced by the new member if applicable.
   * @param member the new member to add
   * @return the old member that has been replaced by the new member or null
   */
  public MemberModel addMember(MemberModel member) {
    MemberModel oldMember = getMember(member.isStatic(), member.getName());
    if (oldMember != null) {
      if (oldMember.isProperty()) {
        PropertyModel oldPropertyModel = (PropertyModel)oldMember;
        if (member.isGetter()) {
          oldMember = oldPropertyModel.getGetter();
        } else if (member.isSetter()) {
          oldMember = oldPropertyModel.getSetter();
        }
      }
      if (oldMember != null) {
        if (oldMember.equals(member)) {
          return null;
        }
        removeMember(oldMember);
      }
    }
    if (member.isProperty()) {
      PropertyModel propertyModel = (PropertyModel)member;
      addIfNotNull(propertyModel.getGetter());
      addIfNotNull(propertyModel.getSetter());
    } else {
      members.add(member);
    }
    return oldMember;
  }

  private void addIfNotNull(MethodModel method) {
    if (method != null) {
      members.add(method);
    }
  }

  public PropertyModel getProperty(boolean isStatic, String name) {
    MemberModel member = getMember(isStatic, name);
    return member != null && member.isProperty() ? (PropertyModel)member : null;
  }

  public MemberModel getMember(String name) {
    return getMember(false, name);
  }

  public MemberModel getStaticMember(String name) {
    return getMember(true, name);
  }

  public MemberModel getMember(boolean isStatic, String name) {
    MemberModel member = getMethodOrField(isStatic, name);
    if (member instanceof MethodModel && member.isAccessor()) {
      return getProperty((MethodModel)member);
    }
    return member;
  }

  public PropertyModel getProperty(MethodModel accessor) {
    if (accessor == null || !accessor.isAccessor()) {
      return null;
    }
    MethodModel counterpart = getMethod(accessor.isStatic(), accessor.isGetter() ? MethodType.SET : MethodType.GET, accessor.getName());
    return new PropertyModel(accessor, counterpart);
  }

  private MemberModel getMethodOrField(boolean isStatic, String name) {
    int index = getMethodOrFieldIndex(isStatic, name);
    return index != -1 ? members.get(index) : null;
  }

  public boolean removeMember(MemberModel memberModel) {
    int index = getMethodOrFieldIndex(memberModel.isStatic(), memberModel.getName());
    return index != -1 && members.remove(index) != null;
  }

  private int getMethodOrFieldIndex(boolean isStatic, String name) {
    for (int i = 0; i < members.size(); i++) {
      MemberModel memberModel = members.get(i);
      if (memberModel.isStatic() == isStatic && name.equals(memberModel.getName())) {
        return i;
      }
    }
    return -1;
  }

  public MethodModel getConstructor() {
    return getMethod(getName());
  }

  public MethodModel getStaticMethod(String name) {
    return getStaticMethod(null, name);
  }

  public MethodModel getStaticMethod(MethodType methodType, String name) {
    return getMethod(false, methodType, name);
  }

  public MethodModel getMethod(String name) {
    return getMethod(false, name);
  }

  public MethodModel getMethod(MethodType methodType, String name) {
    return getMethod(false, methodType, name);
  }

  private MethodModel getMethod(boolean isStatic, String name) {
    return getMethod(isStatic, null, name);
  }

  public MethodModel getMethod(boolean isStatic, MethodType methodType, String name) {
    for (MemberModel memberModel : members) {
      if (memberModel.isMethod() && ((MethodModel)memberModel).getMethodType() == methodType
              && memberModel.isStatic() == isStatic && name.equals(memberModel.getName())) {
        return (MethodModel)memberModel;
      }
    }
    return null;
  }

  public MethodModel createConstructor() {
    MethodModel constructor = new MethodModel(getName(), null);
    addMember(constructor);
    return constructor;
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitClass(this);
  }

  public MemberModel findPropertyWithAnnotation(boolean isStatic, String annotationName) {
    for (MemberModel memberModel : members) {
      if (memberModel.isStatic() == isStatic && !memberModel.getAnnotations(annotationName).isEmpty()) {
        return asFieldOrProperty(memberModel);
      }
    }
    return null;
  }

  private MemberModel asFieldOrProperty(MemberModel memberModel) {
    if (memberModel.isField()) {
      return memberModel;
    }
    if (memberModel instanceof MethodModel) {
      return getProperty((MethodModel) memberModel);
    }
    return null;
  }

  public Set<MemberModel> findPropertiesWithAnnotation(boolean isStatic, String annotationName) {
    Set<MemberModel> result = new LinkedHashSet<MemberModel>();
    for (MemberModel memberModel : members) {
      if (memberModel.isStatic() == isStatic && !memberModel.getAnnotations(annotationName).isEmpty()) {
        result.add(asFieldOrProperty(memberModel));
      }
    }
    return Collections.unmodifiableSet(result);
  }
}
