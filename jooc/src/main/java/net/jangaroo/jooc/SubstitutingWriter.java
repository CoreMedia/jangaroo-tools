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
import java.io.Writer;

/**
 * <p>An SubstitutingWriter is a FilterWriter that replaces
 * each character in the output stream
 * with the String obtained by calling the abstract method substitute
 * on the characters.</p>
 * <p>Characters for which substitute returns null are output verbatim.</p>
 * <p>The implementation is fairly efficient in the number of calls to
 * the various out.write methods.</p>
 *
 * @author Axel Wienberg
 */
public abstract class SubstitutingWriter extends java.io.FilterWriter {

  public SubstitutingWriter(Writer out) {
    super(out);
  }

  /**
   * What should be printed instead of character <code>c</code>?
   * This method is always called synchronized on <code>out</code>.
   * Changes to the substitution function should therefore also be synchronized on <code>out</code>.
   * @return the string to be printed, or null for "no change", i.e. just <code>c</code>
   */
  protected abstract String substitute(char c);

  protected void writeReplacement(String replacement) throws java.io.IOException {
    out.write(replacement);
  }

  /**
   * Write a single character, applying the substitution.
   *
   * @exception  java.io.IOException  If an I/O error occurs
   */
  public void write(int c) throws IOException {
    synchronized(out) {
      char ch = (char) c;
      String replacement = substitute(ch);
      if (replacement == null) {
	out.write(c);
      } else {
	writeReplacement(replacement);
      }
    }
  }

  /**
   * Write a portion of an array of characters, applying the substitution.
   *
   * @param  cbuf  Buffer of characters to be written
   * @param  off   Offset from which to start reading characters
   * @param  len   Number of characters to be written
   *
   * @exception  java.io.IOException  If an I/O error occurs
   */
  public void write(char cbuf[], int off, int len) throws IOException {
    synchronized(out) {
      int nextToWrite = off;
      int guard = off+len;
      for( ; off < guard; ++off) {
	String replacement = substitute(cbuf[off]);
	if(replacement != null) {
	  if (nextToWrite < off) {
	    out.write(cbuf, nextToWrite, off-nextToWrite);
	  }
	  writeReplacement(replacement);
	  nextToWrite = off+1;
	}
      }
      if (nextToWrite < off) {
	out.write(cbuf, nextToWrite, off-nextToWrite);
      }
    }
  }

  /**
   * Write a portion of a string, applying the substitution.
   *
   * @param  str  String to be written
   * @param  off  Offset from which to start reading characters
   * @param  len  Number of characters to be written
   *
   * @exception  java.io.IOException  If an I/O error occurs
   */
  public void write(String str, int off, int len) throws IOException {
    synchronized(out) {
      int nextToWrite = off;
      int guard = off+len;
      for( ; off < guard; ++off) {
	String replacement = substitute(str.charAt(off));
	if(replacement != null) {
	  if (nextToWrite < off) {
	    out.write(str, nextToWrite, off-nextToWrite);
	  }
	  writeReplacement(replacement);
	  nextToWrite = off+1;
	}
      }
      if (nextToWrite < off) {
	out.write(str, nextToWrite, off-nextToWrite);
      }
    }
  }
}


