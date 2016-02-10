package com.ensense.insense.core.analytics.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.cts.mint.analytics.model.TagSignaturesObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TagSignatureReader {

	public static void main(String args[]) {
test1();
	}

	private static void test1(){
		String tagSignatureFile="C:\\testmystuff\\tag-signatures.json";
		for(TagSignaturesObject tag: getTagSignatures(tagSignatureFile)){
			System.out.println("Tag Name: " + tag.getTagName());
			System.out.println("Tag Type: " + tag.getTagType());
			List<String> signatures = Arrays.asList(tag.getTagSignatures());
			int j = 0;
			for (String signature : signatures) {
				j++;
				System.out.println("Signature " + j + " : " + signature);
			}
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	public static List<TagSignaturesObject> getTagSignatures(String tagSignatureJsonFile) {
		List<TagSignaturesObject> webAnalyticsTags = null;
		Gson gson = new Gson();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(tagSignatureJsonFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Type listType = new TypeToken<ArrayList<TagSignaturesObject>>() {
			}.getType();

			webAnalyticsTags = gson.fromJson(br, listType);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return webAnalyticsTags;
	}
}
