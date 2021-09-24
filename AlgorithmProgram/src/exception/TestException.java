package exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


class Parent{
	
	public void m1() throws IOException{
      System.out.println("parent m1");
	}
}

class child extends Parent{
	
	public void m1()  {
	}
	
	public void m2() {
		try {
			super.m1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("child m2");
	}
}

public class TestException {

	public static void main(String[] args) throws IOException {
		Parent p = new Parent();
		p.m1();
		System.out.println("hello main");
	}
}
