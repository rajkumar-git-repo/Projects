package thread;

import java.util.concurrent.locks.ReentrantLock;

class MyLockThread extends Thread{
	public void run() {
		System.out.println("Child thread...");
	}
}

public class TestLock {

	public static void main(String[] args) {
		ReentrantLock l = new ReentrantLock();
		MyLockThread thread = new MyLockThread();
		thread.start();
		System.out.println(l.isLocked());
		System.err.println(l.isHeldByCurrentThread());//false
		l.lock();
		l.lock();
		System.err.println(l.isHeldByCurrentThread());//true
		System.out.println("-----------------");
		System.out.println(l.isLocked());
		System.out.println(l.isFair());
		System.out.println(l.getHoldCount());
		System.out.println(l.getQueueLength());
		
		l.unlock();
		System.out.println("-----------------");
		System.out.println(l.getHoldCount());
		System.out.println(l.isLocked());
		l.unlock();
		System.out.println("-----------------");
		System.out.println(l.getHoldCount());
		System.out.println(l.isFair());
		System.out.println(l.isLocked());
		System.out.println(l.hasQueuedThreads());
		
	}
}
