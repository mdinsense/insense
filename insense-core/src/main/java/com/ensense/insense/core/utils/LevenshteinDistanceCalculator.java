package com.ensense.insense.core.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cts.mint.common.MintFileUtils;

//http://programersparadise.tk/java-library-finding-levenshtein-distance-java/
//http://blog.sodhanalibrary.com/2014/03/text-comparison-in-java_27.html#.VJUxfM8XA
public class LevenshteinDistanceCalculator {
	
	public static String LEVENSHTEIN_DISTANCE="LEVENSHTEIN_DISTANCE";
	public static String PERCENT_TEXT_MATCH="PERCENT_TEXT_MATCH";
	public static String PERCENT_WORD_MATCH="PERCENT_WORD_MATCH";
	public static String PERCENT_SENTENCE_MATCH="PERCENT_SENTENCE_MATCH";
	
	public static void main(String[] args) {
	       
	    String oldfile="C:\\Users\\manni\\Desktop\\Temp\\Viewrecenttransactions.txt";
	    String newfile="C:\\Users\\manni\\Desktop\\Temp\\Viewrecenttransactions1.txt";
	    String s0=MintFileUtils.readFromFile(oldfile);
	    
	    System.out.println("Length of file 1:"+s0.length());
	    String s1=MintFileUtils.readFromFile(newfile);
	    
	    System.out.println("Length of file 2:"+s1.length());
	    
	    try{
	    	/*System.out.println("pecentageOfMatch match start time :"+new Date());
	    	System.out.println("pecentageOfMatch match End time :"+new Date());
    	System.out.println("word match start time :"+new Date());
		System.out.println("Percent Word Match :"+pecentageOfWordMatch(s0, s1));
	    System.out.println("word match end time :"+new Date());
	    //System.out.println("LevenShteinDistance :"+LevenshteinDistance(s0, s1));
*/
	    System.out.println("text match start time :"+new Date());
	    System.out.println("Percentage Text Match :"+pecentageOfTextMatch(s0, s1));
	    System.out.println("text match end time :"+new Date());
	    System.out.println("Percentage word Match start time :"+new Date());
	    System.out.println("Percent Word Match :"+pecentageOfWordMatch(s0, s1));
	    System.out.println("Percentage Word Match End time :"+new Date());
	    System.out.println("Percentage Sentence Match start time :"+new Date());
	    System.out.println("Percentage Sentence Match :"+pecentageOfSentenceMatch(s0, s1));
	    System.out.println("Percentage Sentence Match End time :"+new Date());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   // LevenshteinDistance levenshteinDistance=new LevenshteinDistance(old_data, new_data);
	     
	    
	    }
	
	
	//feed the directory
	public static Map<String, String> calculateAllDistanceBetweenFiles(String srcFileName, String destFileName) throws Exception{
		Map<String, String> levDistMap = new HashMap<String,String>();
		String results="";
		String s0=MintFileUtils.readFromFile(srcFileName);
	    String s1=MintFileUtils.readFromFile(destFileName);
		int levDist=LevenshteinDistance(s0, s1);
		int pctTxtMatch=pecentageOfTextMatch(s0, s1);
		int pctWordMatch=pecentageOfWordMatch(s0, s1);
		int pctSentenceMatch = pecentageOfSentenceMatch(s0, s1);
		
		levDistMap.put(LevenshteinDistanceCalculator.LEVENSHTEIN_DISTANCE, Integer.toString(levDist));
		levDistMap.put(LevenshteinDistanceCalculator.PERCENT_SENTENCE_MATCH, Integer.toString(pctSentenceMatch));
		levDistMap.put(LevenshteinDistanceCalculator.PERCENT_TEXT_MATCH, Integer.toString(pctTxtMatch));
		levDistMap.put(LevenshteinDistanceCalculator.PERCENT_WORD_MATCH, Integer.toString(pctWordMatch));
		
		/*
		System.out.println("srcFile :"+ srcFileName);
		System.out.println("destFile :"+ destFileName);
	    System.out.println("LevenShteinDistance :"+levDist);
	    System.out.println("Percentage Text Match :"+pctTxtMatch);
	    System.out.println("Percent Word Match :"+pctWordMatch);
	    System.out.println("Percentage Sentence Match :"+pctSentenceMatch);
	    */
	    
	    
		return levDistMap;
	}
	
	public static int pecentageOfTextMatch(String s0, String s1) throws Exception{
        int percentage = 0;
        // Trim and remove duplicate spaces
        s0 = s0.trim().replaceAll("\\s+", " ");
        s1 = s1.trim().replaceAll("\\s+", " ");
        percentage=(int) (100 - (float) LevenshteinDistance(s0, s1) * 100 / (float) (s0.length() + s1.length()));
        return percentage;
    }

	public static int pecentageOfSentenceMatch(String s0, String s1) throws Exception{
        // Trim and Replace all . ? ! with ". " to make easy to split to sentences
        s0 = s0.trim().replaceAll("[.?!]", ". ");
        s1 = s1.trim().replaceAll("[.?!]", ". ");
        //Split by ". "
        String[] as0 = s0.split("(?i)(?<=[.])\\s+(?=[a-zA-Z])");
        String[] as1 = s1.split("(?i)(?<=[.])\\s+(?=[a-zA-Z])");
        return pecentageOfMatch(as0, as1);
    }
	
	public static int pecentageOfWordMatch(String s0, String s1) throws Exception{
        // Trim and Replace all . ? ! with spaces to make easy to split to words 
        s0 = s0.trim().replaceAll("[.?!]", " ");
        s1 = s1.trim().replaceAll("[.?!]", " ");
        //Split by space
        String[] as0 = s0.split(" ");
        String[] as1 = s1.split(" ");
        return pecentageOfMatch(as0, as1);
    }
	
	public static int pecentageOfMatch(String[] as0, String[] as1) throws Exception{
        int n = as0.length;
        Integer temp = null;
        
        // String frequency of as0 
        HashMap<String, Integer> hm0 = new HashMap<String, Integer>();
        for (int i = 0; i < n; i++) {
            temp = hm0.get(as0[i]);
            if (temp == null) {
                hm0.put(as0[i], new Integer(1));
            } else {
                hm0.put(as0[i], new Integer(temp.intValue() + 1));
            }
        }

        // String frequency of as1
        n = as1.length;
        HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
        for (int i = 0; i < n; i++) {
            temp = hm1.get(as1[i]);
            if (temp == null) {
                hm1.put(as1[i], new Integer(1));
            } else {
                hm1.put(as1[i], new Integer(temp.intValue() + 1));
            }
        }

        // Frequency difference between hm0 and hm1 to diff
        HashMap<String, Integer> diff = new HashMap<String, Integer>();
        String key;
        Integer value, value1, rval;
        Iterator it = hm0.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it
                    .next();
            key = pairs.getKey();
            value = pairs.getValue();
            value1 = hm1.get(key);
            it.remove();
            hm1.remove(key);
            if (value1 != null)
                rval = new Integer(Math.abs(value1.intValue()
                        - value.intValue()));
            else
                rval = value;
            diff.put(key, rval);
        }

        // Sum all remaining String frequencies in hm1
        int val = 0;
        it = hm1.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it
                    .next();
            val += pairs.getValue().intValue();
        }
        
        // Sum all frequencies in diff
        it = diff.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) it
                    .next();
            val += pairs.getValue().intValue();
        }

        // Calculate word match percentage
        int per = (int) ((((float) val * 100)) / ((float) (as0.length + as1.length)));
        per = 100 - per;
        return per;
    }

	
	public static int LevenshteinDistance(String s0, String s1) throws Exception{

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete),
                        cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

}
