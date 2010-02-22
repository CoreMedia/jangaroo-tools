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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andreas Gawecki
 */
public class ClassDeclaration extends IdeDeclaration {

  protected JooSymbol symClass;
  protected Extends optExtends;
  private Map<String, MemberDeclaration> members = new LinkedHashMap<String, MemberDeclaration>();
  private Set<String> boundMethodCandidates = new HashSet<String>();
  private Map<String, Set<Scope>> classInit = new HashMap<String, Set<Scope>>();
  private List<String> packageImports;

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
        MODIFIER_ABSTRACT | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_DYNAMIC,
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
    if (constructor != null) {
      throw Jooc.error(methodDeclaration, "Only one constructor allowed per class");
    }
//     if (methodDeclaration != body.declararations.get(0))
//       Jooc.error(methodDeclaration, "Constructor declaration must be the first declaration in a class");
    constructor = methodDeclaration;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(symClass);
    ide.generateCode(out);
    if (optExtends != null) {
      optExtends.generateCode(out);
    }
    if (optImplements != null) {
      optImplements.generateCode(out);
    }
    out.endString();
    out.write(",");
    out.write("function(" + ide.getName() + ",$$private){");
    for (String importedPackage : packageImports) {
      out.write("with(" + importedPackage + ")");
    }
    out.write("with(" + ide.getName() + ")with($$private)return[");
    generateClassInits(out);
    body.generateCode(out);
    out.write("];},");
    generateStaticMethodList(out);
  }

  private void generateClassInits(JsWriter out) throws IOException {
    boolean first = true;
    for (Map.Entry<String, Set<Scope>> entry : classInit.entrySet()) {
      String qualifiedNameStr = entry.getKey();
      if (recheckScopes(qualifiedNameStr, entry.getValue())) {
        if (first) {
          first = false;
          out.write("function(){" + Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".init(");
        } else {
          out.write(",");
        }
        out.write(qualifiedNameStr);
      }
    }
    if (!first) {
      out.write(");},");
    }
  }

  private static boolean recheckScopes(String qualifiedNameStr, Set<Scope> recheckScopes) {
    if (recheckScopes == null) {
      return true; // marker for "already verified it is a class"
    }
    // if class guessing is enabled, we still have to check that the identifier was not a forward-access which
    // has been declared in the meantime:
    for (Scope occurredInScope : recheckScopes) {
      if (occurredInScope.findScopeThatDeclares(qualifiedNameStr) == null) {
        return true; // one occurrence per identifier suffices!
      }
    }
    return false;
  }

  private void generateStaticMethodList(JsWriter out) throws IOException {
    out.write("[");
    boolean isFirst = true;
    for (MemberDeclaration memberDeclaration : members.values()) {
      if (memberDeclaration.isMethod() && !memberDeclaration.isPrivate() && !memberDeclaration.isProtected() && memberDeclaration.isStatic() && !memberDeclaration.isNative()) {
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
    out.write("]");
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    // do *not* call super!
    this.parentNode = parentNode;
    packageImports = context.getCurrentPackage().getPackageImports();
    context.getScope().declareIde(getName(), this);
    parentDeclaration = context.getScope().getPackageDeclaration();
    // one scope for static members...
    context.enterScope(this);
    // ...and one scope for instance members!
    context.enterScope(this);
    if (optExtends != null) {
      optExtends.analyze(this, context);
    }
    if (optImplements != null) {
      optImplements.analyze(this, context);
    }
    body.analyze(this, context);
    context.leaveScope(this);
    context.leaveScope(this);
    computeModifiers();
    return this;
  }

  public void registerMember(MemberDeclaration memberDeclaration) {
    members.put(memberDeclaration.getName(), memberDeclaration);
  }

  public MemberDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  public boolean isPrivateMember(String memberName) {
    MemberDeclaration memberDeclaration = getMemberDeclaration(memberName);
    return memberDeclaration != null && memberDeclaration.isPrivate();
  }

  public boolean isPrivateStaticMember(String memberName) {
    MemberDeclaration memberDeclaration = getMemberDeclaration(memberName);
    return memberDeclaration != null && memberDeclaration.isPrivate() && memberDeclaration.isStatic();
  }

  public Type getSuperClassType() {
    return optExtends != null
        ? optExtends.superClass
        : new IdeType(new Ide(new JooSymbol("Object")));
  }

  public String getSuperClassPath() {
    Type type = getSuperClassType();
    //TODO: scope class declarations, implement getSuperClassDeclaration()
    IdeType ideType = (IdeType) type;
    return ideType.getIde().getQualifiedNameStr();
  }

  public void addBoundMethodCandidate(String memberName) {
    boundMethodCandidates.add(memberName);
  }

  public boolean isBoundMethod(String methodName) {
    return boundMethodCandidates.contains(methodName);
  }

  public void addInitIfClass(String qualifiedNameStr, AnalyzeContext context) {
    // really another class?
    if (!(qualifiedNameStr.equals(getName()) || qualifiedNameStr.equals(getQualifiedNameStr()))) {
      Scope declaringScope = context.getScope().findScopeThatDeclares(qualifiedNameStr);
      if (declaringScope != null && declaringScope.getDeclaration().equals(getParentDeclaration())) {
        classInit.put(qualifiedNameStr, null); // null: marker for "already verified it is a class" 
      } else if (declaringScope == null && context.getConfig().isEnableGuessingClasses() &&
          !(ide instanceof QualifiedIde) && Character.isUpperCase(qualifiedNameStr.charAt(0))) {
        // store current context to repeat look-up in rendering phase, when all declarations are known:
        Set<Scope> scopes = classInit.get(qualifiedNameStr);
        if (scopes == null) {
          if (classInit.containsKey(qualifiedNameStr)) {
            return; // "already verified it is a class"
          }
          scopes = new HashSet<Scope>();
          classInit.put(qualifiedNameStr, scopes);
        }
        scopes.add(context.getScope());
      }
    }
  }
}
