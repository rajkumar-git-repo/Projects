package lamda;

import java.util.function.BiConsumer;

public class TestBiConsumer {

	public static void main(String[] args) {
		BiConsumer<Integer, Integer> bc = (a,b) -> System.out.println(a*b);
		bc.accept(10, 20);
		
		BiConsumer<String, String> bc1 = (a,b) -> {
			a=a+b;
			System.out.println(a);
		};
		bc1.accept("aa", "AA");
	}
}
