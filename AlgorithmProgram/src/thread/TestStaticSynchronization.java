package thread;

class Bank {
	public static synchronized void printName(String name) {
		for (int i = 1; i <= 5; i++) {
			System.out.println("Good Morning :" + name);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized void printMoney(int balance) {
		for (int i = 1; i <= 5; i++) {
			System.out.println("Your balance :" + balance);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized void show() {
		for (int i = 1; i <= 5; i++) {
			System.out.println("Hello show method");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized void email(String email) {
		for (int i = 1; i <= 5; i++) {
			System.out.println("Email :" + email);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

public class TestStaticSynchronization {

	public static void main(String[] args) {

		Bank bank = new Bank();

		Thread t1 = new Thread() {
			@Override
			public void run() {
				bank.printName("RAJ");
			}
		};

		Thread t2 = new Thread() {
			public void run() {
				bank.printMoney(1000);
			}
		};

		Thread t3 = new Thread() {
			public void run() {
				bank.show();
			}
		};
		Thread t4 = new Thread() {
			public void run() {
				bank.email("TESS@TEST.COM");
			}
		};

		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}

}
