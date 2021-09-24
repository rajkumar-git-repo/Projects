package com.test;


public class Application {

	public static void main(String[] args) {
		String str1 = "00000";
		String str2 = "10000";
		int number1 = Integer.parseInt(str1);
		int number2 = Integer.parseInt(str2);
		int number = number1 + number2;
		int reverse =0;
		int carry = 0;
		int local = 0;
		int sum = 0;
		String str = number == 0 ? str1 : "";
		while(number >0) {
			int rem = number%10;
			sum = sum*10+rem;
			if(rem == 2) {
				rem = 0;
			}
			local = rem+carry ;
			if(local == 2) {
				local = 0;
				carry = 1;
			}else {
				carry = 0;
			}
			if(sum%10 == 2) {
				carry = 1;
			}
			if(reverse==0 && local ==0) {
				reverse =reverse*10+(local);
				str = str+reverse;
			}else {
				str = str+local;
				reverse =reverse*10+(local);
			}
			if(number <10 && number != 0 && carry !=0) {
				reverse = reverse*10+carry;
				str = str+carry;
			}
			number/=10;	
			System.out.println("sum:"+sum+" reverse:"+reverse +" str:"+new StringBuffer(str).reverse());
		}

		System.out.println("sum:"+sum+" reverse:"+reverse +" str:"+new StringBuffer(str).reverse());
		reverseString();
	}
	
	public static void reverseString() {
		String string = "rajkumar";
		char[] ch = string.toCharArray();
		for(int i=0;i<ch.length/2;i++) {
			char temp = ch[i];
			ch[i] = ch[ch.length-1-i];
			ch[ch.length-1-i] = temp;
			
		}
		System.out.println("string:"+new String(ch));
	}
}
