package lamda;

import java.util.function.BiPredicate;

public class TestBiPredicate {

	public static void main(String[] args) {
		BiPredicate<Integer, Integer> pre1 = (a,b)->(a+b)%2==0;
		BiPredicate<Integer, Integer> pre2 = (a,b)-> a>b;
		System.out.println(pre1.test(10, 30));
		System.out.println(pre2.test(10, 30));
		System.out.println("------------");
		System.out.println(pre1.and(pre2).test(10, 30));
		System.out.println(pre1.or(pre2).test(10, 30));
		System.out.println("------------");
		System.out.println(pre1.negate().test(10, 30));
		System.out.println(pre2.negate().test(10, 30));
	}
}
