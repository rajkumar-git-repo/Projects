package com.raj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringPermutations {
	public static void main(String[] args) {
		// permutation("abcdefgh");
		// findLongestCommonChild("SHINCHAN","NOHARAAA");
		// System.out.println(makeAnagram("fcrxzwscanmligyxyvym",
		// "jxwtrhvujlmrpdoqbisbwhmgpmeoke"));
		// substrCount("abcbaba");
		// isValid("abbac");
		// AAAA
		// BBBBB
		// ABABABAB
		// BABABA
		// AAABBB
		//AAABBBAABB
		//AABBAABB
		//ABABABAA
		System.out.println(alternatingCharacter("AABBAABB"));
	}
	public static int alternatingCharacter(String s) {
		int count=0;
		for(int i=1;i<s.length();i++) {
			if(s.charAt(i-1) == s.charAt(i)) {
				count++;
			}
		}
		return count;
	}
	

	public static int alternatingCharacters(String s) {
		String[] str1 = s.split("AB");
		String[] str2 = s.split("BA");
		StringBuilder  builder1 = new StringBuilder();
		StringBuilder  builder2 = new StringBuilder();
		int result = 0;
		for (int i = 1; i < str1.length; i++) {
			System.out.println("--"+str1[i]);
			builder1.append(str1[i]);
		}
		for (int i = 1; i < str2.length; i++) {
			System.out.println("=="+str2[i]);
			builder2.append(str2[i]);
		}
		if(builder1.length()==0 && builder2.length()==0) {
			return s.length()-1;
		}
		int result1 = s.length()-builder1.length();
		int result2 = s.length()-builder2.length();
		result = result1 < result2 ? result1 : result2;
		if(result+1 == s.length()) {
			result =0;
		}
		return result;
	}

	public static String isValid(String str) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < str.length(); i++) {
			String s = String.valueOf(str.charAt(i));
			if (map.containsKey(s)) {
				map.put(s, map.get(s) + 1);
			} else {
				map.put(s, 1);
			}
		}
		Map<String, Integer> map1 = new HashMap<String, Integer>(map);
		Set<Integer> set = new HashSet<Integer>(map.values());
		int min = set.stream().min(Comparator.comparing(Integer::valueOf)).get();
		int max = set.stream().max(Comparator.comparing(Integer::valueOf)).get();
		Iterator<Entry<String, Integer>> itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> entry = itr.next();
			if (entry.getValue() == min) {
				map.put(entry.getKey(), entry.getValue() - 1);
				if (map.get(entry.getKey()) == 0) {
					map.remove(entry.getKey());
				}
				set = new HashSet<Integer>(map.values());
				break;
			}
			;
		}
		if (set.size() > 1) {
			Iterator<Entry<String, Integer>> itr1 = map1.entrySet().iterator();
			while (itr1.hasNext()) {
				Entry<String, Integer> entry = itr1.next();
				if (entry.getValue() == max) {
					map1.put(entry.getKey(), entry.getValue() - 1);
					if (map.get(entry.getKey()) == 0) {
						map.remove(entry.getKey());
					}
					set = new HashSet<Integer>(map1.values());
					break;
				}
				;
			}
		}
		System.out.println(map1);
		String result = set.size() > 1 ? "NO" : "YES";
		System.out.println(result);
		return result;
	}

	// A string is said to be a special string if either of two conditions is met:
	// All of the characters are the same, e.g. aaa.
	// All characters except the middle one are the same, e.g. aadaa.
	public static boolean isSpacialString(String str) {
		if (str.length() == 1) {
			return true;
		} else {
			char ch = str.charAt(0);
			for (int i = 0; i < str.length() / 2; i++) {
				if (str.charAt(i) != str.charAt(str.length() - i - 1) || ch != str.charAt(i)) {
					return false;
				}
			}
		}
		return true;
	}

	// The special palindromic substrings of asasd are {a,s,a,s,d,asa,sas} so total
	// count 7;
	private static void substrCount(String string) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < string.length(); i++) {
			for (int j = i + 1; j <= string.length(); j++) {
				if (i < j) {
					String s = string.substring(i, j);
					list.add(s);
				}

			}
		}
		System.out.println(list.stream().filter(val -> isSpacialString(val)).count());
	}

	// Given two strings, a and b, that may or may not be of the same length,
	// determine
	// the minimum number of character deletions required to make a and b anagrams.
	// Any characters can be deleted
	// from either of the strings.
	public static int makeAnagram(String a, String b) {
		Map<String, Integer> map = new HashMap<String, Integer>(50);
		int count = 0;
		for (int i = 0; i < a.length(); i++) {
			String str = String.valueOf(a.charAt(i));
			if (map.containsKey(str)) {
				map.put(str, map.get(str) + 1);
			} else {
				map.put(str, 1);
			}
		}

		for (int i = 0; i < b.length(); i++) {
			String str = String.valueOf(b.charAt(i));
			if (map.containsKey(str)) {
				if (map.get(str) != 0)
					map.put(str, map.get(str) - 1);
				else
					count++;
			} else {
				count++;
			}
		}
		System.out.println(map);
		return (int) map.values().stream().collect(Collectors.summingInt(v -> v)) + count;
	}

	public static void findLongestCommonChild(String a, String b) {
		int[][] array = new int[a.length() + 1][b.length() + 1];
		for (int i = 0; i < a.length(); i++) {
			for (int j = 0; j < b.length(); j++) {
				if (a.charAt(i) == b.charAt(j)) {
					array[i + 1][j + 1] = array[i][j] + 1;
				} else {
					array[i + 1][j + 1] = Math.max(array[i + 1][j], array[i][j + 1]);
				}
			}
		}
		for (int i = 0; i < a.length(); i++) {
			for (int j = 0; j < b.length(); j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println(array[a.length()][b.length()]);
	}

	public static void permutation(String input) {
		permutation("", input);
	}

	private static void permutation(String perm, String word) {
		if (word.isEmpty()) {
			System.out.println(perm + word);
		} else {
			for (int i = 0; i < word.length(); i++) {
				permutation(perm + word.charAt(i), word.substring(0, i) + word.substring(i + 1, word.length()));
			}
		}

	}

}
