/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.*;

/**
 * @author Andreas Gawecki
 */
public class ClassDeclaration extends IdeDeclaration {

  protected JooSymbol symClass;
  protected Extends optExtends;
  private Map<String,MemberDeclaration> members = new LinkedHashMap<String,MemberDeclaration>();

  public Extends getOptExtends() {
    return optExtends;
  }

  protected Implements optImplements;

  public ClassBody getBody() {
    return body;
  }

  public MethodDeclaration getConstructor() {
    return constructor;
  }

  protected ClassBody body;

  protected MethodDeclaration constructor = null;

  public MethodDeclaration getConstructorDeclaration() {
    return constructor;
  }

  public ClassDeclaration(JooSymbol[] modifiers, JooSymbol cls, Ide ide, Extends ext, Implements impl, ClassBody body) {
    super(modifiers,
            MODIFIER_ABSTRACT|MODIFIER_FINAL|MODIFIERS_SCOPE|MODIFIER_STATIC,
            ide);
    this.symClass = cls;
    this.optExtends = ext;
    this.optImplements = impl;
    this.body = body;
    body.classDeclaration = this;
  }

  public boolean isInterface() {
    return "interface".equals(symClass.getText());
  }

  public boolean isAbstract() {
    return isInterface() || super.isAbstract();
  }

  // valid after analyze phase
  public PackageDeclaration getPackageDeclaration() {
    return (PackageDeclaration) getParentDeclaration();
  }

  public String getName() {
    return ide.getName();
  }

  public void setConstructor(MethodDeclaration methodDeclaration) {
     if (constructor != null)
       Jooc.error(methodDeclaration, "Only one constructor allowed per class");
//     if (methodDeclaration != body.declararations.get(0))
//       Jooc.error(methodDeclaration, "Constructor declaration must be the first declaration in a class");
     constructor = methodDeclaration;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symClass);
    if (!writeRuntimeModifiersUnclosed(out)) {
      out.write("\"");
    }
    out.writeSymbolToken(symClass);
    ide.generateCode(out);
    if (optExtends != null) optExtends.generateCode(out);
    if (optImplements != null) optImplements.generateCode(out);
    out.write("\",[");
    boolean isFirst = true;
    for (MemberDeclaration memberDeclaration : members.values()) {
      if (memberDeclaration.isMethod() && memberDeclaration.isPublic() && memberDeclaration.isStatic()) {
        if (isFirst) {
          isFirst = false;
        } else {
          out.write(",");
        }
        out.write('"');
        out.write(memberDeclaration.getName());
        out.write('"');
      }
    }
    out.write("],");
    out.write("function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return[");
    body.generateCode(out);
    out.write("];}");
  }

  public void analyze(AnalyzeContext context) {
    parentDeclaration = context.getScope().getPackageDeclaration();
    context.enterScope(this);
    if (optExtends != null) optExtends.analyze(context);
    if (optImplements != null) optImplements.analyze(context);
    body.analyze(context);
    context.leaveScope(this);
    computeModifiers();
  }

  public void registerMember(MemberDeclaration memberDeclaration) {
    members.put(memberDeclaration.getName(), memberDeclaration);
  }

  public MemberDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  public boolean isPrivateMember(String memberName) {
    MemberDeclaration memberDeclaration = getMemberDeclaration(memberName);
    return memberDeclaration!=null && memberDeclaration.isPrivate();
  }

  public boolean isPrivateStaticMember(String memberName) {
    MemberDeclaration memberDeclaration = getMemberDeclaration(memberName);
    return memberDeclaration!=null && memberDeclaration.isPrivate() && memberDeclaration.isStatic();
  }

  public Type getSuperClassType() {
    return optExtends != null
      ? optExtends.superClass
      : new IdeType(new Ide(new JooSymbol(sym.IDE,  "", -1, -1, "", "Object")));
  }

  public String getSuperClassPath() {
    Type type = getSuperClassType();
    //TODO: scope class declarations, implement getSuperClassDeclaration()
    IdeType ideType = (IdeType) type;
    return toPath(ideType.getIde().getQualifiedName());
  }
}
