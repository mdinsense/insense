/*
 * Copyright (c) 2010-2011 Ardesco Solutions - http://www.ardescosolutions.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ensense.insense.data.common.model;

//import com.google.common.annotations.Beta;
//import com.lazerycode.ebselen.EbselenCore;
//import com.lazerycode.ebselen.handlers.FileHandler;

import com.ensense.insense.data.common.utils.FileHandler;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpState;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Set;

//@Beta
public class FileDownloader {

	private static final Logger logger = Logger
			.getLogger(FileDownloader.class);
    private WebDriver driver;
    private String downloadPath = System.getProperty("java.io.tmpdir");
    
    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    public FileDownloader() {
    }
    
    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String getDownloadPath() {
        return this.downloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void setDownloadPath(String filePath) {
        this.downloadPath = filePath;
    }


    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private HttpState mimicCookieState(Set<org.openqa.selenium.Cookie> seleniumCookieSet) {
        HttpState mimicWebDriverCookieState = new HttpState();
        for (org.openqa.selenium.Cookie seleniumCookie : seleniumCookieSet) {
            Cookie httpClientCookie = new Cookie(seleniumCookie.getDomain(), seleniumCookie.getName(), seleniumCookie.getValue(), seleniumCookie.getPath(), seleniumCookie.getExpiry(), seleniumCookie.isSecure());
            mimicWebDriverCookieState.addCookie(httpClientCookie);
        }
        return mimicWebDriverCookieState;
    }

    /**
     * Mimic the WebDriver host configuration
     *
     * @param hostURL
     * @return
     */
    private HostConfiguration mimicHostConfiguration(String hostURL, int hostPort) {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(hostURL, hostPort);
        return hostConfig;
    }

    /*public boolean fileDownloader(WebElement element) throws Exception {
        return downloader(element, "href", "");
    }*/

    public boolean imageDownloader(String imageUrl,String downloadPath) throws Exception {
        return downloader(imageUrl, downloadPath);
    }

    /*public boolean downloadImageFile(){
    	
    }*/
    public boolean downloader(String imageUrl, String downloadPath) throws Exception {
        setSSLContext();
        URL downloadURL = new URL(imageUrl);
        URLConnection conn = downloadURL.openConnection();
        try {
            //logger.info("HTTP Status {} when getting '{}', "+downloadURL.toExternalForm());
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            FileHandler downloadedFile = new FileHandler(downloadPath,true);
            int offset = 0;
            int len = 4096;
            int bytes = 0;
            byte[] block = new byte[len];
            while ((bytes = in.read(block, offset, len)) > -1) {
                downloadedFile.getWritableFileOutputStream().write(block, 0, bytes);
            }
            downloadedFile.close();
            in.close();
        } catch (Exception Ex) {
           // LOGGER.error("Download failed: {}", Ex);
            throw new Exception("Download failed, for the image :"+imageUrl);
        } finally {
        	//conn.releaseConnection();
        }
        
        return true;
    }
    /*
    public String downloader(WebElement element, String attribute, String downloadPath) throws Exception {
    	logger.info("In downloader");
        //Assuming that getAttribute does some magic to return a fully qualified URL
        String downloadLocation = element.getAttribute(attribute);
        if (downloadLocation.trim().equals("")) {
            throw new Exception("The element you have specified does not link to anything!");
        }
        URL downloadURL = new URL(downloadLocation);
        HttpClient client = new HttpClient();
       
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
        logger.info("downloadURL.getHost() :"+downloadURL.getHost());
        logger.info("downloadURL.getPort() :"+downloadURL.getPort());
        
        client.setHostConfiguration(mimicHostConfiguration(downloadURL.getHost(), downloadURL.getPort()));
        client.setState(mimicCookieState(driver.manage().getCookies()));
        HttpMethod getRequest = new GetMethod(downloadURL.getPath());
     //   FileHandler downloadedFile = new FileHandler(downloadPath + downloadURL.getFile().replaceFirst("/|\\\\", ""), true);
        FileHandler downloadedFile = new FileHandler(downloadPath,true);
        try {
            int status = client.executeMethod(getRequest);
            logger.info("HTTP Status {} when getting '{}'" + status +", "+downloadURL.toExternalForm());
            BufferedInputStream in = new BufferedInputStream(getRequest.getResponseBodyAsStream());
            int offset = 0;
            int len = 4096;
            int bytes = 0;
            byte[] block = new byte[len];
            while ((bytes = in.read(block, offset, len)) > -1) {
                downloadedFile.getWritableFileOutputStream().write(block, 0, bytes);
            }
            downloadedFile.close();
            in.close();
        } catch (Exception Ex) {
           // LOGGER.error("Download failed: {}", Ex);
            throw new Exception("Download failed!");
        } finally {
            getRequest.releaseConnection();
        }
        return downloadedFile.getAbsoluteFile();
    }*/

	private void setSSLContext() {
		System.setProperty("https.proxyHost", "localhost");
		System.setProperty("https.proxyPort", "8888");

		try {

			SSLContext sslContext = SSLContext.getInstance("TLS");

			// set up a TrustManager that trusts everything
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} }, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());

			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String arg0, SSLSession arg1) {
							return true;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
