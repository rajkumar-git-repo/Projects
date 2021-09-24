package thread;


class MyTestThread extends Thread{
	
	MyTestThread(ThreadGroup threadGroup ,String name){
		super(threadGroup, name);
	}
	
	public void run() {
		System.out.println("Child Thread");
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			
		}
	}
}
public class ThreadGroupDemo {

	public static void main(String[] args) {
		ThreadGroup pg = new ThreadGroup("Parent Group");
		ThreadGroup cg = new ThreadGroup(pg,"Child Group");
		
		MyTestThread  t1 = new MyTestThread(pg, "Child Thread1");
		MyTestThread  t2 = new MyTestThread(pg, "Child Thread2");
		
		t1.start();
		t2.start();
		pg.list();
		
		System.out.println("Active Count: "+pg.activeCount());
		System.out.println("Active Group Count: "+pg.activeGroupCount());
		
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Active Count: "+pg.activeCount());
		System.out.println("Active Group Count: "+pg.activeGroupCount());
		pg.list();
	}
	
}
