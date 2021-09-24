package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeTest {

	public static void main(String[] args) {
		List<Employee> list = new ArrayList<Employee>();
		list.add(new Employee(10,"aaa"));
		list.add(new Employee(20,"bbb"));
		list.add(new Employee(30,"ccc"));
		list.add(new Employee(40,"ddd"));
		
		System.out.println(list);
		
		//List<Employee> emplist = list.stream().map(e-> new Employee(e.getId(),e.getName().toUpperCase())).collect(Collectors.toList());
	    //System.out.println(emplist);
		list.stream().forEach(e->{
			e.setName(e.getName().toUpperCase());
		});
		
		System.out.println(list);
	}
}
