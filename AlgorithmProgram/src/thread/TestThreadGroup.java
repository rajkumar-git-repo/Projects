package thread;

public class TestThreadGroup {

	public static void main(String[] args) {
		String parantGroupName  = Thread.currentThread().getThreadGroup().getParent().getName();
		System.out.println("Parent thread group :"+parantGroupName);//system
		String currentGroupName = Thread.currentThread().getThreadGroup().getName();
		System.out.println("Current thread group :"+currentGroupName);//main
		
		
		
		
		ThreadGroup tg1 = new ThreadGroup("First Group");
		System.out.println(tg1.getParent().getName());//main
		
		ThreadGroup tg2 = new ThreadGroup(tg1, "Second Group");
		System.out.println(tg2.getParent().getName());//First Group
		
		System.out.println(tg2.getName());
		//for above configuration hierarchy
		//system ------> main ------> First Group -----> Second Group
	}
}
