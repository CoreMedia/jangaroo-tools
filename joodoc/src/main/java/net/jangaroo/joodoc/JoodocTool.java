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

package net.jangaroo.joodoc;


import com.sun.tools.javac.util.List;

import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javadoc.*;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.CompilationUnit;
import net.jangaroo.jooc.AnalyzeContext;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 19.07.2004
 * Time: 15:49:16
 * To change this template use File | Settings | File Templates.
 */
public class JoodocTool extends Jooc {
  private com.sun.tools.javadoc.Messager messager;
  //private final JoodocClassReader reader;


  public JoodocTool(Messager messager) {
    this.messager = messager;

    //this.reader=JoodocClassReader.instance0(new com.sun.tools.javac.v8.util.Context());

  }

  public RootDocImpl getRootDocImpl(String docLocale, String encoding, ModifierFilter showAccess, List list, List optionList, boolean breakiterator, List list2, List list3, boolean flag) {
    ListBuffer listbuffer = new ListBuffer();
    ListBuffer listbuffer1 = new ListBuffer();
    ListBuffer listbuffer2 = new ListBuffer();

    for (List list4 = list; list4.nonEmpty(); list4 = list4.tail) {
      String s2 = (String) list4.head;
      if (s2.endsWith(JS2_SUFFIX) && (new File(s2)).exists()) {
        messager.notice("main.Loading_source_file", s2);
        processSource(s2);

        continue;
      }
      if (isValidPackageName(s2)) {
        listbuffer = listbuffer.append(s2);
        continue;
      }
      if (s2.endsWith(JS2_SUFFIX))
        messager.error(null, "main.file_not_found", s2);
      else
        messager.error(null, "main.illegal_package_name", s2);
    }
    ArrayList units = getCompilationUnits();
    for (int i = 0; i < units.size(); i++) {
        CompilationUnit unit = (CompilationUnit) units.get(i);
        unit.analyze(new AnalyzeContext());

   // searchSubPackages(list2, listbuffer, list3);
    //for(List list5 = listbuffer.toList(); list5.nonEmpty(); list5 = list5.tail)
    //    parsePackageClasses((String)list5.head, listbuffer2, list3);
    //
     }
    return new RootDocImpl(new Context(getCompilationUnits()),optionList);
  }

    boolean isValidPackageName (String s) {
      int i;
      while ((i = s.indexOf('.')) != -1) {
        if (!isValidClassName(s.substring(0, i)))
          return false;
        s = s.substring(i + 1);
      }
      return isValidClassName(s);
    }


  private static boolean isValidClassName(String s) {
        if(s.length() < 1)
            return false;
        if(!Character.isJavaIdentifierStart(s.charAt(0)))
            return false;
        for(int i = 1; i < s.length(); i++)
            if(!Character.isJavaIdentifierPart(s.charAt(i)))
                return false;

        return true;
  }

  /*private void searchSubPackages(List list, ListBuffer listbuffer, List list1) {
    for(List list2 = list; list2.nonEmpty(); list2 = list2.tail)
        searchSubPackages((String)list2.head, listbuffer, list1);
  }*/

    /*   private void searchSubPackages(String s, ListBuffer listbuffer, List list) {
        if(list.contains(s))
            return;
        String s1 = reader.sourceClassPath + File.pathSeparatorChar + reader.classPath;
        int i = s1.length();
        int j = 0;
        String s2 = s.replace('.', File.separatorChar);
        boolean flag = false;
                int k;
        for(; j < i; j = k + 1) {
            k = s1.indexOf(File.pathSeparatorChar, j);
            String s3 = s1.substring(j, k);
            File file = new File(s3, s2);
            String as[] = file.list();
            if(as == null)
                continue;
            for(int l = 0; l < as.length; l++) {
                if(!flag && (isValidJooSourceFile(as[l])) && !listbuffer.contains(s)) {
                    listbuffer.append(s);
                    flag = true;
                    continue;
                        }
                if((new File(file.getPath(), as[l])).isDirectory())
                    searchSubPackages(s + "." + as[l], listbuffer, list);
                    }

                }

            }*/

  private static boolean isValidJooSourceFile(String s) {
        if(!s.endsWith(JS2_SUFFIX)) {
            return false;
      } else {
            String s1 = s.substring(0, s.length() - JS2_SUFFIX.length());
            return isValidClassName(s1);
      }
  }

 /* private void parsePackageClasses(String s, ListBuffer listbuffer, List list) {
        if(list.contains(s))
            return;
        boolean flag = false;
        String s1 = reader.sourceClassPath;
        if(s1 == null)
            s1 = reader.classPath;
        int i = s1.length();
        int j = 0;
        messager.notice("main.Loading_source_files_for_package", s);
        s = s.replace('.', File.separatorChar);
                int k;
        for(; j < i; j = k + 1) {
            k = s1.indexOf(File.separatorChar, j);
            String s2 = s1.substring(j, k);
            File file = new File(s2, s);
            String as[] = file.list();
            if(as == null)
                continue;
            String s3 = file.getAbsolutePath();
            if(!s3.endsWith(File.separator))
                s3 = s3 + File.separator;
            for(int l = 0; l < as.length; l++)
                if(isValidJooSourceFile(as[l])) {
                    String s4 = s3 + as[l];
                    listbuffer.append(parse(s4));
                    flag = true;
                        }

                }

        if(!flag)
            messager.warning(null, "main.no_source_files_for_package", s.replace(File.separatorChar, '.'));
            }*/
}
