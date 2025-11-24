package system;

public class Vehicle {
	private String plateNum;
	private String model;
	private float weightCapacity;
	private float quantityCapacity;
	private float availableWeight;
	private float availableQuantity;
	private String status;
	
	public Vehicle(String plateNum, String model, float weightCapacity, float quantityCapacity, float availableWeight,
			float availableQuantity, String status) {
		this.plateNum = plateNum;
		this.model = model;
		this.weightCapacity = weightCapacity;
		this.quantityCapacity = quantityCapacity;
		this.availableWeight = availableWeight;
		this.availableQuantity = availableQuantity;
		this.status = status;
	}
	
	public Vehicle() {
		this.plateNum = "";
		this.model = "";
		this.weightCapacity = 0;
		this.quantityCapacity = 0;
		this.availableWeight = 0;
		this.availableQuantity = 0;
		this.status = "";
	}
	
	public Vehicle(Vehicle obj) {
		this.plateNum = obj.plateNum;
		this.model = obj.model;
		this.weightCapacity = obj.weightCapacity;
		this.quantityCapacity = obj.quantityCapacity;
		this.availableWeight = obj.availableWeight;
		this.availableQuantity = obj.availableQuantity;
		this.status = obj.status;
	}

	public Vehicle(String plateNum, String model, float weightCapacity, float quantityCapacity) {
		this.plateNum = plateNum;
		this.model = model;
		this.weightCapacity = weightCapacity;
		this.quantityCapacity = quantityCapacity;
	}

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getWeightCapacity() {
		return weightCapacity;
	}

	public void setWeightCapacity(float weightCapacity) {
		this.weightCapacity = weightCapacity;
	}

	public float getQuantityCapacity() {
		return quantityCapacity;
	}

	public void setQuantityCapacity(float quantityCapacity) {
		this.quantityCapacity = quantityCapacity;
	}

	public float getAvailableWeight() {
		return availableWeight;
	}

	public void setAvailableWeight(float availableWeight) {
		this.availableWeight = availableWeight;
	}

	public float getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(float availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Vehicle \nPlate Number: " + plateNum + "\nModel: " + model + "\nWeight Capacity: " + weightCapacity
				+ "\nQuantity Capacity: " + quantityCapacity + "\nAvailable Weight: " + availableWeight
				+ "\nAvailable Quantity: " + availableQuantity + "\nStatus: " + status + "\n";
	}
	
	public float checkCapacity() {
		
		return 0;
	}

}
