package lamda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFlatMap {

	public static void main(String[] args) {
		List<List<String>> list = new ArrayList<List<String>>();
		
		list.add(Arrays.asList("A","B","C","D"));
		list.add(Arrays.asList("E","F"));
		list.add(Arrays.asList("G"));
		System.out.println(list);
		
		List<List<String>>   li = list.stream().map(item -> item).collect(Collectors.toList());
		System.out.println(li);
		
		List<String>   l = list.stream().flatMap(item -> item.stream()).collect(Collectors.toList());
		System.out.println(l);
		
		String[] s = {"aa bb cc dd","ee","fff"};
		List<String>   l1 = Stream.of(s).map(m->m).collect(Collectors.toList());
		System.out.println(l1);
		
		
		Stream<String> stream = Stream.of(s).flatMap(m -> Stream.of(m.split(" ")));
	    List<String>   l2 = (List<String>) stream.collect(Collectors.toList());
		System.out.println(l2);
	}
}
