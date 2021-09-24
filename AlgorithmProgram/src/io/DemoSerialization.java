package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Admin{
	int admin_id = 20;
	
	public Admin() {
      System.out.println("Admin - constructor");
	}
}

class Employee  extends Admin implements Serializable{
	int id =10;
	String name = "RAJ";
	transient String password = "admin";
	
	private void writeObject(ObjectOutputStream output) throws IOException {
		output.defaultWriteObject();
		String pass = "1234"+password;
		output.writeObject(pass);
	}
	
	private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
		input.defaultReadObject();
		String epass = (String)input.readObject();
		password = epass.substring(4);
	}
}
public class DemoSerialization {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Employee employee = new Employee();
		employee.admin_id = 100;
		System.out.println("ID:"+employee.id+" NAME:"+employee.name+" PASSWORD:"+employee.password+" Admin Id:"+employee.admin_id);
		FileOutputStream fos = new FileOutputStream("abc.text");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(employee);
		
		FileInputStream fis = new FileInputStream("abc.text");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Employee emp = (Employee)ois.readObject();
		System.out.println("ID:"+emp.id+" NAME:"+emp.name+" PASSWORD:"+emp.password+" Admin Id:"+emp.admin_id);
	}
}
