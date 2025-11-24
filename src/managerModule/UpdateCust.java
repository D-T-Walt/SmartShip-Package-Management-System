//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Customer;
import system.Manager;
import system.User;

public class UpdateCust extends JFrame {
	
	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(UpdateCust.class);
	private JFrame frame;
	private JPanel contentPane;
	private JLabel header;
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel addressLabel;
	private JLabel phoneLabel;
	
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField addressField;
	private JTextField phoneField;
	private JTextField cityField;
	private JTextField countryField;
		
	private JButton updateBtn;
	private JButton backBtn;
	
	private static Connection myConn = null;
	private Statement stmnt = null;
	private ResultSet result = null;
	private JLabel passwordLabel;
	private JTextField passwordField;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public UpdateCust(Manager man) {
		
		getDatabaseConn();
		
		frame = new JFrame("New Customer Form");
		frame.setSize(450,450);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		getContentPane().setLayout(null);
		
		firstNameLabel = new JLabel("First name: ");
		firstNameLabel.setBounds(27, 53, 87, 13);
		getContentPane().add(firstNameLabel);
		
		firstNameField = new JTextField();
		firstNameField.setBounds(90, 50, 96, 19);
		getContentPane().add(firstNameField);
		firstNameField.setColumns(10);
		
		header = new JLabel("Update Customer ");
		header.setFont(new Font("Verdana", Font.PLAIN, 18));
		header.setBounds(27, 10, 225, 33);
		getContentPane().add(header);
		
		lastNameLabel = new JLabel("Last Name: ");
		lastNameLabel.setBounds(214, 53, 76, 13);
		getContentPane().add(lastNameLabel);
		
		lastNameField = new JTextField();
		lastNameField.setBounds(276, 50, 96, 19);
		getContentPane().add(lastNameField);
		lastNameField.setColumns(10);
		
		addressLabel = new JLabel("Address line1:");
		addressLabel.setBounds(27, 122, 87, 13);
		getContentPane().add(addressLabel);
		
		phoneLabel = new JLabel("Phone #");
		phoneLabel.setBounds(27, 90, 64, 13);
		getContentPane().add(phoneLabel);
		
		phoneField = new JTextField();
		phoneField.setBounds(90, 87, 96, 19);
		getContentPane().add(phoneField);
		phoneField.setColumns(10);
		
		addressField = new JTextField();
		addressField.setBounds(27, 145, 127, 19);
		getContentPane().add(addressField);
		addressField.setColumns(10);
		
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setBounds(237, 148, 53, 13);
		getContentPane().add(cityLabel);
		
		cityField = new JTextField();
		cityField.setBounds(276, 142, 96, 19);
		getContentPane().add(cityField);
		cityField.setColumns(10);
		
		JLabel countryLabel = new JLabel("Country:");
		countryLabel.setBounds(27, 174, 64, 13);
		getContentPane().add(countryLabel);
		
		countryField = new JTextField();
		countryField.setBounds(90, 171, 96, 19);
		getContentPane().add(countryField);
		countryField.setColumns(10);
		
		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(214, 90, 64, 13);
		contentPane.add(passwordLabel);
		
		passwordField = new JTextField();
		passwordField.setBounds(276, 87, 96, 19);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(127, 216, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				UpdateAccView prev = new UpdateAccView(man);
				prev.setVisible(true);
								
				dispose(); 
			}
		});
		
		updateBtn = new JButton("Update");
		updateBtn.setBounds(248, 216, 85, 21);
		getContentPane().add(updateBtn);
		
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(262, 24, 45, 13);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(288, 21, 96, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		updateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String idText = textField.getText().trim();
				String firstName = firstNameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String phone = phoneField.getText().trim();
				String password = passwordField.getText().trim();
				String addressLine = addressField.getText().trim();
				String city = cityField.getText().trim();
				String country = countryField.getText().trim();
								
				//checking if all fields are filled before proceeding
				if (idText.isEmpty() || firstName.isEmpty() || lastName.isEmpty() 
						|| addressLine.isEmpty() || phone.isEmpty() || 
						city.isEmpty() || country.isEmpty() || password.isEmpty()){
					JOptionPane.showMessageDialog(
							frame, 
							"Data missing. Fill in all fields", 
							"Update Error", 
							JOptionPane.ERROR_MESSAGE); // Beep
					return;
				}
				
				int ID = 0;
				try {
					ID = Integer.parseInt(idText);
				} catch (NumberFormatException ex) {
					logger.error("Invalid Customer ID format entered: " + idText, ex);
					JOptionPane.showMessageDialog(
							frame, 
							"Customer ID must be a valid number.", 
							"Input Error", 
							JOptionPane.ERROR_MESSAGE); // Beep
					return;
				}
				
				Customer newCust = new Customer(createAddress(addressLine, city, country),phone);

				//creating parent class with sub class to populate both user and customer tables with
				//the appropriate information
				boolean userAdded = update(new User(ID,firstName,lastName,password),newCust);
				if (userAdded) {
					JOptionPane.showMessageDialog(
							frame, 
							"User updated", 
							"Update Status", 
							JOptionPane.INFORMATION_MESSAGE);
				}else {
					logger.warn("Both tables weren't updated");
					JOptionPane.showMessageDialog(
							null, 
							"Both tables were not updated, Select the right type of staff and reselect the update button\n Or check your ID Number", 
							"Update Status", 
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
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

	private String createAddress(String addressLine, String city, String country) {
		String address = addressLine +","+ city+ "," + country;
		
		return address;
	}
	
	private boolean update(User user,Customer cust) {
		String sql = "UPDATE smartship.user "
				+ "SET firstName = '" + user.getFirstName()+"', "
					+"lastName = '"+user.getLastName()+"', "
					+"hashedPassword = '"+User.hashPassword(user.getHashPassword())+"' "
					+ "WHERE userID = '" + user.getUserID()+"';";
		try {
			stmnt = myConn.createStatement();
	        boolean userUpdated = (stmnt.executeUpdate(sql) == 1);

	        boolean custUpdated = updateCustomer(user, cust);

	        return userUpdated && custUpdated;
		}catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during User table update for customer: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Customer Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during User table update for customer: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Customer Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		 return false;
	}
	
	private boolean updateCustomer(User user, Customer cust) {
		
		String custSql = "UPDATE smartship.customer "
				+ "SET address = '" + cust.getAddress()+"', "
				+"phone = '"+cust.getPhone()+"' "
				+ "WHERE custID = '" + user.getUserID()+"';";
		
		try {
			//System.out.println(sql);
			stmnt = myConn.createStatement();
			int affectedRows = stmnt.executeUpdate(custSql);
			return affectedRows == 1;
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during Customer table update for User: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Customer Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during Customer table update for User: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Customer Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		return false;
	}
}
