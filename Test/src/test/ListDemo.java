package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListDemo {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(50);
		list.add(20);
		list.add(30);
		list.add(40);
		list.add(10);
		list.add(60);
		list.add(20);
		list.add(60);
		list.add(20);
		
		List<Integer> i = list.stream().sorted().collect(Collectors.toList());
		//System.out.println(i);
		
		Integer total=list.stream().collect(Collectors.summingInt(x->x.intValue()));
	    System.out.println(total);
	    
	    Integer totals=list.stream().mapToInt(Integer::intValue).sum();
	    //System.out.println(totals);
	    
	    //find max occurrence of integer
	    Map<Integer, Long> map = list.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
	    Map map1 = (Map) map.entrySet().stream().sorted(Map.Entry.<Integer, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap<Integer, Long>::new));
	    //System.out.println(map1);
	    
	    List values = list.stream().filter(val -> val >= 30).map(s -> s+100).collect(Collectors.toList());
	    //System.out.println(values);
	    
	}
}
