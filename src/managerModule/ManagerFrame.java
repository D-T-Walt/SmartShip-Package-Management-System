//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import clerkModule.ProcessShipmentMenu;
import gui.MainMenu;
import system.Manager;
import system.User;

import javax.swing.JTextArea;


/*
  FEATURES
 	add,update,delete accounts for drivers, customers,clerks
 	view different reports
 	
 */
public class ManagerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ManagerFrame.class);

	private JPanel contentPane;
	private JButton createAccBtn;
	private JButton updateAccBtn;
	private JButton deleteAccBtn;
	private JButton addVehicleBtn;
	private JButton	reportBtn;
	private JButton backBtn;
	private GridBagConstraints gbc;
	private JButton assignmentBtn;
	
	private static Connection myConn = null;	

	/**
	 * Create the frame.
	 */
	public ManagerFrame(Manager man) {
		getDatabaseConn();
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,400);
		contentPane = new JPanel();
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
		
		createAccBtn = new JButton("Create Account");
		gbc.gridx = 0;
		gbc.gridy= 1 ;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(createAccBtn,gbc);
		createAccBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateAccView accounts = new CreateAccView(man);
				accounts.setVisible(true);
		
				dispose();
			}
		});
		
		updateAccBtn = new JButton("Update Account");
		gbc.gridx = 0;
		gbc.gridy= 2 ;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(updateAccBtn,gbc);
		updateAccBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				UpdateAccView accounts = new UpdateAccView(man);
				accounts.setVisible(true);
		
				dispose();
			}
		});
		
		deleteAccBtn = new JButton("Delete Account");
		gbc.gridx = 0;
		gbc.gridy= 3 ;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(deleteAccBtn,gbc);
		deleteAccBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				AccDelete accounts = new AccDelete(man);
				accounts.setVisible(true);
		
				dispose();
				
			}
		});
		
		addVehicleBtn = new JButton("Add Vehicle");
		gbc.gridx = 3;
		gbc.gridy=  1;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(addVehicleBtn,gbc);
		addVehicleBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				AddVehicle add = new AddVehicle(man);
				add.setVisible(true);
		
				dispose();
			}
		});
		
		reportBtn = new JButton("Generate Report");
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(reportBtn,gbc);
		reportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				ReportPg add = new ReportPg(man);
				add.setVisible(true);
		
				dispose();
			}
		});
		
		assignmentBtn = new JButton("Clerk Functionalities");
		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(assignmentBtn,gbc);
		assignmentBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				User use= (User)man; //Polymorphism
				
				ProcessShipmentMenu next = new ProcessShipmentMenu(use);
				next.setVisible(true);
		
				dispose();
			}
		});
		
		backBtn = new JButton("Back to Menu");
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(backBtn,gbc);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				MainMenu add = new MainMenu();
				add.setVisible(true);
		
				dispose();
			}
		});
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 79, 416, 159);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); //for alignment
		textArea.setEditable(false); //make it read-only
		contentPane.add(textArea);

		String allUsers = getAllUsers();
		if (allUsers != null) {
		    textArea.setText(allUsers);
		} else {
		    textArea.setText("There are no users in the database");
		}
		
		
		
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
	
	public String getAllUsers() {
	    String sql = "SELECT u.userID, u.firstName, u.lastName, c.address, c.phone, "
	            + "cl.salary AS clerkSalary, cl.yearEmployed AS clerkYear, "
	            + "d.salary AS driverSalary, d.yearEmployed AS driverYear, "
	            + "m.salary AS managerSalary, m.yearEmployed AS managerYear "
	            + "FROM smartship.user u "
	            + "LEFT JOIN customer c ON c.custID = u.userID "
	            + "LEFT JOIN clerk cl ON cl.clerkID = u.userID "
	            + "LEFT JOIN driver d ON d.driverID = u.userID "
	            + "LEFT JOIN manager m ON managerID = u.userID "
	            + "ORDER BY u.userID ASC";

	    try (Statement stmnt = myConn.createStatement();
	         ResultSet result = stmnt.executeQuery(sql)) {

	        StringBuilder table = new StringBuilder();
	        
	        
	        table.append(String.format("%-6s %-12s %-12s %-15s %-12s %-6s %-8s%n",
	                "ID", "First", "Last", "Address", "Phone", "Employed", "Salary"));
	        table.append("-".repeat(75)).append("\n");

	        boolean hasRows = false;
	        while (result.next()) {
	            hasRows = true;
	            
	            // Combine clerk/driver/manager fields using whichever is available
	            Object year = result.getObject("clerkYear");
	            if (year == null) {
	            	year = result.getObject("driverYear");
	            	if(year == null) {
	            		year = result.getObject("managerYear");
	            	}
	            }
	            
	            Object salary = result.getObject("clerkSalary");
	            if (salary == null) {
	            	salary = result.getObject("driverSalary");
	            			if (salary == null) {
	            				salary = result.getObject("managerSalary");
	            			}
	            }
	            
	            table.append(String.format("%-6d %-12s %-12s %-15s %-12s %-6s %-8s%n",
	                    result.getInt("userID"),
	                    nullSafe(result.getString("firstName")),
	                    nullSafe(result.getString("lastName")),
	                    truncate(result.getString("address"), 15),
	                    nullSafe(result.getString("phone")),
	                    nullSafe(year),
	                    nullSafe(salary)));
	        }

	        return hasRows ? table.toString() : null;

	    } catch (SQLException e) {
	    	logger.error("Database error retrieving list of all users", e);
	        JOptionPane.showMessageDialog(null, 
	        		"Failed to retrieve user list from database.", 
	        		"Database Error", 
	        		JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	}

	// Helper methods
	private String nullSafe(Object value) {
	    return value == null ? "-" : value.toString();
	}

	private String truncate(String str, int maxLen) {
	    if (str == null) return "-";
	    return str.length() > maxLen ? str.substring(0, maxLen - 2) + ".." : str;
	}
}
