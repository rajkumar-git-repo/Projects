package deep.shalow.copy;

public class Employee implements Cloneable{

	private int id;
	private String name;
	private Department department;
	
	public Employee(int id, String name, Department department) {
		super();
		this.id = id;
		this.name = name;
		this.department = department;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Employee employee = (Employee)super.clone();
		employee.department = (Department)department.clone();
		return employee;
	}
	
}
