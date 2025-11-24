//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Manager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateAccView extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(CreateAccView.class);

	private JPanel contentPane;
	private JLabel header;
	private JButton customerAcc;
	private JButton staffAcc;
	private JButton backBtn;
	private GridBagConstraints gbc;
	private static Connection myConn = null;

	/**
	 * Create the frame.
	 */
	public CreateAccView(Manager man) {
		getDatabaseConn();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 562, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
		setContentPane(contentPane);
		header = new JLabel("What type of account are we creating");
		header.setBounds(170, 10, 222, 13);
		contentPane.setLayout(null);
		contentPane.add(header);
		
		//customer account creation
		customerAcc = new JButton("Customer");
		customerAcc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CustomerForm form = new CustomerForm(man);
				form.setVisible(true);
		
				dispose();
			}
		});
		
		customerAcc.setBounds(273, 33, 119, 43);
		contentPane.add(customerAcc);
		
		
		//clerk creation button
		staffAcc = new JButton("Staff Account");
		staffAcc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
								
				StaffForm form = new StaffForm(man);
				form.setVisible(true);
		
				dispose();
				
			}
			
		});
		staffAcc.setBounds(127, 33, 119, 43);
		contentPane.add(staffAcc);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(225, 208, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ManagerFrame prev = new ManagerFrame(man);
				prev.setVisible(true);
		
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
