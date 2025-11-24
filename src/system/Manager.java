package system;

public class Manager extends User {
	private double salary;
	private int yearEmployed;
	
	public Manager(int userID, String firstName, String lastName, String hashPassword, double salary, int yearEmployed) {
		super(userID, firstName, lastName, hashPassword);
		this.salary = salary;
		this.yearEmployed = yearEmployed;
	}
	
	public Manager() {
		super();
		this.salary = 0.0;
		this.yearEmployed = 0;
	}
	
	public Manager(Manager manager) {
		super(manager);
		this.salary = manager.salary;
		this.yearEmployed = manager.yearEmployed;
	}
	
	public Manager(double salary, int yearEmployed) {
		this.salary = salary;
		this.yearEmployed = yearEmployed;
	}

	public double getSalary() {
		return salary;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public int getYearEmployed() {
		return yearEmployed;
	}
	
	public void setYearEmployed(int yearEmployed) {
		this.yearEmployed = yearEmployed;
	}
	
	@Override
	public String toString() {
		return "Manager's Info \nManager ID: " + userID + "\nFirst Name: " 
		+ firstName + "\nLast Name: " + lastName +  "\nSalary: $" + salary 
		+ "\nYear Employed: " + yearEmployed + "\n";
	}
}
