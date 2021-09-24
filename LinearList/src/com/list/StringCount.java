package com.list;

import java.util.HashMap;

public class StringCount {

	public static void main(String[] args) {
		String str = "aabcdss";
		
		HashMap map = new HashMap();
		
		char[] ch = str.toCharArray();
		for(int i =0; i<ch.length;i++) {
			if(!map.containsKey(ch[i])) {
				map.put(ch[i], ch[i]);
			}else {
				map.put(ch[i], ch[i]);
				map.put(ch[i],'1');
			}
		}
		
		System.out.println("Map:"+map);
	}
}
