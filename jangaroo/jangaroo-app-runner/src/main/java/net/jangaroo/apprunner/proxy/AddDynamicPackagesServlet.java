package net.jangaroo.apprunner.proxy;

import net.jangaroo.apprunner.util.DynamicPackagesDeSerializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AddDynamicPackagesServlet extends HttpServlet {

  private final String url;
  private final List<String> senchaPackageNames;

  public AddDynamicPackagesServlet(String url, List<String> senchaPackageNames) {
    this.url = url;
    this.senchaPackageNames = senchaPackageNames;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Set<String> dynamicPackages;
    try {
      HttpResponse httpResponse = HttpClientBuilder.create().setSSLSocketFactory(HttpClientUtil.createSSLSocketFactory()).build().execute(new HttpGet(url));
      dynamicPackages = new LinkedHashSet<>(DynamicPackagesDeSerializer.readDynamicPackages(httpResponse.getEntity().getContent()));
      dynamicPackages.addAll(senchaPackageNames);
    } catch (IOException e) {
      // probably no dynamic-packages.json on server: just use the local dynamic packages.
      dynamicPackages = new LinkedHashSet<>(senchaPackageNames);
    }
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Content-Type", "application/json");
    DynamicPackagesDeSerializer.writeDynamicPackages(resp.getOutputStream(), dynamicPackages);
  }
}
