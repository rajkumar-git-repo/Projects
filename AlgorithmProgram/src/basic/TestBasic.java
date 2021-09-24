package basic;
//public, protected, private, abstract, static, final, synchronized, native , strictfp 
class Parent {
	strictfp void parent() {
		System.out.println("Parent method");
	}
}

public class TestBasic extends Parent {
	synchronized void parent() {
		System.out.println("Parent method------");
	}
	
	public void child() {
		System.out.println("Child method");
	}

	public static void main(String[] args) {
		Parent p = new Parent();
		p.parent();
		//p.child();
		System.out.println("-------------------");
		TestBasic test = new TestBasic();
		test.parent();
		test.child();
		System.out.println("-------------------");
		Parent pr  = new TestBasic();
		pr.parent();
		//pr.child();
	}

}
