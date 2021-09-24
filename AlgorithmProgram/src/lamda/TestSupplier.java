package lamda;

import java.util.Date;
import java.util.function.Supplier;

public class TestSupplier {

	public static void main(String[] args) {
		Supplier<Integer> sup = () -> 10;
		System.out.println(sup.get());
		
		Supplier<Date> s = () -> new Date();
		System.out.println(s.get());
	}
}
