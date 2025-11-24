/*
 * Class Author: Olivia McFarlane (2301555)
 * */
package clerkModule;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.MainMenu;
import system.Clerk;
import system.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;


public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Login.class);
	private JPanel contentPane;
	private JTextField idTextField;
	private JPasswordField passwordField;

	private Clerk clerk;
	private static Connection myConn = null;
	private Statement stmt = null;
	private ResultSet result = null;


	//Frame Creation
	public Login() {
		logger.info("Initializing Clerk Login GUI.");
		
		getDatabaseConn();
		this.clerk = new Clerk();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel welcomeLabel = new JLabel("WELCOME CLERK :)");
		welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 43));
		panel.add(welcomeLabel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);
		
		Insets insets = new Insets(10, 10, 10, 10);
		
		JLabel idLabel = new JLabel("User ID:");
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_idLabel = new GridBagConstraints();
		gbc_idLabel.anchor = GridBagConstraints.WEST;
		gbc_idLabel.insets = insets;
		gbc_idLabel.gridx = 0;
		gbc_idLabel.gridy = 0;
		panel_1.add(idLabel, gbc_idLabel);
		
		idTextField = new JTextField();
		GridBagConstraints gbc_idTextField = new GridBagConstraints();
		gbc_idTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_idTextField.anchor = GridBagConstraints.WEST;
		gbc_idTextField.insets = insets;
		gbc_idTextField.gridx = 1;
		gbc_idTextField.gridy = 0;
		panel_1.add(idTextField, gbc_idTextField);
		idTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.insets = insets;
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 1;
		panel_1.add(passwordLabel, gbc_passwordLabel);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.insets = insets;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 1;
		gbc_passwordField.anchor = GridBagConstraints.EAST;
		panel_1.add(passwordField, gbc_passwordField);
		passwordField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		JButton backBtn = new JButton("<--    BACK");
		backBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Back button clicked. Returning to MainMenu.");
				MainMenu prevMenu = new MainMenu();
				prevMenu.setVisible(true);
		
				dispose(); 	
				
			}
		});
		
		JButton loginBtn = new JButton("LOGIN   -->");
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_2.add(loginBtn);
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Login button clicked.");
				//Checks for empty fields
				if(idTextField.getText().isBlank() || new String(passwordField.getPassword()).isBlank()) {
					logger.warn("Login failed: User ID or Password fields are empty.");
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in all required fields.", 
			                "Validation Error", 
			                JOptionPane.ERROR_MESSAGE);
			            
			            return; 
				}
				int clerkId = -1;
				
				//Checks if Clerk ID is numeric
				try {
		            clerkId = Integer.parseInt(idTextField.getText().trim());
		        	logger.info("Attempting login for Clerk ID: {}", clerkId);
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Clerk ID must be a number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Clerk ID entered: " + idTextField.getText(), ex);
		            return; // Stop processing if input is invalid
		        }
				
				String password = new String(passwordField.getPassword()).trim();
				
				// Check the clerk login
				boolean loginSuccess = checkLogin(clerkId);
				
				if(loginSuccess) {
					logger.info("Login successful for Clerk ID: {}. Navigating to ProcessShipmentMenu.", clerkId);
					JOptionPane.showInternalMessageDialog(null,"You are Logged In "+ clerk.getFirstName()+ " "+ clerk.getLastName(), 
							"Log in Status", JOptionPane.INFORMATION_MESSAGE);
							User userObject = (User)clerk;
							ProcessShipmentMenu menu = new ProcessShipmentMenu(userObject);
							menu.setVisible(true);
							
							dispose();
				} 
				else {
					logger.warn("Login failed for Clerk ID: {}. Invalid credentials.", clerkId);
					JOptionPane.showInternalMessageDialog(null,"LOG IN FAILED:(\nTRY AGAIN",
							"Log in Status", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
		});
		
		getRootPane().setDefaultButton(loginBtn); //this button is the window default and is used when enter key is clicked


	}
	
	/*
	Checks the clerk by checking their login credentials against the database.
	It returns true if the login is successful or false if unsuccessful.
	*/
	public boolean checkLogin(int clerkId) {
		// SQL query to retrieve clerk details from the user table using an INNER JOIN
		String sql = "SELECT u.userID, u.firstName, u.lastName, u.hashedPassword, cl.salary, cl.yearEmployed "
				+ "FROM smartship.user u "
				+ "INNER JOIN smartship.clerk cl ON u.userID = cl.clerkID "
				+ "WHERE u.userID = " + clerkId + ";";
		
		logger.debug("Executing SQL for checkLogin: {}", sql);
		
		try {
			System.out.println(sql);
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			// Check if a record was returned and populate the clerk object
			if(result.next()) {
				clerk.setUserID(result.getInt("userID")); 
	            clerk.setFirstName(result.getString("firstName"));
	            clerk.setLastName(result.getString("lastName"));
	            clerk.setHashPassword(result.getString("hashedPassword"));
	            clerk.setSalary(result.getDouble("salary")); 
	            clerk.setYearEmployed(result.getInt("yearEmployed"));
	            logger.debug("Clerk data retrieved for ID: {}", clerkId);
			}else {
			    logger.warn("Login failed: No record found for Clerk ID: {}", clerkId);
			    return false; // Clerk ID not found in database
			}
			
			String password= new String(passwordField.getPassword()).trim();
			
			// Compare the hashed user input password with the hashed password from the DB
			if(clerk.getHashPassword().equals(User.hashPassword(password))) {
				logger.info("Clerk ID " + clerkId + " password verified.");
				return true;
			}
		
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during login for Clerk ID: {}. Query: {}", clerkId, sql, e);
			System.err.println(e.getMessage());			
		} catch(SQLException e) {
			logger.error("Database Error during login for Clerk ID: {}", clerkId, e);
			e.printStackTrace();
		}
		
		return false;
	}
	
	// Establishes connection to the database
	private static Connection getDatabaseConn() {
		if(myConn== null) {
			final String url= "jdbc:mysql://localhost:3307/smartship";
			
			try {
				logger.info("Attempting to connect to database at: {}", url);
				myConn= DriverManager.getConnection(url, "root", "usbw");
				logger.info("Database connection successfully established.");
			}
			catch(Exception e) {
				logger.fatal("FATAL ERROR: Failed to connect to the database at {}.", url, e);
				e.printStackTrace();
			}
		}
		return myConn;
	}

}
