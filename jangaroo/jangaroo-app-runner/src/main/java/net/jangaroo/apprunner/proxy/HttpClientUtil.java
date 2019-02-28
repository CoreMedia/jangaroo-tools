package net.jangaroo.apprunner.proxy;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

class HttpClientUtil {
  private static final X509TrustManager TRUST_MANAGER = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  };

  static SSLConnectionSocketFactory createSSLSocketFactory() {
    SSLContext ctx;
    try {
      ctx = SSLContext.getInstance("TLS");
      ctx.init(null, new TrustManager[]{TRUST_MANAGER}, null);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new IllegalStateException(e);
    }

    return new SSLConnectionSocketFactory(ctx);
  }
}
