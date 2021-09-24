package com.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class DigitalReader {
	
	public static final String FILE_NAME = "/home/rajkumar/Desktop/important/digital.txt";

	public static void main(String[] args) throws Exception {
		File file = new File(FILE_NAME);
		StringBuilder sb = new StringBuilder();
		if(file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			    String line = br.readLine();
			    StringBuilder builder = new StringBuilder();
			    int count = 1;
			    while (line != null) {
			        if(!line.isEmpty()) {
			          builder.append(line);
			          if(count%4 != 0) {
			            builder.append(System.lineSeparator());
			          }
			        }
			        count++;
				    if(count%4 == 0) {
				        	findNumber(builder);
				        	builder = new StringBuilder();
				    }
			        line = br.readLine();
			    }
		}
	}
	
	
	public static  void findNumber( StringBuilder builder) {
		System.out.println(builder);
		String[] charactor = builder.toString().split("\n");
		String s1 = charactor[0];
		String s2 = charactor[1];
		String s3 = charactor[2];
		for(int i=0;i<s1.length();i = i+3) {
			StringBuilder build = new StringBuilder();
			build.append(s1.substring(i, i+3));
			build.append(s2.substring(i, i+3));
			build.append(s3.substring(i, i+3));
			findAnalog(build.toString());
		}
		System.out.println();
	}

	private static void findAnalog(String string) {
		System.out.print(getNumber().get(string));
	}
	
	public static HashMap<String, Integer> getNumber(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put(" _ | ||_|", 0);
		map.put("     |  |", 1);
		map.put(" _  _||_ ", 2);
		map.put(" _  _| _|", 3);
		map.put("   |_|  |", 4);
		map.put(" _ |_  _|", 5);
		map.put(" _ |_ |_|", 6);
		map.put(" _   |  |", 7);
		map.put(" _ |_||_|", 8);
		map.put(" _ |_| _|", 9);
		return map;
		
	}
}
