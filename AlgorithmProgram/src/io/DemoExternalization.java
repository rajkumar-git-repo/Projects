package io;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

class TestExternal implements Externalizable{
	int id;
	String name;
	String password;
	
	public TestExternal() {
		System.out.println("No-arg cunstructor");
	}
	
	public TestExternal(int id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(id);
		out.writeObject(name);
		out.writeObject(password);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		 id = in.readInt();
		 name = (String) in.readObject();
		 password = (String) in.readObject();
	}
}

public class DemoExternalization {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		TestExternal test = new TestExternal(10,"abc","admin");
		System.out.println("Id:"+test.id+" Name:"+test.name+" password:"+test.password);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("abc.ser"));
		oos.writeObject(test);
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("abc.ser"));
		TestExternal testExternal = (TestExternal) ois.readObject();
		
		System.out.println("Id:"+testExternal.id+" Name:"+testExternal.name+" password:"+testExternal.password);
		
	}
}
