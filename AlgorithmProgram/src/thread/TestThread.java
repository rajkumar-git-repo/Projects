package thread;


class MyThread extends Thread {
	public void run() {
		for (int i = 1; i <= 5; i++) {
			System.out.println("Thread child thread");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("I Got interrupted");
			}
		}
		
	}
}


public class TestThread {

	public static void main(String[] args) {
		Thread th = Thread.currentThread();
		MyThread t = new MyThread();
		t.start();
		t.interrupt();
		System.out.println("End of main thread");
	}
}
