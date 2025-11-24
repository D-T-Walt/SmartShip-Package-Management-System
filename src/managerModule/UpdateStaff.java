//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Clerk;
import system.Driver;
import system.Manager;
import system.User;

public class UpdateStaff extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(UpdateStaff.class);
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
	private Statement stmnt = null;
	private JTextField idTextField;
	
	public UpdateStaff(Manager man) {
		getDatabaseConn();
		
		frame = new JFrame("Staff Update Form");
		frame.setSize(450,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		staffComboBox = new JComboBox(staffOptions);
		staffComboBox.setEditable(true);
		staffComboBox.setBounds(128, 70, 155, 21);
		contentPane.add(staffComboBox);
		
		JLabel header = new JLabel("Staff Account Update");
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		header.setBounds(23, 29, 227, 31);
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
		
		JButton createBtn = new JButton("Update");
		createBtn.setBounds(221, 216, 85, 21);
		contentPane.add(createBtn);
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String idText = idTextField.getText().trim();
					String choice = staffComboBox.getSelectedItem().toString();
					String firstName = firstNameField.getText().trim();
					String lastName = lastNameField.getText().trim();
					String password = passwordField.getText().trim();
					String salaryText = salaryField.getText().trim();
					String hireDateText = hireDateField.getText().trim();
					
					if (idText.isEmpty() || firstName.isEmpty() || lastName.isEmpty() 
							|| password.isEmpty() ||  choice.isEmpty() || salaryText.isEmpty() || hireDateText.isEmpty()){
						JOptionPane.showMessageDialog(
								frame, 
								"Data missing. Fill in all fields", 
								"Validation Error", 
								JOptionPane.ERROR_MESSAGE); // Beep
						return;
					}
					
					int ID = 0;
					double salary = 0;
					int hireYr = 0;
					
					try {
						ID = Integer.parseInt(idText);
						salary = Double.parseDouble(salaryText);
						hireYr = Integer.parseInt(hireDateText);
						if (ID<=0) throw new NumberFormatException();
						if (salary<=0) throw new NumberFormatException();
						if (hireYr<=1900) throw new NumberFormatException();

					} catch (NumberFormatException ex) {
						logger.error("Invalid number format in Update Staff form.", ex);
						JOptionPane.showMessageDialog(
								frame, 
								"ID, Salary, and Year Hired must be valid numbers.", 
								"Input Error", 
								JOptionPane.ERROR_MESSAGE); // Beep
						return;
					}
					
					Clerk newClerk = new Clerk(salary,hireYr);
					Driver newDriver = new Driver(salary,hireYr);
					Manager newManager = new Manager(salary,hireYr);
					
					boolean userAdded;
					
					switch(choice) {
					
						case "Clerk":
							//updating parent class with sub class to populate both user and role tables with
							//the appropriate information
							userAdded = update(new User(ID,firstName,lastName,password),newClerk);
							if (userAdded) {
								JOptionPane.showMessageDialog(
										null, 
										"Clerk updated", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
								UpdateAccView prev = new UpdateAccView(man);
								prev.setVisible(true);
												
								dispose();										
							} else {
								logger.warn("Both tables weren't updated");

								JOptionPane.showMessageDialog(
										null, 
										"Both tables were not updated, Select the right type of staff and reselect the update button\n Or check your ID Number", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
							}
							
							break;
						case "Driver":
							userAdded = update(new User(ID,firstName,lastName,password),newDriver);
							if (userAdded) {
								JOptionPane.showMessageDialog(
										null, 
										"Driver " + ID +" updated", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
								UpdateAccView prev = new UpdateAccView(man);
								prev.setVisible(true);
												
								dispose();
							} else {
								logger.warn("Both tables weren't updated");
								JOptionPane.showMessageDialog(
										null, 
										"Both tables were not updated, Select the right type of staff and reselect the update button\n Or check your ID Number", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
							}
							break;
							
						case "Manager":
							userAdded = update(new User(ID,firstName,lastName,password),newManager);
							if (userAdded) {
								JOptionPane.showMessageDialog(
										null, 
										"Manager created", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
								UpdateAccView prev = new UpdateAccView(man);
								prev.setVisible(true);
												
								dispose();
							} else {
								logger.warn("Both tables weren't updated");
								JOptionPane.showMessageDialog(
										null, 
										"Both tables were not updated, Select the right type of staff and reselect the update button\n Or check your ID Number", 
										"Update Status", 
										JOptionPane.INFORMATION_MESSAGE);
							}
							break;
					}
					}
				});
			
		
		JButton backBtn = new JButton("Back");
		backBtn.setBounds(110, 216, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				UpdateAccView prev = new UpdateAccView(man);
				prev.setVisible(true);
								
				dispose();
			}
		});
		
		JLabel hireDateLabel = new JLabel("Year Hired: ");
		hireDateLabel.setBounds(10, 195, 172, 13);
		contentPane.add(hireDateLabel);
		
		hireDateField = new JTextField();
		hireDateField.setBounds(86, 192, 96, 19);
		contentPane.add(hireDateField);
		hireDateField.setColumns(10);
		
		
		JLabel idLabel = new JLabel("ID:");
		idLabel.setBounds(273, 42, 45, 13);
		contentPane.add(idLabel);
		
		
		idTextField = new JTextField();
		idTextField.setBounds(308, 39, 96, 19);
		contentPane.add(idTextField);
		idTextField.setColumns(10);
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
	
	/*
	private String confirmRole(String ID) {
		String[] tables = {"clerk", "driver", "manager"};
	    String[] columns = {"clerkID", "driverID", "managerID"};

	    for (int i = 0; i < tables.length; i++) {
	        String sql = "SELECT COUNT(*) FROM " + tables[i] + " WHERE " + columns[i] + " = ?";

	        try (PreparedStatement pstmt = myConn.prepareStatement(sql)) {
	            pstmt.setString(1, ID);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next() && rs.getInt(1) > 0) {
	                    return tables[i]; // return which role found
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	*/
	
	
	
	private boolean update(User user,Clerk clerk) {
		String sql = "UPDATE smartship.user "
				+ "SET firstName = '" + user.getFirstName()+"', "
					+"lastName = '"+user.getLastName()+"', "
					+"hashedPassword = '"+User.hashPassword(user.getHashPassword())+"' "
					+ "WHERE userID = '" + user.getUserID()+"';";
		try {
			stmnt = myConn.createStatement();
	        boolean userUpdated = (stmnt.executeUpdate(sql) == 1);

	        boolean custUpdated = updateClerk(user, clerk);

	        return userUpdated && custUpdated;
		}catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during User table update for clerk: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Clerk Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during User table update for clerk: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Clerk Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		 return false;
	}
	
	
	private boolean update(User user,Driver driver) {
		String sql = "UPDATE smartship.user "
				+ "SET firstName = '" + user.getFirstName()+"', "
					+"lastName = '"+user.getLastName()+"', "
					+"hashedPassword = '"+User.hashPassword(user.getHashPassword())+"' "
					+ "WHERE userID = '" + user.getUserID()+"';";
		try {
			stmnt = myConn.createStatement();
	        boolean userUpdated = (stmnt.executeUpdate(sql) == 1);

	        boolean custUpdated = updateDriver(user, driver);

	        return userUpdated && custUpdated;
		}catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during User table update for driver: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Driver Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during User table update for driver: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Driver Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		 return false;
	}
	
	private boolean update(User user,Manager manager) {
		String sql = "UPDATE smartship.user "
				+ "SET firstName = '" + user.getFirstName()+"', "
					+"lastName = '"+user.getLastName()+"', "
					+"hashedPassword = '"+User.hashPassword(user.getHashPassword())+"' "
					+ "WHERE userID = '" + user.getUserID()+"';";
		try {
			stmnt = myConn.createStatement();
	        boolean userUpdated = (stmnt.executeUpdate(sql) == 1);

	        boolean custUpdated = updateManager(user, manager);

	        return userUpdated && custUpdated;
		}catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during User table update for manager: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Manager Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during User table update for manager: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Manager Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		 return false;
	}
	
	
	private boolean updateClerk(User user, Clerk clerk) {
		
		String sql = "UPDATE smartship.clerk "
				+ "SET yearEmployed = '" + clerk.getYearEmployed()+"', "
				+"salary = '"+clerk.getSalary()+"' "
				+ "WHERE clerkID = '" + user.getUserID()+"';";
		
		try {
			//System.out.println(sql);
			stmnt = myConn.createStatement();
			int affectedRows = stmnt.executeUpdate(sql);
			return affectedRows == 1;
		}catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during Clerk table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Clerk Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during Clerk table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Clerk Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		return false;
	}
	
	private boolean updateDriver(User user, Driver Driver) {
		
		String sql = "UPDATE smartship.driver "
				+ "SET yearEmployed = '" + Driver.getYearEmployed()+"', "
				+"salary = '"+Driver.getSalary()+"' "
				+ "WHERE driverID = '" + user.getUserID()+"';";
		
		try {
			//System.out.println(sql);
			stmnt = myConn.createStatement();
			int affectedRows = stmnt.executeUpdate(sql);
			return affectedRows == 1;
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during Driver table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Clerk Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during Driver table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Driver Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		return false;
	}

	private boolean updateManager(User user, Manager manager) {
	
		String sql = "UPDATE smartship.manager "
				+ "SET yearEmployed = '" + manager.getYearEmployed()+"', "
				+"salary = '"+manager.getSalary()+"' "
				+ "WHERE managerID = '" + user.getUserID()+"';";
		
		try {
			stmnt = myConn.createStatement();
			int affectedRows = stmnt.executeUpdate(sql);
			return affectedRows == 1;
		} catch(SQLSyntaxErrorException e) {
			logger.error("SQL Syntax Error during Manager table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Manager Account Update failed due to a SQL error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException e) {
			logger.error("Database Error during Manager table update for user: " + user.getFirstName(), e);
			JOptionPane.showMessageDialog(null,
					"Manager Account Update failed due to a database error.", 
					"Update Accunt Error", JOptionPane.ERROR_MESSAGE);		
		}
		
		return false;
	}
}
