package net.jangaroo.jooc.mvnplugin.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class AddDynamicPackagesServlet extends HttpServlet {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String url;
  private final List<String> senchaPackageNames;

  public AddDynamicPackagesServlet(String url, List<String> senchaPackageNames) {
    this.url = url;
    this.senchaPackageNames = senchaPackageNames;
  }

  private List<String> readDynamicPackages() throws IOException {
    HttpResponse httpResponse = HttpClientUtil.createHttpsAwareHttpClientBuilder().build().execute(new HttpGet(url));
    InputStream inputStream = httpResponse.getEntity().getContent();
    //noinspection unchecked
    return (List<String>) objectMapper.readValue(inputStream, List.class);
  }

  private void serializeDynamicPackages(List<String> dynamicPackages, OutputStream outputStream) throws IOException {
    objectMapper.writeValue(outputStream, dynamicPackages);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LinkedHashSet<String> dynamicPackages = new LinkedHashSet<>();
    try {
      dynamicPackages.addAll(readDynamicPackages());
    } catch (IOException e) {
      // ignore, probably no dynamic-packages.json on server
      
    }
    dynamicPackages.addAll(senchaPackageNames);

    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Content-Type", "application/json");
    serializeDynamicPackages(new ArrayList<>(dynamicPackages), resp.getOutputStream());
  }
}
