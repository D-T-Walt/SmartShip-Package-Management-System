/*
 * Class Author: Diwani Walters (2303848)
 * */
package customerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Customer;
import system.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Color;
public class CreateAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
    private static final Logger logger = LogManager.getLogger(CreateAccount.class);
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JTextField addressTxt;
    private JTextField phoneTxt;
    private JPasswordField passwordField;
    
    private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;


	//Frame Creation
	public CreateAccount() {
		getDatabaseConn(); //connect to database
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);		
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0)); //Frame content pane layout
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH); 
		
		JLabel headingLabel = new JLabel("CREATE ACCOUNT");
		headingLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headingLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		
		GridBagLayout gbl_midPanel = new GridBagLayout();
		gbl_midPanel.columnWeights = new double[]{0.0, 0.0};
		midPanel.setLayout(gbl_midPanel);
		
		JLabel firstName = new JLabel("First Name: ");
		firstName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_firstName = new GridBagConstraints();
		gbc_firstName.insets = new Insets(10,0,10,10);
		gbc_firstName.gridx = 0;
		gbc_firstName.gridy = 0;
		midPanel.add(firstName, gbc_firstName);
		
		firstNameTxt = new JTextField(25);
		GridBagConstraints gbc_firstNameTxt = new GridBagConstraints();
		gbc_firstNameTxt.insets = new Insets(10,0,10,10);
		gbc_firstNameTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstNameTxt.gridx = 1;
		gbc_firstNameTxt.gridy = 0;
		midPanel.add(firstNameTxt, gbc_firstNameTxt);
		firstNameTxt.setColumns(10);
		
		JLabel lastName = new JLabel("Last Name: ");
		lastName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_lastName = new GridBagConstraints();
		gbc_lastName.anchor = GridBagConstraints.EAST;
		gbc_lastName.insets = new Insets(10,0,10,10);
		gbc_lastName.gridx = 0;
		gbc_lastName.gridy = 1;
		midPanel.add(lastName, gbc_lastName);
		
		lastNameTxt = new JTextField(25);
		GridBagConstraints gbc_lastNameTxt = new GridBagConstraints();
		gbc_lastNameTxt.insets = new Insets(10,0,10,10);
		gbc_lastNameTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastNameTxt.gridx = 1;
		gbc_lastNameTxt.gridy = 1;
		midPanel.add(lastNameTxt, gbc_lastNameTxt);
		lastNameTxt.setColumns(10);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_addressLabel = new GridBagConstraints();
		gbc_addressLabel.anchor = GridBagConstraints.EAST;
		gbc_addressLabel.insets = new Insets(10,0,10,10);
		gbc_addressLabel.gridx = 0;
		gbc_addressLabel.gridy = 2;
		midPanel.add(addressLabel, gbc_addressLabel);
		
		addressTxt = new JTextField(250);
		GridBagConstraints gbc_addressTxt = new GridBagConstraints();
		gbc_addressTxt.insets = new Insets(10,0,10,10);
		gbc_addressTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_addressTxt.gridx = 1;
		gbc_addressTxt.gridy = 2;
		midPanel.add(addressTxt, gbc_addressTxt);
		addressTxt.setColumns(40);
		
		JLabel phoneLabel = new JLabel("Phone Number: ");
		phoneLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_phoneLabel = new GridBagConstraints();
		gbc_phoneLabel.anchor = GridBagConstraints.EAST;
		gbc_phoneLabel.insets = new Insets(10,0,10,10);
		gbc_phoneLabel.gridx = 0;
		gbc_phoneLabel.gridy = 3;
		midPanel.add(phoneLabel, gbc_phoneLabel);
		
		phoneTxt = new JTextField(25);
		GridBagConstraints gbc_phoneTxt = new GridBagConstraints();
		gbc_phoneTxt.insets = new Insets(10,0,10,10);
		gbc_phoneTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_phoneTxt.gridx = 1;
		gbc_phoneTxt.gridy = 3;
		midPanel.add(phoneTxt, gbc_phoneTxt);
		phoneTxt.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.insets = new Insets(10,0,10,10);
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 4;
		midPanel.add(passwordLabel, gbc_passwordLabel);
		
		passwordField = new JPasswordField(25);
		passwordField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(10,0,10,10);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 4;
		midPanel.add(passwordField, gbc_textField);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerMenu1 prevMenu = new CustomerMenu1();
				prevMenu.setVisible(true);
		
				dispose(); 	
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
		
		JButton createButt = new JButton("CREATE ACCOUNT");
		createButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Retrive input from GUI
				String first= firstNameTxt.getText().trim();
				String last= lastNameTxt.getText().trim();
				String address= addressTxt.getText().trim();
				String phone= phoneTxt.getText().trim();
				String password= new String(passwordField.getPassword()).trim();

				//Check inputs for empty fields
				if (first.isEmpty() || last.isEmpty() || address.isEmpty() || phone.isEmpty() || password.isEmpty()) {
		            
		            JOptionPane.showInternalMessageDialog(null, 
		                "Please fill in all required fields.", 
		                "Validation Error", 
		                JOptionPane.ERROR_MESSAGE);
		            
		            return; 
		        }
				
				//User inputs and hashed version of the password is used to create a customer object
				Customer cus= new Customer(0, first, last, User.hashPassword(password) ,address, phone);
				
				//Customer object passed to add the record to the database and shows a sucess message before going back to customer menu
				boolean isCreated= create(cus);
				if(isCreated) {
					JOptionPane.showInternalMessageDialog(null,"Account Created", 
							"Account Status", JOptionPane.INFORMATION_MESSAGE);
					
					CustomerMenu1 prevMenu = new CustomerMenu1();
					prevMenu.setVisible(true);
			
					dispose(); 
				} else {
					//log issue and notify user of failure
					logger.warn("Account creation process returned false. Check logs for SQL error details.");
					JOptionPane.showInternalMessageDialog(null,"Account NOT Created", 
							"Account Status", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		createButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(createButt);
		getRootPane().setDefaultButton(createButt); //log button is the window default and is used when enter key is clicked

	}
	//Function to add the customer to the user and customer table
	private boolean create(Customer cus) {
		int affectedRows= 0;
		
		String sql= "INSERT INTO smartship.user(firstName, lastName, hashedPassword) "
					+ "VALUES ('"+ cus.getFirstName()+"', '"
					+ cus.getLastName()+"', '"+ cus.getHashPassword()+"');";
		
		try {
			stmt= myConn.createStatement(); //
			affectedRows= stmt.executeUpdate(sql); //Execute insert
		} catch(SQLSyntaxErrorException e) {
			//Syntax problem in SQL query logged and user notified
			logger.error("SQL Syntax error during insert for: " + cus.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a SQL error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		} catch(SQLException e) {
			//Connection issue logged and user notified
			logger.error("Error during insert for: " + cus.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a database error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		}
		
		//Last userID that was auto generated is stored
		String sql3 = "SELECT LAST_INSERT_ID();";		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql3);
			
			if(result.next()) {
				//the last store userID is stored in the customer object
				cus.setUserID((result.getInt(1))); //get the information based on the column index
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during insert for: " + cus.getFirstName(), e);
		} catch(SQLException e) {
			logger.error("Database Error retrieving the last insert userID.", e);
		}
		
		//Insert into customer table
		String sql1= "INSERT INTO smartship.customer(custID, address, phone) "
				+ "VALUES ('"+ cus.getUserID()+"', '"
				+ cus.getAddress()+"', '"+ cus.getPhone()+"');";
		
		try {
			stmt= myConn.createStatement();
			affectedRows= affectedRows+ stmt.executeUpdate(sql1);
			
			//User notified of their create userID
			JOptionPane.showInternalMessageDialog(null,"Your UserID for log in is "+ cus.getUserID(), 
					"Account Status", JOptionPane.INFORMATION_MESSAGE);
			logger.info("Customer Account created. Affected rows: " + affectedRows);
			
			return affectedRows== 2; //return true if both table inserts were successful
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during insert for: " + cus.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a SQL error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
	    } catch(SQLException e) {
	    	logger.error("Error during insert for: " + cus.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a database error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
	    }
		
		//if the code reaches here then the inserts failed
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
