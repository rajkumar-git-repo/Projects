package thread;

import java.util.concurrent.locks.ReentrantLock;

class MyTryLockThread extends Thread{
	static ReentrantLock reentrantLock = new ReentrantLock();
	
	String name;
	MyTryLockThread(String name){
		super(name);
	}
	
	public void run() {
		if(reentrantLock.tryLock()) {
			System.out.println("Got Lock:"+Thread.currentThread().getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			reentrantLock.unlock();
			System.out.println("Release Lock:"+Thread.currentThread().getName());
		}else {
			System.out.println("Wait for Lock:"+Thread.currentThread().getName());
		}
	}
}

public class TestTryLock {

	public static void main(String[] args) {
		MyTryLockThread t1 = new MyTryLockThread("First Thread");
		MyTryLockThread t2 = new MyTryLockThread("Second Thread");
		t1.start();
		t2.start();
	}
}
