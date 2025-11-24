//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainMenu;
import system.Manager;
import system.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import javax.swing.SwingConstants;

public class ManagerLogin extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ManagerFrame.class);
	private JPanel contentPane;
	private JTextField idField;
	private JTextField passwordField;
	
	private static Connection myConn =null;
	private Statement stmnt = null;
	private ResultSet result = null;

	/**
	 * Create the frame.
	 */
	public ManagerLogin() {
		getDatabaseConn();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel header = new JLabel("Manager Login");
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		header.setBounds(114, 25, 231, 41);
		contentPane.add(header);
		
		JLabel ldLabel = new JLabel("ID:");
		ldLabel.setBounds(109, 114, 45, 13);
		contentPane.add(ldLabel);
		
		idField = new JTextField();
		idField.setBounds(173, 111, 96, 19);
		contentPane.add(idField);
		idField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(109, 142, 63, 13);
		contentPane.add(passwordLabel);
		
		passwordField = new JTextField();
		passwordField.setBounds(173, 139, 96, 19);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		JButton loginBtn = new JButton("LOGIN");
		loginBtn.setBounds(204, 181, 85, 21);
		contentPane.add(loginBtn);
		loginBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String id = idField.getText().trim();
				String password = passwordField.getText().trim();
				boolean check = retrieve(id,password);
				
				if (!check) {
					JOptionPane.showMessageDialog(ManagerLogin.this, 
							"Invalid ID or Password", 
							"Login Failed", 
							JOptionPane.ERROR_MESSAGE);
					MainMenu prevMenu = new MainMenu();
					prevMenu.setVisible(true);
			
					dispose(); 
					
					return;
				}
				logger.info("Manager Login Successful for ID: " + id);
				JOptionPane.showMessageDialog(ManagerLogin.this, 
		                "Login Successful");
				
				Manager man= new Manager();
				man.setUserID(Integer.parseInt(id));
				ManagerFrame next = new ManagerFrame(man);
				next.setVisible(true);
		
				dispose(); 	
			}			
		});
		
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu prevMenu = new MainMenu();
				prevMenu.setVisible(true);
		
				dispose(); 
				
				return;
			}
		});
		backBtn.setBounds(89, 181, 85, 21);
		contentPane.add(backBtn);
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
	
	
	
	
	private boolean retrieve(String managerID, String password) {
		String sql = "SELECT * FROM manager"
				+ " INNER JOIN user"
				+ " ON manager.managerID = user.userID"
				+ " WHERE manager.managerID = '" + managerID + "';";
		
		try {
			
			stmnt = myConn.createStatement();
			result = stmnt.executeQuery(sql);
			
			if (result.next()) {
	            
	            
	            String storedPass = result.getString("hashedPassword");
	            
	            String enteredHash = User.hashPassword(password);
	            
	            return enteredHash.equals(storedPass); //true only if match
	        
	        }

		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax error during manager login.", e);	
			JOptionPane.showMessageDialog(
		            null, 
		            "A SQL error occurred during login. Try again later.",
		            "Login SQL Error", 
		            JOptionPane.ERROR_MESSAGE
		        );
		} catch(SQLException e) {
			logger.error("Database error during customer login.", e);	
			JOptionPane.showMessageDialog(
		            null, 
		            "A database error occurred during login. Try again later.",
		            "Login Database Error", 
		            JOptionPane.ERROR_MESSAGE
		        );	
		}
		return false;
	}
}
