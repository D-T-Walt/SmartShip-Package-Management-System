package system;

public class Clerk extends User {
	private double salary;
	private int yearEmployed;
	
	public Clerk(int userID, String firstName, String lastName, String hashPassword, double salary, int yearEmployed) {
		super(userID, firstName, lastName, hashPassword);
		this.salary = salary;
		this.yearEmployed = yearEmployed;
	}
	
	public Clerk() {
		super();
		this.salary = 0.0;
		this.yearEmployed = 0;
	}
	
	public Clerk(Clerk clerk) {
		super(clerk);
		this.salary = clerk.salary;
		this.yearEmployed = clerk.yearEmployed;
	}
	
	public Clerk(double salary, int yearEmployed) {
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
		return "Clerk's Info \nClerk ID: " + userID + "\nFirst Name: " 
		+ firstName + "\nLast Name: " + lastName +  "\nSalary: $" + salary 
		+ "\nYear Employed: " + yearEmployed + "\n";
	}
	
	
}
