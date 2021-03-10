package net.jangaroo.apprunner.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class AdditionalPackagesFromFolderServlet extends HttpServlet {
  private static final Logger LOG = getLogger(lookup().lookupClass());
  private static final String PACKAGES = "packages";
  private static final String PACKAGES_PATH_INFO = "/" + PACKAGES + "/";

  private final File[] additionalPackagesDirectories;

  public AdditionalPackagesFromFolderServlet(File[] additionalPackagesDirectories) {
    this.additionalPackagesDirectories = additionalPackagesDirectories;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String pathInfo = req.getPathInfo();
    Collection<String> additionalPackages;
    if (pathInfo == null || "/".equals(pathInfo)) {
      // We collect all 'packages' subfolders of all configured directories in a virtual folder "packages".
      // If there are any directories configured, we return ["packages"], otherwise, just the empty list:
      additionalPackages = additionalPackagesDirectories == null
              ? Collections.emptyList() : Collections.singletonList(PACKAGES);
    } else if (PACKAGES_PATH_INFO.equals(pathInfo)) {
      additionalPackages = new LinkedHashSet<>();
      for (File additionalPackagesDirectory : additionalPackagesDirectories) {
        File directoryToList = new File(additionalPackagesDirectory, PACKAGES);
        List<String> subdirectoryNames = getSubdirectoryNames(directoryToList);
        if (subdirectoryNames == null) {
          LOG.warn("Directory " + directoryToList.getAbsolutePath() +
                  " could not be listed, please check paths configured as 'additionalPackagesDirs'.");
        } else {
          additionalPackages.addAll(subdirectoryNames);
        }
      }
    } else {
      // I can't handle this path info:
      resp.sendError(404);
      return;
    }
    List<Map<String, String>> nginxAutoIndexJson = additionalPackages.stream()
            .map(AdditionalPackagesFromFolderServlet::createNginxAutoIndexJsonEntry)
            .collect(Collectors.toList());
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Content-Type", "application/json");
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new PrintWriter(resp.getOutputStream()),
            nginxAutoIndexJson);
  }

  private static List<String> getSubdirectoryNames(File directoryToList) {
    try {
      File[] subdirs = directoryToList.listFiles(File::isDirectory);
      if (subdirs != null) {
        return Arrays.stream(subdirs)
                .map(File::getName)
                .collect(Collectors.toList());
      }
    } catch (SecurityException e) {
      // ignore, additionalPackages remains null
    }
    return null;
  }

  private static Map<String, String> createNginxAutoIndexJsonEntry(String packageName) {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();
    result.put("name", packageName);
    result.put("type", "directory");
    return result;
  }
}
