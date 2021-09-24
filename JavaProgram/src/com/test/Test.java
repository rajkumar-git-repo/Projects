package com.test;

public class Test {

	static public void main(String[] args) {
		reverseString("raj kumar prajapati");
		
	}
	
	private static void reverseString(String string) {
		String[] strArray = string.split(" ");
		StringBuilder builder = new StringBuilder();
		for(String str : strArray) {
			builder.append(reverse(str)+" ");
		}
		System.out.println(new String(builder).trim());
	}



	private static String reverse(String string) {
        char[] ch = string.toCharArray();
        int count=0;
		for(int i=0;i<ch.length/2;i++) {
			count++;
			char temp = ch[i];
			ch[i] = ch[string.length()-1-i];
			ch[string.length()-1-i] = temp;
		}
		System.out.println(ch.length +" "+count);
		return new String(ch);
	}
}
