package net.jangaroo.joodoc;

import com.sun.javadoc.*;
import net.jangaroo.jooc.PackageDeclaration;

import java.util.List;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 09:15:06
 * To change this template use File | Settings | File Templates.
 */
public class PackageDocImpl extends DocImpl implements PackageDoc{


  public PackageDocImpl(PackageDeclaration declaration) {
    super (declaration);
  }

  public ClassDoc[] allClasses() {
    String qualifiedPackageName = name();
    ClassDoc[] allClasses = DocMap.getClasses();
    List/*ClassDoc*/ myClasses = new ArrayList(allClasses.length);
    //System.out.println("Searching for classes in package "+qualifiedPackageName+"...");
    for (int i = 0; i < allClasses.length; i++) {
      ClassDoc classDoc = allClasses[i];
      if (qualifiedPackageName.equals(classDoc.containingPackage().name())) {
        myClasses.add(classDoc);
      }
    }
    //System.out.println("Found "+myClasses.size()+" classes in package "+qualifiedPackageName);
    ClassDoc[] classes = (ClassDoc[])myClasses.toArray(new ClassDoc[myClasses.size()]);
    return classes;
  }

  public ClassDoc[] errors() {
    return new ClassDoc[0];
  }

  public ClassDoc[] exceptions() {
    return new ClassDoc[0];
  }

  public ClassDoc[] interfaces() {
    return new ClassDoc[0];
  }

  public ClassDoc[] ordinaryClasses() {
    return allClasses();
  }

  public ClassDoc[] allClasses(boolean b) {
    return allClasses();
  }

  public ClassDoc findClass(String s) {
    ClassDoc[] classes = allClasses();
    for (int i = 0; i < classes.length; i++) {
      ClassDocImpl classDoc = (ClassDocImpl) classes[i];
      if (classDoc.name().equals(s)) return classDoc;
    }
    return null;
  }




  

  public String commentText() {
    return "package comment";
  }

  public String getRawCommentText() {
    return "package comment2";
  }

  public String name() {
    String[] qualifiedName = ((PackageDeclaration)declaration).getQualifiedName();
    return Util.getQualifiedName(qualifiedName);
  }

  public void setRawCommentText(String s) {

  }

  public boolean equals(Object model) {
    if (this == model) return true;
    if (!(model instanceof PackageDocImpl)) return false;

    final PackageDocImpl packageDoc = (PackageDocImpl)model;

    if (!name().equals(packageDoc.name())) return false;

    return true;
  }

  public int hashCode() {
    return name().hashCode();
  }

  public ClassDoc[] enums() {
    return new ClassDoc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AnnotationTypeDoc[] annotationTypes() {
    return new AnnotationTypeDoc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AnnotationDesc[] annotations() {
    return new AnnotationDesc[0];  //To change body of implemented methods use File | Settings | File Templates.
  }
}
