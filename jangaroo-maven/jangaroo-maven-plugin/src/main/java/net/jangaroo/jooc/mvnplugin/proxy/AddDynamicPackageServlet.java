package net.jangaroo.jooc.mvnplugin.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddDynamicPackageServlet extends HttpServlet {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String url;
  private final String senchaPackageName;

  public AddDynamicPackageServlet(String url, String senchaPackageName) {
    this.url = url;
    this.senchaPackageName = senchaPackageName;
  }

  private List<String> readDynamicPackages() throws IOException {
    InputStream inputStream = new URL(url).openStream();
    //noinspection unchecked
    return (List<String>) objectMapper.readValue(inputStream, List.class);
  }

  private void serializeDynamicPackages(List<String> dynamicPackages, OutputStream outputStream) throws IOException {
    objectMapper.writeValue(outputStream, dynamicPackages);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<String> dynamicPackages = new ArrayList<>(readDynamicPackages());
    dynamicPackages.add(senchaPackageName);

    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Content-Type", "application/json");
    serializeDynamicPackages(dynamicPackages, resp.getOutputStream());
  }
}
