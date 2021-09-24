package lamda;

import java.util.function.Function;

public class TestFunction {

	public static void main(String[] args) {
		Function<Integer, Integer> func1 = i-> i+2;
		Function<Integer, Integer> func2 = i-> i*i;
		Function<Integer, Integer> func3 = i-> i*2;
		System.out.println(func1.andThen(func2).andThen(func3).apply(3));
		System.out.println(func2.andThen(func1).andThen(func3).apply(3));
	}
}
