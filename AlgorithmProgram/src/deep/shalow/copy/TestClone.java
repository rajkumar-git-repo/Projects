package deep.shalow.copy;

public class TestClone {

	public static void main(String[] args) throws CloneNotSupportedException {
		Department dept = new Department(11, "HR");
		Employee emp1 = new Employee(10, "abc", dept);
		
		Employee emp2 = (Employee) emp1.clone();
		
		Department d1  = emp1.getDepartment();
		
		Department d2  = emp2.getDepartment();
		d2.setDept_name("QA");
		emp2.setDepartment(d2);   
		                             //shalow,deep
		System.out.println(d1 == d2);//true,false
		System.out.println(emp1 == emp2);//false,false
		
		System.out.println(emp1.getDepartment().getDept_name());//QA,HR
		System.out.println(emp2.getDepartment().getDept_name());//QA,QA
		
		emp2.setName("Aman");
		System.out.println(emp1.getName());//abc,abc
		System.out.println(emp2.getName());//aman,aman
	}
}
