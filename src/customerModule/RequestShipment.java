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
import system.Invoice;
import system.Shipment;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class RequestShipment extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private static final Logger logger = LogManager.getLogger(RequestShipment.class);
    
	private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;
	private Invoice invoice;
	private Shipment ship;
	private JTextField recipientTxt;
	private JTextField destinationTxt;
	private JComboBox<Integer> zoneBox;
	private JComboBox<String> typeBox;
	private JTextField weightTxt;
	private JTextField heightTxt;
	private JTextField widthTxt;
	private JTextField lengthTxt;
	private JComboBox<String> methodBox;
	/**
	 * Create the frame.
	 */
	public RequestShipment(Customer cus) {
		getDatabaseConn(); //connect to database
		invoice= new Invoice();
		ship= new Shipment();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,800);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel headingLabel = new JLabel("REQUEST SHIPMENT");
		headingLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headingLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		GridBagLayout gbl_midPanel = new GridBagLayout();
		midPanel.setLayout(gbl_midPanel);
		
		JLabel recipientLabel = new JLabel("Recipient Name:");
		recipientLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_recipientLabel = new GridBagConstraints();
		gbc_recipientLabel.insets = new Insets(10, 0, 10, 10);
		gbc_recipientLabel.gridx = 0;
		gbc_recipientLabel.gridy = 0;
		midPanel.add(recipientLabel, gbc_recipientLabel);
		
		recipientTxt = new JTextField(10);
		GridBagConstraints gbc_recipientTxt = new GridBagConstraints();
		gbc_recipientTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_recipientTxt.insets = new Insets(10, 0, 10, 10);
		gbc_recipientTxt.gridx = 1;
		gbc_recipientTxt.gridy = 0;
		midPanel.add(recipientTxt, gbc_recipientTxt);
		
		JLabel destinationLabel = new JLabel("Destination Address: ");
		destinationLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_destinationLabel = new GridBagConstraints();
		gbc_destinationLabel.anchor = GridBagConstraints.EAST;
		gbc_destinationLabel.insets = new Insets(10, 0, 10, 10);
		gbc_destinationLabel.gridx = 0;
		gbc_destinationLabel.gridy = 1;
		midPanel.add(destinationLabel, gbc_destinationLabel);
		
		destinationTxt = new JTextField(40);
		GridBagConstraints gbc_destinationTxt = new GridBagConstraints();
		gbc_destinationTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_destinationTxt.insets = new Insets(10, 0, 10, 10);
		gbc_destinationTxt.gridx = 1;
		gbc_destinationTxt.gridy = 1;
		midPanel.add(destinationTxt, gbc_destinationTxt);
		
		JLabel zoneLabel = new JLabel("Zone:");
		zoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_zoneLabel = new GridBagConstraints();
		gbc_zoneLabel.anchor = GridBagConstraints.EAST;
		gbc_zoneLabel.insets = new Insets(10, 0, 10, 10);
		gbc_zoneLabel.gridx = 0;
		gbc_zoneLabel.gridy = 2;
		midPanel.add(zoneLabel, gbc_zoneLabel);
		
		zoneBox = new JComboBox<Integer>();
		zoneBox.addItem(1);
		zoneBox.addItem(2);
		zoneBox.addItem(3);
		zoneBox.addItem(4);
		GridBagConstraints gbc_zoneTxt = new GridBagConstraints();
		gbc_zoneTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_zoneTxt.insets = new Insets(10, 0, 10, 10);
		gbc_zoneTxt.gridx = 1;
		gbc_zoneTxt.gridy = 2;
		midPanel.add(zoneBox, gbc_zoneTxt);
		
		JLabel typeLabel = new JLabel("Type:");
		typeLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_typeLabel = new GridBagConstraints();
		gbc_typeLabel.anchor = GridBagConstraints.EAST;
		gbc_typeLabel.insets = new Insets(10, 0, 10, 10);
		gbc_typeLabel.gridx = 0;
		gbc_typeLabel.gridy = 3;
		midPanel.add(typeLabel, gbc_typeLabel);
		
		typeBox = new JComboBox<String>();
		typeBox.addItem("Standard");
		typeBox.addItem("Express");
		typeBox.addItem("Fragile");
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(10, 0, 10, 10);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 3;
		midPanel.add(typeBox, gbc_textField_3);
		
		JLabel weightLabel = new JLabel("Weight (lbs):");
		weightLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.insets = new Insets(10, 0, 10, 10);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 4;
		midPanel.add(weightLabel, gbc_passwordLabel);
		
		weightTxt = new JTextField(10);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(10, 0, 10, 10);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 4;
		midPanel.add(weightTxt, gbc_passwordField);
		
		JLabel heightLabel = new JLabel("Height (in):");
		heightLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_heightLabel = new GridBagConstraints();
		gbc_heightLabel.anchor = GridBagConstraints.EAST;
		gbc_heightLabel.insets = new Insets(10, 0, 10, 10);
		gbc_heightLabel.gridx = 0;
		gbc_heightLabel.gridy = 5;
		midPanel.add(heightLabel, gbc_heightLabel);
		
		heightTxt = new JTextField(10);
		GridBagConstraints gbc_heightField = new GridBagConstraints();
		gbc_heightField.insets = new Insets(10, 0, 10, 10);
		gbc_heightField.fill = GridBagConstraints.HORIZONTAL;
		gbc_heightField.gridx = 1;
		gbc_heightField.gridy = 5;
		midPanel.add(heightTxt, gbc_heightField);
		
		JLabel widthLabel = new JLabel("Width (in):");
		widthLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_widthLabel = new GridBagConstraints();
		gbc_widthLabel.anchor = GridBagConstraints.EAST;
		gbc_widthLabel.insets = new Insets(10, 0, 10, 10);
		gbc_widthLabel.gridx = 0;
		gbc_widthLabel.gridy = 6;
		midPanel.add(widthLabel, gbc_widthLabel);
		
		widthTxt = new JTextField(10);
		GridBagConstraints gbc_widthField = new GridBagConstraints();
		gbc_widthField.insets = new Insets(10, 0, 10, 10);
		gbc_widthField.fill = GridBagConstraints.HORIZONTAL;
		gbc_widthField.gridx = 1;
		gbc_widthField.gridy = 6;
		midPanel.add(widthTxt, gbc_widthField);
		
		JLabel lengthLabel = new JLabel("Length (in):");
		lengthLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_lengthLabel = new GridBagConstraints();
		gbc_lengthLabel.anchor = GridBagConstraints.EAST;
		gbc_lengthLabel.insets = new Insets(10, 0, 10, 10);
		gbc_lengthLabel.gridx = 0;
		gbc_lengthLabel.gridy = 7;
		midPanel.add(lengthLabel, gbc_lengthLabel);
		
		lengthTxt = new JTextField(10);
		GridBagConstraints gbc_lengthField = new GridBagConstraints();
		gbc_lengthField.insets = new Insets(10, 0, 10, 10);
		gbc_lengthField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lengthField.gridx = 1;
		gbc_lengthField.gridy = 7;
		midPanel.add(lengthTxt, gbc_lengthField);
		
		JLabel methodLabel = new JLabel("Payment Method:");
		methodLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_methodLabel = new GridBagConstraints();
		gbc_methodLabel.anchor = GridBagConstraints.EAST;
		gbc_methodLabel.insets = new Insets(10, 0, 10, 10);
		gbc_methodLabel.gridx = 0;
		gbc_methodLabel.gridy = 8;
		midPanel.add(methodLabel, gbc_methodLabel);
		
		methodBox = new JComboBox<String>();
		methodBox.addItem("Cash");
		methodBox.addItem("Card");
		GridBagConstraints gbc_methodField_3 = new GridBagConstraints();
		gbc_methodField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_methodField_3.insets = new Insets(10, 0, 10, 10);
		gbc_methodField_3.gridx = 1;
		gbc_methodField_3.gridy = 8;
		midPanel.add(methodBox, gbc_methodField_3);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerFunctionalities func = new CustomerFunctionalities(cus);
				func.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
		
		JButton btnRequest = new JButton("REQUEST");
		btnRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check to ensure no empty fields
				if(recipientTxt.getText().isBlank() || destinationTxt.getText().isBlank() 
						||weightTxt.getText().isBlank() ||heightTxt.getText().isBlank()
						||widthTxt.getText().isBlank() ||lengthTxt.getText().isBlank()){
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in all required fields.", 
			                "Validation Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				
				String recieve= recipientTxt.getText();
				String des= destinationTxt.getText();
				int zone= (int) zoneBox.getSelectedItem();
				String type= typeBox.getSelectedItem().toString();
				String method= methodBox.getSelectedItem().toString();
				float weight= -1, height=-1, length=-1, width=-1;
				double discount= 0, cost= 0;
				
				//weight is made sure to be a positive number
				try {
					weight= Float.parseFloat(weightTxt.getText().trim());
					if (weight<=0) throw new NumberFormatException();
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Weight must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid weight entered: " + weightTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//height is made sure to be a positive number

				try {
					height = Float.parseFloat(heightTxt.getText().trim());
					if (height<=0) throw new NumberFormatException();
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Height must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid height entered: " + heightTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//length is made sure to be a positive number
				try {
					length = Float.parseFloat(lengthTxt.getText().trim());
					if (length<=0) throw new NumberFormatException();

		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Length must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid length entered: " + lengthTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//width is made sure to be a positive number

				try {
					width = Float.parseFloat(widthTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Width must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid width entered: " + widthTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//setting the values needed to calculate cost
				ship.setWeight(weight);
				ship.setZone(zone);
				ship.setType(type);
				ship.calculateCost();
				
				//discount
				if(ship.getCost()> 150000) {
					discount= ship.getCost()* 0.1;
				}
				
				//dscount calculated from final price
				cost= ship.getCost()- discount;
				
				//Shipment and invoice objects created to be added to the database
				ship= new Shipment(-1, recieve, height, length, width, des, zone, type, cost, weight, "Pending", cus.getUserID() );
				invoice= new Invoice(-1, -1, method, "Unpaid", discount, cost, cost, cus.getUserID());
				
				//User is notified whether or not the the database created the shipment and invoice and the invoice is shown to them
				boolean isRequestd= requestShipment();
				if(isRequestd) {
					JOptionPane.showInternalMessageDialog(null,"Shipment Request was Successful\nTake note of the following Invoice ID for payment and the Tracking Number for tracking", 
							"Shipment Request Status", JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showInternalMessageDialog(null, invoice.toString(), 
							"Invoice", JOptionPane.INFORMATION_MESSAGE);
				} else {
					logger.warn("Shipment request process returned false. Check logs for SQL error details.");
					JOptionPane.showInternalMessageDialog(null,"Shipment Request Failed", 
							"Shipment Request Status", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRequest.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(btnRequest);
		getRootPane().setDefaultButton(btnRequest); //log button is the window default and is used when enter key is clicked
	}
	
	//Insert the shipment and generated invoice into the database
	private boolean requestShipment() {
		int affectedRows= 0;
		
		String sql = "INSERT INTO smartship.shipment ("
		        + "recipientName, height, length, width, destinationAddress, zone, type, cost, weight, status, custID"
		        + ") VALUES ('"
		        + ship.getRecipientName() + "', "
		        + ship.getHeight() + ", "
		        + ship.getLength() + ", "
		        + ship.getWidth() + ", '"
		        + ship.getDestinationAddr() + "', "
		        + ship.getZone() + ", '"
		        + ship.getType() + "', "
		        + ship.getCost() + ", "
		        + ship.getWeight() + ", '"
		        + ship.getStatus() + "', "
		        + ship.getCustID() + ");";
		
		try {
			stmt= myConn.createStatement();
			affectedRows= stmt.executeUpdate(sql);
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error adding the shipment: ", e);
			JOptionPane.showMessageDialog(null,
					"Shipment Request failed due to a SQL error.", 
					"Shipment Request Error", JOptionPane.ERROR_MESSAGE);
		} catch(SQLException e) {
			logger.error("Database Error adding the shipment: ", e);
			JOptionPane.showMessageDialog(null,
					"Shipment Request failed due to a database error.", 
					"Shipment Request Error", JOptionPane.ERROR_MESSAGE);
		}
		
		//The last create shipment ID is retriveed and used to set the shipment and invoice ID in their respective objects
		String sql3 = "SELECT LAST_INSERT_ID();";		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql3);
			
			if(result.next()) {
				ship.setTrackingNum((result.getInt(1))); //get the information based on the column index
				invoice.setTrackingNum((result.getInt(1))); //get the information based on the column index
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error adding the shipment: ", e);
		} catch(SQLException e) {
			logger.error("Database Error retrieving the last insert trackingNumber.", e);
		}
		
		
		String sql1= "INSERT INTO smartship.invoice(trackingNumber, method, status, discount, cost, balance, custID"
				+ ") VALUES ("
		        + invoice.getTrackingNum() + ", '"
		        + invoice.getMethod() + "', '"
		        + invoice.getStatus() + "', "
		        + invoice.getDiscount() + ", "
		        + invoice.getCost() + ", "
		        + invoice.getBalance() + ", "
				+ invoice.getCustID() + ");";
		
		try {
			stmt= myConn.createStatement();
			affectedRows= affectedRows+ stmt.executeUpdate(sql1);
			
			//User notified of the shipment tracking number
			JOptionPane.showInternalMessageDialog(null,"Your tracking number is "+ ship.getTrackingNum(), 
					"Shipment Request", JOptionPane.INFORMATION_MESSAGE);
			logger.info("Shipment Request created. Affected rows: " + affectedRows);
			
			String sql4 = "SELECT LAST_INSERT_ID();";		
			try {
				stmt= myConn.createStatement();
				result= stmt.executeQuery(sql4);
				
				if(result.next()) {
					invoice.setInvoiceID((result.getInt(1))); //get the information based on the column index
				}
			} catch(SQLSyntaxErrorException e) {
				logger.error("SQL Syntax error adding the shipment: ", e);
			} catch(SQLException e) {
				logger.error("Database Error retrieving the last insert trackingNumber.", e);
			}
			
			return affectedRows== 2; //return if it is true
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during request for shipment: " + ship.getTrackingNum(), e);
			JOptionPane.showMessageDialog(null,
					"Shipment Request failed due to a SQL error.", 
					"Shipment Request Error", JOptionPane.ERROR_MESSAGE);
	    } catch(SQLException e) {
			logger.error("Error during request for shipment: " + ship.getTrackingNum(), e);
			JOptionPane.showMessageDialog(null,
					"Shipment Request failed due to a database error.", 
					"Shipment Request Error", JOptionPane.ERROR_MESSAGE);
	    }
		
		return false;
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
