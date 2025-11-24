package system;

public class Shipment {

	private int trackingNum;
	private String recipientName;
	private float height;
	private float length;
	private float width;
	private String destinationAddr;
	private int zone;
	private String type;
	private double cost;
	private float weight;
	private String status;
	private int custID;
	
	public Shipment(int trackingNum, String recipientName, float height, float length, float width,
			String destinationAddr, int zone, String type, double cost, float weight, String status, int custID) {
		super();
		this.trackingNum = trackingNum;
		this.recipientName = recipientName;
		this.height = height;
		this.length = length;
		this.width = width;
		this.destinationAddr = destinationAddr;
		this.zone = zone;
		this.type = type;
		this.cost = cost;
		this.weight = weight;
		this.status = status;
		this.custID = custID;
	}
	
	public Shipment (int trackingNum, int zone, float weight, String status) {
		this.trackingNum = trackingNum;
		this.zone = zone;
		this.weight = weight;
		this.status = status;
	}
	
	public Shipment() {
		this.trackingNum = 0;
		this.recipientName = "";
		this.height = 0;
		this.length = 0;
		this.width = 0;
		this.destinationAddr = "";
		this.zone = 0;
		this.type = "";
		this.cost = 0;
		this.weight = 0;
		this.status = "";
		this.custID = 0;
	}
	
	public Shipment(Shipment obj) {
		this.trackingNum = obj.trackingNum;
		this.recipientName = obj.recipientName;
		this.height = obj.height;
		this.length = obj.length;
		this.width = obj.width;
		this.destinationAddr = obj.destinationAddr;
		this.zone = obj.zone;
		this.type = obj.type;
		this.cost = obj.cost;
		this.weight = obj.weight;
		this.status = obj.status;
		this.custID = obj.custID;
	}

	public int getTrackingNum() {
		return trackingNum;
	}

	public void setTrackingNum(int trackingNum) {
		this.trackingNum = trackingNum;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public String getDestinationAddr() {
		return destinationAddr;
	}

	public void setDestinationAddr(String destinationAddr) {
		this.destinationAddr = destinationAddr;
	}

	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCustID() {
		return custID;
	}

	public void setCustID(int custID) {
		this.custID = custID;
	}
	
	public void calculateCost() {
		double costPerWeight= 500.00;
		double costPerDistance= 500.00;
		double distance=0, typeCost= 0;
		
		switch(zone) {
			case 1: 
				distance = Math.floor(Math.random() * (50 - 1 + 1) + 1);				
				break;
			case 2: 
				distance = Math.floor(Math.random() * (150 - 51 + 1) + 51);				
				break;
			case 3: 
				distance = Math.floor(Math.random() * (300 - 151 + 1) + 151);				
				break;
			case 4: 
				distance = Math.floor(Math.random() * (1000 - 301 + 1) + 301);				
				break;
		}
		
		if(type.equalsIgnoreCase("Express")) {
			typeCost= 1500.00;
		} 
		else if(type.equalsIgnoreCase("Fragile")){
			typeCost= 1000.00;
		}
		
		cost= (weight* costPerWeight)+ (distance* costPerDistance)+ typeCost;

	}

	@Override
	public String toString() {
	    return "Package Info"
	            + "\nTracking Number: " + trackingNum
	            + "\nRecipient Name: " + recipientName
	            + "\nHeight: " + height
	            + "\nLength: " + length
	            + "\nWidth: " + width
	            + "\nDestination Address: " + destinationAddr
	            + "\nZone: " + zone
	            + "\nType: " + type
	            + "\nCost: " + cost
	            + "\nWeight: " + weight
	            + "\nStatus: " + status
	            + "\nCustomer ID: " + custID
	            + "\n";
	}
	
}
