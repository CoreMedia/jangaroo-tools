package net.jangaroo.jooc.model;

import net.jangaroo.utils.CompilerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A model of an ActionScript class or interface.
 */
public class ClassModel extends DocumentedModel implements ModelWithVisibility {
  private boolean isInterface = false;
  private Visibility visibility = Visibility.PUBLIC;
  private String superclass = null;
  private List<String> interfaces = new ArrayList<String>();
  private List<MemberModel> members = new ArrayList<MemberModel>();
  private List<AnnotationModel> annotations = new ArrayList<AnnotationModel>();

  public ClassModel() {
  }

  public ClassModel(String name) {
    super(name);
  }

  public ClassModel(String name, String superclass) {
    super(name);
    this.superclass = superclass;
  }

  public Visibility getVisibility() {
    return visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
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

  public List<MemberModel> getMembers() {
    return Collections.unmodifiableList(members);
  }

  public void setMembers(List<MemberModel> members) {
    this.members = members;
  }

  public void addMember(MemberModel member) {
    MemberModel oldMember = getMember(member.isStatic(), member.getName());
    if (oldMember != null) {
      if (oldMember.equals(member)) {
        return;
      }
      throw new IllegalArgumentException("Someone tried to add a different " + (member.isStatic() ? "static " : "") + "member called " + member.getName() + ": " + oldMember + " -> " + member);
    }
    members.add(member);
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

  private MemberModel getMember(boolean isStatic, String name) {
    for (MemberModel memberModel : members) {
      if (memberModel.isStatic() == isStatic && name.equals(memberModel.getName())) {
        return memberModel;
      }
    }
    return null;
  }

  public boolean removeMember(MemberModel memberModel) {
    return members.remove(memberModel);
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

  private MethodModel getMethod(boolean isStatic, MethodType methodType, String name) {
    MemberModel member = getMember(isStatic, name);
    if (member != null) {
      if (methodType == null) {
        if (member.isMethod()) {
          return (MethodModel)member;
        }
      } else {
        if (member.isProperty()) {
          return methodType == MethodType.GET
            ? ((PropertyModel)member).getGetter()
            : ((PropertyModel)member).getSetter();
        }
      }
    }
    return null;
  }

  public List<AnnotationModel> getAnnotations() {
    return Collections.unmodifiableList(annotations);
  }

  public void setAnnotations(List<AnnotationModel> annotations) {
    this.annotations = annotations;
  }

  public void addAnnotation(AnnotationModel annotation) {
    annotations.add(annotation);
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

}
