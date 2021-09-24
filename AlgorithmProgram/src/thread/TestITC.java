package thread;

class Customer{
	
	int amount =10000;;
	
	public synchronized void withdraw(int amount) {
		System.out.println("Enter to withdraw amount...");
		if(this.amount < amount) {
			System.out.println("Please deposit amount");
			try {
				wait();
			}catch (Exception e) {
				
			}
		}
		
		this.amount = this.amount-amount;
		System.out.println("Amount withdraw successfully...");
	}
	
	public synchronized void deposit(int amount) {
		System.err.println("Enter to deposit amount...");
		this.amount = this.amount+amount;
		System.err.println("Deposit completed");
		notify();
	}
}

public class TestITC {

	public static void main(String[] args) {
		Customer customer = new Customer();
		Thread t1 = new Thread() {
			public void run() {
				customer.withdraw(15000);
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				customer.deposit(10000);
			}
		};
		
		t1.start();
		t2.start();
	}
}
