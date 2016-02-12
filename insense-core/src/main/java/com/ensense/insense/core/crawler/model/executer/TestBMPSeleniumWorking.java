package com.ensense.insense.core.crawler.model.executer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ensense.insense.data.common.model.MintProxyServer;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.dns.AdvancedHostResolver;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.cts.mint.common.CRUDObjectsToFileUtil;

public class TestBMPSeleniumWorking {
	
	static boolean useRemoteProxy=false;
	static boolean useCustomDns=false;
	
	
	static String authUser = "SER_Mint_Support";
	static String authPassword = "July2015";

	static String corporate_proxy_host="proxy.ops.tiaa-cref.org";
	static String corporate_proxy_ip = "10.165.144.18";
	static String corporate_proxy_port = "8080";
	
	//10.1.9.87
	static String charles_proxy_host="TIAA-9TQAMDGC6Q.ad.tiaa-cref.org";//"TIAA-9TQAMDGC6Q.ad.tiaa-cref.orgg";
	static int charles_proxy_port=8888;
	
	static String mob_host="TIAA-9TQAMDGC6Q.ad.tiaa-cref.org";//"TIAA-9TQAMDGC6Q.ad.tiaa-cref.org";
	static int mob_port=8989;
	
	static String mobProxy = mob_host +":"+mob_port;
	static String upstreamLocalProxy=charles_proxy_host+ ":"+charles_proxy_port;
	static String upstreamRemoteProxy=corporate_proxy_ip+":"+corporate_proxy_port;

	static String strFilePath = "C:\\dhinakar\\temp\\";

	public static final Map<String, String> hostHeaderSpoofMap;
	static {
		hostHeaderSpoofMap = new LinkedHashMap<String, String>();
		hostHeaderSpoofMap.put("iwc.tiaa-cref.org.edgekey-staging.net", "www.tiaa-cref.org");
	}
	
