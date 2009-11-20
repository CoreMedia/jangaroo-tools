package com.intellij.javascript.debugger.execution;

import com.intellij.javascript.debugger.impl.DebuggableFileFinder;
import com.intellij.javascript.debugger.impl.ScriptUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.CharFilter;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.impl.http.HttpVirtualFile;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class RemoteDebuggingFileFinder extends DebuggableFileFinder {
  private Map<String, VirtualFile> myUrl2Local;
  private final THashSet<VirtualFile> myLocalFiles;
  private final Project myProject;

  public RemoteDebuggingFileFinder(Project project, List<RemoteJavaScriptDebugConfiguration.RemoteUrlMappingBean> mappings) {
    this.myProject = project;
    this.myUrl2Local = createUrl2LocalMap(mappings);
    this.myLocalFiles = new THashSet<VirtualFile>(this.myUrl2Local.values());
  }

  private static Map<String, VirtualFile> createUrl2LocalMap(List<RemoteJavaScriptDebugConfiguration.RemoteUrlMappingBean> mappings) {
    Map<String,VirtualFile> url2Local = new THashMap<String,VirtualFile>();
    LocalFileSystem fileSystem = LocalFileSystem.getInstance();
    for (RemoteJavaScriptDebugConfiguration.RemoteUrlMappingBean mapping : mappings) {
      VirtualFile file = fileSystem.findFileByPath(mapping.getLocalFilePath());
      if (file != null)
        url2Local.put(mapping.getRemoteUrl(), file);
    }

    return url2Local;
  }

  private static String trimUrlParameters(@NotNull String url) {
    int end = StringUtil.findFirst(url, new CharFilter() {
      public boolean accept(char ch) {
        return ((ch == '?') || (ch == '#') || (ch == ';'));
      }

    });
    return ((end != -1) ? url.substring(0, end) : url);
  }

  public VirtualFile findFile(@NotNull String fileUrl) {
    String url = trimUrlParameters(fileUrl);
    int i = url.length();
    while (i != -1) {
      String prefix = url.substring(0, i);
      VirtualFile file = this.myUrl2Local.get(prefix);
      if (file == null) {
        file = this.myUrl2Local.get(prefix + "/");
      }

      if (file != null) {
        if (i >= url.length()) {
          if (!(ScriptUtil.isHtmlOrJavaScript(file))) break;
          return file;
        }

        String relativePath = url.substring(i + 1);
        VirtualFile child = file.findFileByRelativePath(relativePath);
        // Begin Jangaroo hack: try with tweaked extension!
        if (child == null && relativePath.endsWith(".js")) {
          relativePath = relativePath.substring(0,relativePath.length()-3)+".as";
          child = file.findFileByRelativePath(relativePath);
        }
        // End Jangaroo hack
        if ((child != null) && (ScriptUtil.isHtmlOrJavaScript(child))) {
          return child;
        }

        break;
      }
      i = url.lastIndexOf(47, i - 1);
    }
    return VirtualFileManager.getInstance().findFileByUrl(url);
  }

  public boolean isDebuggable(@NotNull VirtualFile file) {
    if (file instanceof HttpVirtualFile) {
      return true;
    }

    VirtualFile current = file;
    while (current != null) {
      if (this.myLocalFiles.contains(current))
        return true;

      current = current.getParent();
    }
    return false;
  }

  public boolean canSetRemoteUrl(@NotNull VirtualFile file) {
    return ProjectRootManager.getInstance(this.myProject).getFileIndex().isInContent(file);
  }

  public boolean updateRemoteUrlMapping(List<RemoteJavaScriptDebugConfiguration.RemoteUrlMappingBean> mappings) {
    Map<String, VirtualFile> newMap = createUrl2LocalMap(mappings);
    if (!(newMap.equals(this.myUrl2Local))) {
      this.myUrl2Local = newMap;
      this.myLocalFiles.clear();
      this.myLocalFiles.addAll(this.myUrl2Local.values());
      return true;
    }
    return false;
  }
}