package com.list;

public class Test {

	public static int lastModified(int input1 ,int[] input2) {
		String str = "";
		boolean flag = false;
		for(int i = 0;i<input2.length;i++) {
			if(input2[i] != 9) {
				flag = true; 
			}
			str = str+input2[i];
		}
		if (flag) {
			int i = Integer.parseInt(str);
			i = i + 1;
			String s = "" + i;
			char[] ch1 = str.toCharArray();
			char[] ch2 = s.toCharArray();
			for (int j = 0; j < ch1.length; j++) {
				if (ch1[j] != ch2[j]) {
					return j + 1;
				}
			}
			return 0;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		int input1 = 5;
		int[] input2 = {5,4,3,5,2};
		System.out.println("i :"+lastModified(input1,input2));
	}
}
