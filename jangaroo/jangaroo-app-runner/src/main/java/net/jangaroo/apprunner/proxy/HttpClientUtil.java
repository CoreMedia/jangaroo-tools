package net.jangaroo.apprunner.proxy;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

class HttpClientUtil {

  static SSLConnectionSocketFactory createSSLSocketFactory() {
    try {
      SSLContextBuilder sslContextBuilder = SSLContextBuilder.create()
              .loadTrustMaterial(null, new TrustSelfSignedStrategy());
      return new SSLConnectionSocketFactory(sslContextBuilder.build(), new DefaultHostnameVerifier());
    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
      throw new IllegalStateException(e);
    }

  }
}
