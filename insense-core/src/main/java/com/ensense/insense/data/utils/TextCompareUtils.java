package com.ensense.insense.data.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.LinkedList;

public class TextCompareUtils {
	
	public static void main(String[] args){
		String oldfile="C:\\Users\\manni\\Desktop\\Temp\\Viewrecenttransactions.txt";
	    String newfile="C:\\Users\\manni\\Desktop\\Temp\\Viewrecenttransactions1.txt";
	    
	    String s0=MintFileUtils.readFromFile(oldfile);
	    
	    System.out.println("Length of file 1:"+s0.length());
	    String s1=MintFileUtils.readFromFile(newfile);
	    
	    System.out.println("Length of file 2:"+s1.length());
	    
	    System.out.println("Before start :"+new Date());
	    LinkedList<DiffMatchPatch.Diff> diffs = getDifferencesBetweenTextFile( s0, s1);
	    System.out.println("End :"+new Date());
	    
	    System.out.println("diff count :"+diffs.size());
	    double differenceLength = 0;
	    for (DiffMatchPatch.Diff aDiff : diffs) {
	        String text = aDiff.text;
	        switch (aDiff.operation) {
	        case INSERT:
	        		System.out.println("Add count :"+text.length());
	        		differenceLength += text.length();
	          break;
	        case DELETE:
	        	System.out.println("Delete count :"+text.length());
	        	differenceLength += text.length();
	          break;
	        case EQUAL:

	          break;
	        }
	      }
	    System.out.println("differenceLength :"+differenceLength);
	    System.out.println("percentage Match :"+ (1.0 - (differenceLength/s0.length())) * 100 );
	}
	public static LinkedList<DiffMatchPatch.Diff> getDifferencesBetweenTextFile(String text1, String text2) {
		DiffMatchPatch dmp = new DiffMatchPatch();
		dmp.Diff_Timeout = 0;
		LinkedList<DiffMatchPatch.Diff> difference = new LinkedList<DiffMatchPatch.Diff>();

		try {
			difference = dmp.diff_main(text2, text1, false);
			dmp.diff_cleanupSemantic(difference);

		} catch(Exception e){
			
		}
		return difference;
	}
	
	public static boolean createTextDiffFile(LinkedList<DiffMatchPatch.Diff> difference, String differenceFilePath){
		DiffMatchPatch dmp = new DiffMatchPatch();
		try{
			if (CollectionUtils.isNotEmpty(difference)) {
				String diffhtml = "";
				diffhtml = dmp.diff_prettyHtml(difference);
				//MintFileUtils.deleteFile(differenceFileDirectory + "wholePageDiff.html");
				MintFileUtils.writeHtmlContentToFile(diffhtml, differenceFilePath, false);
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
}
