/*
 * Class Author: Diwani Walters (2303848)
 * */
package driverModule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainMenu;
import system.Assignment;
import system.Driver;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextArea;

public class DriverDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(DriverDashboard.class);
	private JPanel contentPane;

	private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;
	private JTextField assignTxt;
	private JTextField assIDTxt;
	private JComboBox<String> statusBox;


	/**
	 * Create the frame.
	 */
	public DriverDashboard(Driver drive) {
		getDatabaseConn(); //connect to database
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel headLabel = new JLabel(drive.getFirstName().toUpperCase()+ " ASSIGNMENTS");
		headLabel.setForeground(Color.BLACK);
		headLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(1, 0, 5, 5));
		
		JPanel functionalitiesPanel = new JPanel();
		midPanel.add(functionalitiesPanel);
		functionalitiesPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel specificAssignPanel = new JPanel();
		functionalitiesPanel.add(specificAssignPanel);
		specificAssignPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel explanationLabel = new JLabel("Enter assignment ID to get additional information");
		explanationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		explanationLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		specificAssignPanel.add(explanationLabel, BorderLayout.NORTH);
		
		JPanel internalPanel = new JPanel();
		specificAssignPanel.add(internalPanel, BorderLayout.CENTER);
		GridBagLayout gbl_internalPanel = new GridBagLayout();
		internalPanel.setLayout(gbl_internalPanel);
		
		JLabel assignLabel = new JLabel("Assignment Number: ");
		assignLabel.setFont(new Font("Tahoma", Font.PLAIN, 45));
		GridBagConstraints gbc_assignLabel = new GridBagConstraints();
		gbc_assignLabel.insets = new Insets(10, 0, 10, 10);
		gbc_assignLabel.gridx = 0;
		gbc_assignLabel.gridy = 0;
		internalPanel.add(assignLabel, gbc_assignLabel);
		
		assignTxt = new JTextField(10);
		assignTxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_assignTxt = new GridBagConstraints();
		gbc_assignTxt.insets = new Insets(10, 0, 10, 10);
		gbc_assignTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_assignTxt.gridx = 1;
		gbc_assignTxt.gridy = 0;
		internalPanel.add(assignTxt, gbc_assignTxt);
		
		//Return a specific assignment for the driver based on the assigment number
		JButton assignButt = new JButton("View Assignment");
		assignButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(assignTxt.getText().isBlank()) {
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in the assignment number.", 
			                "View Assignment Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				
				int assign= -1;
				
				try {
					assign = Integer.parseInt(assignTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Assignment Number must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Assignment Number entered: " + assignTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				retrieveAssigned(assign, drive);
			}
		});
		assignButt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		specificAssignPanel.add(assignButt, BorderLayout.SOUTH);
		
		JPanel updatePanel = new JPanel();
		functionalitiesPanel.add(updatePanel);
		updatePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel explainLabel = new JLabel("Update Assignment Status");
		explainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		explainLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		updatePanel.add(explainLabel, BorderLayout.NORTH);
		
		JPanel internalUpdatePanel = new JPanel();
		updatePanel.add(internalUpdatePanel, BorderLayout.CENTER);
		GridBagLayout gbl_internalUpdatePanel = new GridBagLayout();
		internalUpdatePanel.setLayout(gbl_internalUpdatePanel);
		
		JLabel updateAssIDLabel = new JLabel("Assignment Number: ");
		updateAssIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 45));
		GridBagConstraints gbc_updateAssIDLabel = new GridBagConstraints();
		gbc_updateAssIDLabel.insets = new Insets(10, 0, 10, 10);
		gbc_updateAssIDLabel.gridx = 0;
		gbc_updateAssIDLabel.gridy = 0;
		internalUpdatePanel.add(updateAssIDLabel, gbc_updateAssIDLabel);
		
		assIDTxt = new JTextField(10);
		assIDTxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_assIDTxt = new GridBagConstraints();
		gbc_assIDTxt.insets = new Insets(10, 0, 10, 10);
		gbc_assIDTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_assIDTxt.gridx = 1;
		gbc_assIDTxt.gridy = 0;
		internalUpdatePanel.add(assIDTxt, gbc_assIDTxt);
		
		JLabel statusLabel = new JLabel("Assignment Number: ");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 45));
		GridBagConstraints gbc_statusLabel = new GridBagConstraints();
		gbc_statusLabel.insets = new Insets(10, 0, 10, 10);
		gbc_statusLabel.gridx = 0;
		gbc_statusLabel.gridy = 1;
		internalUpdatePanel.add(statusLabel, gbc_statusLabel);
		
		statusBox = new JComboBox<String>();
		statusBox.addItem("In Transit");
		statusBox.addItem("Delivered");
		statusBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_statusBox = new GridBagConstraints();
		gbc_statusBox.insets = new Insets(10, 0, 10, 10);
		gbc_statusBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusBox.gridx = 1;
		gbc_statusBox.gridy = 1;
		internalUpdatePanel.add(statusBox, gbc_statusBox);
		
		//Driver is able to update a status of an assignment to in transit or delivered
		JButton updateButt = new JButton("Update Status");
		updateButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(assIDTxt.getText().isBlank()) {
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in the assignment ID.", 
			                "Update Assignment Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				
				int assign= -1;
				
				try {
					assign = Integer.parseInt(assIDTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Assignment ID must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Assignment ID entered: " + assIDTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//Store the selected status as a string
				String status= statusBox.getSelectedItem().toString();
				
				
				boolean isUpdated = updateAssigned(assign, status ,drive);
				if (isUpdated) {
					logger.error("Assignment Update Succeeded");
					JOptionPane.showMessageDialog(
							null, 
							"Assignment Updated",
							"Update Status",
							JOptionPane.INFORMATION_MESSAGE);
					
					DriverDashboard prev = new DriverDashboard(drive);
					prev.setVisible(true);
			
					dispose();					
				}
				else {
					logger.error("Assignment Update Failed");
					JOptionPane.showMessageDialog(
				            null, 
				            "Update Failed.",
				            "Update Status",
				            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		updateButt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		updatePanel.add(updateButt, BorderLayout.SOUTH);
		
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		midPanel.add(scrollPanel);
		
		String allAssignment= retrieveAllAssignment(drive);
		
		//All assignments of a driver is displayed
		JTextArea allAssignments = new JTextArea();
		allAssignments.setTabSize(5);
		allAssignments.setFont(new Font("Monospaced", Font.PLAIN, 22));
		allAssignments.setEditable(false);
		
		if(allAssignment!= null) {
			allAssignments.setText("Number\tPlate Number\tZone\t\tType\t\tStatus\n"+ allAssignment);
		} else {
			allAssignments.setText("YOU HAVEN'T BEEN ASSIGNED ANY SHIPMENTS YET");
		}
		
		scrollPanel.setViewportView(allAssignments);		
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu prev = new MainMenu();
				prev.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);

	}
	
	//Display all the assigments of the driver
	public String retrieveAllAssignment(Driver drive){
		String all= null;
		//Inner join to find all the assignments that belong to the driver
		String sql = "SELECT * "
	               + "FROM smartship.assignment a "
	               + "INNER JOIN smartship.driver d ON a.driverID = d.driverID "
	               + "INNER JOIN smartship.shipment s ON a.trackingNumber = s.trackingNumber "
	               + "WHERE a.driverID = " + drive.getUserID() + ";";		
		try {
			//Allows for forward and backward movement in the result set
			stmt= myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result= stmt.executeQuery(sql);
			
			if(result.next()) {
				all= "";
				
				//make sure the first record is included in the results
				result.beforeFirst();
				while (result.next()) {
	                
	                //Add the required fields
	                String assignDetails = result.getInt("assignmentID") + "\t\t"
                            				+ result.getString("plateNumber") + "\t\t"
	                                       + result.getString("zone") + "\t\t"
	                                       + result.getString("type") + "\t"
	                                       + result.getString("s.status") + "\n";
	                
	                all += assignDetails;
	            }
			}
			else {
				return null;
			}
			
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error retrieving your shipments: ", e);
			JOptionPane.showMessageDialog(null, 
		            "A SQL Syntax error occurred while trying to load your shipments.",
		            "Shipment Retrieval Error", 
		            JOptionPane.ERROR_MESSAGE);
		} catch(SQLException e) {
			logger.error("Database error retrieving your shipments: ", e);
			JOptionPane.showMessageDialog(null, 
		            "A database error occurred while trying to load your shipments.",
		            "Shipment Retrieval Error", 
		            JOptionPane.ERROR_MESSAGE);
		}
		
		return all; //return the string with all 
	}
	
	//Retrive a specfic assignment of the drivers and displays it
	public void retrieveAssigned(int assigned, Driver drive) {	
		//Find the specifc assignmnet and make sure it belongs to the driver
		String sql = "SELECT * "
	               + "FROM smartship.assignment a "
	               + "INNER JOIN smartship.driver d ON a.driverID = d.driverID "
	               + "INNER JOIN smartship.shipment s ON a.trackingNumber = s.trackingNumber "
	               + "WHERE a.driverID = " + drive.getUserID() + " AND a.assignmentID = " + assigned + ";";
		
		//Add the shipments details to a string and display them in a Joptionpane
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			if(result.next()) {
				String shipmentDetails;
				shipmentDetails= "Assignment Info"
					            + "\nAssignment ID: " + result.getInt("assignmentID")
					            + "\nPlate Number: " + result.getString("plateNumber")
					            + "\nTracking Number: " + result.getInt("trackingNumber")
					            + "\nRecipient: " + result.getString("recipientName")
					            + "\nDestination Address: " + result.getString("destinationAddress")
					            + "\nZone: " + result.getInt("zone")
					            + "\nType: " + result.getString("type")
					            + "\nWeight: " + result.getFloat("weight")
					            + "\nStatus: " + result.getString("status")
					            + "\n";
				JOptionPane.showInternalMessageDialog(null, shipmentDetails, 
						"Assignment Report", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showInternalMessageDialog(null, "Invalid Assignment Number\nAssignment number not yours or doesn't exist", 
						"Assignment Report Status", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error retrieving the assignment: ", e);
			JOptionPane.showMessageDialog(null, "A SQL Syntax error occurred while retrieving the assignment.", 
                    "Retrieval Error", JOptionPane.ERROR_MESSAGE);
		} catch(SQLException e) {
			logger.error("Database error retrieving the assignment: ", e);
			JOptionPane.showMessageDialog(null, "A database error occurred while retrieving the assignment.", 
                    "Retrieval Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return;
	}
	
	//Perform the update of the assignment status along with all its implications
	private boolean updateAssigned(int key, String state, Driver drive) {
		
		//Assignment and shipment information queried and stored for the current driver and the assignment number entered
		Assignment rec= new Assignment();
		int affectedRows= 0;
		float weight = 0;
		String sql = "SELECT * FROM smartship.assignment a "
		           + "INNER JOIN smartship.shipment s ON a.trackingNumber = s.trackingNumber "
		           + "WHERE a.driverID = " + drive.getUserID()
		           + " AND a.assignmentID = " + key + ";";
		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			//Assignment information stored in the objcet if result was saved but returns and display false if none was there
			if(result.next()) {
				rec.setAssignmentID(key); 
				rec.setPlateNumber(result.getString("plateNumber"));
				rec.setTrackingNum(result.getInt("trackingNumber"));
				weight= result.getFloat("weight");
			}
			else {
				JOptionPane.showInternalMessageDialog(null, "Invalid Assignment ID\nAssignment ID not yours or doesn't exist", 
						"Update Error", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during Assignment query: " + key, e);
			JOptionPane.showMessageDialog(null, "Failed to retrieve assignment data.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch(SQLException e) {
			logger.error("Database Error during Assignment query: " + key, e);
			JOptionPane.showMessageDialog(null, "Failed to retrieve assignment data.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//if the status was in transit
		if(state.equalsIgnoreCase("In Transit")) {
			//the state of the shipment is set to In Transit based on the tracking number of the assignment
			String sql1= "UPDATE smartship.shipment"
					+ " SET status= '"+ state+"' "
					+ "WHERE trackingNumber= "+ rec.getTrackingNum()+";";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql1);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during shipment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update shipment data.", "Shipment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
		    } catch(SQLException e) {
		    	logger.error("Database Error during shipment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update shipment data.", "Shipment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;	
			}
			
			//The start date and time is set for the assignment
			String sql2 = "UPDATE smartship.assignment "
		            + "SET startDate = CURDATE(), " 
		            + "    startTime = CURTIME() "
		            + "WHERE assignmentID = " + key + ";";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql2);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update assignment data.", "Assignment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update assignment data.", "Assignment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//The vehicle status is set to active based on the Plate Number in the assignment object
			String sql4 = "UPDATE smartship.vehicle SET status = 'active' "
                    + "WHERE plateNumber = '" + rec.getPlateNumber() + "'; ";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql4);
				return affectedRows== 3;
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during vehicle update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;			
			}
		}
		else {
			//If the staus change is delivered
			//The status in the shipment table is changed to status
			String sql1= "UPDATE smartship.shipment"
					+ " SET status= '"+ state+"' "
					+ "WHERE trackingNumber= "+ rec.getTrackingNum()+";";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql1);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during shipment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update shipment data.", "Shipment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during shipment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update shipment data.", "Shipment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//The assignment end date and time are set as the current date and time
			String sql2 = "UPDATE smartship.assignment "
		            + "SET endDate = CURDATE(), " 
		            + "    endTime = CURTIME() "
		            + "WHERE assignmentID = " + key + ";";	
		
			try {
				System.out.println(sql2);
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql2);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update assignment data.", "Assignment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update assignment data.", "Assignment Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//The vehicle available weight and quantity are increased based on the weight of the shipment because the shipment is no longer in the vehicle
			String sql3 = "UPDATE smartship.vehicle "
		            + "SET availableWeight = availableWeight+ "+ weight+"  , " 
		            + "    availableQuantity = availableQuantity+ 1 "
		            + "WHERE plateNumber = '" + rec.getPlateNumber() + "';";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql3);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during vehicle update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//The vehicle status is set to inactive if the available quanity and the capacity are the same
			//meaning that it is empty
			String sql4 = "UPDATE smartship.vehicle SET status = 'inactive' "
                    + "WHERE plateNumber = '" + rec.getPlateNumber() + "' "
                    + "AND availableQuantity = quantityCapacity;";	
		
			try {
				stmt= myConn.createStatement();
				affectedRows+= stmt.executeUpdate(sql4);
				return affectedRows== 4;
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during vehicle update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;			
			}
		}		
	}
	
	private static Connection getDatabaseConn() {
		if(myConn== null) {
			final String url= "jdbc:mysql://localhost:3307/smartship";
			
			try {
				myConn= DriverManager.getConnection(url, "root", "usbw");
				logger.info("Database connection successfully established.");
			}
			catch(Exception e) {
				logger.fatal("ERROR: Failed to connect to the database.", e);
	            
	            // Inform the user immediately via JOptionPane
	            JOptionPane.showMessageDialog(
	                null, 
	                "Fatal Error: Could not connect to the database.\nCheck server status.",
	                "Database Connection Error", 
	                JOptionPane.ERROR_MESSAGE
	            );	
	            System.exit(1); //Exit the app
	        }
		}
		return myConn;
	}

}
