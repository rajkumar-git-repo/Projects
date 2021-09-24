package collection;

import java.util.HashMap;
import java.util.IdentityHashMap;

public class TestMap {

	public static void main(String[] args) {
		HashMap<Employee, String> map = new HashMap<Employee, String>();
		Employee s1 = new Employee(10);
		Employee s2 = new Employee(10);
		Employee s3 = new Employee(25);
		map.put(s1, "AAA");
		System.out.println(map.put(s2, "BBB"));
		map.put(s3, "CCC");
		System.out.println(s1.equals(s2));
		System.out.println(s1.hashCode()+" : "+s2.hashCode()+" : "+s3.hashCode());
		System.out.println(map);
	}
}

class Employee{
	int id;
	
	public Employee(int id) {
       this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.id+"";
	}
	
	
}