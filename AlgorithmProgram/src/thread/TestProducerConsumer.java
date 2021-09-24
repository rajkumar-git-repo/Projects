package thread;

import java.util.LinkedList;
import java.util.Queue;

class MyQueue{
	Queue<Integer> queue;
	int queueSize;
	
	public MyQueue(int queueSize) {
		this.queue = new LinkedList<Integer>();
		this.queueSize = queueSize;
	}
	
	public void produce() {
		int value =0;
		while (true) {
			synchronized (this) {
                 while(queue.size() >= queueSize) {
                	 try {
                		//wait for consumer
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                 }
                 queue.add(value);
                 System.out.println("Produce:"+value);
                 value++;
                 notify();
                 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void consume() {
		while (true) {
			synchronized (this) {
                while(queue.size() == 0) {
                	try {
                		// wait for producer
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
                int value = queue.poll();
                System.out.println("Consume:"+value);
                notify();
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
public class TestProducerConsumer {

	public static void main(String[] args) {
		MyQueue myQueue = new MyQueue(5);
		Thread t1 = new Thread() {
			public void run() {
				myQueue.produce();
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				myQueue.consume();
			}
		};
		
		t1.start();
		t2.start();
	}
}
