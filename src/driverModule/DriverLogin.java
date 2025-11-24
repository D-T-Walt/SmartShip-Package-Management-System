/*
 * Class Author: Diwani Walters (2303848)
 * */
package driverModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.Driver;
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

import gui.MainMenu;

public class DriverLogin extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(DriverLogin.class);

	private JPanel contentPane;
	private JTextField userTxt;
	private JPasswordField passwordField;
	private Driver drive;
	
	private static Connection myConn= null;
	private Statement stmt= null;
	private ResultSet result= null;
	/**
	 * Create the frame.
	 */
	public DriverLogin() {
		getDatabaseConn(); //connect to database
		this.drive = new Driver();
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
		
		JLabel headingLabel = new JLabel("DRIVER LOG IN ");
		headingLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(headingLabel);
		
		JPanel midPanel = new JPanel();
		midPanel.setBackground(Color.WHITE);
		contentPane.add(midPanel, BorderLayout.CENTER);
		GridBagLayout gbl_midPanel = new GridBagLayout();
		midPanel.setLayout(gbl_midPanel);
		
		JLabel userLabel = new JLabel("Driver User ID: ");
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
				//navigate toe previous menu
				MainMenu prevMenu = new MainMenu();
				prevMenu.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
		
		JButton logButt = new JButton("LOG IN");
		logButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//ensure the input fields aren't empty
				if(userTxt.getText().isBlank() || new String(passwordField.getPassword()).isBlank()) {
					JOptionPane.showInternalMessageDialog(null, 
			                "Please fill in all required fields.", 
			                "Validation Error", 
			                JOptionPane.ERROR_MESSAGE);
			            return; 
				}
				int driveId= -1;
				
				//ID parsed as integer and checked to make sure it is an integer before continuing

				try {
		            driveId = Integer.parseInt(userTxt.getText().trim());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showInternalMessageDialog(null, "Driver User ID must be a valid number.", 
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            logger.error("Invalid Driver ID entered: " + userTxt.getText(), ex.getMessage());
		            return; // Stop processing if input is invalid
		        }
				
				//Password taken from the GUI inputs and parsed as a string
				String password= new String(passwordField.getPassword()).trim();

				boolean isLogged= log(driveId, password); 
				if(isLogged) {
					JOptionPane.showInternalMessageDialog(null,"You are Logged In "+ drive.getFirstName()+ " "+ drive.getLastName(), 
							"Log in Status", JOptionPane.INFORMATION_MESSAGE);
					//edited customer object is passed to the next page
					DriverDashboard next = new DriverDashboard(drive);
					next.setVisible(true);
			
					dispose();
				}
				else {
					JOptionPane.showInternalMessageDialog(null,"LOG IN FAILED\nWHO ARE YOUUUUU?!", 
							"Log in Status", JOptionPane.ERROR_MESSAGE);
					
					MainMenu prevMenu = new MainMenu();
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
		
		//Join to ensure that the user logging in is a driver and also to retrive their information
		String sql = "SELECT u.userID, u.firstName, u.lastName, u.hashedPassword, d.salary, d.yearEmployed "
	               + "FROM smartship.user u "
	               + "INNER JOIN smartship.driver d ON u.userID = d.driverID "
	               + "WHERE u.userID = " + id + ";";
		
		try {
			stmt= myConn.createStatement();
			result= stmt.executeQuery(sql);
			
			//Customer information stored in the driver object
			if(result.next()) {
				drive.setUserID(result.getInt("userID")); 
	            drive.setFirstName(result.getString("firstName"));
	            drive.setLastName(result.getString("lastName"));
	            drive.setHashPassword(result.getString("hashedPassword"));
	            drive.setSalary(Double.parseDouble(result.getString("salary"))); 
	            drive.setYearEmployed(Integer.parseInt(result.getString("yearEmployed")));
			}
			
			//the stored customer hash password is checked to make sure it matches the hashed password entered
			//Notifying the user and return a true value
			if(drive.getHashPassword().equals(User.hashPassword(password))) {
				logger.info("Driver ID " + id + " password verified.");
				return true;
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error during driver login.", e);	
			JOptionPane.showMessageDialog(
		            null, 
		            "A database error occurred during login. Try again later.",
		            "Database Error", 
		            JOptionPane.ERROR_MESSAGE
		        );
		} catch(SQLException e) {
			logger.error("Database error during driver login.", e);	
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
