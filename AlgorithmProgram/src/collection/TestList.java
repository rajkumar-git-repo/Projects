package collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class TestList {

	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		list.add(2);
		list.add(5);
		list.add(8);
		
		ListIterator listItr = list.listIterator();
		while(listItr.hasNext()) {
			System.out.println("Index:"+listItr.nextIndex());
			System.out.println("Item:"+listItr.next());
		}
		
		while(listItr.hasPrevious()) {
			System.out.println("Pre Index:"+listItr.previousIndex());
			System.out.println("Pre Item:"+listItr.previous());
		}
		
		while(listItr.hasNext()) {
			Integer item = (Integer) listItr.next();
			if(item == 5){
				listItr.set(55);
				listItr.add(22);
			}
			if(item == 2) {
				listItr.remove();
			}
		}
		
		System.out.println(list);
	}
}
