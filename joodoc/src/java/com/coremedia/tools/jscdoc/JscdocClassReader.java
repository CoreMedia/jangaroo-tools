package com.coremedia.tools.jscdoc;

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

class JscdocClassReader extends ClassReader {

   private static final com.sun.tools.javac.util.Context.Key jscdocClassReaderKey = new com.sun.tools.javac.util.Context.Key();
   private DocEnv docenv;

   private JscdocClassReader(com.sun.tools.javac.util.Context context) {
       super(context, true);
       context.put(jscdocClassReaderKey, this);
       docenv = DocEnv.instance(context);
   }

   public static JscdocClassReader instance0(Context context) {
       JscdocClassReader jscdocclassreader = (JscdocClassReader)context.get(jscdocClassReaderKey);
       if(jscdocclassreader == null)
           jscdocclassreader = new JscdocClassReader(context);
       return jscdocclassreader;
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
