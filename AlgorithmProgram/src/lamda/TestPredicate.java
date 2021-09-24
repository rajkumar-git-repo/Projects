 package lamda;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TestPredicate {
	

	public static void main(String[] args) {
		Consumer<String> c = (str)-> {
			System.out.println(str);
		};
		
		String[] str = {"AAAA","BBBBB","CC","DDD"};
		Predicate<String> p1 = s -> s.length() > 3;
		Predicate<String> p2 = s -> s.length() % 2 ==0;
		Stream.of(str).forEach(ss -> {
			if(p1.and(p2).test(ss)) {
				c.accept(ss);
			}
		});
		
	}
}
