package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ArrayProgram {

	public static void main(String[] args) {
		//findMaxOccurance(new int[] {4,2,4,2,5,3,6,6,2});
		//findBalanced("{[()]}");
		//findThirdMaxPairSum(new int[]{3,6,2,7,8,19,13,5});
		//findThirdMax(new int[]{3,6,2,7,8,19,13,5});
		//findCharCount("aaddcbb");
		//printPrime();
		//printPrimeMethod();
		//System.out.println(substrings("1234"));
		//decode("121");
		//generatePrimeNumberListByStream();
		System.out.println("Value:"+bodmasString("10+5+15*2+20"));
	}
	
	private static int bodmasString(String string) {
		String[] digit = string.split("[-+*/]");
		String[] opr = string.split("[^-+*/]");
		int first = Integer.parseInt(digit[0]);
		int j=0;
		for(int i=1;i<digit.length;i++) {
			int second = Integer.parseInt(digit[i]);
			String str = opr[i+j];
			if(str.equals("+")) {
				first+=second;
			}else if(str.equals("-")) {
				first-=second;
			}else if(str.equals("*")) {
				first*=second;
			}else if(str.equals("/")) {
				first/=second;
			}else {
				//return -1;
			}
		    j = 1;
		}
		return first;
	}

	public static void generatePrimeNumberListByStream(){
	    List<Integer> primeNumbers =
	            IntStream.range(2,30)
	                    .filter(number -> IntStream.range(2,number).noneMatch(divider -> number % divider == 0))
	                    .boxed()
	                    .collect(Collectors.toList());
	    System.out.println(primeNumbers);
	}
	

	
	private static List<List<String>> substrings(String string) {
		if(string.length() == 1) {
			return Collections.singletonList(Collections.singletonList(string));
		}
		List<List<String>> output = new ArrayList<List<String>>();
		for(List<String> in : substrings(string.substring(1))) {
			List<String> list1 = new ArrayList<String>(in);
			list1.set(0, string.charAt(0)+list1.get(0));
			output.add(list1);
			List<String> list2 = new ArrayList<String>(in);
			list2.add(0, string.substring(0, 1));
			output.add(list2);
		}
		return output;
	}

	private static void decode(String string) {
		List<List<String>> output = substrings(string);
		int count=0;
		for(List<String> in : output) {
			boolean flag = true;
			for(String str : in) {
				if(Integer.parseInt(str) > 27) {
					flag =false;
				}
			}
			if(flag) {
				count++;
			}
		}
		System.out.println("Decode:"+count);
	}
	

	public static void printPrimeMethod() {
		IntStream.range(1, 51).forEach(num ->{
			if(isPrime(num) && num >1) {
				System.out.println(num);
			}
		});
	}
	
	public static boolean isPrime(int number) {
		return !IntStream.rangeClosed(2, number/2).anyMatch(i->number%i==0);
	}

	private static void printPrime() {
		IntStream.range(1,51).forEachOrdered(num-> {
			boolean flag = true;
			for(int i=2;i < num/2+1; i++) {
				if(num % i == 0) {
					flag = false;
				}
			}
			if(flag && num > 1) {
				System.out.println(num);
			}
		});;
	}

	private static void findCharCount(String string) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0;i<string.length();i++) {
			String s = String.valueOf(string.charAt(i));
			if(map.containsKey(s)) {
				map.put(s, map.get(s)+1);
			}else {
				map.put(s, 1);
			}
		}
		StringBuilder builder = new StringBuilder();
		map.entrySet().stream().forEach(entry -> {
			builder.append(entry.getKey()+""+entry.getValue());
		});
		System.out.println(builder);
	}

	private static void findThirdMax(int[] array) {
		int first = array[0],second = Integer.MIN_VALUE,third=Integer.MIN_VALUE;
		for(int i=0;i<array.length;i++) {
			if(array[i] > first) {
				third = second;
				second=first;
				first = array[i];
			}else if(array[i] > second){
				third = second;
				second=array[i];
			}else if(array[i] > third){
				third = array[i];
			}
		}
		System.out.println(first+"--"+second+"---"+third);
	}

	private static void findThirdMaxPairSum(int[] array) {
	   List<Integer> list = new ArrayList<Integer>();
	   for(int i=0;i<array.length;i++) {
		   for(int j=i+1;j<array.length;j++) {
			   int sum = array[i]+array[j];
			   if(!list.contains(sum)) {
				   list.add(sum);
			   }
		   }
	   }
	   Collections.sort(list);
	   System.out.println(list);
	   System.out.println(list.get(list.size()-3));
	}

	private static void findBalanced(String str) {
	    boolean flag = true;
		while (flag) {
			System.out.println(str);
			if (str.contains("()")) {
				str = str.replace("()", "");
			}else
			if(str.contains("{}")) {
				str = str.replace("{}", "");
			}else
			if(str.contains("[]")) {
				str = str.replace("[]", "");
			}else {
				break;
			}
		}
		System.out.println(str);
		System.out.println(str.length() == 0 ? "YES" : "N0");
	}

	private static void findMaxOccurance(int[] array) {
       HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
       for(int i=0;i<array.length;i++) {
    	   if(map.containsKey(array[i])) {
    		   map.put(array[i],map.get(array[i])+1);
    	   }else {
    		   map.put(array[i], 1);
    	   }
       }
      Entry<Integer,Integer> max = map.entrySet().stream().max(Map.Entry.<Integer,Integer>comparingByValue()).get();
	  System.out.println(max.getKey());
	}
}
