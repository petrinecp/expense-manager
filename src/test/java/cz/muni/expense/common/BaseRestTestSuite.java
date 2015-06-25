package cz.muni.expense.common;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.ws.rs.client.Client;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public abstract class BaseRestTestSuite {

	protected Client createRestClient() throws GeneralSecurityException{
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(createAllTrustingClient());
    	return new ResteasyClientBuilder().httpEngine(engine).build();
	}
	
	@SuppressWarnings("deprecation")
	private DefaultHttpClient createAllTrustingClient() throws GeneralSecurityException {
		  SchemeRegistry registry = new SchemeRegistry();
		  registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
	
		  TrustStrategy trustStrategy = new TrustStrategy() {
		    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		      return true;
		    }
		  };
		  SSLSocketFactory factory = new SSLSocketFactory(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
		  registry.register(new Scheme("https", 443, factory));
	
		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
		  mgr.setMaxTotal(1000);
		  mgr.setDefaultMaxPerRoute(1000);
	
		  DefaultHttpClient client = new DefaultHttpClient(mgr, new DefaultHttpClient().getParams());
		  return client;
	}
}
