//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Clerk;
import system.Driver;
import system.Manager;
import system.User;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import java.awt.Color;

public class StaffForm extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(StaffForm.class);
	private JFrame frame;
	private JPanel contentPane;
	private JComboBox staffComboBox;
	private String[] staffOptions = {"Clerk","Driver","Manager"};
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField passwordField;
	private JTextField salaryField;
	private JTextField hireDateField;
	
	private static Connection myConn = null;
	private Statement stmt = null;

	/**
	 * Create the frame.
	 */
	public StaffForm(Manager man) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBackground(Color.CYAN);
		
		getDatabaseConn();
		
		new JFrame("New Customer Form");
		setSize(450,300);
		setLocationRelativeTo(null);// not in relation to any component so it just centers the screen		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		staffComboBox = new JComboBox(staffOptions);
		staffComboBox.setEditable(true);
		staffComboBox.setBounds(128, 70, 155, 21);
		contentPane.add(staffComboBox);
		
		JLabel header = new JLabel("Staff Account Creation");
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		header.setBounds(110, 29, 227, 31);
		contentPane.add(header);
		
		JLabel staffTypeLabel = new JLabel("Staff Type:");
		staffTypeLabel.setBounds(33, 74, 85, 13);
		contentPane.add(staffTypeLabel);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(10, 114, 66, 13);
		contentPane.add(firstNameLabel);
		
		firstNameField = new JTextField();
		firstNameField.setBounds(86, 111, 96, 19);
		contentPane.add(firstNameField);
		firstNameField.setColumns(10);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(217, 114, 66, 13);
		contentPane.add(lastNameLabel);
		
		lastNameField = new JTextField();
		lastNameField.setBounds(288, 111, 96, 19);
		contentPane.add(lastNameField);
		lastNameField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 160, 66, 13);
		contentPane.add(passwordLabel);
		
		passwordField = new JTextField();
		passwordField.setBounds(83, 157, 99, 19);
		contentPane.add(passwordField);
		
		JLabel salaryLabel = new JLabel("Salary: $");
		salaryLabel.setBounds(217, 160, 59, 13);
		contentPane.add(salaryLabel);
		
		salaryField = new JTextField();
		salaryField.setBounds(288, 157, 96, 19);
		contentPane.add(salaryField);
		salaryField.setColumns(10);
		
		JButton createBtn = new JButton("Create");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
				int ID = 0;
				String choice = staffComboBox.getSelectedItem().toString();
				String firstName = firstNameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String password = passwordField.getText().trim();
				
				double salary= -1;				
				try {
					salary = Double.parseDouble(salaryField.getText().trim());	
					if (salary<=0) throw new NumberFormatException();
				} catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "User salary must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Customer ID entered: " + salaryField.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
								
				int hireYr= -1;				
				try {
					hireYr = Integer.parseInt(hireDateField.getText().trim());	
					if (hireYr<=1900) throw new NumberFormatException();
				} catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "User year must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Customer ID entered: " + hireDateField.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				Clerk newClerk = new Clerk(salary,hireYr);
				Driver newDriver = new Driver(salary,hireYr);
				Manager newManager = new Manager(salary,hireYr);
				
				//checking if all fields are filled before proceeding
				if (firstName.isEmpty() || lastName.isEmpty() 
						|| password.isEmpty() ||  choice.isEmpty()){
					JOptionPane.showMessageDialog(
							frame, 
							"Data missing. Fill in all fields", 
							"Create Status", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean userAdded;
				
				switch(choice) {
					case "Clerk":
						//creating parent class with sub class to populate both user and customer tables with
						//the appropriate information
						userAdded = createClerk(new User(ID,firstName,lastName,password),newClerk);
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
						
						break;
					case "Driver":
						userAdded = createDriver(new User(ID,firstName,lastName,password),newDriver);
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
						break;
						
					case "Manager":
						userAdded = createManager(new User(ID,firstName,lastName,password),newManager);
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
						break;
				}
					
			}
		});
		createBtn.setBounds(221, 216, 85, 21);
		contentPane.add(createBtn);
		
		JButton backBtn = new JButton("Back");
		backBtn.setBounds(110, 216, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateAccView accounts = new CreateAccView(man);
				accounts.setVisible(true);
								
				dispose(); 
			}
		});
		
		JLabel hireDateLabel = new JLabel("Year Hired:");
		hireDateLabel.setBounds(10, 195, 172, 13);
		contentPane.add(hireDateLabel);
		
		hireDateField = new JTextField();
		hireDateField.setBounds(86, 192, 96, 19);
		contentPane.add(hireDateField);
		hireDateField.setColumns(10);
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
	
	private boolean createClerk(User newUser, Clerk newClerk) {

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
	                JOptionPane.showInternalMessageDialog(null,"Clerk's UserID for log in is "+ newUser.getUserID(), 
	    					"Account Status", JOptionPane.INFORMATION_MESSAGE);
	            }

	            boolean clerkAdded = addClerk(newUser, newClerk); // Insert Clerk after USER
	            return clerkAdded;
	        }

	    } catch (SQLException e) {
	    	logger.error("Error during user insert for clerk: " + newUser.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a database error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
	    }

	    return false;
	}
	
	
	private boolean createDriver(User newUser, Driver newDriver) {

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
	                JOptionPane.showInternalMessageDialog(null,"Driver's UserID for log in is "+ newUser.getUserID(), 
	    					"Account Status", JOptionPane.INFORMATION_MESSAGE);
	            }

	            boolean clerkAdded = addDriver(newUser, newDriver); // Insert Clerk after USER
	            return clerkAdded;
	        }

	    } catch (SQLException e) {
	    	logger.error("Error during user insert for driver: " + newUser.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a database error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
	    }

	    return false;
	}

	private boolean createManager(User newUser, Manager newManager) {

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
	                JOptionPane.showInternalMessageDialog(null,"Manager's UserID for log in is "+ newUser.getUserID(), 
	    					"Account Status", JOptionPane.INFORMATION_MESSAGE);
	            }

	            boolean clerkAdded = addManager(newUser, newManager); // Insert Clerk after USER
	            return clerkAdded;
	        }

	    } catch (SQLException e) {
	    	logger.error("Error during user insert for manager: " + newUser.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a database error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);	    }

	    return false;
	}

	
	private boolean addClerk(User user, Clerk clerk) {
		
		String custSql = "INSERT INTO smartship.clerk (clerkID,salary,yearEmployed) "
				   + "VALUES ('"+user.getUserID()+"','"+clerk.getSalary()+"','"+clerk.getYearEmployed()+"');";
		
		try {
			
			stmt = myConn.createStatement();
			int affectedRows = stmt.executeUpdate(custSql);
			return affectedRows == 1;
		} catch (SQLSyntaxErrorException e) {
			logger.error("SQL syntax error during clerk insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			logger.error("Database error during clerk insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a  error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return false;
	}
	
	private boolean addDriver(User user, Driver driver) {
		
		String driverSql = "INSERT INTO smartship.driver (driverID,salary,yearEmployed) "
				   + "VALUES ('"+user.getUserID()+"','"+driver.getSalary()+"','"+driver.getYearEmployed()+"');";
		
		try {
			
			stmt = myConn.createStatement();
			int affectedRows = stmt.executeUpdate(driverSql);
			return affectedRows == 1;
		} catch (SQLSyntaxErrorException e) {
			logger.error("SQL syntax error during driver insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			logger.error("Database error during driver insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a  error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return false;
	}

	private boolean addManager(User user, Manager manager) {
	
		String mngSql = "INSERT INTO smartship.manager (managerID,salary,yearEmployed) "
				   + "VALUES ('"+user.getUserID()+"','"+manager.getSalary()+"','"+manager.getYearEmployed()+"');";
		
		try {
			
			stmt = myConn.createStatement();
			int affectedRows = stmt.executeUpdate(mngSql);
			return affectedRows == 1;
		} catch (SQLSyntaxErrorException e) {
			logger.error("SQL syntax error during clerk insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			logger.error("Database error during clerk insert: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Account Creation failed due to a  error.", 
					"Create Accunt Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return false;
	}
}
