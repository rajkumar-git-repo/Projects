package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class List8Demo {

	public static void main(String[] args) {
		List<Employee> list = new ArrayList<Employee>();
		list.add(new Employee(11, "hhh"));
		list.add(new Employee(10, "ppp"));
		list.add(new Employee(13, "qqq"));
		list.add(new Employee(12, "aaa"));
		list.add(new Employee(11, "kkk"));
		list.add(new Employee(13, "fff"));
		System.out.println(list);
		
		Comparator<Employee> c = (e1,e2)-> e1.getId() < e2.getId() ? -1  : e1.getId() > e2.getId() ? 1 : e1.getName().compareTo(e2.getName());
		
		List<Employee> elist = list.stream().sorted(c).collect(Collectors.toList());
		System.out.println(elist);
		
		Map<Integer, List<Employee>> mlist = list.stream().collect(Collectors.groupingBy(Employee::getId));
		System.out.println(mlist);
		
	 }
}
