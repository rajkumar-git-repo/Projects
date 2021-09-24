package thread;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
	
	public Queue<Integer> queue;
	int size ;
	
	ProducerConsumer(int size){
		this.queue = new LinkedList<Integer>();
		this.size=size;
	}
	
	public void produce() {
		int value=1;
		while(true) {
			synchronized (this) {
				if (queue.size() >= size) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				queue.add(value);
				System.out.println("produce:" + value);
				value++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				notify();
			}
		}
	}
	
	public void consume() {
		while (true) {
			synchronized (this) {
				if (queue.size() == 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int value = queue.poll();
				System.out.println("consume:" + value);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				notify();
			}
		}
	}

	public static void main(String[] args) {
		ProducerConsumer pc = new ProducerConsumer(5);
		Thread t1 = new Thread() {
			public void run() {
				pc.produce();
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				pc.consume();
			}
		};
		t1.start();
		t2.start();
	}
}
