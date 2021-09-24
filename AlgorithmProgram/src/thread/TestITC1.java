package thread;

class EvenOdd{
	
	static int i = 1;
	int max =10;
	
	public synchronized void printEven() {
		while(i <= max) {
			if(i%2==0) {
				System.out.println(i);
				i++;
				notify();
				System.out.println("notify...1");
			}else {
				try {
					wait();
					System.out.println("wait...1");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void printOdd() {
		while(i <= max) {
			if(i%2 != 0) {
				System.err.println(i);
				i++;
				notify();
				System.out.println("notify...2");
			}else {
				try {
					wait();
					System.out.println("wait...2");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
public class TestITC1 {

	public static void main(String[] args) {
		EvenOdd evenOdd = new EvenOdd();
		Thread t1 = new Thread() {
			public void run() {
				evenOdd.printEven();;
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				evenOdd.printOdd();;
			}
		};
		
		t1.start();
		t2.start();
		
		try {
			t2.join();
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
