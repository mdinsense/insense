package com.ensense.insense.core.analytics.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cts.mint.analytics.model.SupportedTagData;
import com.cts.mint.analytics.model.TagSignaturesObject;

public class TagSignatureUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
test1();
//test2();
	}
	
private static void test1(){
	
	//String requestUrl="https://smetrics.tiaa-cref.org/b/ss/tiaamain/1/H.26/s85336798259637?AQB=1&ndh=1&t=10%2F2%2F2015%2016%3A38%3A55%202%20240&fid=6078EB46B49B9230-3F4B7E6F9DE23064&ns=tiaacref&pageName=Public%3AHome&g=https%3A%2F%2Fwww.tiaa-cref.org%2Fpublic%2Findex.html&events=event9&c33=D%3DpageName&l1=Carousel%3ASlot1%3AThe%20Power%20of%20One%20TIAA-CREF%20IRA&pe=lnk_o&pev2=Carousel%3ASlot1%3AThePowerofOneTIAA-CREFIRA&s=1920x1080&c=24&j=1.8.5&v=Y&k=Y&bw=1920&bh=223&p=Shockwave%20Flash%3BGoogle%20Update%3BActiveTouch%20General%20Plugin%20Container%3BAdobe%20Acrobat%3BQuickTime%20Plug-in%207.7.6%3BJava%28TM%29%20Platform%20SE%207%20U67%3BJava%20Deployment%20Toolkit%207.0.670.1%3BShockwave%20for%20Director%3BSilverlight%20Plug-In%3BCitrix%20Online%20Web%20Deployment%20Plugin%201.0.0.104%3BNVIDIA%203D%20VISION%3BNVIDIA%203D%20Vision%3BMicrosoft%20Office%202010%3B&AQE=1";
	
	String requestUrl ="https://connect.facebook.net/en_US/fbds.js?_=1432824071666";
	String tagSignatureJsonFile="C:\\Projects\\PortalCommonsV8A\\mintToolsV2\\mintCommonsJAR\\resources\\tag-signatures.json";
	List<TagSignaturesObject> tagSignatures = TagSignatureReader.getTagSignatures(tagSignatureJsonFile);
	
	SupportedTagData std = getSupportedTagDetails(requestUrl, tagSignatures);

	System.out.println("Tag Name : " +std.getTagObject().getTagName());
	System.out.println("Tag Supported :" +std.isSupportedTag());
	}

private static void test2(){
	String ensightenCodeFile="C:\\testmystuff\\ensighten-code.txt";
	String tagSignatureJsonFile="C:\\testmystuff\\tag-signatures.json";
	BufferedReader br=null;
	StringBuffer sb = new StringBuffer();
	try {
		 br = new BufferedReader(new FileReader(ensightenCodeFile));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
String line;
	 try {
		 int i=0;
		while ((line = br.readLine()) != null) {
			i++;
		//	 System.out.println("line :" + i + " :" + line);
			 sb.append(line);
		 }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		br.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Iterator it =getTagsDeployedByEnsighten(sb.toString(),tagSignatureJsonFile).iterator();
	while(it.hasNext()){
		System.out.println("Deployed tag :" +it.next());
	}
}

public static SupportedTagData getSupportedTagDetails(String requestUrl, List<TagSignaturesObject> tagSignatures){
//String tagSignatureJsonFile="C:\\testmystuff\\tag-signatures.json";

SupportedTagData supportedTagData=null;

boolean supportedTagFound=false;
for(TagSignaturesObject tagObject: tagSignatures){
	//loop through and match the signature with the requesturl. if the signature matches return the SupportedTagData
	List<String> signatures = Arrays.asList(tagObject.getTagSignatures());

	for(String signature: signatures){
		Pattern pattern = Pattern.compile(signature,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(requestUrl);
		if(matcher.find()){
			supportedTagFound=true;
			supportedTagData=new SupportedTagData();
			supportedTagData.setSupportedTag(true);
			supportedTagData.setTagObject(tagObject);
		}
	}
	if(supportedTagFound){
		//no need to loop as tag is found. this will cut the time for finding tags
		break;
	}
}

return supportedTagData;
}

public static Set<String> getTagsDeployedByEnsighten(String ensightenPageRulesCode, String tagSignatureJsonFile){
	//String tagSignatureJsonFile="C:\\testmystuff\\tag-signatures.json";
	Set<String> deployedTagsList= new LinkedHashSet<String>();
	
	List<TagSignaturesObject> tagSignatures = TagSignatureReader.getTagSignatures(tagSignatureJsonFile);
	//SupportedTagData supportedTagData=null;
	for(TagSignaturesObject tagObject: tagSignatures){
		//loop through and match the signature with the requesturl. if the signature matches return the SupportedTagData
		List<String> signatures = Arrays.asList(tagObject.getTagSignatures());
		for(String signature: signatures){
			
			Pattern pattern = Pattern.compile(signature,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(ensightenPageRulesCode);
			if(matcher.find()){
				deployedTagsList.add(tagObject.getTagName());
				//System.out.println("group :" +matcher.group());
				//int start = matcher.start(0);
				//int end = matcher.end(0);
				//System.out.println("Deployent Found :" + tagObject.getTagName());
				//System.out.println("text matched  :" +ensightenPageRules.substring(start, end));
				//supportedTagData=new SupportedTagData();
				//supportedTagData.setSupportedTag(true);
				//supportedTagData.setTagObject(tagObject);
				//matcher.reset();
			}
		}
	}

	return deployedTagsList;
	}

}
