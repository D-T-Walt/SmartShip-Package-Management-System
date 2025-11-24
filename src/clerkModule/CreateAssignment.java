/*
 * Class Author: Olivia McFarlane (2301555)
 * */
package clerkModule;


import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Assignment;
import system.Clerk;
import system.Shipment;
import system.User;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class CreateAssignment extends JFrame {
	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(CreateAssignment.class);
	private JPanel contentPane;
	private JComboBox<String> vehicleDropdown;
    private JComboBox<String> driverDropdown;
    private JTextField textField;
    private JPanel driverPanel;
    
    private Clerk clerk;
    private Assignment assign;
    private Shipment s;
	private static Connection myConn = null;
	private Statement stmt = null;
	private ResultSet result = null;
	
	Insets insets = new Insets(10, 10, 10, 10);
	
	//Frame Creation
	public CreateAssignment(User userObj) {
		logger.info("Initializing CreateAssignment for User ID: {}", userObj.getUserID());
		getDatabaseConn();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblAssignment = new JLabel("ASSIGNMENT");
		lblAssignment.setFont(new Font("Tahoma", Font.PLAIN, 60));
		panel.add(lblAssignment);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel trackingNumLabel = new JLabel("Enter Tracking #:");
		trackingNumLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
		GridBagConstraints gbc_trackingNumLabel = new GridBagConstraints();
		gbc_trackingNumLabel.anchor = GridBagConstraints.WEST;
		gbc_trackingNumLabel.fill = GridBagConstraints.WEST;
		gbc_trackingNumLabel.insets = insets;
		gbc_trackingNumLabel.gridx = 0;
		gbc_trackingNumLabel.gridy = 0;
		panel_1.add(trackingNumLabel, gbc_trackingNumLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 35));
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.EAST;
		gbc_textField.fill = GridBagConstraints.EAST;
		gbc_textField.insets = insets;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_1.add(textField, gbc_textField);
		textField.setColumns(10); //check that the package is fully paid for n tat its stat isn't assigned, intransit
		
		JButton checkPkgBtn = new JButton("Check Package");
		checkPkgBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		GridBagConstraints gbc_checkPkg = new GridBagConstraints();
		gbc_checkPkg.insets = insets;
		gbc_checkPkg.gridx = 1;
		gbc_checkPkg.gridy = 1;
		panel_1.add(checkPkgBtn, gbc_checkPkg);
		// Action listener for checking vehicle assignment status
		checkPkgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Check Package button clicked.");
				String t = textField.getText().trim();
				// textfield checks
				if (t.isEmpty()) {
					logger.warn("Input validation failed: Tracking number is empty.");
					JOptionPane.showMessageDialog(null, "Enter a tracking number first.", "Input Error",
					JOptionPane.ERROR_MESSAGE);
					return;
				}
				int tracNum;
				try {
					tracNum = Integer.parseInt(t);
					logger.info("Attempting to retrieve shipment for tracking number: {}", tracNum);
				} catch (NumberFormatException ex) {
					logger.error("Input validation failed: Tracking number is not numeric: {}", t, ex);
					JOptionPane.showMessageDialog(null, "Tracking number must be numeric.", "Input Error",
					JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Retrieve shipment dat
				s = getShipment(tracNum);
				if (s == null) {
					logger.info("Shipment not found for tracking number: {}", tracNum);
					JOptionPane.showMessageDialog(null, "Shipment not found.", "No Record",
					JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				// Check shipment status
				if(s.getStatus().equalsIgnoreCase("pending")) {
					logger.info("Shipment {} is PENDING. Loading vehicles for zone {} and weight {}", tracNum, s.getZone(), s.getWeight());
					// populate vehicles filtered by this shipment's zone, weight and capacity
					loadVehicles(vehicleDropdown, s.getZone(), s.getWeight());
					JOptionPane.showMessageDialog(null, "Shipment loaded. Choose a vehicle and press Check Vehicle.",
					"Shipment", JOptionPane.INFORMATION_MESSAGE);
				}else {
					logger.warn("Shipment {} status is '{}'. Cannot be assigned.", tracNum, s.getStatus());
					JOptionPane.showMessageDialog(null, "Only shipments of status 'pending' can be assigned", "Status Error",
							JOptionPane.INFORMATION_MESSAGE);
							return;
				}
				
				
			}
		});
		
		
		JLabel vehicleLabel = new JLabel("Assign Vehicle:");
		vehicleLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
		vehicleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_vehicleLabel = new GridBagConstraints();
		gbc_vehicleLabel.anchor = GridBagConstraints.WEST;
		gbc_vehicleLabel.fill = GridBagConstraints.WEST;
		gbc_vehicleLabel.insets = insets;
		gbc_vehicleLabel.gridx = 0;
		gbc_vehicleLabel.gridy = 2;
		panel_1.add(vehicleLabel, gbc_vehicleLabel);	
		
		vehicleDropdown = new JComboBox<>();
		vehicleDropdown.setFont(new Font("Tahoma", Font.PLAIN, 35));
		vehicleDropdown.setMaximumRowCount(10);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.anchor = GridBagConstraints.EAST;
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.insets = insets;
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 2;
		panel_1.add(vehicleDropdown, gbc_comboBox_1);
		
		// check if driver is already assigned
		JButton checkVehicleBtn = new JButton("Check Vehicle");
		checkVehicleBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		GridBagConstraints gbc_checkVehicle = new GridBagConstraints();
		gbc_checkVehicle.insets = insets;
		gbc_checkVehicle.gridx = 1;
		gbc_checkVehicle.gridy = 3;
		panel_1.add(checkVehicleBtn, gbc_checkVehicle);
		checkVehicleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Check Vehicle button clicked.");
				vehicleCheck();
			}
		});
		
		// Panel that will show assigned driver OR dropdown list
		driverPanel = new JPanel();
		GridBagConstraints gbc_driverPanel = new GridBagConstraints();
		gbc_driverPanel.gridx = 1;
		gbc_driverPanel.gridy = 4;
		gbc_driverPanel.insets = insets;
		gbc_driverPanel.fill = GridBagConstraints.HORIZONTAL;
		panel_1.add(driverPanel, gbc_driverPanel);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JButton backBtn = new JButton("<--    BACK");
		backBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Back button clicked. Returning to ProcessShipmentMenu.");
				ProcessShipmentMenu prevMenu = new ProcessShipmentMenu(userObj);
				prevMenu.setVisible(true);
		
				dispose(); 	
				
			}
		});
				
		JButton btnSubmit = new JButton("SUBMIT");
		btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Submit button clicked. Starting assignment creation process.");
				String t = textField.getText().trim();
				int tracNum;
				// check tracking num
				try {
				    tracNum = Integer.parseInt(t);
				} catch (NumberFormatException ex) {
				    logger.error("Submit validation failed: Tracking number is not numeric: {}", t, ex);
				    JOptionPane.showMessageDialog(null, "Tracking number must be numeric.", "Input Error",
				    JOptionPane.ERROR_MESSAGE);
				    return;
				}

				s = getShipment(tracNum);
				
				if (s == null) {
					logger.warn("Submit failed: Shipment not loaded (s is null). Tracking number: {}", tracNum);
					JOptionPane.showMessageDialog(null, "Load a shipment first (Check Package).", "Error",
					JOptionPane.ERROR_MESSAGE);
					return;
				}
				// check vehicle selection
		        String plate = (String) vehicleDropdown.getSelectedItem();
		        if (plate == null) {
					logger.warn("Submit failed: No vehicle selected.");
		            JOptionPane.showMessageDialog(null, 
		                "Select a vehicle.",
		                "Input Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        
		        // determine driver ID
		        Integer driverID = null;
		        String alreadyAssignedDriver = checkVehicleDriver(plate);

		        if (alreadyAssignedDriver != null) {
		            logger.info("Vehicle {} already has an active driver: {}", plate, alreadyAssignedDriver);
		            // find driverID for that driverName
		            try {
		                driverID = Integer.parseInt(alreadyAssignedDriver);
		            } catch (NumberFormatException ex) {
		                logger.error("Error parsing assigned driver ID: {}", alreadyAssignedDriver, ex);
		                JOptionPane.showMessageDialog(null, "Internal error with assigned driver ID.", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		        } else { 
		            if (driverDropdown == null || driverDropdown.getSelectedItem() == null) {
		                logger.warn("Submit failed: No driver selected for unassigned vehicle {}.", plate);
		                JOptionPane.showMessageDialog(null, 
		                    "Select a driver.",
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            // Get driver ID from dropdown
		            try {
		                driverID = Integer.parseInt((String) driverDropdown.getSelectedItem());
		                logger.info("Selected new driver ID: {}", driverID);
		            } catch (NumberFormatException ex) {
		                logger.error("Error parsing selected driver ID from dropdown.", ex);
		                JOptionPane.showMessageDialog(null, "Internal error with selected driver ID.", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		        }
		        
		        int overseerID = userObj.getUserID();
		        LocalDate startDate = null;
		    	LocalDate endDate = null;
		    	LocalTime startTime = null;
		    	LocalTime endTime = null;
		        
		        assign = new Assignment(0, plate, s.getTrackingNum(), startDate, endDate, startTime, endTime, overseerID, driverID);
				logger.info("Attempting to create assignment: Plate={}, TrackingNum={}, DriverID={}", plate, s.getTrackingNum(), driverID);
				boolean isCreated = create(assign, s.getWeight());
				if(isCreated) {
					 logger.info("Assignment created successfully for shipment {}", s.getTrackingNum());
					 JOptionPane.showMessageDialog(null,
				                "Assignment created successfully.",
				                "Assignment Status", JOptionPane.INFORMATION_MESSAGE);
				}else {
					logger.error("Assignment creation failed for shipment {}", s.getTrackingNum());
					JOptionPane.showMessageDialog(null,
			                "Error creating assignment.",
			                "Assignment Status", JOptionPane.ERROR_MESSAGE);
				}
				
				ProcessShipmentMenu prevMenu = new ProcessShipmentMenu(userObj);
				prevMenu.setVisible(true);
		
				dispose(); 	
			}
		});
		//create a new assignment and change the status of the shipment to assigned
		getRootPane().setDefaultButton(btnSubmit); //this button is the window default and is used when enter key is clicked

		
	}
	
	// Retrieves the basic shipment details of a shipment based on the tracking num entered
	private Shipment getShipment(int tracNum) {
		String sql = "SELECT trackingNumber, zone, weight, status FROM smartship.shipment "
			+ "WHERE trackingNumber = " + tracNum + ";";
		logger.debug("Executing SQL for getShipment: {}", sql);
		
		try {
        	stmt = myConn.createStatement();
        	ResultSet result = stmt.executeQuery(sql);

        	if (result.next()) {
            	int trackNum = result.getInt("trackingNumber");
        		int zone = result.getInt("zone");
            	float weight = result.getFloat("weight");
            	String stat = result.getString("status");
            	logger.info("Shipment found: TrackingNum={}, Zone={}, Weight={}, Status={}", trackNum, zone, weight, stat);
            	return new Shipment(trackNum, zone, weight, stat);
        	}else {
        	    logger.info("No shipment record found for tracking number: {}", tracNum);
        	}
    	} catch (SQLException e) {
        	logger.error("SQL error while retrieving shipment for tracking number: {}", tracNum, e);
    	}

    	return null; // no such shipment
	}
	
	// Loads available vehicles that can handle weight and quantity and matches the zone of the shipment
	private void loadVehicles(JComboBox<String> combo, int zone, float weight) {
		logger.debug("Loading vehicles for Zone {} and min weight {}", zone, weight);
		combo.removeAllItems();
		
		try {
	        stmt = myConn.createStatement();
			/*
			SQL gets vehicles with status 'inactive', has enough available weight 
			and quantity and either has no other undelivered shipments assigned to it or the 
			shipments assigned are from the same zone as the current shipment
			*/
	        
	        String sql =
	            "SELECT v.plateNumber " +
	            "FROM smartship.vehicle v " +
	            "LEFT JOIN smartship.assignment a " +
	            "ON v.plateNumber = a.plateNumber AND a.endDate IS NULL " +
	            "LEFT JOIN smartship.shipment s " +
	            "ON a.trackingNumber = s.trackingNumber " +
	            "WHERE v.status = 'inactive' " +
	            "AND v.availableWeight >= " + weight +
	            " AND v.availableQuantity > 0 " +
	            // allow vehicles with NO assignment OR matching zone
	            "AND (s.zone IS NULL OR s.zone = " + zone + ")" + 
	            "GROUP BY v.plateNumber";// Group to handle multiple shipments on one vehicle

			logger.debug("Executing SQL for loadVehicles: {}", sql);
	        ResultSet result = stmt.executeQuery(sql);

	        while (result.next()) {
	            combo.addItem(result.getString("plateNumber"));
	        }

	    } catch (SQLException e) {
			logger.error("SQL error while loading vehicles for zone {} and weight {}", zone, weight, e);
			JOptionPane.showMessageDialog(null, 
					"Failed to load available vehicles.",
					"Database Error", JOptionPane.ERROR_MESSAGE);	    
		}
	}
	
	/* 
	Updates the driver panel based on the vehicle selected.
	It either displays the driver that is currently assigned to the selected vehicle
	or a dropdown of available drivers.
	*/
	private void vehicleCheck() {
		logger.debug("Starting vehicleCheck().");
		driverPanel.removeAll();

		String vehicle = (String) vehicleDropdown.getSelectedItem();
		if (vehicle == null) {
			logger.warn("vehicleCheck() aborted: No vehicle selected in dropdown.");
			return;
		}
		logger.info("Checking driver assignment for vehicle: {}", vehicle);
		
		String driverID = checkVehicleDriver(vehicle);

		if (driverID != null) {
			// Vehicle already has a currently assigned driver
			logger.info("Vehicle {} is actively assigned to driver ID: {}", vehicle, driverID);
			JLabel assigned = new JLabel("Driver Assigned: " + driverID);
			assigned.setFont(new Font("Tahoma", Font.PLAIN, 35));
			driverPanel.add(assigned);
		} else {
			// Vehicle has no currently assigned driver, must select one
			logger.info("Vehicle {} has no active driver assignment. Loading available drivers.", vehicle);
			JLabel driverLabel = new JLabel("Assign Driver:");
			driverLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
			driverLabel.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_driverLabel = new GridBagConstraints();
			gbc_driverLabel.anchor = GridBagConstraints.WEST;
			gbc_driverLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_driverLabel.insets = insets;
			gbc_driverLabel.gridx = 0;
			gbc_driverLabel.gridy = 2;
			driverPanel.add(driverLabel, gbc_driverLabel);
			
			driverDropdown = new JComboBox<>();
			driverDropdown.setMaximumRowCount(10);
			driverDropdown.setFont(new Font("Tahoma", Font.PLAIN, 35));
			GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
			gbc_comboBox_2.insets = new Insets(10, 10, 10, 10);
			gbc_comboBox_2.anchor = GridBagConstraints.EAST;
			gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox_2.gridx = 1;
			gbc_comboBox_2.gridy = 2;
			loadDrivers(driverDropdown);
			driverPanel.add(driverDropdown, gbc_comboBox_2);
		}
			// Refresh panel to display changes
			driverPanel.revalidate();
			driverPanel.repaint();
	}


	// Checks the database for an assignment associated with the vehicle where endDate is null and gets the driver ID 
	private String checkVehicleDriver(String vehiclePlate) {
		String sql = "SELECT a.driverID " +
		"FROM smartship.assignment a " +
		"WHERE a.plateNumber = '" + vehiclePlate + "' " +
		"AND a.endDate IS NULL;";
		logger.debug("Executing SQL for checkVehicleDriver: {}", sql);
		
		try {
			stmt = myConn.createStatement();
			ResultSet result = stmt.executeQuery(sql);


			if (result.next()) {
				String driverID = result.getString("driverID");
				logger.debug("Active driver found for vehicle {}: {}", vehiclePlate, driverID);
				return driverID;			}
		} catch (SQLException e) {
			logger.error("SQL error while checking active driver for vehicle: {}", vehiclePlate, e);		
			JOptionPane.showMessageDialog(null, 
					"Failed to check driver status.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		logger.debug("No active driver found for vehicle: {}", vehiclePlate);
		return null; // No active assignment
	}
	
	// load drivers not currently connected to an active assignment
	private void loadDrivers(JComboBox<String> combo) {
		logger.debug("Loading available drivers.");
		combo.removeAllItems();
		
		
		String sql = "SELECT d.driverID " +
			    	 "FROM smartship.driver d " +
			    	 "LEFT JOIN smartship.assignment a ON d.driverID = a.driverID " +
			    	 "     AND a.endDate IS NULL " +   
			    	 "WHERE a.assignmentID IS NULL;";  
		logger.debug("Executing SQL for loadDrivers: {}", sql);
		
		try {
			stmt = myConn.createStatement();
			result = stmt.executeQuery(sql);
			
			
			while(result.next()) {
				combo.addItem(result.getString("driverID"));
			}
		}catch(SQLException e) {
			logger.error("SQL error while loading available drivers.", e);
			JOptionPane.showMessageDialog(null, 
					"Failed to load available drivers.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	Creates the assignment and safely updates vehicle capacity using a database transaction 
    and SELECT FOR UPDATE to prevent concurrency issues (overbooking).
	*/
	private boolean create(Assignment assign, float weight) {
		logger.info("Attempting concurrency-safe assignment for TrackingNum: {}", assign.getTrackingNum());
		
		try {
            // Database transaction begins
			myConn.setAutoCommit(false); 
            
            // Check Invoice status within the transaction
            String sqlCheckInvoice = "SELECT status FROM smartship.invoice WHERE trackingNumber = "
                    + assign.getTrackingNum() + ";";
            
            String stat;
            try (Statement stmt = myConn.createStatement();
                 ResultSet result = stmt.executeQuery(sqlCheckInvoice)) {
                
                if (result.next()) {
                    stat = result.getString("status");
                } else {
                    logger.warn("Invoice not found for shipment {}", assign.getTrackingNum());
                    myConn.rollback();
                    JOptionPane.showMessageDialog(null, "Error: Invoice not found for this shipment.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            if (!stat.equalsIgnoreCase("Paid")) {
                logger.warn("Assignment blocked: Shipment {} invoice status is '{}'. Must be 'Paid'.", assign.getTrackingNum(), stat);
                myConn.rollback();
                JOptionPane.showMessageDialog(null, "Error: Package invoice needs to be paid in full ('Paid' status).", "Assignment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Concurrency Control: SELECT vehicle weight and capacity and LOCK the row
            String sqlLockAndCheckCapacity = 
				"SELECT availableWeight, availableQuantity FROM smartship.vehicle WHERE plateNumber = '"
                + assign.getPlateNumber() + "' FOR UPDATE;"; 
            
            float availableWeight = 0;
            float availableQuantity = 0;

            try (Statement stmt = myConn.createStatement();
                 ResultSet result = stmt.executeQuery(sqlLockAndCheckCapacity)) {
                
                if (result.next()) {
                    availableWeight = result.getFloat("availableWeight");
                    availableQuantity = result.getFloat("availableQuantity");
                } else {
                    logger.error("Vehicle {} not found during capacity check.", assign.getPlateNumber());
                    myConn.rollback();
                    JOptionPane.showMessageDialog(null, "Error: Vehicle not found.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            float newAvailableWeight = availableWeight - weight;
            float newAvailableQuantity = availableQuantity - 1;

            // Check if capacity is suitable
            if (newAvailableWeight < 0 || newAvailableQuantity < 0) {
                logger.warn("Assignment blocked: Capacity check failed for vehicle {}. Required Weight={}, Available Weight={}, Required Quantity={}, Available Quantity={}",
                    assign.getPlateNumber(), weight, availableWeight, 1, availableQuantity);
                myConn.rollback();
                JOptionPane.showMessageDialog(null, "Error: Vehicle capacity exceeded.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Do all updates (While lock is held)

            // UPDATE 1: Update Vehicle Capacity
            String sqlUpdateVehicle = "UPDATE smartship.vehicle SET availableWeight = " + newAvailableWeight 
                                        + ", availableQuantity = " + newAvailableQuantity 
                                        + " WHERE plateNumber = '" + assign.getPlateNumber() + "';";
            
            // UPDATE 2: Insert new Assignment
            String sqlInsertAssignment = "INSERT INTO smartship.assignment "
                                        + "(plateNumber, trackingNumber, overseerID, driverID) "
                                        + "VALUES ('" + assign.getPlateNumber() + "', " + assign.getTrackingNum() + ", "
                                        + assign.getOverseerID() + ", " + assign.getDriverID() + ") ;";
            
            // UPDATE 3: Update Shipment Status
            String sqlUpdateShipment = "UPDATE smartship.shipment SET status = 'Assigned' "
                                        + "WHERE trackingNumber = " + assign.getTrackingNum() + ";";
            
            int affectedRows = 0;
            try (Statement stmt = myConn.createStatement()) {
                affectedRows += stmt.executeUpdate(sqlUpdateVehicle);
                affectedRows += stmt.executeUpdate(sqlInsertAssignment);
                affectedRows += stmt.executeUpdate(sqlUpdateShipment);
            }
            
            // Commit tranzaction (releases the lock)
            myConn.commit();
            logger.info("Transaction committed. Assignment created for TrackingNum: {}", assign.getTrackingNum());
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Handles if assignment already exists (trackingNumber, plateNumber combination)
            logger.error("Integrity error (Duplicate Assignment/Shipment) during assignment for {}.", assign.getTrackingNum(), e);
            JOptionPane.showMessageDialog(null, "Error: This package is already assigned.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            logger.error("Database error during concurrent assignment for {}.", assign.getTrackingNum(), e);
            try {
                if (myConn != null) myConn.rollback(); // 5b. ROLLBACK on failure (releases the lock)
            } catch (SQLException ex) {
                logger.error("Rollback failed.", ex);
            }
            JOptionPane.showMessageDialog(null, 
                "Assignment failed due to a database error or concurrent access conflict. Try again.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // End of database transaction, reset autoCommit
            try {
                if (myConn != null) myConn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.fatal("Failed to reset autoCommit to true.", e);
            }
        }
	}
	
	// Establishes connection to the database
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
