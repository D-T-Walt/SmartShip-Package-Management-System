package system;

import java.time.LocalDate;
import java.time.LocalTime;

public class Assignment {
	private int assignmentID;
	private String plateNumber;
	private int trackingNum;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private int overseerID;
	private int driverID;
	
	public Assignment(int assignmentID, String plateNumber, int trackingNum, LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime, int overseerID, int driverID) {
		this.assignmentID = assignmentID;
		this.plateNumber = plateNumber;
		this.trackingNum = trackingNum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.overseerID = overseerID;
		this.driverID = driverID;
	}
	
	public Assignment() {
		this.assignmentID = 0;
		this.plateNumber = "";
		this.trackingNum = 0;
		this.startDate = LocalDate.now();
		this.endDate = LocalDate.now();
		this.startTime = LocalTime.now();
		this.endTime = LocalTime.now();
		this.overseerID = 0;
		this.driverID = 0;
	}
	
	public Assignment(Assignment assignment) {
		this.assignmentID = assignment.assignmentID;
		this.plateNumber = assignment.plateNumber;
		this.trackingNum = assignment.trackingNum;
		this.startDate = assignment.startDate;
		this.endDate = assignment.endDate;
		this.startTime = assignment.startTime;
		this.endTime = assignment.endTime;
		this.overseerID = assignment.overseerID;
		this.driverID = assignment.driverID;
	}
	
	public int getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(int assignmentID) {
		this.assignmentID = assignmentID;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public int getTrackingNum() {
		return trackingNum;
	}

	public void setTrackingNum(int trackingNum) {
		this.trackingNum = trackingNum;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getOverseerID() {
		return overseerID;
	}

	public void setOverseerID(int overseerID) {
		this.overseerID = overseerID;
	}

	public int getDriverID() {
		return driverID;
	}

	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}

	@Override
	public String toString() {
		return "Assignment Info \nAssignment ID: " + assignmentID + "Plate Number: " + plateNumber + "Tracking Number: "
				+ trackingNum + "Start Date: " + startDate + "End Date: " + endDate + "Start Time: " + startTime
				+ "End Time=" + endTime + "Overseer ID: " + overseerID + ", Driver ID: " + driverID + "\n";
	}
	
}
