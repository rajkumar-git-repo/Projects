package collection;

import java.util.HashMap;

public class MyHashMap {

	public static void main(String[] args) {
		HashMap<Temp, String> map = new HashMap<Temp, String>();
		map.put(new Temp(5), "A");
		map.put(new Temp(2), "B");
		map.put(new Temp(6), "C");
		map.put(new Temp(15), "D");
		map.put(new Temp(23), "E");
		map.put(new Temp(16), "F");
		map.put(new Temp(7), "G");
		System.out.println(map);
	}
	
}

class Temp{
	int i;
	Temp(int i){
		this.i = i;
	}
	
	public int hashCode() {
		return i;
	}

	@Override
	public String toString() {
		return i+"";
	}
	
}

