package system;

public class Customer extends User {
	private String address;
	private String phone;
	
	public Customer(int userID, String firstName, String lastName, String hashPassword, String address, String phone) {
		super(userID, firstName, lastName, hashPassword);
		this.address = address;
		this.phone = phone;
	}
	
	public Customer(String address, String phone) {
		this.address = address;
		this.phone = phone;
	}
	
	public Customer() {
		super();
		this.address = "address";
		this.phone = "000-000-0000";
	}
	
	public Customer(Customer obj) {
		super(obj);
		this.address = obj.address;
		this.phone = obj.phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Customer's Info \nCustomer ID:" + userID + "\nFirst Name: " + firstName
				+ "\nLast Name: " + lastName +  "\nAddress: " + address + "\nPhone: " + phone + "\n";
	}
	
	public void createAccount() {
		
	}
	
	public void requestShipment() {
		
	}
	
}
