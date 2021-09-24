package lamda;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public class TestBiFunction {

	public static void main(String[] args) {
		ArrayList<Employee> list = new ArrayList<Employee>();
		BiFunction< String,String, Integer>  bf = (s1,s2) -> s1.length()+s2.length();
		
		System.out.println(bf.apply("raj","kumar"));
		
		BiFunction<Integer, String, Employee> b = (id,name) -> new Employee(id, name);
		list.add(b.apply(10, "AAA"));
		list.add(b.apply(20, "BBB"));
		list.add(b.apply(30, "CCC"));
		list.add(b.apply(40, "DDD"));
		list.add(b.apply(50, "EEE"));
		
		BiConsumer<Integer,String> c = (id,name) -> System.out.println("Id:"+id+", Name:"+name);
		
		for(Employee e : list) {
			c.accept(e.getId(), e.getName());
		}
	}
}
