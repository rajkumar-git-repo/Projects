package collection;

import java.util.HashMap;
import java.util.IdentityHashMap;

public class CompareList {

	public static void main(String[] args) {
		model.Employee i1 = new model.Employee(10,"aaa");
		model.Employee i2 = new model.Employee(10,"aaa");
		HashMap<model.Employee, String> map = new HashMap<model.Employee, String>();
		
		//Integer i1 = new Integer(10);
		//Integer i2 = new Integer(20);
		//HashMap<Integer, String> map = new HashMap<Integer, String>();
		//IdentityHashMap<model.Employee, String> map = new IdentityHashMap<model.Employee, String>();
		
		System.out.println(i1.equals(i2)+"----"+(i1==i2));
		System.out.println(i1.hashCode()+"----"+i2.hashCode());
		map.put(i1, "AAA");
		map.put(i2, "BBB");
		System.out.println(map);
	}
}
