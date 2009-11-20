package com.intellij.javascript.debugger.impl;

import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.xdebugger.XSourcePosition;
import java.net.URI;
import java.net.URISyntaxException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mozilla.interfaces.jsdIScript;

public class ScriptUtil {

  @NonNls
  private static final String FILE_SCHEME = "file";

  @NonNls
  private static final String HTTP_SCHEME = "http";

  @Nullable
  public static String getFileUrl(@NotNull jsdIScript script) {
    return fixFileUrl(script.getFileName());
  }

  @Nullable
  public static String fixFileUrl(String fileUrl) {
    try {
      URI uri = new URI(fileUrl);
      String scheme = uri.getScheme();
      if (FILE_SCHEME.equals(scheme)) {
        String path = FileUtil.toSystemIndependentName(uri.getPath());
        if ((SystemInfo.isWindows) && (path.startsWith("/")))
          path = path.substring(1);

        return VirtualFileManager.constructUrl(FILE_SCHEME, path);
      }
      if (HTTP_SCHEME.equals(scheme))
        return fileUrl;

      return null;
    } catch (URISyntaxException e) {
      // ignore
    }
    return null;
  }

  public static boolean isPositionInside(@NotNull XSourcePosition position, @NotNull jsdIScript script, DebuggableFileFinder finder) {
    String fileUrl = getFileUrl(script);
    if (fileUrl == null) return false;

    if (!(position.getFile().equals(finder.findFile(fileUrl)))) {
      return false;
    }

    int line = position.getLine();
    long firstLine = script.getBaseLineNumber() - 1L;
    return ((firstLine <= line) && (line < firstLine + script.getLineExtent()));
  }

  @NonNls
  public static String getScriptDebugDescription(@Nullable jsdIScript script) {
    if (script == null)
      return "null";

    long from = script.getBaseLineNumber() - 1L;
    long to = from + script.getLineExtent() - 1L;
    return script.getFileName() + "(" + from + "-" + to + "), tag=" + script.getTag() + ((script.getIsValid()) ? "" : ", invalid");
  }

  @Nullable
  public static VirtualFile findContaingingFile(@Nullable jsdIScript script, DebuggableFileFinder finder) {
    if (script == null) {
      return null;
    }

    String fileUrl = getFileUrl(script);
    if (fileUrl == null) {
      return null;
    }

    return finder.findFile(fileUrl);
  }

  public static boolean isHtmlOrJavaScript(@NotNull VirtualFile file) {
    FileType type = file.getFileType();
    return type == StdFileTypes.HTML || type == StdFileTypes.XHTML || type == JavaScriptSupportLoader.JAVASCRIPT; // && !"as".equals(file.getExtension());
  }
}