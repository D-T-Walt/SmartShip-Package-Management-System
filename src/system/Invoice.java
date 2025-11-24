package system;

public class Invoice {
	private int invoiceID;
	private int trackingNum;
	private String method;
	private String status;
	private double discount;
	private double cost;
	private double balance;
	private int custID;

	
	public Invoice(int invoiceID, int trackingNum, String method, String status, double discount, double cost,
			double balance, int custID) {
		this.invoiceID = invoiceID;
		this.trackingNum = trackingNum;
		this.method = method;
		this.status = status;
		this.discount = discount;
		this.cost = cost;
		this.balance = balance;
		this.custID= custID;
	}
	
	public Invoice() {
		this.invoiceID = 0;
		this.trackingNum = 0;
		this.method = "card/cash";
		this.status = "";
		this.discount = 0;
		this.cost = 0;
		this.balance = 0;
		this.custID= 0;

	}
	
	public Invoice(Invoice obj) {
		this.invoiceID = obj.invoiceID;
		this.trackingNum = obj.trackingNum;
		this.method = obj.method;
		this.status = obj.status;
		this.discount = obj.discount;
		this.cost = obj.cost;
		this.balance = obj.balance;
		this.custID= obj.custID;

	}

	public int getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}

	public int getTrackingNum() {
		return trackingNum;
	}

	public void setTrackingNum(int trackingNum) {
		this.trackingNum = trackingNum;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getCustID() {
		return custID;
	}

	public void setCustID(int custID) {
		this.custID = custID;
	}

	@Override
	public String toString() {
		return "Invoice ID: " + invoiceID + "\nTracking Number: " + trackingNum+ "\nCustomer ID: " + custID + "\nPayment Method: " + method + 
				"\nDiscount Amount: " + discount + "\nCost: " + cost + "\nBalance: " + balance  + "\nStatus: " + status +  "\n";
	}
	
	
	
}
