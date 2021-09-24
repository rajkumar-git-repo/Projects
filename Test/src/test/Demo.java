package test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {

	public static void main(String[] args) {
		m();
		printPrime();
		HashMap<Character,Integer> map1 = new HashMap<Character, Integer>();
		HashMap<Character,Integer> map2 = new HashMap<Character, Integer>();
		String s1 = "abca";
		String s2 = "bcaa";
        char[] chArray1 = s1.toCharArray();
        char[] chArray2 = s2.toCharArray();
        for(int i=0;i<chArray1.length;i++) {
        	if(map1.containsKey(chArray1[i])) {
        		map1.put(chArray1[i],map1.get(chArray1[i])+1);
        	}else {
        		map1.put(chArray1[i], 1);
        	}
        }
        for(int i=0;i<chArray2.length;i++) {
        	if(map2.containsKey(chArray2[i])) {
        		map2.put(chArray2[i],map2.get(chArray2[i])+1);
        	}else {
        		map2.put(chArray2[i], 1);
        	}
        }   
	}
	
	public static void printPrime() {
		List<Integer> list = IntStream.range(2, 50).filter(number -> !IntStream.rangeClosed(2, number/2).anyMatch(i->(number%i==0))).boxed().collect(Collectors.toList());
	   // System.out.println(list);
	}
	
	public static void m() {
		try {
			int i = 10/0;
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

