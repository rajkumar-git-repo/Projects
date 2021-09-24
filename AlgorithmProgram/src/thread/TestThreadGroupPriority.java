package thread;

public class TestThreadGroupPriority {

	public static void main(String[] args) {
		ThreadGroup tg = new ThreadGroup("First Group");
		Thread t1 = new Thread(tg,"Thread1");
		Thread t2 = new Thread(tg,"Thread2");
		System.out.println("Thread Group Priority:"+tg.getMaxPriority());//10
		System.out.println("Thread1 Priority:"+t1.getPriority());//5
		System.out.println("Thread2 Priority:"+t2.getPriority());//5
		tg.setMaxPriority(3);
		Thread t3 = new Thread(tg,"Thread3");
		System.out.println("Thread3 Priority:"+t3.getPriority());//3
		
		
		System.out.println("Thread Group Priority:"+tg.getMaxPriority());//3
		System.out.println("Thread1 Priority:"+t1.getPriority());//5
		System.out.println("Thread2 Priority:"+t2.getPriority());//5
	}
}
