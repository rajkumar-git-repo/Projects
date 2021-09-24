package target;

import exception.AccountNotFoundException;

public class BankBusiness {

	int amount = 10000;
	String accountNumber = "sbi123";
	
	public int deposit(int amount,String accountNo) {
		if(accountNo.equals(this.accountNumber)) {
			this.amount = this.amount+amount;
			System.out.println("Deposit successfully..");
			return amount;
		}else {
			throw new AccountNotFoundException("Invalid account number");
		}
	}
}
