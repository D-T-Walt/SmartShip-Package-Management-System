package system;

public class Driver extends User {
	private double salary;
	private int yearEmployed;
	
	public Driver(int userID, String firstName, String lastName, String hashPassword, double salary, int yearEmployed) {
		super(userID, firstName, lastName, hashPassword);
		this.salary = salary;
		this.yearEmployed = yearEmployed;
	}
	
	public Driver() {
		super();
		this.salary = 0.0;
		this.yearEmployed = 0;
	}
	
	public Driver(Driver driver) {
		super(driver);
		this.salary = driver.salary;
		this.yearEmployed = driver.yearEmployed;
	}
	
	public Driver(double salary, int yearEmployed) {
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
		return "Driver's Info \nDriver ID: " + userID + "\nFirst Name: " 
		+ firstName + "\nLast Name: " + lastName +  "\nSalary: $" + salary 
		+ "\nYear Employed: " + yearEmployed + "\n";
	}
}
