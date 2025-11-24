/*
 * Class Author: Olivia McFarlane (2301555)
 * */
package clerkModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.MainMenu;
import system.User;

public class ProcessInvoice extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ProcessInvoice.class);
	private JPanel contentPane;
    private JTextField amtTxt;
    private JTextArea invoiceDetailsArea;

    private static Connection myConn = null;
    private Statement stmt = null;
    private ResultSet result = null;
    private double currentBalance = 0.0;
    private int currentInvID = -1;

	//Frame Creation
	public ProcessInvoice(User userObj) {
		getDatabaseConn();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLocationRelativeTo(null);// not in relation to any component so it just centers the screen

        contentPane = new JPanel();
        contentPane.setBackground(Color.CYAN);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel topPanel = new JPanel();
        JLabel headLabel = new JLabel("PROCESS INVOICE");
        headLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
        topPanel.add(headLabel);
        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel midPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPane.add(midPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        midPanel.add(leftPanel);

        Insets insets = new Insets(10, 10, 10, 10);

        // Invoice number label
        JLabel invoiceLabel = new JLabel("Invoice Number: ");
        GridBagConstraints gbc_invoiceLabel = new GridBagConstraints();
        gbc_invoiceLabel.insets = insets;
        invoiceLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        gbc_invoiceLabel.gridx = 0; 
        gbc_invoiceLabel.gridy = 0; 
        gbc_invoiceLabel.anchor = GridBagConstraints.WEST;
        leftPanel.add(invoiceLabel, gbc_invoiceLabel);

        // Invoice input text field
        JComboBox<Integer> availableInvIDs = new JComboBox<>();
        availableInvIDs.setFont(new Font("Tahoma", Font.PLAIN, 35));
		GridBagConstraints gbc_availableInvIDs = new GridBagConstraints();
		gbc_availableInvIDs.fill = GridBagConstraints.HORIZONTAL;
		gbc_availableInvIDs.insets = insets;
		gbc_availableInvIDs.gridx = 1;
		gbc_availableInvIDs.gridy = 0;
		loadInvIDs(availableInvIDs);
		leftPanel.add(availableInvIDs, gbc_availableInvIDs);

        // Button to process invoice
        JButton retrieveButton = new JButton("Retrieve Invoice");
        GridBagConstraints gbc_retrieveButton = new GridBagConstraints();
        gbc_retrieveButton.insets = insets;
        retrieveButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
        gbc_retrieveButton.gridx = 1; 
        gbc_retrieveButton.gridy = 1; 
        gbc_retrieveButton.fill = GridBagConstraints.NONE;
        leftPanel.add(retrieveButton, gbc_retrieveButton);
     
        retrieveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer invNum =  (Integer) availableInvIDs.getSelectedItem();
         		logger.debug("Retrieve Invoice button clicked. Selected ID: {}", invNum);
         		
                if (invNum == null) {
                	logger.warn("Retrieval failed: No invoice selected in dropdown.");
                	JOptionPane.showMessageDialog(null,
                            "No invoices selected :(. Please select from the options provided.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                retrieveInvoice(invNum);
            }
        });
        
        // Amount Paid Label
        JLabel amtLabel = new JLabel("Amount Paid: ");
        GridBagConstraints gbc_amtLabel = new GridBagConstraints();
        amtLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        gbc_amtLabel.gridx = 0; 
        gbc_amtLabel.gridy = 2; 
        gbc_amtLabel.anchor = GridBagConstraints.WEST;
        leftPanel.add(amtLabel, gbc_amtLabel);

        // Amount Paid input field
        amtTxt = new JTextField(20);
        GridBagConstraints gbc_amtTxt = new GridBagConstraints();
        amtTxt.setFont(new Font("Tahoma", Font.PLAIN, 35));
        gbc_amtTxt.gridx = 1; 
        gbc_amtTxt.gridy = 2; 
        gbc_amtTxt.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(amtTxt, gbc_amtTxt);

        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        midPanel.add(scrollPanel);

        invoiceDetailsArea = new JTextArea();
        invoiceDetailsArea.setEditable(false);
        invoiceDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 28));
        scrollPanel.setViewportView(invoiceDetailsArea);

        JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
        
		JButton backBtn = new JButton("<--    BACK");
		backBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bottomPanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Back button clicked. Returning to ProcessShipmentMenu.");
				ProcessShipmentMenu prevMenu = new ProcessShipmentMenu(userObj);
				prevMenu.setVisible(true);
		
				dispose(); 	
				
			}
		});
		
		JButton submitBtn = new JButton("SUBMIT");
		submitBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bottomPanel.add(submitBtn);
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.debug("Submit button clicked.");
				
				if (currentInvID == -1) {
					logger.warn("Submit failed: No invoice retrieved.");
					JOptionPane.showMessageDialog(null,
			                "Retrieve an invoice first.",
			                "Error", JOptionPane.ERROR_MESSAGE);
			        return;
				}
				
				double amtPaid;
				try {
					amtPaid = Double.parseDouble(amtTxt.getText().trim());
					if (amtPaid <= 0) {
					    logger.warn("Submit failed: Amount paid is not positive: {}", amtPaid);
					    JOptionPane.showMessageDialog(null, "Amount Paid must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
					    return;
					}
					logger.info("Processing payment of ${} for Invoice ID {}", amtPaid, currentInvID);
				}catch (NumberFormatException ex) {
					logger.error("Input validation failed: Amount Paid must be numeric. Input: {}", amtTxt.getText(), ex);
					JOptionPane.showMessageDialog(null,
			                "Amount Paid must be numeric.",
			                "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				double diff = amtPaid - currentBalance;
				double newBal = currentBalance - amtPaid;
				double prevBalance = currentBalance;
				updateInvStat(currentInvID, diff, newBal);
				
				String receiptMessage;
                
                if (diff < 0) {
                    // Partial payment scenario
                    logger.info("Invoice {} partially paid. Remaining balance: ${}", currentInvID, String.format("%.2f", newBal));
                    receiptMessage = String.format(
                        "PAYMENT RECEIPT (Partial)\n" +
                        "===========================\n" +
                        "Invoice ID: %d\n" +
                        "---------------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "---------------------------\n" +
                        "Remaining Balance: $%.2f",
                        currentInvID, prevBalance, amtPaid, newBal
                    );
                } else if (diff == 0) {
                    // Fully paid scenario (exact or overpaid)
                    logger.info("Invoice {} fully paid (exact amount).", currentInvID);
                    receiptMessage = String.format(
                        "PAYMENT RECEIPT (Paid)\n" +
                        "======================\n" +
                        "Invoice ID: %d\n" +
                        "----------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "----------------------\n" +
                        "Balance Due:    $0.00\n" +
                        "Change Due:     $0.00\n" +
                        "======================",
                        currentInvID, prevBalance, amtPaid
                    );
                }else{
					logger.info("Invoice {} fully paid with change. Change due: ${}", currentInvID, String.format("%.2f", diff));
					receiptMessage = String.format(
                        "PAYMENT RECEIPT (Paid)\n" +
                        "======================\n" +
                        "Invoice ID: %d\n" +
                        "----------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "----------------------\n" +
                        "Balance Due:    $0.00\n" +
                        "Change Due:     $%.2f\n" +
                        "======================",
                        currentInvID, prevBalance, amtPaid, diff
                    );
				}
				// Display receipt 
				JOptionPane.showMessageDialog(null, receiptMessage, "Payment Status", JOptionPane.INFORMATION_MESSAGE);
				
				ProcessShipmentMenu prevMenu = new ProcessShipmentMenu(userObj);
				prevMenu.setVisible(true);
		
				dispose();
			}
		});
		
		JButton submitAssignBtn = new JButton("SUBMIT & ASSIGN");
		submitAssignBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bottomPanel.add(submitAssignBtn);

		submitAssignBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				logger.debug("Submit & Assign button clicked.");
				
		        if (currentInvID == -1) {
		            logger.warn("Submit & Assign failed: No invoice retrieved.");
		            
		            JOptionPane.showMessageDialog(null,
		                    "Retrieve an invoice first.",
		                    "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        double amtPaid;
		        try {
		            amtPaid = Double.parseDouble(amtTxt.getText().trim());
		        	if (amtPaid <= 0) {
					    logger.warn("Submit & Assign failed: Amount paid is not positive: {}", amtPaid);
					    JOptionPane.showMessageDialog(null, "Amount Paid must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
					    return;
					}
		            logger.info("Processing payment of ${} for Invoice ID {} before creating assignment.", amtPaid, currentInvID);
		        } catch (NumberFormatException ex) {
		            logger.error("Input validation failed: Amount Paid must be numeric. Input: {}", amtTxt.getText(), ex);
		            JOptionPane.showMessageDialog(null,
		                    "Amount Paid must be numeric.",
		                    "Input Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        double diff = amtPaid - currentBalance;
		        double newBal = currentBalance - amtPaid;
				double prevBalance = currentBalance;

		        // Update DB (same as regular submit)
		        updateInvStat(currentInvID, diff, newBal);
				String receiptMessage;
		        // Full Payment Check
		        if (diff < 0) {
		            logger.info("Invoice {} partially paid. Remaining balance: ${}", currentInvID, String.format("%.2f", newBal));
                    receiptMessage = String.format(
                        "PAYMENT RECEIPT (Partial)\n" +
                        "===========================\n" +
                        "Invoice ID: %d\n" +
                        "---------------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "---------------------------\n" +
                        "Remaining Balance: $%.2f",
                        currentInvID, prevBalance, amtPaid, newBal
                    );
                    JOptionPane.showMessageDialog(null, receiptMessage, "Payment Status", JOptionPane.INFORMATION_MESSAGE);
		            logger.warn("Assignment blocked: Invoice {} remains partially paid. New balance: ${}", currentInvID, newBal);
		            JOptionPane.showMessageDialog(null,
		                    "Cannot proceed to assignment.\n"
		                    + "This invoice is not fully paid.",
		                    "Payment Required",
		                    JOptionPane.ERROR_MESSAGE);
		            ProcessShipmentMenu prevMenu = new ProcessShipmentMenu(userObj);
					prevMenu.setVisible(true);
		
					dispose(); 	
		        }else if (diff == 0) {
                    // Fully paid scenario (exact or overpaid)
                    logger.info("Invoice {} fully paid (exact amount).", currentInvID);
                    receiptMessage = String.format(
                        "PAYMENT RECEIPT (Paid)\n" +
                        "======================\n" +
                        "Invoice ID: %d\n" +
                        "----------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "----------------------\n" +
                        "Balance Due:    $0.00\n" +
                        "Change Due:     $0.00\n" +
                        "======================",
                        currentInvID, prevBalance, amtPaid
                    );
                }else{
					logger.info("Invoice {} fully paid with change. Change due: ${}", currentInvID, String.format("%.2f", diff));
					receiptMessage = String.format(
                        "PAYMENT RECEIPT (Paid)\n" +
                        "======================\n" +
                        "Invoice ID: %d\n" +
                        "----------------------\n" +
                        "Previous Balance: $%.2f\n" +
                        "Amount Paid:    $%.2f\n" +
                        "----------------------\n" +
                        "Balance Due:    $0.00\n" +
                        "Change Due:     $%.2f\n" +
                        "======================",
                        currentInvID, prevBalance, amtPaid, diff
                    );
				}
				JOptionPane.showMessageDialog(null, receiptMessage, "Payment Status", JOptionPane.INFORMATION_MESSAGE);
				logger.info("Invoice {} fully paid. Proceeding to CreateAssignment screen.", currentInvID);
		        JOptionPane.showMessageDialog(null,
		                "Invoice fully paid! Proceeding to assignment.",
		                "Success",
		                JOptionPane.INFORMATION_MESSAGE);

		        // Launch Assignment screen
		        CreateAssignment assignPkg = new CreateAssignment(userObj);
				assignPkg.setVisible(true);
		
				dispose();;
		        
		    }
		});
	}
	
	
	// Loads invoice IDs with 'Unpaid' or 'Partially Paid' status into the JComboBox.
	private void loadInvIDs(JComboBox<Integer> combo) {
		logger.debug("Loading unpaid/partially paid Invoice IDs.");
		combo.removeAllItems();
		
		String sql = "SELECT invoiceID FROM smartship.invoice "
					+ "WHERE status IN ('Unpaid', 'Partially Paid')"; 
		
		try {
			stmt = myConn.createStatement();
			result = stmt.executeQuery(sql);
			while(result.next()) {
				combo.addItem(result.getInt("invoiceID"));
			}
			logger.info("Loaded eligible Invoice IDs into dropdown.");
		}catch(SQLException e) {
			logger.error("SQL Error loading Invoice IDs:", e);
		}
		
	}
	
	// Retrieves the details of a specific invoice and updats the display area.
	private void retrieveInvoice(int invNum) {
		logger.info("Retrieving details for Invoice ID: {}", invNum);
		String details = null;
		String sql = "SELECT * FROM smartship.invoice "
                + "WHERE invoiceID = " + invNum + ";";

        try {
            stmt = myConn.createStatement();
            result = stmt.executeQuery(sql);

            if (result.next()) {
                currentInvID = result.getInt("invoiceID");
                currentBalance = result.getDouble("balance");
                
            	details =
                      " INVOICE DETAILS\n"
                    + " ----------------------------\n"
                    + " Invoice Number: " + result.getInt("invoiceID") + "\n"
                    + " Tracking Number: " + result.getInt("trackingNumber") + "\n"
                    + " Payment Method: " + result.getString("method") + "\n"
                    + " Payment Status: " + result.getString("status") + "\n"
                    + " Cost: $" + String.format("%.2f", result.getDouble("cost"))+ "\n"
                    + " Discount: $" + String.format("%.2f", result.getDouble("discount"))+ "\n"
                    + " Balance: $" + String.format("%.2f", result.getDouble("balance")) + "\n"
                    + " Customer ID: " + result.getInt("custID") + "\n";
                invoiceDetailsArea.setText(details);
                logger.info("Invoice {} details loaded. Current Balance: ${}", currentInvID, String.format("%.2f", currentBalance));
            } else {
                invoiceDetailsArea.setText("");
                logger.warn("Invoice ID {} not found in database.", invNum);
                JOptionPane.showMessageDialog(null,
                        "Invoice not found.",
                        "No Record", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            logger.error("Database Error retrieving Invoice ID {}:", invNum, e);
            JOptionPane.showMessageDialog(null,
                "Error retrieving invoice.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	// Updates the invoice status and balance in the database after a payment.
    private void updateInvStat(int currentInvID, double diff, double newBal) {
    	String sql = "";
    	
    	if (diff < 0) {
    		sql = "UPDATE smartship.invoice SET status = 'Partially Paid', balance = " + newBal
					+ " WHERE invoiceID = " + currentInvID + ";";
		}else{
			sql = "UPDATE smartship.invoice SET status = 'Paid', balance = 0 "
					+ "WHERE invoiceID = " + currentInvID + ";";
		}
    	
    	try {
			stmt= myConn.createStatement();
			stmt.executeUpdate(sql);
			logger.info("Invoice {} status updated.", currentInvID);		
		}catch(SQLException e) {
			logger.error("Failed to update invoice status for Invoice {}:", currentInvID, e);
			JOptionPane.showMessageDialog(null, 
					"Failed to update invoice status.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
    }

	// Establishes connection to the database
    private static Connection getDatabaseConn() {
        if (myConn == null) {
            logger.info("Attempting to establish database connection.");
            try {
                myConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/smartship", "root", "usbw"
                );
                logger.info("Database connection successfully established.");
            } catch (Exception e) {
                logger.fatal("FATAL ERROR: Could not connect to the database.", e);
                JOptionPane.showMessageDialog(null,
                    "Could not connect to DB.",
                    "DB Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        return myConn;
    }

}
