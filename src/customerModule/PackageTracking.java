/*
 * Class Author: Diwani Walters (2303848)
 * */
package customerModule;

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

import system.Customer;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

public class PackageTracking extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PackageTracking.class);
	private JPanel contentPane;
	private JTextField trackingNumTxt;
	
	private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;

	/**
	 * Create the frame.
	 */
	public PackageTracking(Customer cus) {
		getDatabaseConn();
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
		
		JLabel headLabel = new JLabel(cus.getFirstName().toUpperCase()+" PACKAGES");
		headLabel.setForeground(Color.BLACK);
		headLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(1, 0, 5, 5));
		
		JPanel specificPackagePanel = new JPanel();
		midPanel.add(specificPackagePanel);
		specificPackagePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel explanationLabel = new JLabel("Enter tracking number to get additional information");
		explanationLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		explanationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		specificPackagePanel.add(explanationLabel, BorderLayout.NORTH);
		
		JPanel internalPanel = new JPanel();
		specificPackagePanel.add(internalPanel, BorderLayout.CENTER);
		GridBagLayout gbl_internalPanel = new GridBagLayout();
		internalPanel.setLayout(gbl_internalPanel);
		
		JLabel trackingNum = new JLabel("Tracking Number: ");
		trackingNum.setFont(new Font("Tahoma", Font.PLAIN, 45));
		GridBagConstraints gbc_trackingNum = new GridBagConstraints();
		gbc_trackingNum.insets = new Insets(10,0,10,10);
		gbc_trackingNum.gridx = 0;
		gbc_trackingNum.gridy = 0;
		internalPanel.add(trackingNum, gbc_trackingNum);
		
		trackingNumTxt = new JTextField(25);
		trackingNumTxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_trackingNumTxt = new GridBagConstraints();
		gbc_trackingNumTxt.insets = new Insets(10,0,10,10);
		gbc_trackingNumTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_trackingNumTxt.gridx = 1;
		gbc_trackingNumTxt.gridy = 0;
		internalPanel.add(trackingNumTxt, gbc_trackingNumTxt);
		trackingNumTxt.setColumns(10);
		
		//Track a specific shipment
		JButton packageButton = new JButton("Track Package");
		packageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Check to make sure the input filed isnt empty
				if(trackingNumTxt.getText().isBlank()) {
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in the tracking number.", 
			                "View Package Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				
				int trackingNumber= -1;
				//Integer check for the tracking number entered
				try {
					trackingNumber = Integer.parseInt(trackingNumTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Tracking Number must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Tracking Number entered: " + trackingNumTxt.getText(), ex);
		            return; // Stop processing if input is invalid
		        }
				
				//retrive and display shipment
				retrieveShipment(trackingNumber, cus);
			}
		});
		packageButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		specificPackagePanel.add(packageButton, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(packageButton); //log button is the window default and is used when enter key is clicked

		//scrollpane is added to the midPanel
		//it houses a text area that is used to display all shipments for a user
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		midPanel.add(scrollPanel);
		
		//Retrieve all shipments for the customer and store in a string
		String allPackage= retrieveAllShipment(cus);
		
		JTextArea allPackages = new JTextArea();
		allPackages.setTabSize(5);
		allPackages.setEditable(false);
		
		//if the string isnt empty a message is displayed about the customers lack of shipments
		//if it isnt null then the headings of the makeshift tabular structure is set for the text area along with the string storing the shipments
		if(allPackage!= null) {
			allPackages.setText("Number\t\tZone\t\tType\t\tStatus\n"+ allPackage);
		} else {
			allPackages.setText("YOU HAVEN'T REQUESTED ANY SHIPMENTS YET");
		}
		allPackages.setFont(new Font("Monospaced", Font.PLAIN, 28));
		scrollPanel.setViewportView(allPackages);		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//navigate to previous menu
				CustomerFunctionalities func = new CustomerFunctionalities(cus);
				func.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
	}
	
	public String retrieveAllShipment(Customer cus){
		String all= null;
		
		String sql = "SELECT * "
	               + "FROM smartship.shipment s "
	               + "WHERE s.custID = " + cus.getUserID() + ";";		
		try {
			//Allows for forward and backward movement in the result set
			stmt= myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result= stmt.executeQuery(sql);
			
			if(result.next()) {
				all= "";
				
				//make sure the first record is included in the results
				result.beforeFirst();
				while (result.next()) {
	                
	                // add the required fields with tab (\t) separators
	                String shipmentDetails = result.getInt("trackingNumber") + "\t\t\t"
	                                       + result.getString("zone") + "\t\t"
	                                       + result.getString("type") + "\t"
	                                       + result.getString("status") + "\n";
	                
	                all += shipmentDetails;
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
		
		return all;
	}
	
	//use information from both the invoice and shipment table to give 
	public void retrieveShipment(int track, Customer cus) {
		String sql = "SELECT * "
	               + "FROM smartship.shipment s "
	               + "INNER JOIN smartship.invoice i ON s.trackingNumber = i.trackingNumber "
	               + "WHERE s.trackingNumber = " + track + " AND s.custID = " + cus.getUserID() + ";";		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			if(result.next()) {
				String shipmentDetails;
				shipmentDetails= "Package Info"
					            + "\nTracking Number: " + result.getInt("trackingNumber")
					            + "\nRecipient Name: " + result.getString("recipientName")
					            + "\nDestination Address: " + result.getString("destinationAddress")
					            + "\nZone: " + result.getInt("zone")
					            + "\nType: " + result.getString("type")
					            + "\nWeight: " + result.getFloat("weight") +"lbs"
					            + "\nHeight: " + result.getFloat("height") +"in"
					            + "\nLength: " + result.getFloat("length") +"in"
					            + "\nWidth: " + result.getFloat("width") +"in"
					            + "\nCost: $" + result.getDouble("s.cost")
					            + "\nShipment Status: " + result.getString("s.status")
					            + "\nPayment Status: " + result.getString("i.status")
					            + "\nBalance: $" + result.getDouble("balance")
					            + "\nPayment Method: " + result.getString("method")
					            + "\n";
				JOptionPane.showInternalMessageDialog(null, shipmentDetails, 
						"Package Tracking Status", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showInternalMessageDialog(null, "Invalid Tracking Number\nTracking number not yours or doesn't exist", 
						"Package Tracking Status", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error retrieving the shipment: ", e);
		} catch(SQLException e) {
			logger.error("Database error retrieving the shipment: ", e);
		}
		
		return;
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
