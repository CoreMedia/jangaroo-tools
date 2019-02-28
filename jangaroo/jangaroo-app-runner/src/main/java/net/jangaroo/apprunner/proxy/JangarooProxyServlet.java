package net.jangaroo.apprunner.proxy;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mitre.dsmiley.httpproxy.ProxyServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;

/**
 * Add support for matrix parameters and SSL.
 */
public class JangarooProxyServlet extends ProxyServlet {

  @Override
  protected String rewritePathInfoFromRequest(HttpServletRequest servletRequest) {
    // Patched: use getRequestURI instead of getPathInfo to preserve matrix parameters
    String servletPath = servletRequest.getServletPath();
    return servletRequest.getRequestURI().replaceFirst(servletPath, "");
  }

  /**
   * {@inheritDoc}
   * <p>
   * Adjusted to work around
   * <a href="https://github.com/mitre/HTTP-Proxy-Servlet/issues/3">Test with HTTPS · Issue #3 · mitre/HTTP-Proxy-Servlet</a>.
   * </p>
   */
  @Override
  protected HttpClient createHttpClient() {
    HttpClientBuilder clientBuilder = HttpClientBuilder.create()
            .setDefaultRequestConfig(buildRequestConfig())
            .setDefaultSocketConfig(buildSocketConfig());

    // PATCHED: Use custom SSL Socket Factory
    // Workaround for https://github.com/mitre/HTTP-Proxy-Servlet/issues/3
    clientBuilder.setSSLSocketFactory(HttpClientUtil.createSSLSocketFactory());

    clientBuilder.setMaxConnTotal(maxConnections);

    if (useSystemProperties) {
      clientBuilder.useSystemProperties();
    }
    return clientBuilder.build();
  }

  /**
   * {@inheritDoc}
   * <p>
   * Only set the secure flag when the incoming request is already https to allow proxying from http to https.
   * </p>
   */
  @Override
  protected void copyProxyCookie(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse, String headerValue) {
    //build path for resulting cookie
    String path = servletRequest.getContextPath(); // path starts with / or is empty string
    path += servletRequest.getServletPath(); // servlet path starts with / or is empty string
    if (path.isEmpty()) {
      path = "/";
    }

    for (HttpCookie cookie : HttpCookie.parse(headerValue)) {
      //set cookie name prefixed w/ a proxy value so it won't collide w/ other cookies
      String proxyCookieName = doPreserveCookies ? cookie.getName() : getCookieNamePrefix(cookie.getName()) + cookie.getName();
      Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
      servletCookie.setComment(cookie.getComment());
      servletCookie.setMaxAge((int) cookie.getMaxAge());
      servletCookie.setPath(path); //set to the path of the proxy servlet
      // don't set cookie domain
      // PATCHED
      servletCookie.setSecure(applyCookieSecurePatch(servletRequest, cookie));
      servletCookie.setVersion(cookie.getVersion());
      servletCookie.setHttpOnly(cookie.isHttpOnly());
      servletResponse.addCookie(servletCookie);
    }
  }

  private static boolean applyCookieSecurePatch(HttpServletRequest servletRequest, HttpCookie cookie) {
    return "https".equals(servletRequest.getScheme()) && cookie.getSecure();
  }
}
