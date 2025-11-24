//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Manager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import javax.swing.JButton;

public class AccDelete extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ManagerFrame.class);

	private JPanel contentPane;
	private JTextField textField;
	
	private static Connection myConn = null;

	/**
	 * Create the frame.
	 */
	public AccDelete(Manager man) {
		getDatabaseConn();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel header = new JLabel("Which Account would you like to delete?");
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		header.setBounds(31, 36, 416, 55);
		contentPane.add(header);
		
		JLabel IDLabel = new JLabel("ID:");
		IDLabel.setBounds(139, 118, 45, 13);
		contentPane.add(IDLabel);
		
		textField = new JTextField();
		textField.setBounds(171, 115, 96, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton deleteBtn = new JButton("Delete");
		deleteBtn.setBounds(247, 160, 85, 21);
		contentPane.add(deleteBtn);
		deleteBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {

		        String idText = textField.getText().trim();

		        if (idText.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Please enter a User ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        int ID;
		        try {
		            ID = Integer.parseInt(idText);
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "User ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        String role = confirmRole(ID);

		        if (role == null) {
		            JOptionPane.showMessageDialog(null, "No user found with that ID.", "Not Found", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        // Perform deletion using the logged-in manager's ID
		        delete(role, ID, man);
		    }
		});

		
		JButton backBtn = new JButton("Back");
		backBtn.setBounds(111, 160, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				ManagerFrame main = new ManagerFrame(man);
				main.setVisible(true);
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
	
	private String confirmRole(int ID) {
		String[] tables = {"clerk", "driver", "manager"};
	    String[] columns = {"clerkID", "driverID", "managerID"};

	    for (int i = 0; i < tables.length; i++) {
	        String sql = "SELECT COUNT(*) FROM " + tables[i] + " WHERE " + columns[i] + " = ?";

	        try (PreparedStatement pstmt = myConn.prepareStatement(sql)) {
	            pstmt.setInt(1, ID);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next() && rs.getInt(1) > 0) {
	                    return tables[i]; //return which role was found
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	private void delete(String role, int ID, Manager man) {

	    // Prevent deleting the currently logged-in manager
	    if (ID == man.getUserID()) {
	        JOptionPane.showMessageDialog(
	                this,
	                "You cannot delete the manager who is currently logged in.",
	                "Access Denied",
	                JOptionPane.WARNING_MESSAGE
	        );
	        return;
	    }

	    // Extra validation: Driver must NOT have active shipments
	    if ("driver".equals(role)) {

	        String checkSql =
	            "SELECT COUNT(*) AS total FROM shipment s "
	          + "INNER JOIN assignment a ON a.trackingNumber = s.trackingNumber "
	          + "WHERE a.driverID = ? AND s.status IN ('In Transit', 'Assigned')";

	        try (PreparedStatement checkStmt = myConn.prepareStatement(checkSql)) {

	            checkStmt.setInt(1, ID);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next() && rs.getInt("total") > 0) {
	                JOptionPane.showMessageDialog(
	                        this,
	                        "This driver cannot be deleted because they are currently assigned to active shipments.",
	                        "Deletion Blocked",
	                        JOptionPane.ERROR_MESSAGE
	                );
	                return;
	            }

	        } catch (SQLException e) {
	            logger.error("Error checking driver active shipments.", e);
	            JOptionPane.showMessageDialog(this, "A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	    }

	    // DELETE from role table
	    String roleSQL  = "DELETE FROM " + role + " WHERE " + role + "ID = ?";
	    String userSQL  = "DELETE FROM user WHERE userID = ?";

	    try (
	        PreparedStatement pstmntRole = myConn.prepareStatement(roleSQL);
	        PreparedStatement pstmntUser = myConn.prepareStatement(userSQL)
	    ) {

	        pstmntRole.setInt(1, ID);
	        pstmntUser.setInt(1, ID);

	        pstmntRole.executeUpdate();
	        pstmntUser.executeUpdate();

	        logger.info("Account with userID {} deleted successfully.", ID);
	        JOptionPane.showMessageDialog(this, "User deleted successfully!");

	    } catch (SQLException e) {
	        logger.error("Error deleting user: " + ID, e);
	        JOptionPane.showMessageDialog(
	                this,
	                "A database error occurred while deleting the user.",
	                "Deletion Error",
	                JOptionPane.ERROR_MESSAGE
	        );
	        return;
	    }

	    // Return to manager dashboard only after success
	    this.setVisible(false);
	    new ManagerFrame(man).setVisible(true);
	    dispose();
	}

}
