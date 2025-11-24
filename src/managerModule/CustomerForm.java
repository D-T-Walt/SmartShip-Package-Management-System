//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

public class CustomerForm extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(CustomerForm.class);
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
		
	private JButton createBtn;
	private JButton backBtn;
	
	private static Connection myConn = null;
	private Statement stmt = null;
	private JLabel passwordLabel;
	private JTextField passwordField;

	/**
	 * Create the frame.
	 */
	public CustomerForm(Manager man) {
		
		getDatabaseConn();
		
		frame = new JFrame("New Customer Form");
		frame.setSize(450,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
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
		
		header = new JLabel("New Customer Form");
		header.setFont(new Font("Verdana", Font.PLAIN, 18));
		header.setBounds(132, 10, 240, 33);
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
				CreateAccView accounts = new CreateAccView(man);
				accounts.setVisible(true);
								
				dispose(); 
			}
		});
		
		createBtn = new JButton("Create");
		createBtn.setBounds(248, 216, 85, 21);
		getContentPane().add(createBtn);
		
		createBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int ID = 0;
				String firstName = firstNameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String phone = phoneField.getText().trim();
				String password = passwordField.getText().trim();
				String addressLine = addressField.getText().trim();
				String city = cityField.getText().trim();
				String country = countryField.getText().trim();
				Customer newCust = new Customer(createAddress(addressLine, city, country),phone);
				
				//checking if all fields are filled before proceeding
				if (firstName.isEmpty() || lastName.isEmpty() 
						|| addressLine.isEmpty() || phone.isEmpty() || 
						city.isEmpty() || country.isEmpty() || password.isEmpty()){
					JOptionPane.showMessageDialog(
							frame, 
							"Data missing. Fill in all fields", 
							"Create Status", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//creating parent class with sub class to populate both user and customer tables with
				//the appropriate information
				boolean userAdded = createCus(new User(ID,firstName,lastName,password),newCust);
				if (userAdded) {
					JOptionPane.showMessageDialog(
							frame, 
							"User created", 
							"Create Status", 
							JOptionPane.INFORMATION_MESSAGE);
					CreateAccView accounts = new CreateAccView(man);
					accounts.setVisible(true);
									
					dispose(); 
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
	
	private boolean createCus(User newUser, Customer newCust) {

	    String hashedPassword = User.hashPassword(newUser.getHashPassword());

	    String userSql = "INSERT INTO smartship.user (firstname, lastname, hashedPassword) "
	            + "VALUES ('" + newUser.getFirstName() + "', '" + newUser.getLastName() + "', '" + hashedPassword + "')";

	    try {
	        stmt = myConn.createStatement();
	        int affectedRows = stmt.executeUpdate(userSql, Statement.RETURN_GENERATED_KEYS);

	        if (affectedRows == 1) {
	            // Get generated userID
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                newUser.setUserID(generatedKeys.getInt(1));  // Store new ID
	            }

	            boolean custAdded = addCustomer(newUser, newCust); // Insert CUSTOMER after USER
	            return custAdded;
	        }

	    } catch (SQLException e) {
	    	logger.error("Database error creating User account for: " + newUser.getFirstName(), e);
			JOptionPane.showMessageDialog(null, 
					"Database Error creating User account", 
					"Database Error", JOptionPane.ERROR_MESSAGE);	
	    }

	    return false;
	}

	
	private String createAddress(String addressLine, String city, String country) {
		String address = addressLine +","+ city+ "," + country;
		
		return address;
	}
	private boolean addCustomer(User user, Customer cust) {
		
		String custSql = "INSERT INTO smartship.customer (custID,address,phone) "
				   + "VALUES ('"+user.getUserID()+"','"+cust.getAddress()+"','"+cust.getPhone()+"');";
		
		try {
			//System.out.println(sql);
			stmt = myConn.createStatement();
			int affectedRows = stmt.executeUpdate(custSql);
			if(affectedRows == 1) {
				JOptionPane.showMessageDialog(null, 
                		"Customer account created. Their new UserID is: " + user.getUserID(), 
                		"Account Created", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		} catch (SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error inserting customer.", e);
			JOptionPane.showMessageDialog(null, 
					"SQL Syntax Error inserting customer details", 
					"Database Error", JOptionPane.ERROR_MESSAGE);
			
		} catch (SQLException e) {
			logger.error("Database Error inserting customer for ID: " + user.getUserID(), e);
			JOptionPane.showMessageDialog(null, 
					"User created, but failed to add Customer details.", 
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return false;
	}
}
