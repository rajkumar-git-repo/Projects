package test;

class CustDemo extends Thread{

	static int custId=0;
	
	static ThreadLocal local = new ThreadLocal() {
		protected Object initialValue() {
			return ++custId;
		};
	};
	
	CustDemo(String name){
		super(name);
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName()+"----CustId:"+local.get());
	}
}

public class CustThreadDemo {

	public static void main(String[] args) {
		CustDemo c1 = new CustDemo("A");
		CustDemo c2 = new CustDemo("B");
		CustDemo c3 = new CustDemo("C");
		CustDemo c4 = new CustDemo("D");
		c1.start();
		c2.start();
		c3.start();
		c4.start();
	}
}
