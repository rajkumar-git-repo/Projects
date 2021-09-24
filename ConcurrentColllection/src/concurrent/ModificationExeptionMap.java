package concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModificationExeptionMap extends Thread{

	//static ConcurrentHashMap<Integer, String> map  = new ConcurrentHashMap<Integer, String>();
	static HashMap<Integer, String> map  = new HashMap<Integer, String>();
	
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Update by Child Thread");
		map.put(103, "C");
	}
	
	public static void main(String[] args) {
		map.put(101, "A");
		map.put(102, "B");
	    Set<Integer> set = map.keySet();
	    Iterator<Integer> itr = set.iterator();
	    
	    ModificationExeptionMap thread = new ModificationExeptionMap();
	    thread.start();
	    
	    while(itr.hasNext()) {
	    	Integer i = itr.next();
	    	System.out.println("Iterated By Main Thread");
	    	System.out.println(i+"="+map.get(i));
	    	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	    
	    System.out.println(map);
	}
 }
