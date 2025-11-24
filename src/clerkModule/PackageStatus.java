/*
 * Class Author: Olivia McFarlane (2301555)
 * */
package clerkModule;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import driverModule.DriverDashboard;
import gui.MainMenu;
import system.Assignment;
import system.Clerk;
import system.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class PackageStatus extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PackageStatus.class);
	private JPanel contentPane;
	private JTextField textField;
	
	private static Connection myConn = null;
	private Statement stmt = null;
	private ResultSet result = null;
	

	//Frame Creation
	public PackageStatus(User userObj) {
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
		
		JLabel pkgStatLabel = new JLabel("UPDATE PACKAGE STATUS");
		pkgStatLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		panel.add(pkgStatLabel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		Insets inset = new Insets(10, 10, 10, 10);
		
		JLabel trackingNumLabel = new JLabel("Enter Package Tracking #:");
		GridBagConstraints gbc_trackingNumLabel = new GridBagConstraints();
        trackingNumLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
		gbc_trackingNumLabel.insets = inset;
		gbc_trackingNumLabel.anchor = GridBagConstraints.EAST;
		gbc_trackingNumLabel.gridx = 0;
		gbc_trackingNumLabel.gridy = 0;
		panel_3.add(trackingNumLabel, gbc_trackingNumLabel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 35));
		gbc_textField.insets = inset;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_3.add(textField, gbc_textField);
		textField.setColumns(10); 
		
		JLabel lblNewLabel = new JLabel("Select Status:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = inset;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel_3.add(lblNewLabel, gbc_lblNewLabel);
		
		JComboBox<String> comboBox = new JComboBox<>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 35));
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = inset;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"In Transit", "Delivered", "Cancelled"})); 
		panel_3.add(comboBox, gbc_comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel_1.add(scrollPane);
		
		String pkgs = allPackages();
		
		JTextArea textArea = new JTextArea();
		textArea.setTabSize(15);
		textArea.setEditable(false);
		
		if(pkgs!= null) {
			logger.info("Displaying {} packages in the list view.", pkgs.split("\n").length);
			textArea.setText("Tracking #\t\tRecipient\t\tStatus\n"+ pkgs);
		} else {
			logger.warn("No packages retrieved from the database to display.");
			textArea.setText("NO PACKAGES");
		}
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 17));
		scrollPane.setViewportView(textArea);
		
		JLabel viewLabel = new JLabel("ALL PACKAGES");
		viewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setColumnHeaderView(viewLabel);
		
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
		
		JButton submitBtn = new JButton("SUBMIT CHANGES");
		submitBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(submitBtn);
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trackNumText = textField.getText().trim();
				String newStat = comboBox.getSelectedItem().toString();
				
				if (trackNumText.isEmpty()) {
				    JOptionPane.showMessageDialog(null, "Please enter a tracking number.", "Input Error", JOptionPane.ERROR_MESSAGE);
				    logger.warn("Submit clicked with empty tracking number field.");
				    return;
				}
				
				int trackingNum;
				try {
				    trackingNum = Integer.parseInt(trackNumText);
				    logger.info("Attempting to update status for Tracking #{} to '{}'.", trackingNum, newStat);
				} catch (NumberFormatException ex) {
				    JOptionPane.showMessageDialog(null, "Tracking number must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
				    logger.error("Input validation failed: Tracking number is not numeric: {}", trackNumText);
				    return;
				}
				
				if(updatePkgStat(trackingNum, newStat)) {
					JOptionPane.showMessageDialog(null,
							"Status Updated!");
					// Refresh the package list display after successful update
					String updatedPkgs = allPackages();
					if(updatedPkgs != null) {
						textArea.setText("Tracking #\t\tRecipient\t\tStatus\n"+ updatedPkgs);
					}
					logger.info("Status updated successfully for Tracking #{}.", trackingNum);
				}else {
					JOptionPane.showMessageDialog(null, 
							"Update Failed :(. Check if tracking number exists.", 
							"Error", JOptionPane.ERROR_MESSAGE);
					logger.warn("Status update failed for Tracking #{}.", trackingNum);
				}
			}
		});
		
		getRootPane().setDefaultButton(submitBtn); //this button is the window default and is used when enter key is clicked

	}
	
	/*
	Updates the status of a shipment and related assignment/vehicle records.
	This method handles the logic for 'In Transit', 'Delivered', and 'Cancelled' statuses.
	Returns true if the update was successful or false if unsuccessful.
	*/
	public boolean updatePkgStat(int trackingNum, String newStatus) {
		logger.debug("Starting updatePkgStat for Tracking #{} to '{}'.", trackingNum, newStatus);
		int rows = 0;
		// Checks for an assignment based on the tracking number and gets the plate number
		String sqlGetPlate = "SELECT a.plateNumber, s.weight FROM smartship.assignment a " + 
							 "JOIN smartship.shipment s ON s.trackingNumber = a.trackingNumber " + 
							 "WHERE s.trackingNumber = " + trackingNum + ";";
		ResultSet result;
		String plate = null;
		Float weight = null;
		
		try {
			stmt = myConn.createStatement();
			result = stmt.executeQuery(sqlGetPlate);
			if (result.next()) {
				plate = result.getString("plateNumber");
				weight = result.getFloat("weight");
				logger.debug("Assignment found: Plate={}, Weight={}", plate, weight);
			}else {
				logger.warn("No active assignment found for Tracking #{}", trackingNum);
			}
		} catch (SQLException e) {
			logger.error("SQL Error during initial assignment lookup for Tracking #{}", trackingNum, e);
			return false;
		}
		
		// Updates made based on the specific statuses
		if(newStatus.equalsIgnoreCase("In Transit")) {
			logger.info("Processing 'In Transit' status update.");
			if(plate == null) {
				JOptionPane.showMessageDialog(null, "Error: This package has not been assigned "
						+ "to a vehicle. Package needs to be assigned before status can be updated "
						+ "to 'In Transit'", "Status Update Error", JOptionPane.ERROR_MESSAGE);
				logger.warn("Status change to 'In Transit' blocked: No vehicle assigned.");
				return false;
			}
			
			//Update assignment with start date & time
			String sqlAssignment = "UPDATE smartship.assignment "
		                          + "SET startDate = CURDATE(), " 
		                          + "    startTime = CURTIME() "
		                          + "WHERE trackingNumber = " + trackingNum + ";";	
		
			try {
				stmt= myConn.createStatement();
				rows += stmt.executeUpdate(sqlAssignment);
				logger.debug("Assignment start time & date updated. Rows affected: {}", rows);
			} catch(SQLSyntaxErrorException e) {
				System.err.println(e.getMessage());			
			} catch(SQLException e) {
				logger.error("Database Error updating assignment start time for Tracking #{}", trackingNum, e);
			}
			
			//Update vehicle status
			String sqlVeh = "UPDATE smartship.vehicle SET status = 'active' "
							+ "WHERE plateNumber = '" + plate + "';";
			try {
				stmt= myConn.createStatement();
				rows += stmt.executeUpdate(sqlVeh);
				logger.debug("Vehicle {} status updated to 'active'. Rows affected: {}", plate, rows);
			} catch(SQLSyntaxErrorException e) {
				System.err.println(e.getMessage());			
			} catch(SQLException e) {
				logger.error("Database Error updating vehicle status to 'active' for plate {}", plate, e);			
			}
		}else if (newStatus.equalsIgnoreCase("Delivered")) {
			logger.info("Processing 'Delivered' status update.");
			
			if(plate == null) {
				JOptionPane.showMessageDialog(null, "Error: This package has not been assigned "
						+ "to a vehicle. Package needs to be assigned before status can be updated "
						+ "to 'Delivered'", "Update Error", JOptionPane.ERROR_MESSAGE);
				logger.warn("Status change to 'Delivered' blocked: No vehicle assigned.");
				return false;
			}
			
			String sqlAssignment = "UPDATE smartship.assignment "
		            			 + "SET endDate = CURDATE(), " 
		            			 + "    endTime = CURTIME() "
		            			 + "WHERE trackingNumber = " + trackingNum + ";";	
			
			try {
				stmt= myConn.createStatement();
				rows += stmt.executeUpdate(sqlAssignment);
				logger.debug("Assignment end date &ntime updated. Rows affected: {}", rows);
			} catch(SQLSyntaxErrorException e) {
				System.err.println(e.getMessage());			
			} catch(SQLException e) {
				logger.error("Database Error updating assignment end time for Tracking #{}", trackingNum, e);
			}
			
			// Update package weight and quantity capacity to the vehicle
			String sqlVehicle = "UPDATE smartship.vehicle SET availableWeight = availableWeight + " + weight + " , "
					+ "availableQuantity = availableQuantity + 1 "
					+ "WHERE plateNumber = '" + plate + "';";
			try {
				stmt= myConn.createStatement();
				rows+= stmt.executeUpdate(sqlVehicle);
				logger.info("Vehicle {} capacity restored: weight {} and quantity 1 returned.", plate, weight);
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax Error during vehicle update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch(SQLException e) {
				logger.error("Database Error during assignment update: ", e);
				JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}else if (newStatus.equalsIgnoreCase("Cancelled")) {
			logger.info("Processing 'Cancelled' status update.");
			
			String sqlAssignment = "UPDATE smartship.assignment "
		            + "SET startDate = NULL, " 
		            + "    startTime = NULL "
		            + "WHERE trackingNumber = " + trackingNum + ";";	
		
			try {
				stmt= myConn.createStatement();
				rows+= stmt.executeUpdate(sqlAssignment);
				logger.debug("Assignment start date & time cleared for cancellation. Rows affected: {}", rows);
			} catch(SQLSyntaxErrorException e) {
				System.err.println(e.getMessage());			
			} catch(SQLException e) {
				logger.error("Database Error clearing assignment dates for Tracking #{}", trackingNum, e);
			}
			
			if (plate != null) {
				String sqlVehicle = "UPDATE shipment.vehicle SET availableWeight = availableWeight + " + weight + " ,"
									+ "SET availableQuantity = availableQuantity + 1 "
									+ "WHERE plateNumber = '" + plate + "';";
				try {
					stmt= myConn.createStatement();
					rows+= stmt.executeUpdate(sqlVehicle);
					logger.info("Vehicle {} capacity restored due to cancellation.", plate);
				} catch(SQLSyntaxErrorException e) {
					logger.error("SQL Syntax Error during vehicle update: ", e);
					JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
					return false;
				} catch(SQLException e) {
					logger.error("Database Error during assignment update: ", e);
					JOptionPane.showMessageDialog(null, "Failed to update vehicle data.", "Vehicle Update Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}else {
				logger.debug("No vehicle capacity modification needed, package was not assigned.");
			}
		}		
		
		// Shipment status update		
		String sqlPkgStat = "UPDATE smartship.shipment SET status = '" + newStatus + 
							"' WHERE trackingNumber = " + trackingNum + ";";
		
		try {
			stmt = myConn.createStatement();
			rows += stmt.executeUpdate(sqlPkgStat);
			logger.info("Shipment status set to '{}'. Total affected DB operations: {}", newStatus, rows);
	        return rows > 0;  // true if update occurred
	    } catch (SQLException e) {
	        logger.error("Final status update failed for Tracking #{}", trackingNum, e);
	        e.printStackTrace();
	        return false;
	    }
	}  

	// Retrives all shipments to be displayed in the JTextArea
	public String allPackages() {
		String sql = "SELECT trackingNumber, recipientName, status FROM smartship.shipment;";
		String pkgs = "";
		
		try {
			stmt = myConn.createStatement();
			result = stmt.executeQuery(sql);
			
			while (result.next()) {
				pkgs += result.getInt("trackingNumber") + "\t\t" +
				result.getString("recipientName") + "\t\t" +
				result.getString("status") + "\n";
			}
			logger.info("Successfully retrieved package records.");
			return pkgs;
		}
		catch (SQLException e) {
			logger.error("SQL Error fetching all packages:", e);
			e.printStackTrace();
			return null;
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
