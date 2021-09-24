package test;

import java.util.HashMap;

public class MapTest {

	public static void main(String[] args) {
		HashMap<Employee , String> map = new HashMap<Employee, String>();
		map.put(new Employee(20), "A");
		map.put(new Employee(16), "B");
		map.put(new Employee(15), "C");
		map.put(new Employee(10), "D");
		map.put(new Employee(25), "E");
		map.put(new Employee(9), "F");
		map.put(new Employee(5), "G");
		System.out.println(map);
	}
}
