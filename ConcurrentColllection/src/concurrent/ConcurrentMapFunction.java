package concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapFunction {

	public static void main(String[] args) {
		ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<Integer, String>();
		map.put(101, "A");
		map.put(102, "B");
		map.put(103, "C");
		//these 3 method related to concurrent map interface
		map.putIfAbsent(102, "D");//not add becoze value D with key 102 is not present
		map.remove(101,"F");//not remove 102 becoze value is not present
		map.replace(103, "C", "ZZZ");//it replace 103 = ZZZ
		System.out.println(map);
	}
}
