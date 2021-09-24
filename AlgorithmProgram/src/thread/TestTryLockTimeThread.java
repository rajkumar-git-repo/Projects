package thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class MyTryLockTimeThread extends Thread{
	static ReentrantLock reentrantLock = new ReentrantLock();
	
	MyTryLockTimeThread(String name){
		super(name);
	}
	
	public void run() {
		do {
			try {
				if(reentrantLock.tryLock(5000,TimeUnit.MILLISECONDS)) {
					System.out.println("GETTING LOCK:"+Thread.currentThread().getName());
					Thread.sleep(30000);
					System.out.println("WAITING THREAD :"+reentrantLock.getQueueLength());
					reentrantLock.unlock();
					System.out.println("RELEASE LOCK:"+Thread.currentThread().getName());
					break;
				}else {
					System.out.println("WAITING LOCK:"+Thread.currentThread().getName());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(true);
		
	}
	
}

public class TestTryLockTimeThread {

	public static void main(String[] args) {
		MyTryLockTimeThread t1 = new MyTryLockTimeThread("First Thread");
		MyTryLockTimeThread t2 = new MyTryLockTimeThread("Second Thread");
		t1.start();
		t2.start();
		
	}
}
/*Output:
 *  GETTING LOCK:Second Thread
	WAITING LOCK:First Thread
	WAITING LOCK:First Thread
	WAITING LOCK:First Thread
	WAITING LOCK:First Thread
	WAITING LOCK:First Thread
	RELEASE LOCK:Second Thread
	GETTING LOCK:First Thread
 */
