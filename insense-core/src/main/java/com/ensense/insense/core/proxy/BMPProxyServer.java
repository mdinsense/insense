package com.ensense.insense.core.proxy;

import com.ensense.insense.data.common.model.MintProxyServer;
import com.ensense.insense.data.common.model.ScheduleDetails;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class BMPProxyServer {

	private static final Logger logger = Logger.getLogger(BMPProxyServer.class);

	private String mobProxyHost;
	private int mobProxyPort;
	private Map<String, String> hostHeaderSpoofMap;
	
	public BMPProxyServer() {

	}

	public ProxyServer getBMPProxyServer(String mobProxyHost, int mobProxyPort) {
		this.mobProxyHost = mobProxyHost;
		this.mobProxyPort = mobProxyPort;
		logger.info("mobProxyHost :"+mobProxyHost);
		logger.info("mobProxyPort :"+mobProxyPort);
		ProxyServer mobProxyServer = new ProxyServer(8989);
		return mobProxyServer;
	}

	/*public static boolean startBMPProxyServer(MintProxyServer mobProxyServer,
			String authUser, String authPassword,
			final Map<String, String> hostHeaderSpoofMap,
			boolean captureHeader, boolean captureContent)*/
	public boolean startBMPProxyServer(ScheduleDetails appConfig){
		boolean status = true;
		
		try {
			Security.setProperty("networkaddress.cache.ttl", "60");

			// allow lookup of host for charles proxy
			System.setProperty("bmp.allowNativeDnsFallback", "true");
			System.setProperty("jsse.enableSNIExtension", "false");

			System.setProperty("java.net.preferIPv4Stack", "true");

			logger.info("Before starting Proxy server");
			MintProxyServer mobProxyServer = new MintProxyServer(Integer.parseInt(appConfig.getUpstreamProxyPort()));
			//mobProxyServer.start();

			//mobProxyServer.remapHost("www.google.com", "74.125.224.72");
			//mobProxyServer.remapHost("www.tiaa-cref.org","184.28.194.36");
			
			//TODO
			boolean captureHeader = true;
			boolean captureContent = true;
			
			//mobProxyServer.setCaptureHeaders(captureHeader);
			//mobProxyServer.setCaptureContent(captureContent);

			// intercept http request conditionally
			/* mobProxyServer.addRequestInterceptor(new RequestInterceptor() {
				@Override
				public void process(BrowserMobHttpRequest request, Har har) {
					URI requestURI = request.getMethod().getURI();

					String requestHost = requestURI.getHost();
					if (hostHeaderSpoofMap.containsKey(requestHost)) {
						logger.info("request uri:" + requestURI.toString());
						logger.info("Host header -Before :"
								+ request.getMethod().getHeaders("Host")[0]);

						// spoof the host header for
						request.getMethod().setHeader("Host",
								hostHeaderSpoofMap.get(requestHost));
						logger.info("Host header -After>> :"
								+ request.getMethod().getHeaders("Host")[0]);
					}
				}
			});
			mobProxyServer.addResponseInterceptor(new ResponseInterceptor() {

				@Override
				public void process(BrowserMobHttpResponse response, Har har) {
					// System.out.println("response StatusCode :"
					// + response.getRawResponse().getStatusLine()
					// .getStatusCode());
					try {
						// System.out.println("response getParams :"
						// + response.getRawResponse().getParams());
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});*/

			mobProxyServer = addProxyOptions(mobProxyServer, "localhost", 8888);
		} catch (Exception e) {
			status = false;
			logger.error("Exception while starting the proxy server." + e);
			logger.error("Stack Trace :"+ ExceptionUtils.getStackTrace(e));
		}
		return status;
	}

	public MintProxyServer addProxyOptions(MintProxyServer mobProxyServer,
			String upstreamLocalProxyHost, int upstreamLocalProxyPort) {
		Map<String, String> mobProxyServerOptions = new HashMap<String, String>();
		logger.info("mobProxyHost :"+mobProxyHost);
		logger.info("mobProxyHost :"+mobProxyHost);
		//mobProxyServerOptions.put("httpProxy", upstreamLocalProxyHost + ":"
				//+ upstreamLocalProxyPort);
		mobProxyServerOptions.put("httpProxy", "localhost" + ":"
				+ 8888);
		//mobProxyServer.setOptions(mobProxyServerOptions);

		return mobProxyServer;
	}

	public boolean stopBMPProxyServer(ProxyServer mobProxyServer) {

		try {
			mobProxyServer.clearBlacklist();
			mobProxyServer.waitForNetworkTrafficToStop(40000, 20000);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

			logger.info("Before stopping the Proxy server");
			try {
				mobProxyServer.stop();
			} catch (Exception e) {
				logger.error("Exception while stopping BMP Server");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			logger.info("After stopping the Proxy server");
		}
		return true;
	}

	public Proxy getBMPProxyClient() {
		return createBMPProxyClient(mobProxyHost, mobProxyPort);
	}

	private Proxy createBMPProxyClient(String host, int port) {
		Proxy mobProxySeleniumClient = new Proxy();
		String mobProxyString = host + ":" + port;
		// mobProxySeleniumClient.setHttpProxy("");
		mobProxySeleniumClient.setSslProxy(mobProxyString);
		return mobProxySeleniumClient;
	}

	public static synchronized Har openUrl(WebDriver driver,
			ProxyServer server, String url) {
		String pageRef = "test1";// generatePageRef(url);
		server.newHar(pageRef);
		driver.get(url);

		return server.getHar();
	}
}
