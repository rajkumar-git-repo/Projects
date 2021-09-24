package thread;

class Multithread 
{ 
    private int i = 0; 
    public synchronized void increment() 
    { 
        i++; 
    } 
  
    public synchronized int getValue() 
    { 
        return i; 
    } 
}

public class Test {
   
	public static void main(String[] args) {
		Multithread t = new Multithread();
		Thread t1 = new Thread() {
			public void run() {
				t.increment();
				System.out.println("T1 :"+t.getValue());
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				t.increment();
				System.out.println("T2 :"+t.getValue());
			}
		};
		
		Thread t3 = new Thread() {
			public void run() {
				t.increment();
				System.out.println("T3 :"+t.getValue());
			}
		};
		
		t1.start();
		t2.start();
		t3.start();
	}
}
