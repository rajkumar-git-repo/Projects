package list;

import java.util.ArrayList;
import java.util.Optional;

public class TestList {

	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(5);
		list.add(8);
		list.add(1);
		list.add(3);
		System.out.println(list);
		Optional<Integer> s = list.stream().min((i1,i2)-> i1 < i2 ? 1 : (i1>i2 ? -1:0));
		if(s.isPresent()) {
			System.out.println(s.get());
		}
	}
}
