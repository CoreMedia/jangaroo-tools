package net.jangaroo.joodoc;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javadoc.*;


import java.io.File;

// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packfields(3) packimports(7) deadcode fieldsfirst splitstr(64) nonlb lnc radix(10) lradix(10)
// Source File Name:   JavadocClassReader.java


// Referenced classes of package com.sun.tools.javadoc:
//            DocEnv, PackageDocImpl

class JoodocClassReader extends ClassReader {

   private static final com.sun.tools.javac.util.Context.Key joodocClassReaderKey = new com.sun.tools.javac.util.Context.Key();
   private DocEnv docenv;

   private JoodocClassReader(com.sun.tools.javac.util.Context context) {
       super(context, true);
       context.put(joodocClassReaderKey, this);
       docenv = DocEnv.instance(context);
   }

   public static JoodocClassReader instance0(Context context) {
       JoodocClassReader joodocclassreader = (JoodocClassReader)context.get(joodocClassReaderKey);
       if(joodocclassreader == null)
           joodocclassreader = new JoodocClassReader(context);
       return joodocclassreader;
   }

   protected void extraFileActions(com.sun.tools.javac.code.Symbol.PackageSymbol packagesymbol, String s, File file) {
       if(docenv != null && s.equals("package.html"))
           docenv.getPackageDoc(packagesymbol).setDocPath(file.getAbsolutePath());
   }

   protected void extraZipFileActions(com.sun.tools.javac.code.Symbol.PackageSymbol packagesymbol, String s, String s1, String s2) {
       if(docenv != null && s.endsWith("package.html"))
           docenv.getPackageDoc(packagesymbol).setDocPath(s2, s1);
         }

}
