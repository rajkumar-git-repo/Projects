package thread;


class Block{
	
	public void print(String name) {
		/*
		 * for(int i=1;i<=5;i++) { System.out.println(name+": Outside block"); }
		 */
		synchronized (this) {
			for(int i=1;i<=5;i++) {
				System.out.println("Inside block :"+name);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

public class TestSynchronizedBlock {

	public static void main(String[] args) {
		Block block = new Block();
		Thread t1 = new Thread() {
			public void run() {
				block.print("RAJ");
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				block.print("AMIT");
			}
		};
		t1.start();
		t2.start();
	}
}
