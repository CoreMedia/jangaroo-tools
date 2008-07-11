// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packfields(3) packimports(7) deadcode fieldsfirst splitstr(64) nonlb lnc radix(10) lradix(10)
// Source File Name:   Start.java

package net.jangaroo.joodoc;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.*;
import com.sun.tools.javac.main.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

// Referenced classes of package com.sun.tools.javadoc:
//            DocletInvoker, JavadocTool, Messager, ModifierFilter

class Start {

  com.sun.tools.javac.util.Context context;
  private final String defaultDocletClassName; // "com.sun.tools.doclets.standard.Standard";
  private ListBuffer options;
  private ModifierFilter showAccess;
  private long defaultFilter;
  private net.jangaroo.joodoc.Messager messager;
  String docLocale, encoding;
  boolean breakiterator;
  private DocletInvoker docletInvoker;
   private boolean rejectWarnings;

  Start() {
    this("javadoc");
  }

  int begin(String as[]) {
        boolean flag = false;
        flag = !parseAndExecute(as);
        messager.exitNotice();
        messager.flush();
        flag |= messager.nerrors() > 0;
        flag |= rejectWarnings && messager.nwarnings() > 0;
        return flag ? 1 : 0;
  }


  private void exit() {
    messager.exit();
  }

  private void usage() {
    messager.notice("main.usage");
    if (docletInvoker != null)
      docletInvoker.optionLength("-help");
  }

  private void setFilter(long l) {
    if (showAccess != null) {
      messager.error(null, "main.incompatible.access.flags");
      usage();
      exit();
    }
    showAccess = new ModifierFilter(l);
  }

  Start(String s) {
    this(s, "com.sun.tools.doclets.standard.Standard");
  }

  private void setOption(String s) {
    String as[] = {
      s
    };
    options.append(as);
  }

  private void usageError(String s) {
    messager.error(null, s);
    usage();
    exit();
  }


  private void setDocletInvoker(String as[]) {
    String s = null;
    String s1 = null;
    for (int i = 0; i < as.length; i++) {
      String s2 = as[i];
      if (s2.equals("-doclet")) {
        oneArg(as, i++);
        if (s != null)
          usageError("main.more_than_one_doclet_specified_0_and_1", s, as[i]);
        s = as[i];
        continue;
      }
      if (!s2.equals("-docletpath"))
        continue;
      oneArg(as, i++);
      if (s1 == null)
        s1 = as[i];
      else
        s1 = s1 + File.pathSeparator + as[i];
    }

    if (s == null)
      s = defaultDocletClassName;
    docletInvoker = new DocletInvoker(messager, s, s1);
  }

