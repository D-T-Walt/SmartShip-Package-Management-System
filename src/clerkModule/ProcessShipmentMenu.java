/*
 * Class Author: Olivia McFarlane (2301555)
 * */
package clerkModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.Clerk;
import system.User;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.awt.Font;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessShipmentMenu extends JFrame {
	//Should actually be clerk menu, opts: update package status, assign package to vehicle(show available vehicles and if it already has a route), view invoice
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ProcessShipmentMenu.class);
	private JPanel contentPane;
	
	private Clerk clerk;
	private static Connection myConn = null;
	private Statement stmt = null;
	private ResultSet result = null;


	/**
	 * Create the frame.
	 */
	public ProcessShipmentMenu(User userObj) {
		getDatabaseConn(); //connect to database
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);// not in relation to any component so it just centers the screen


		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel welLabel = new JLabel("PROCESS SHIPMENT MENU");
		welLabel.setFont(new Font("Tahoma", Font.PLAIN, 58));
		topPanel.add(welLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(2, 2, 5, 5));
		
		//Should actually be clerk menu, opts: update package status, assign package to vehicle(show available vehicles and if it already has a route), view invoice
		
		JButton updateBtn = new JButton("Update Package Status");
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PackageStatus pkgStat = new PackageStatus(userObj);
				pkgStat.setVisible(true);
		
				dispose();
			}
		});
		
		updateBtn.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(updateBtn);
		
		JButton assignPkgBtn = new JButton("Assign Package to Vehicle"); 
		assignPkgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateAssignment assignPkg = new CreateAssignment(userObj);
				assignPkg.setVisible(true);
		
				dispose();
			}
		});
		assignPkgBtn.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(assignPkgBtn);
		
		JButton invoiceBtn = new JButton("View Invoice");
		invoiceBtn.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(invoiceBtn);
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProcessInvoice processInv = new ProcessInvoice(userObj);
				processInv.setVisible(true);
				
				dispose();
				
			}
		});
		
		JButton logOutBtn = new JButton("LOGOUT");
		logOutBtn.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(logOutBtn);
		logOutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				login.setVisible(true);
		
				dispose(); 
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
				logger.fatal("ERROR: Failed to connect to the database.", e.getMessage());
	            
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
