package lamda;

import java.util.function.Consumer;

public class TestConsumer {

	public static void main(String[] args) {
		Consumer<String> c1 = str-> System.out.println(str);
		Consumer<String> c2 = str-> System.out.println(str+"111");
		
		String[] strings = {"aaa","bbb","ccc","ddd","eee"};
		for(String s: strings) {
			c1.andThen(c2).accept(s);
		}
	}
}
