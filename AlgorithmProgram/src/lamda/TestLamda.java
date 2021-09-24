package lamda;

interface Test{

	public static void print() {
		System.out.println("Test default method");
	}
}

class child implements Test{
	
	public static void print() {
		System.out.println("Child method");
	}
}

public class TestLamda {

	public static void main(String[] args) {
		child.print();
		Test.print();
	}
}
