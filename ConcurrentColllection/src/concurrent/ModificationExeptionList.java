package concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModificationExeptionList extends Thread{

	static ArrayList<Integer> list = new ArrayList<Integer>();
	//static CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Updated by Child Thread");
		list.add(9);
	}
	
	public static void main(String[] args) {
		list.add(3);
		list.add(1);
		list.add(5);
		list.add(2);
		
		ModificationExeptionList thread = new ModificationExeptionList();
		thread.start();
		
		Iterator<Integer> itr = list.iterator();
		while(itr.hasNext()) {
			System.out.println("Iterate by Main Thread");
			System.out.println(itr.next());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(list);
	}
}
