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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class AdditionalPackagesFromFolderServlet extends HttpServlet {
  private static final Logger LOG = getLogger(lookup().lookupClass());

  private final File additionalPackagesDirectory;

  public AdditionalPackagesFromFolderServlet(File additionalPackagesDirectory) {
    this.additionalPackagesDirectory = additionalPackagesDirectory;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String pathInfo = req.getPathInfo();
    List<String> additionalPackages;
    if (additionalPackagesDirectory == null) {
      additionalPackages = Collections.emptyList();
    } else {
      File directoryToList = pathInfo != null && pathInfo.startsWith("/")
              ? new File(additionalPackagesDirectory, pathInfo.substring(1))
              : additionalPackagesDirectory;
      additionalPackages = getSubdirectoryNames(directoryToList);
      if (additionalPackages == null) {
        LOG.warn("Directory " + directoryToList.getAbsolutePath() +
                " could not be listed, please check path configured as 'additionalPackagesDir'.");
        resp.sendError(404, "Directory could not be listed, see log for details.");
        return;
      }
    }
    List<Map<String, String>> nginxAutoindexJson = additionalPackages.stream()
            .map(AdditionalPackagesFromFolderServlet::createNginxAutoIndexJsonEntry)
            .collect(Collectors.toList());
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Content-Type", "application/json");
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new PrintWriter(resp.getOutputStream()),
            nginxAutoindexJson);
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