	public static void main(String args[]){
	//public static ProxyServer test(){
		//custom dns server
	
		if(useCustomDns){
		System.setProperty("sun.net.spi.nameservice.nameservers",corporate_proxy_ip );
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		}
	
	Security.setProperty("networkaddress.cache.ttl" , "60");		
		
	// allow lookup of host for charles proxy
	System.setProperty("bmp.allowNativeDnsFallback", "true");
	System.setProperty("jsse.enableSNIExtension", "false");

	System.setProperty("java.net.preferIPv4Stack","true");
	
	if(useRemoteProxy){
		System.setProperty("http.proxyUser", authUser);
		System.setProperty("http.proxyPassword", authPassword);	
		System.setProperty("http.proxyHost", charles_proxy_host);
		System.setProperty("http.proxyPort", Integer.toString(charles_proxy_port));
		System.setProperty("https.proxyHost", charles_proxy_host);
		System.setProperty("https.proxyPort", Integer.toString(charles_proxy_port));
		
	//	System.setProperty("trustProxy", "true");
	}
		
		//try for dns lookup
	//	System.setProperty("socksProxyHost",corporate_proxy_host);
	//	System.setProperty("socksProxyPort",corporate_proxy_port);
		
		MintProxyServer server = new MintProxyServer(mob_port);
		
		try {
			server.start();
			System.out.println("Started the server.");
			server.remapHost("www.google.com", "74.125.224.72");
			server.remapHost("www.tiaa-cref.org","184.28.194.36");
			server.addRequestInterceptor(new RequestInterceptor() {
	            @Override
	            public void process(BrowserMobHttpRequest request, Har har) {
	            
      	            
	            	URI requestURI=request.getMethod().getURI();
	            
	            	String requestHost=requestURI.getHost();
	      
	            	if(hostHeaderSpoofMap.containsKey(requestHost)){
	            	 	System.out.println("request Intercepted");
	            	System.out.println("request uri:" + requestURI.toString());
	           // 	if(requestHost.contains("iwc.tiaa-cref.org.edgekey-staging.net")){
	            	System.out.println("Host header -Before :" + request.getMethod().getHeaders("Host")[0]);
	            	//System.out.println("proxy request :"+request.getProxyRequest());
	                //request.getMethod().removeHeaders("User-Agent");
	                //request.getMethod().addHeader("User-Agent", "Bananabot/1.0");
	            	
	            	//spoof the host header for 
	            	request.getMethod().setHeader("Host",hostHeaderSpoofMap.get(requestHost) );
	            	System.out.println("Host header -After>> :" + request.getMethod().getHeaders("Host")[0]);
	            	System.out.println(">>>>>>");
	            	System.out.println("");
	            	}
	            }
	        });
			//server.setCaptureContent(true);
			//server.setCaptureHeaders(true);
			//server.setCaptureBinaryContent(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			
	        Map<String,String> mobProxyServerOptions = new HashMap<String, String>();
	        //set charles as upstream proxy
	       if(useRemoteProxy){
	        mobProxyServerOptions.put("httpProxy", upstreamRemoteProxy);
	       }else{
	        mobProxyServerOptions.put("httpProxy", upstreamLocalProxy);
	       }
	       
			server.setOptions(mobProxyServerOptions);
			//server.clearDNSCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("server port :"+server);
		
		CRUDObjectsToFileUtil.writeObjectToFile(server, "C:\\Tools\\mint\\Application\\Mint\\proxy.obj");


		// start the browser up
		WebDriver driver = createWebDriver();
		

		List<String> urls = new ArrayList<String>();
	
		urls.add("https://www.tiaa-cref.org/public/index.html");
		//urls.add("http://intranet.ops.tiaa-cref.org/public/home.html");
		urls.add("https://origin-publictools-st4.test.tiaa-cref.org/private/selfservices/sso/login.do");
		urls.add("https://origin-publictools-st2.test.tiaa-cref.org/private/selfservices/sso/login.do");
		urls.add("https://origin-publictools-pf.test.tiaa-cref.org/private/selfservices/sso/login.do");
		urls.add("https://origin-publictools-at.test.tiaa-cref.org/private/selfservices/sso/login.do");
		urls.add("https://publictools.tiaa-cref.org/private/selfservices/sso/login.do");

		
		
		
		for(String url: urls){

		testHar(driver, server, url);
		}

		/*
		// open google.com
	     server.newHar("google");
	    
	     String url;
	     url="https://www.google.com";
	       driver.get(url);
	       Har har = server.getHar();
		   System.out.println("server.getHar().getLog().getEntries().size :"+ har.getLog().getEntries().size());
		   try {
			writeHarToFile(har, new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		System.out.println("local testig complete.");
		//return server;
	}
	
	public static void testHar(WebDriver driver, ProxyServer server, String url){
		String pageRef=generatePageRef(url);
		server.newHar(pageRef);
		 driver.get(url); 
		 
		 Har har = server.getHar();
		 System.out.println("Url Intercepted :" + url);
		   System.out.println("server.getHar().getLog().getEntries().size :"+ server.getHar().getLog().getEntries().size());
		   try {
				writeHarToFile(har, new URL(url));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public static String generatePageRef(String url){
		URL newUrl = null;
		String pageRef=null;
		try {
			newUrl = new URL(url);
			pageRef=newUrl.getHost()+newUrl.getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 	pageRef;
	}
	
	public static void writeHarToFile(Har har, URL pageUrl){
		 try {
			har.writeTo(new File(strFilePath+pageUrl.getHost()+ pageUrl.getPath().replaceAll("/", "_")+".har"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 private static WebDriver createWebDriver(){
	        // configure it as a desired capability
	       

		        // configure it as a desired capability
		        DesiredCapabilities capabilities = new DesiredCapabilities();
		       
		        FirefoxProfile profile = new FirefoxProfile();
		        profile.setAcceptUntrustedCertificates(true);
		        profile.setAssumeUntrustedCertificateIssuer(true);              
		      //  profile.setPreference("network.proxy.share_proxy_settings", true);

		        /*
		        profile.setPreference("network.proxy.ftp", mob_host);
		        profile.setPreference("network.proxy.ftp_port", mob_port);

		        profile.setPreference("network.proxy.http", mob_host);
		        profile.setPreference("network.proxy.http_port", mob_port);
		       
		        
		        profile.setPreference("network.proxy.socks", mob_host);
		        profile.setPreference("network.proxy.socks_port", mob_port);
		       
		        profile.setPreference("network.proxy.ssl", mob_host);
		        profile.setPreference("network.proxy.ssl_port", mob_port);
		       
*/
		        Proxy proxyManual = new Proxy();
		        proxyManual.setProxyType(Proxy.ProxyType.MANUAL);
		        proxyManual.setHttpProxy(mobProxy);
		        proxyManual.setSslProxy(mobProxy);
		       // proxyManual.setSocksProxy(upstreamLocalProxy);
		          
		  	   capabilities.setCapability(CapabilityType.PROXY, proxyManual);     
		         
		      
		      capabilities.setCapability(FirefoxDriver.PROFILE,profile);

		      capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		    

		        WebDriver driver = new FirefoxDriver(capabilities);
		        return driver;
		    }
		
		
	    }
	
	

