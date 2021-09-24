package thread;

import java.util.concurrent.locks.ReentrantLock;

class Wish{
	ReentrantLock reentrantLock = new ReentrantLock(); 
	
	public void wish(String name) {
		reentrantLock.lock();
		for(int i=1;i<=5;i++) {
			System.out.println("Good Morning :"+name);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Hold Count:"+reentrantLock.getHoldCount());
		System.out.println("Waiting Thread count:"+reentrantLock.getQueueLength());
		reentrantLock.unlock();
	}
}

class MyThreadLock extends Thread{
	Wish wish;
	String threadName;
	MyThreadLock(Wish wish, String threadName){
		super(threadName);
		this.wish = wish;
		this.threadName = threadName;
	}
	
	public void run() {
		wish.wish(threadName);
	}
	
}

public class TestThreadLock {

	public static void main(String[] args) {
		Wish wish = new Wish();
		MyThreadLock t1 = new MyThreadLock(wish, "First Thread");
		MyThreadLock t2 = new MyThreadLock(wish, "Second Thread");
		MyThreadLock t3 = new MyThreadLock(wish, "Third Thread");
		MyThreadLock t4 = new MyThreadLock(wish, "Third Thread");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
