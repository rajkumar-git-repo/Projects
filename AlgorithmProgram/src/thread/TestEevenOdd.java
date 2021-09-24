package thread;

class Even{
	
	public synchronized void even(int size) throws InterruptedException {
		for(int i=1;i<=size;i++) {
			if(i%2==0) {
				System.out.println(i);
				Thread.sleep(1000);
			}
		}
	}
	
	public synchronized void odd(int size) throws InterruptedException {
		for(int i=1;i<=size;i++) {
			if(i%2 != 0) {
				System.out.println(i);
				Thread.sleep(1000);
			}
		}
	}
}


class MyThread1 extends Thread{
	Even even;
	int size;
	
	MyThread1(Even even,int size){
		this.even = even;
		this.size = size;
	}
	
	public void run() {
		try {
			even.even(size);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class MyThread2 extends Thread{
	Even even;
	int size;
	
	MyThread2(Even even,int size){
		this.even = even;
		this.size = size;
	}
	
	public void run() {
		try {
			even.odd(size);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class TestEevenOdd {

	public static void main(String[] args) {
		Even even1 = new Even();
		Even even2 = new Even();
		MyThread1 t1 = new MyThread1(even1, 10);
		MyThread2 t2 = new MyThread2(even2, 10);
		t1.start();
		t2.start();
	}
}
