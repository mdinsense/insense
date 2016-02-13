package com.ensense.insense.core.proxy;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.LegacyProxyServer;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import net.lightbody.bmp.proxy.http.RequestInterceptor;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Security;
import java.util.*;

public class BMPServer {

	private static final Logger logger = Logger.getLogger(BMPServer.class);
	private String mobProxyHost;
	private int mobProxyPort;

	public static void main(String[] args) {
		BMPServer bmpServer = new BMPServer();
		Map<String, String> hostHeaderSpoofMap;
		hostHeaderSpoofMap = new LinkedHashMap<String, String>();
		hostHeaderSpoofMap.put("iwc.tiaa-cref.org.edgekey-staging.net",
				"www.tiaa-cref.org");

		ProxyServer mobProxyServer = bmpServer.startServer("manni",
				"April2015", "TIAA-9TQAMDGC6Q.ad.tiaa-cref.org", 8989);
		bmpServer.addRequestIntercentor(mobProxyServer, hostHeaderSpoofMap);

		bmpServer.addProxyOptions(mobProxyServer, "TIAA-9TQAMDGC6Q.ad.tiaa-cref.org", 8888);
		WebDriver driver = bmpServer.createWebDriver();

		List<String> urls = new ArrayList<String>();

		urls.add("https://www.tiaa-cref.org/public/index.html");
		//urls.add("http://intranet.ops.tiaa-cref.org/public/home.html");
		for (String url : urls) {
			String pageRef = generatePageRef(url);
			mobProxyServer.newHar(pageRef);
			driver.get(url);

			Har har = mobProxyServer.getHar();
			System.out.println("Url Intercepted :" + url);
			System.out.println("server.getHar().getLog().getEntries().size :"
					+ mobProxyServer.getHar().getLog().getEntries().size());
		}
	}

	private ProxyServer addProxyOptions(ProxyServer mobProxyServer, String upstreamLocalProxyHost, int upstreamLocalProxyPort) {
		Map<String, String> mobProxyServerOptions = new HashMap<String, String>();
		mobProxyServerOptions.put("httpProxy", upstreamLocalProxyHost + ":"
				+ upstreamLocalProxyPort);
		mobProxyServer.setOptions(mobProxyServerOptions);
		
		return mobProxyServer;
	}

	public static String generatePageRef(String url) {
		URL newUrl = null;
		String pageRef = null;
		try {
			newUrl = new URL(url);
			pageRef = newUrl.getHost() + newUrl.getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pageRef;
	}

	public ProxyServer startServer(String authUser, String authPassword,
			String mobProxyHost, int mobProxyPort) {
		this.mobProxyHost = mobProxyHost;
		this.mobProxyPort = mobProxyPort;

		Security.setProperty("networkaddress.cache.ttl", "60");

		// allow lookup of host for charles proxy
		System.setProperty("bmp.allowNativeDnsFallback", "true");
		System.setProperty("jsse.enableSNIExtension", "false");

		System.setProperty("java.net.preferIPv4Stack", "true");

		ProxyServer mobProxyServer = new ProxyServer(mobProxyPort);
		try {
			logger.info("Starting the server...");
			mobProxyServer.start();
			mobProxyServer.remapHost("www.tiaa-cref.org", "184.28.194.36");
			logger.info("Server started...");
		} catch (Exception e) {
			try {
				// stop if already running
				logger.error("Not able to start the proxy...");
				mobProxyServer.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		// mobProxyServer.autoBasicAuthorization("tiaa-cref.org", authUser,
		// authPassword);

		// mobProxyServer.setCaptureHeaders(true);
		// mobProxyServer.setCaptureContent(true);

		return mobProxyServer;
	}

	public LegacyProxyServer addRequestIntercentor(LegacyProxyServer mobProxyServer,
			final Map<String, String> hostHeaderSpoofMap) {
		mobProxyServer.addResponseInterceptor(new ResponseInterceptor() {

			@Override
			public void process(BrowserMobHttpResponse response, Har har) {
				logger.info("response StatusCode :"
						+ response.getRawResponse().getStatusLine()
								.getStatusCode());
				try {
					logger.info("response getParams :"
							+ response.getRawResponse().getParams());
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		mobProxyServer.addRequestInterceptor(new RequestInterceptor() {
			@Override
			public void process(BrowserMobHttpRequest request, Har har) {
				URI requestURI = request.getMethod().getURI();

				String requestHost = requestURI.getHost();
				logger.info("requestURI :" + requestURI);
				// System.out.println("request host :"+requestHost);
				if (hostHeaderSpoofMap.containsKey(requestHost)) {
					logger.info("request Intercepted");
					logger.info("request uri:" + requestURI.toString());
					// if(requestHost.contains("iwc.tiaa-cref.org.edgekey-staging.net")){
					logger.info("Host header -Before :"
							+ request.getMethod().getHeaders("Host")[0]);
					// System.out.println("proxy request :"+request.getProxyRequest());
					// request.getMethod().removeHeaders("User-Agent");
					// request.getMethod().addHeader("User-Agent",
					// "Bananabot/1.0");

					// spoof the host header for
					request.getMethod().setHeader("Host",
							hostHeaderSpoofMap.get(requestHost));
					logger.info("Host header -After>> :"
							+ request.getMethod().getHeaders("Host")[0]);
				}
			}
		});

		return mobProxyServer;
	}

	public WebDriver createWebDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(true);
		// profile.setPreference("network.proxy.share_proxy_settings", true);

		/*
		 * profile.setPreference("network.proxy.ftp", mob_host);
		 * profile.setPreference("network.proxy.ftp_port", mob_port);
		 * 
		 * profile.setPreference("network.proxy.http", mob_host);
		 * profile.setPreference("network.proxy.http_port", mob_port);
		 * 
		 * 
		 * profile.setPreference("network.proxy.socks", mob_host);
		 * profile.setPreference("network.proxy.socks_port", mob_port);
		 * 
		 * profile.setPreference("network.proxy.ssl", mob_host);
		 * profile.setPreference("network.proxy.ssl_port", mob_port);
		 */
		Proxy proxyManual = new Proxy();
		proxyManual.setProxyType(Proxy.ProxyType.MANUAL);
		proxyManual.setHttpProxy(mobProxyHost + ":" + mobProxyPort);
		proxyManual.setSslProxy(mobProxyHost + ":" + mobProxyPort);

		capabilities.setCapability(CapabilityType.PROXY, proxyManual);

		capabilities.setCapability(FirefoxDriver.PROFILE, profile);

		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		WebDriver driver = new FirefoxDriver(capabilities);
		return driver;
	}

	public void stopProxyServer(LegacyProxyServer mobProxyServer) {
		try {
			// stop if already running
			mobProxyServer.waitForNetworkTrafficToStop(40000, 20000);
			logger.info("Stopping the proxy...");
			mobProxyServer.stop();
			logger.info("Proxy stopped...");
		} catch (Exception e) {
			logger.error("Not able to stop..." + e);
			e.printStackTrace();
		}
	}
}
