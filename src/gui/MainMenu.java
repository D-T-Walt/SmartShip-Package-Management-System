package gui;

import java.awt.EventQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import customerModule.CustomerMenu1;
import driverModule.DriverLogin;
import managerModule.ManagerFrame;
import managerModule.ManagerLogin;
import managerModule.StaffForm;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private static Connection myConn= null;

    private static final Logger logger = LogManager.getLogger(MainMenu.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		getDatabaseConn(); //connect to database
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);// not in relation to any component so it just centers the screen


		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel welLabel = new JLabel("WELCOME!! Choose your role");
		welLabel.setFont(new Font("Tahoma", Font.PLAIN, 58));
		topPanel.add(welLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(2, 2, 5, 5));
		
		JButton customerButton = new JButton("CUSTOMER");
		customerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerMenu1 customerMenu = new CustomerMenu1();
				customerMenu.setVisible(true);
		
				dispose(); 
			}
		});
		customerButton.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(customerButton);
		
		JButton clerkButt = new JButton("CLERK");
		clerkButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clerkModule.Login clerkLog = new clerkModule.Login();
				clerkLog.setVisible(true);
		
				dispose(); 
			}
		});
		clerkButt.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(clerkButt);
		
		JButton driveButt = new JButton("DRIVER");
		driveButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DriverLogin driverLog = new DriverLogin();
				driverLog.setVisible(true);
		
				dispose(); 
			}
		});
		driveButt.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(driveButt);
		
		JButton manButton = new JButton("MANAGER");
		manButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerLogin manLog = new ManagerLogin();
				manLog.setVisible(true);
		
				dispose(); 
			}
		});
		manButton.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(manButton);

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
