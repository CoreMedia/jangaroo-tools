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
