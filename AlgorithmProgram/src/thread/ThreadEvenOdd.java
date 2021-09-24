package thread;

public class ThreadEvenOdd {

	private int size;
	private int start;
	
	ThreadEvenOdd(int start,int size){
		this.start = start;
		this.size=size;
	}
	
	public void even() {
		while(start <= size) {
			synchronized (this) {
				while(start % 2 != 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Even:"+start);
				start++;
				notify();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void odd() {
		while(start < size) {
			synchronized (this) {
				while(start % 2 == 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Odd:"+start);
				start++;
				notify();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ThreadEvenOdd evenOdd = new ThreadEvenOdd(1,10);
		new Thread() {
			public void run() {
				evenOdd.even();
			}
		}.start();
		
		new Thread() {
			public void run() {
				evenOdd.odd();
			}
		}.start();
	}
}