  private boolean parseAndExecute(String as[]) {
    long l = System.currentTimeMillis();
    ListBuffer listbuffer = new ListBuffer();
    try {
      as = CommandLine.parse(as);
    } catch (FileNotFoundException filenotfoundexception) {
      messager.error(null, "main.cant.read", filenotfoundexception.getMessage());
      exit();
    } catch (IOException ioexception) {
      ioexception.printStackTrace();
      exit();
    }
    setDocletInvoker(as);
    ListBuffer listbuffer1 = new ListBuffer();
    ListBuffer listbuffer2 = new ListBuffer();
    Options options1 = Options.instance(context);
    boolean flag = false;
    for (int i = 0; i < as.length; i++) {
      String s = as[i];
      if (s.equals("-subpackages")) {
        oneArg(as, i++);
        addToList(listbuffer1, as[i]);
        continue;
      }
      if (s.equals("-exclude")) {
        oneArg(as, i++);
        addToList(listbuffer2, as[i]);
        continue;
      }
      if (s.equals("-verbose")) {
        setOption(s);
        options1.put("-verbose", "");
        continue;
      }
      if (s.equals("-encoding")) {
        oneArg(as, i++);
        encoding = as[i];
        options1.put("-encoding", as[i]);
        continue;
      }
      if (s.equals("-breakiterator")) {
        breakiterator = true;
        setOption("-breakiterator");
        continue;
      }
      if (s.equals("-help")) {
        usage();
        exit();
        continue;
      }
      if (s.equals("-Xclasses")) {
        setOption(s);
        flag = true;
        continue;
      }
      if (s.equals("-Xwerror")) {
        setOption(s);
        rejectWarnings = true;
        continue;
      }
      if (s.equals("-private")) {
        setOption(s);
        setFilter(-9223372036854775801L);
        continue;
      }
      if (s.equals("-package")) {
        setOption(s);
        setFilter(-9223372036854775803L);
        continue;
      }
      if (s.equals("-protected")) {
        setOption(s);
        setFilter(5L);
        continue;
      }
      if (s.equals("-public")) {
        setOption(s);
        setFilter(1L);
        continue;
      }
      if (s.equals("-source")) {
        oneArg(as, i++);
        if (options1.get("-source") != null)
          usageError("main.option.already.seen", s);
        options1.put("-source", as[i]);
        continue;
      }
      if (s.equals("-gj")) {
        options1.put("-gj", "-gj");
        continue;
      }
      if (s.equals("-prompt")) {
        options1.put("-prompt", "-prompt");
        messager.promptOnError = true;
        continue;
      }
      if (s.equals("-sourcepath")) {
        oneArg(as, i++);
        if (options1.get("-sourcepath") != null)
          usageError("main.option.already.seen", s);
        options1.put("-sourcepath", as[i]);
        continue;
      }
      if (s.equals("-classpath")) {
        oneArg(as, i++);
        if (options1.get("-classpath") != null)
          usageError("main.option.already.seen", s);
        options1.put("-classpath", as[i]);
        continue;
      }
      if (s.equals("-sysclasspath")) {
        oneArg(as, i++);
        if (options1.get("-bootclasspath") != null)
          usageError("main.option.already.seen", s);
        options1.put("-bootclasspath", as[i]);
        continue;
      }
      if (s.equals("-bootclasspath")) {
        oneArg(as, i++);
        if (options1.get("-bootclasspath") != null)
          usageError("main.option.already.seen", s);
        options1.put("-bootclasspath", as[i]);
        continue;
      }
      if (s.equals("-extdirs")) {
        oneArg(as, i++);
        if (options1.get("-extdirs") != null)
          usageError("main.option.already.seen", s);
        options1.put("-extdirs", as[i]);
        continue;
      }
      if (s.equals("-overview")) {
        oneArg(as, i++);
        continue;
      }
      if (s.equals("-doclet")) {
        i++;
        continue;
      }
      if (s.equals("-docletpath")) {
        i++;
        continue;
      }
      if (s.equals("-locale")) {
        if (i != 0)
          usageError("main.locale_first");
        oneArg(as, i++);
        docLocale = as[i];
        continue;
      }
      if (s.startsWith("-XD")) {
        String s1 = s.substring("-XD".length());
        int k = s1.indexOf('=');
        String s2 = k >= 0 ? s1.substring(0, k) : s1;
        String s3 = k >= 0 ? s1.substring(k + 1) : s1;
        options1.put(s2, s3);
        continue;
      }
      if (s.startsWith("-")) {
        int j = docletInvoker.optionLength(s);
        if (j < 0) {
          exit();
          continue;
        }
        if (j == 0) {
          usageError("main.invalid_flag", s);
          continue;
        }
        if (i + j > as.length)
          usageError("main.requires_argument", s);
        ListBuffer listbuffer3 = new ListBuffer();
        for (int i1 = 0; i1 < j - 1; i1++)
          listbuffer3.append(as[++i]);

        setOption(s, listbuffer3.toList());
      } else {
        listbuffer.append(s);
      }
    }

    if (listbuffer.isEmpty() && listbuffer1.isEmpty())
      usageError("main.No_packages_or_classes_specified");
    if (!docletInvoker.validOptions(options.toList()))
      exit();
    JoodocTool joodocTool = new JoodocTool(messager);
    if (joodocTool == null)
      return false;
    if (showAccess == null)
      setFilter(defaultFilter);
    net.jangaroo.joodoc.RootDocImpl rootdocimpl = joodocTool.getRootDocImpl(docLocale, encoding, showAccess, listbuffer.toList(), options.toList(), breakiterator, listbuffer1.toList(), listbuffer2.toList(), flag);
    boolean flag1 = rootdocimpl != null;
    if (flag1)
      flag1 = docletInvoker.start(rootdocimpl);
    if (options1.get("-verbose") != null) {
      l = System.currentTimeMillis() - l;
      messager.notice("main.done_in", Long.toString(l));
    }
    return flag1;
  }

  private void oneArg(String as[], int i) {
    if (i + 1 < as.length)
      setOption(as[i], as[i + 1]);
    else
      usageError("main.requires_argument", as[i]);
  }

  private void setOption(String s, List list) {
    String as[] = new String[list.length() + 1];
    int i = 0;
    as[i++] = s;
    for (List list1 = list; list1.nonEmpty(); list1 = list1.tail)
      as[i++] = (String) list1.head;

    options = options.append(as);
  }

  private void addToList(ListBuffer listbuffer, String s) {
    String s1;
    for (StringTokenizer stringtokenizer = new StringTokenizer(s, ":"); stringtokenizer.hasMoreTokens(); listbuffer.append(s1))
      s1 = stringtokenizer.nextToken();

  }

  Start(String s, String s1) {
    options = new ListBuffer();
    showAccess = null;
    defaultFilter = 5L;
    docLocale = "";
    breakiterator = false;
    encoding = null;
    rejectWarnings = false;
    context = new Context();
    messager = new net.jangaroo.joodoc.Messager(context, s);
    defaultDocletClassName = s1;
  }

  private void setOption(String s, String s1) {
    String as[] = {
      s, s1
    };
    options.append(as);
  }

  private void usageError(String s, String s1) {
    messager.error(null, s, s1);
    usage();
    exit();
  }

  private void usageError(String s, String s1, String s2) {
    messager.error(null, s, s1, s2);
    usage();
    exit();
  }

  Start(String s, PrintWriter printwriter, PrintWriter printwriter1, PrintWriter printwriter2, String s1) {
    options = new ListBuffer();
    showAccess = null;
    defaultFilter = 5L;
    docLocale = "";
    breakiterator = false;
    encoding = null;
    rejectWarnings = false;
    context = new Context();
    messager = new net.jangaroo.joodoc.Messager(context, s, printwriter, printwriter1, printwriter2);
    defaultDocletClassName = s1;
  }
}
