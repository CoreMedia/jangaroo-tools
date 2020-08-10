package net.jangaroo.apprunner.proxy;

import net.jangaroo.apprunner.util.AppsDeSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class AddBootstrapJsonServlet extends HttpServlet {
  private static final String APP_JSON = "app.json";

  private final String appUrl;
  private final Map<String, String> pathMapping;

  public AddBootstrapJsonServlet(String appUrl, Map<String, String> pathMapping) {
    this.appUrl = appUrl;
    this.pathMapping = pathMapping;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    URI requestUri;
    try {
      requestUri = new URI(req.getRequestURI());
    } catch (URISyntaxException e) {
      throw new ServletException("Could not parse URI: " + req.getRequestURI(), e);
    }
    String[] pathSegments = requestUri.getPath().split("/");
    String requestedFileName = pathSegments[pathSegments.length - 1];
    // avoid endless loop
    if (requestedFileName.endsWith(".json") && !APP_JSON.equals(requestedFileName)) {
      HttpResponse httpResponse = HttpClientBuilder.create().setSSLSocketFactory(HttpClientUtil.createSSLSocketFactory()).build().execute(new HttpGet(appUrl + "/" + APP_JSON));
      List<String> locales = AppsDeSerializer.readLocales(httpResponse.getEntity().getContent());

      for (String locale : locales) {
        if ((locale + ".json").equals(requestedFileName)) {
          resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
          resp.setHeader("Content-Type", "application/json");

          httpResponse = HttpClientBuilder.create().setSSLSocketFactory(HttpClientUtil.createSSLSocketFactory()).build().execute(new HttpGet(appUrl + "/" + locale + ".json"));
          AppsDeSerializer.rewriteBootstrapJsonPaths(httpResponse.getEntity().getContent(), resp.getOutputStream(), pathMapping);
          return;
        }
      }
    }
  }
}
