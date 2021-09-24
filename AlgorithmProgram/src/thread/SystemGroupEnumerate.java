package thread;

public class SystemGroupEnumerate {

	public static void main(String[] args) {
		ThreadGroup system = Thread.currentThread().getThreadGroup().getParent();
		Thread[] list = new Thread[system.activeCount()];
		system.enumerate(list);
		
		for(Thread t : list) {
			System.out.println(t.getName()+": "+t.isDaemon());
		}
	}
}
