package net.jangaroo.joodoc;

import com.sun.javadoc.*;
import net.jangaroo.jooc.*;
import net.jangaroo.jooc.Type;


import java.util.ArrayList;
import java.lang.reflect.Modifier;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 08:59:00
 * To change this template use File | Settings | File Templates.
 */
public class ClassDocImpl extends DocImpl implements ClassDoc {


  ArrayList declarations;
  ConstructorDoc[] constructors;
  ArrayList fields = new ArrayList();
  ArrayList methods = new ArrayList();
  private static final ConstructorDoc[] NO_CONSTRUCTOR = new ConstructorDoc[0];


  public ClassDocImpl(ClassDeclaration declaration) {
    super (declaration);


    if (declaration!=null) {

      this.declarations = ((ClassDeclaration)declaration).getBody().getDeclararations();
      // fields
      for (int i = 0; i < declarations.size(); i++) {
        NodeImplBase decl =  (NodeImplBase)declarations.get(i);
        if (decl instanceof FieldDeclaration) {
          Doc fieldDoc = (Doc)DocMap.getDoc(decl);
          if (fieldDoc.isIncluded()) {
            fields.add(fieldDoc);
          }
        }
      }
      // constructor & methods
      ConstructorDoc constructor=null;
      for (int i = 0; i < declarations.size(); i++) {
        NodeImplBase decl =  (NodeImplBase)declarations.get(i);
        if (decl instanceof MethodDeclaration) {
          MethodDoc methodDoc = (MethodDoc)DocMap.getDoc(decl);
          if (constructor==null) {
            constructor = (ConstructorDoc)methodDoc;
          } else {
            if (methodDoc.isIncluded()) {
              methods.add(methodDoc);
            }
          }
        }
      }
      constructors = constructor==null || !constructor.isIncluded()
                     ? NO_CONSTRUCTOR
                     : new ConstructorDoc[]{constructor};
    }

  }

  protected ClassDeclaration getClassDeclaration() {
    return (ClassDeclaration)declaration;
  }

  public boolean isIncluded() {
    return isPublic() || isProtected();
  }

  public boolean definesSerializableFields() {
    return false;
  }

  public boolean isAbstract() {
    return getClassDeclaration().isAbstract();
  }

  public boolean isExternalizable() {
    return false;
  }

  public boolean isSerializable() {
    return false;
  }

  public ClassDoc superclass() {
    Extends optExtends = getClassDeclaration().getOptExtends();
    Type type = null;
    Ide ide = null;
    if (optExtends!=null) type=optExtends.getSuperClass();

    String[] qualIde = null;
    if (type instanceof IdeType) ide = ((IdeType)type).getIde();
    if (ide instanceof QualifiedIde) {
      qualIde=((QualifiedIde)ide).getQualifiedName();
      return (ClassDoc)DocMap.getDocByQualifiedName(Util.getQualifiedName(qualIde));
    }
    return null;
  }

  public ClassDoc[] importedClasses() {
    return new ClassDoc[0];
  }

  public ClassDoc[] innerClasses() {
    return new ClassDoc[0];
  }

  public ClassDoc[] interfaces() {
    return new ClassDoc[0];
  }

  public boolean subclassOf(ClassDoc classDoc) {
    ClassDoc superClass = this;
    while (superClass!=null && !superClass.equals(classDoc)) {
      superClass = superClass.superclass();
    }
    return superClass!=null;
  }

  public ClassDoc[] innerClasses(boolean b) {
    return new ClassDoc[0];
  }

  public ConstructorDoc[] constructors() {
    return constructors;
  }

  public ConstructorDoc[] constructors(boolean b) {
    return constructors();
  }

  public FieldDoc[] fields() {
    return  (FieldDoc[]) this.fields.toArray(new FieldDoc[0]);
  }

  public FieldDoc[] serializableFields() {
    return new FieldDoc[0];
  }

  public FieldDoc[] fields(boolean b) {
    return fields();
  }

  public MethodDoc[] methods() {
    return (MethodDoc[]) this.methods.toArray(new MethodDoc[0]);
  }

  public MethodDoc[] serializationMethods() {
    return new MethodDoc[0];
  }

  public MethodDoc[] methods(boolean b) {
      return methods();
  }

  public PackageDoc[] importedPackages() {
    return new PackageDoc[0];
  }

  public ClassDoc findClass(String s) {
    return null;
  }

  public int modifierSpecifier() {
    int modifier = 0;
    if (isPublic())
      modifier = Modifier.PUBLIC;
    else if (isProtected())
      modifier=Modifier.PROTECTED;
    else if (isPrivate())
      modifier=Modifier.PRIVATE;
    if (isStatic())
      modifier |= Modifier.STATIC;

    return modifier;
  }

  public boolean isFinal() {
    return getClassDeclaration().isFinal();
  }

  public boolean isPackagePrivate() {
    return !getClassDeclaration().isPublic() && !getClassDeclaration().isProtected();
  }

  public boolean isPrivate() {
    return getClassDeclaration().isPrivate();
  }

  public boolean isProtected() {
    return getClassDeclaration().isProtected();
  }

  public boolean isPublic() {
    return getClassDeclaration().isPublic();
  }

  public boolean isStatic() {
    return getClassDeclaration().isStatic();
  }

  public ClassDoc containingClass() {
    return null;
  }

  public PackageDoc containingPackage() {
    return (PackageDoc)(DocMap.getDoc(getClassDeclaration().getPackageDeclaration()));
  }

  public String modifiers() {
    return (isPublic()?"public ":"")+
        (isStatic()?"static ":"")+
        (isFinal()?"final ":"")+
        (isAbstract()?"abstract ":"")
        ;
  }

  public String qualifiedName() {
    String[] qual=((IdeDeclaration)declaration).getQualifiedName();
    return Util.getQualifiedName(qual);
  }

  public boolean isClass() {
    return true;
  }

  public boolean isOrdinaryClass() {
    return true;
  }

  public String commentText() {
    return "hallo";
  }

  public String getRawCommentText() {
    return "hello";
  }

  public String name() {
    return getClassDeclaration().getName();
  }

  public void setRawCommentText(String s) {

  }

  public ClassDoc asClassDoc() {
    return this;
  }

  public String dimension() {
    return "";
  }

  public String qualifiedTypeName() {
    String[] qual=((IdeDeclaration)declaration).getQualifiedName();
    return Util.getQualifiedName(qual);
  }

  public String typeName() {
    return null;
  }

  public TypeVariable asTypeVariable() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ParamTag[] typeParamTags() {
    return new ParamTag[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public FieldDoc[] enumConstants() {
    return new FieldDoc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public WildcardType asWildcardType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public com.sun.javadoc.Type superclassType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AnnotationTypeDoc asAnnotationTypeDoc() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ParameterizedType asParameterizedType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isPrimitive() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public String simpleTypeName() {
    return sym.class.getSimpleName();    
  }

  public com.sun.javadoc.Type[] interfaceTypes() {
    return new com.sun.javadoc.Type[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public TypeVariable[] typeParameters() {
    return new TypeVariable[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AnnotationDesc[] annotations() {
    return new AnnotationDesc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  

  
}
