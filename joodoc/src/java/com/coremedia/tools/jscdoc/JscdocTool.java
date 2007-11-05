package com.coremedia.tools.jscdoc;


import com.sun.tools.javac.v8.util.List;

import com.sun.tools.javac.v8.util.ListBuffer;
import com.sun.tools.javadoc.*;

import com.coremedia.jscc.Jscc;
import com.coremedia.jscc.CompilationUnit;
import com.coremedia.jscc.AnalyzeContext;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 19.07.2004
 * Time: 15:49:16
 * To change this template use File | Settings | File Templates.
 */
public class JscdocTool extends Jscc {
  private com.sun.tools.javadoc.Messager messager;
  //private final JscdocClassReader reader;


  public JscdocTool(Messager messager) {
    this.messager = messager;

    //this.reader=JscdocClassReader.instance0(new com.sun.tools.javac.v8.util.Context());

  }

  public RootDocImpl getRootDocImpl(String docLocale, String encoding, ModifierFilter showAccess, List list, List optionList, boolean breakiterator, List list2, List list3, boolean flag) {
    ListBuffer listbuffer = new ListBuffer();
    ListBuffer listbuffer1 = new ListBuffer();
    ListBuffer listbuffer2 = new ListBuffer();

    for (List list4 = list; list4.nonEmpty(); list4 = list4.tail) {
      String s2 = (String) list4.head;
      if (s2.endsWith(".jsc") && (new File(s2)).exists()) {
        messager.notice("main.Loading_source_file", s2);
        processSource(s2);

        continue;
      }
      if (isValidPackageName(s2)) {
        listbuffer = listbuffer.append(s2);
        continue;
      }
      if (s2.endsWith(".jsc"))
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
                if(!flag && (isValidJscSourceFile(as[l])) && !listbuffer.contains(s)) {
                    listbuffer.append(s);
                    flag = true;
                    continue;
                        }
                if((new File(file.getPath(), as[l])).isDirectory())
                    searchSubPackages(s + "." + as[l], listbuffer, list);
                    }

                }

            }*/

  private static boolean isValidJscSourceFile(String s) {
        if(!s.endsWith(".jsc")) {
            return false;
      } else {
            String s1 = s.substring(0, s.length() - ".jsc".length());
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
                if(isValidJscSourceFile(as[l])) {
                    String s4 = s3 + as[l];
                    listbuffer.append(parse(s4));
                    flag = true;
                        }

                }

        if(!flag)
            messager.warning(null, "main.no_source_files_for_package", s.replace(File.separatorChar, '.'));
            }*/
}
