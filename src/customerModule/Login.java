/*
 * Class Author: Diwani Walters (2303848)
 *  */
package customerModule;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.Customer;
import system.User;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(Login.class);

	private JPanel contentPane;
	private JTextField userTxt;
	private JPasswordField passwordField;
	private Customer cus;
	
	private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;
	/**
	 * Create the frame.
	 */
	public Login() {
		getDatabaseConn(); //connect to database
		this.cus = new Customer();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel headingLabel = new JLabel("LOG IN ");
		headingLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headingLabel);
		
		JPanel midPanel = new JPanel();
		midPanel.setBackground(Color.WHITE);
		contentPane.add(midPanel, BorderLayout.CENTER);
		GridBagLayout gbl_midPanel = new GridBagLayout();
		midPanel.setLayout(gbl_midPanel);
		
		JLabel userLabel = new JLabel("Customer ID: ");
		userLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_userLabel = new GridBagConstraints();
		gbc_userLabel.insets = new Insets(10, 0, 10, 10);
		gbc_userLabel.gridx = 0;
		gbc_userLabel.gridy = 0;
		midPanel.add(userLabel, gbc_userLabel);
		
		userTxt = new JTextField(10);
		GridBagConstraints gbc_userTxt = new GridBagConstraints();
		gbc_userTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_userTxt.insets = new Insets(10, 0, 10, 10);
		gbc_userTxt.gridx = 1;
		gbc_userTxt.gridy = 0;
		midPanel.add(userTxt, gbc_userTxt);
		
		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.insets = new Insets(10, 0, 10, 10);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 4;
		midPanel.add(passwordLabel, gbc_passwordLabel);
		
		passwordField = new JPasswordField(10);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(10, 0, 10, 10);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 4;
		midPanel.add(passwordField, gbc_passwordField);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//navigate back to the previous menu
				CustomerMenu1 prevMenu = new CustomerMenu1();
				prevMenu.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
		
		JButton logButt = new JButton("LOG IN");
		logButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Input fields checked to make sure not empty
				if(userTxt.getText().isBlank() || new String(passwordField.getPassword()).isBlank()) {
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in all required fields.", 
			                "Validation Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				int cusId= -1;
				
				//ID parsed as integer and checked to make sure it is an integer before continuing
				try {
		            cusId = Integer.parseInt(userTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Customer ID must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Customer ID entered: " + userTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//Password taken from the GUI inputs and parsed as a string
				String password= new String(passwordField.getPassword()).trim();

				//ID and Password checked against database and kicks the user from the page if the info in incorrect
				boolean isLogged= log(cusId, password); 
				if(isLogged) {
					JOptionPane.showInternalMessageDialog(null,"You are Logged In "+ cus.getFirstName()+ " "+ cus.getLastName(), 
							"Log in Status", JOptionPane.INFORMATION_MESSAGE);
					
					//edited customer object is passed to the next menu
					CustomerFunctionalities next = new CustomerFunctionalities(cus);
					next.setVisible(true);
			
					dispose();
				}
				else {
					JOptionPane.showInternalMessageDialog(null,"LOG IN FAILED\nWHO ARE YOUUUUU?!", 
							"Log in Status", JOptionPane.ERROR_MESSAGE);
					
					CustomerMenu1 prevMenu = new CustomerMenu1();
					prevMenu.setVisible(true);
			
					dispose(); 
					
					return;
				}
			}
		});
		logButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(logButt);
		
		getRootPane().setDefaultButton(logButt); //log button is the window default and is used when enter key is clicked
	}
	
	public boolean log(int id, String password) {
		
		//Join to ensure that the user logging in is a customer and also to retrive their information
		String sql = "SELECT u.userID, u.firstName, u.lastName, u.hashedPassword, c.address, c.phone "
	               + "FROM smartship.user u "
	               + "INNER JOIN smartship.customer c ON u.userID = c.custID "
	               + "WHERE u.userID = " + id + ";";
		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			//Customer information stored in the customer object
			if(result.next()) {
				cus.setUserID(result.getInt("userID")); 
	            cus.setFirstName(result.getString("firstName"));
	            cus.setLastName(result.getString("lastName"));
	            cus.setHashPassword(result.getString("hashedPassword"));
	            cus.setAddress(result.getString("address")); 
	            cus.setPhone(result.getString("phone"));
			}
			
			//the stored customer hash password is checked to make sure it matches the hashed password entered
			//Notifying the user and return a true value
			if(cus.getHashPassword().equals(User.hashPassword(password))) {
				logger.info("Customer ID " + id + " password verified.");
				return true;
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error during customer login.", e);	
			JOptionPane.showMessageDialog(
		            null, 
		            "A database error occurred during login. Try again later.",
		            "Database Error", 
		            JOptionPane.ERROR_MESSAGE
		        );
		} catch(SQLException e) {
			logger.error("Database error during customer login.", e);	
			JOptionPane.showMessageDialog(
		            null, 
		            "A database error occurred during login. Try again later.",
		            "Database Error", 
		            JOptionPane.ERROR_MESSAGE
		        );		}
		
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
