package thread;

class Display{
	
	
	public synchronized void wish(String name) {
		for(int i=1;i<=5;i++) {
			System.out.println("Good Morning :"+name);
			try {
				Thread.sleep(1000);
			}catch (Exception e) {
			}
		}
	}
}

class MySyncThread extends Thread{
	String name;
	Display display;
	
	MySyncThread(Display display,String name){
		this.display=display;
		this.name = name;
	}
	
	public void run() {
		display.wish(name);
	}
}

public class TestSyncronized {
	public static void main(String[] args) {
        Display d1 = new Display();
        Display d2 = new Display();
        MySyncThread t1= new MySyncThread(d1, "RAJ");
        MySyncThread t2= new MySyncThread(d2, "KAMAL");
        t1.start();
        t2.start();
	}
}
