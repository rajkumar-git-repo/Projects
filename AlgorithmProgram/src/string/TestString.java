package string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TestString {

	public static void main(String[] args) {
		String str = "google";
		//deleteDuplicate(str);
		//smallestSubString(str);
		//longestCommonSubString("rajkumar", "rajshashi");
		//getLongestCommonSubstring("abcdx", "xycdyx");
	    getLongestCommonSubsequence("SHINCHAN","NOHARAAA");
	}
	
	//Input:google ,output:le explaination:g and o both duplicate
	public static void deleteDuplicate(String s) {
		Set<Character> hs = new LinkedHashSet<Character>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			hs.add(s.charAt(i));
		}
		for (Character character : hs) {
			sb.append(character);
		}
		hs.clear();
		System.out.println(sb.toString());
	}
	
	//count longest common subsequence
	public static void getLongestCommonSubsequence(String a, String b){
		int m = a.length();
		int n = b.length();
		int[][] dp = new int[m+1][n+1];
	 
		for(int i=0; i<=m; i++){
			for(int j=0; j<=n; j++){
				if(i==0 || j==0){
					dp[i][j]=0;
				}else if(a.charAt(i-1)==b.charAt(j-1)){
					dp[i][j] = 1 + dp[i-1][j-1];
				}else{
					dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
				}
			}
		}
	 
		System.out.println("Count:"+dp[m][n]);
		for(int i=0;i<m+1;i++) {
			for(int j=0;j<n+1;j++) {
				System.out.print(" "+dp[i][j]);
			}
			System.out.println();
		}
	}
	
	//count longest common substring
	//https://dzone.com/articles/longest-common-substring
	//source-https://www.programcreek.com/2015/04/longest-common-substring-java/
	public static void getLongestCommonSubstring(String a, String b){
		int m = a.length();
		int n = b.length();
		int max = 0;
		int[][] dp = new int[m][n];
		for(int i=0; i<m; i++){
			for(int j=0; j<n; j++){
				if(a.charAt(i) == b.charAt(j)){
					if(i==0 || j==0){
						dp[i][j]=1;
					}else{
						dp[i][j] = dp[i-1][j-1]+1;
					}
	 
					if(max < dp[i][j])
						max = dp[i][j];
				}
	 
			}
		}
		System.out.println("Count of longest common subString:"+max);
		
		for(int i=0;i<m;i++) {
			for(int j=0;j<n;j++) {
				System.out.print(" "+dp[i][j]);
			}
			System.out.println();
		}
	}
	
	//find longest common substring length
	private static void longestCommonSubString(String str1,String str2) {
		ArrayList<String> list = new ArrayList<String>();
		String maxStr = "";
		String minStr = str1;
		int length1 = str1.length();
		for(int i=0;i<length1;i++) {
			for(int j=i+1;j<=length1;j++) {
				String sub = str1.substring(i,j);
				list.add(sub);
				if(str2.contains(sub)) {
					if(sub.length() > maxStr.length()) {
						maxStr = sub;
					}else {
						minStr = sub;
					}
				}
			}
		}
		System.out.println("List of possible substring:"+list);
		System.out.println("Longest common substring:"+maxStr);
		System.out.println("Smallest common substring:"+minStr);
	}
	
	
	
	//count number of unique character in string
	private static int countMaxUniqueCharInString(String str){
		HashSet<String> set = new HashSet<String>();
		for(int i=0;i<str.length();i++) {
			set.add(""+str.charAt(i));
		}
		return set.size();
	}

	//find smallest substring with maximum number of distinct character
	private static void smallestSubString(String str) {
		int length = str.length();
		int max = countMaxUniqueCharInString(str);
		int min = max;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				String sub = null;
				if (i < j)
					sub = str.substring(i, j);
				else
					sub = str.substring(j, i);
                int sub_length = sub.length();
				int sum_max = countMaxUniqueCharInString(sub);

				if (sub_length < min && max == sum_max) {
					min = sub_length;
				}
			}
		}
		System.out.println("Smallest SubString with maximum number of distinct character:" + min);
	}
	
}
