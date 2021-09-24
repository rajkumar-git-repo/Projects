package string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestChildString {

	public static void main(String[] args) {
		String str = "Parent";
		String word ="rent PaPa Parent aRea";
		int number = 2;
		//System.out.println(findSubString(str, number, word));
		String string = "Apple elephant Employee trace quota";
		chainString(string);
	}
	
	public static void chainString(String str) {
		String[] strs = str.toLowerCase().split(" ");
		List<String> output = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<strs.length;i++) {
			list.add(strs[i]);
		}
		output.add(strs[0]);
		for(int i=0;i<strs.length;i++) {
			String st = String.valueOf(strs[i].charAt(strs[i].length()-1));
			List<String> slist = list.stream().filter(val->val.startsWith(st)).distinct().collect(Collectors.toList());
			if(slist.size() > 0 && !strs[i].equalsIgnoreCase(slist.get(0))) {
			   output.add(slist.get(0));
			   list.remove(strs[i]);
			}
		}
		output = output.stream().distinct().collect(Collectors.toList());
		if(list.size() ==1){
		  String s = list.get(0);
		  if(output.get(0).charAt(0) == s.charAt(s.length()-1)) {
			  output.add(0, s);
		  }
		}
		if(output.size() == strs.length) {
			System.out.println("TURE");
			System.out.println(output);
		}else {
			System.out.println("FALSE");
		}
	}
	
	

	public static List<List<String>> substrings(String input) {
	    if (input.length() == 1)
	        return Collections.singletonList(Collections.singletonList(input));
	    List<List<String>> result = new ArrayList<>();
	    for (List<String> subresult : substrings(input.substring(1))) {
	        // Case: Don't split
	        List<String> l2 = new ArrayList<>(subresult);
	        l2.set(0, input.charAt(0) + l2.get(0));
	        result.add(l2);
	        // Case: Split
	        List<String> l = new ArrayList<>(subresult);
	        l.add(0, input.substring(0, 1));
	        result.add(l);
	    }
	    return result;
	}
	
	public static Set<String> findSubString(String str,int number,String word) {
		Set<String> set = new HashSet<String>();
		StringBuilder builder = new StringBuilder();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<str.length();i++) {
			for(int j=i+1;j<=str.length();j++) {
				String sub = str.substring(i,j);
				builder.append(sub+" ");
				list.add(sub);
			}
		}
		String[] wordArray = word.split(" ");
		for(int i=0;i<wordArray.length;i++) {
			List<List<String>> multiList = substrings(wordArray[i]);
			List<List<String>> sizeList =  multiList.stream().filter(val-> val.size()==number).collect(Collectors.toList());
			for(List<String> allstr : sizeList) {
				allstr = allstr.stream().distinct().collect(Collectors.toList());
				StringBuilder addStr = new StringBuilder();
				for(String string : allstr) {
					if(list.stream().anyMatch(string::equalsIgnoreCase)) {
						addStr.append(string);
					}
				}
				if(wordArray[i].equalsIgnoreCase(new String(addStr))) {
					set.add(wordArray[i]);
				}
			}
		}
		return set;
	}
}
