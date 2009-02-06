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
import java.util.Arrays;

/**
 * @author Andreas Gawecki
 */
class DotExpr extends BinaryOpExpr {

  ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    classDeclaration = context.getCurrentClass();
  }

  private String[] getQualifiedName() {
    String[] arg2QualifiedName = getQualifiedName(arg2);
    if (arg2QualifiedName!=null) {
      String[] arg1QualifiedName = getQualifiedName(arg1);
      if (arg1QualifiedName!=null) {
        return concat(arg1QualifiedName, arg2QualifiedName);
      }
    }
    return null;
  }

  private static String[] getQualifiedName(Expr expr) {
      return
          expr instanceof IdeExpr ? new String[]{((IdeExpr)expr).ide.getName()}
        : expr instanceof DotExpr ? ((DotExpr)expr).getQualifiedName()
        : null;
  }

  private static String[] concat(String[] strarr1, String[] strarr2) {
    String[] result = new String[strarr1.length + strarr2.length];
    System.arraycopy(strarr1, 0, result, 0, strarr1.length);
    System.arraycopy(strarr2, 0, result, strarr1.length, strarr2.length);
    return result;
  }

  public void generateCode(JsWriter out) throws IOException {
    if (classDeclaration!=null
      && arg2 instanceof IdeExpr) {
      String property = ((IdeExpr)arg2).ide.getName();
      // check and handle instance methods declared in same file, accessed as function:
      if (arg1 instanceof ThisExpr
        && !(parentNode instanceof DotExpr)
        && !(parentNode instanceof ApplyExpr)
        && !(parentNode instanceof DeleteStatement)
        && !((parentNode instanceof AssignmentOpExpr) && ((AssignmentOpExpr)parentNode).arg1==this)) {
        MemberDeclaration memberDeclaration = classDeclaration.getMemberDeclaration(property);
        if (memberDeclaration!=null && memberDeclaration.isMethod()) {
          //Jooc.warning(arg2.getSymbol(), "Found method used as function, have to bind!");
          out.writeToken("joo.bind(");
          arg1.generateCode(out);
          out.write(",");
          out.writeSymbolWhitespace(arg2.getSymbol());
          if (memberDeclaration.isPrivate()) {
            out.write("$"+property);
          } else {
            out.write("\""+property+"\"");
          }
          out.write(")");
          return;
        }
      }
      // check and handle private instance members and super method access:
      if ( arg1 instanceof ThisExpr && classDeclaration.isPrivateMember(property)
        || arg1 instanceof SuperExpr) {
        out.writeSymbolWhitespace(arg1.getSymbol());
        out.writeToken("this");
        out.write("[");
        // awkward, but we have to be careful if we add characters to tokens:
        out.writeSymbol(arg2.getSymbol(),  "$", "");
        out.write("]");
        return;
      }
      // check and handle private static member access:
      if (classDeclaration.isPrivateStaticMember(property)) {
        String[] qualifiedName = getQualifiedName(arg1);
        if (qualifiedName!=null) {
          //System.out.println("private static access: found class identifier "+ Arrays.asList(qualifiedName)+", comparing with "+Arrays.asList(classDeclaration.getQualifiedName())+".");
          if (qualifiedName.length==1 && qualifiedName[0].equals(classDeclaration.getName())
            || Arrays.equals(qualifiedName, classDeclaration.getQualifiedName())) {
            out.writeSymbolWhitespace(arg1.getSymbol());
            out.writeToken("$jooPrivate");
            out.writeSymbol(op);
            arg2.generateCode(out);
            return;
          }
        }
      }
    }
    super.generateCode(out);
  }
}
