package com.ensense.insense.core.utils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FireFoxUtils {

	public static DesiredCapabilities getCapabilities(String batchID){
		  //start configuration for exporting network traffic capture from firebug.net tab to a har file
			
		  File firebug = new File("C:\\projects\\PortalCommonsV8A\\mintTools\\dist\\Firefox_Addons\\firebug-1.11.4b1.xpi");
		  File netExport = new File("C:\\projects\\PortalCommonsV8A\\mintTools\\dist\\Firefox_Addons\\netExport-0.9b3.xpi");

		  FirefoxProfile profile = new FirefoxProfile();
		  try {
		  profile.addExtension(firebug);
		  profile.addExtension(netExport);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }

		  
		 	profile.setAcceptUntrustedCertificates(true);
		 	profile.setAssumeUntrustedCertificateIssuer(true);
		 	
		  //http://www-archive.mozilla.org/projects/security/components/ConfigPolicy.html
		 
		  	
			//suppress firefox print dialog
			 profile.setPreference("print.extend_native_print_dialog", false);
			
			  //suppress all kinds of alert boxes
			 // profile.setPreference("capability.policy.strict.Window.alert",  "noAccess");
			  
			 //suppress download dialog, this may not be required as jquery preventdefault wont let the documents to open
			  profile.setPreference("browser.download.panel.shown", false);
			  
			 // profile.setPreference("capability.policy.default.Window.open", "noAccess");
			 // profile.setPreference("capability.policy.strict.Window.confirm", "noAccess");
			  //profile.setPreference("capability.policy.strict.Window.prompt", "noAccess");
		  
		  profile.setPreference("app.update.enabled", false);

		  //Setting Firebug preferences
		  profile.setPreference("extensions.firebug.currentVersion", "2.0");
		  profile.setPreference("extensions.firebug.addonBarOpened", true);
		  profile.setPreference("extensions.firebug.console.enableSites", true);
		  profile.setPreference("extensions.firebug.script.enableSites", true);
		  profile.setPreference("extensions.firebug.net.enableSites", true);
		  profile.setPreference("extensions.firebug.previousPlacement", 1);
		  profile.setPreference("extensions.firebug.allPagesActivation", "on");
		  profile.setPreference("extensions.firebug.onByDefault", true);
		  profile.setPreference("extensions.firebug.defaultPanelName", "net");
		//  profile.setPreference("extensions.firebug.net.defaultPersist", "true");
		  profile.setPreference("extensions.firebug.net.LogLimit", 2000);
		  // Setting netExport preferences
		  profile.setPreference("extensions.firebug.netexport.alwaysEnableAutoExport", true);
		  profile.setPreference("extensions.firebug.netexport.autoExportToFile", true);
		  profile.setPreference("extensions.firebug.netexport.Automation", true);
		  profile.setPreference("extensions.firebug.netexport.showPreview", false);
		  profile.setPreference("extensions.firebug.netexport.compress", true);
		  profile.setPreference("extensions.firebug.netexport.defaultLogDir", "C:\\workspace\\CaptureNetworkTraffic\\"+ batchID);

		  DesiredCapabilities capabilities = new DesiredCapabilities();
		  capabilities.setBrowserName("firefox");
		  capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
		  capabilities.setCapability(FirefoxDriver.PROFILE, profile);

		//end configuration for exporting network traffic capture from firebug.net tab to a har file
		return capabilities;  
	}
}
