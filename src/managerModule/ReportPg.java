//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am

package managerModule;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import system.Manager;

public class ReportPg extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(ReportPg.class);
	private JPanel contentPane;
	private String[] reports = {"Total Shipments", "Revenue Report", "Performance", "Vehicle Utilization"}; //Report Types

	private static Connection myConn = null;

	/**
	 * Create the frame.
	 */
	public ReportPg(Manager man) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600); //Increased size for better report viewing
		
		this.setLocationRelativeTo(null);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();

		JLabel title = new JLabel("Generate a report");
		title.setFont(new Font("Tahoma", Font.BOLD, 22));

		JComboBox<String> comboBox = new JComboBox<>(reports);
		JButton generateBtn = new JButton("Generate");
		JButton exportBtn = new JButton("Export to PDF"); //Added Export Button

		topPanel.add(title);
		topPanel.add(comboBox);
		topPanel.add(generateBtn);
		topPanel.add(exportBtn); //Add to panel

		contentPane.add(topPanel, BorderLayout.NORTH);
		    
		JButton backBtn = new JButton("Back");
		topPanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    dispose();
			    new ManagerFrame(man).setVisible(true);
			}
		});

		//Use Monospaced font so columns align correctly using String.format
		JTextArea display = new JTextArea("Select a report type and click Generate.");
		display.setEditable(false);
		display.setFont(new Font("Monospaced", Font.PLAIN, 14)); 

		JScrollPane scrollPane = new JScrollPane(display);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		generateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String choice = comboBox.getSelectedItem().toString();
				display.setText("Generating " + choice + "...\n");

		        try {
		            switch(choice) {
			            case "Total Shipments":
			                totalShipments(display);
			                break;
			            case "Revenue Report":
			                revenueReport(display);
			                break;
			            case "Performance":
			                performanceReport(display);
			                break;
			            case "Vehicle Utilization":
			                vehicleUtilization(display);
			                break;
			            default:
			            	logger.warn("Invalid report selection attempted.");
			                JOptionPane.showMessageDialog(contentPane, "Please select a report type.");
			                break;
		            }
		            //catches the possible SQL exceptions from the report generation
		        } catch (SQLException ex) {
		            logger.error("Database error while generating report: " + choice, ex);
		            
		            JOptionPane.showMessageDialog(contentPane, 
		            		"Database Error: Could not generate report.\nDetails: " + ex.getMessage(), 
		            		"Report Database Error", 
		            		JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		
		//PDF Export Action
		exportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = display.getText();
				if (content.isEmpty() || content.startsWith("Select a report")) {
					JOptionPane.showMessageDialog(contentPane, "Please generate a report first.");
					return;
				}
				
				LocalDateTime now= LocalDateTime.now(); //get the date and time of now
				//Generate a simple filename based on timestamp
				String fileName = "Report_" + now.getDayOfMonth()+"-"+now.getMonthValue()+"-"+now.getYear()+"("+now.getHour()+"_"
				+ now.getMinute()+"_"+now.getSecond()+")" + ".pdf";
				exportToPDF(content, fileName); //The contents of the text area are stored and passed along side the file path
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
	            
	            //Inform the user immediately via JOptionPane
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
	
	//The text area is manipulated to store the total shipments report
	//throws the SQL exception caught in the constructor
	private void totalShipments(JTextArea display) throws SQLException {
	    Connection conn = getDatabaseConn();
	    Statement stmt = conn.createStatement();
	    StringBuilder report = new StringBuilder();
	    
	    report.append("=========================================\n");
	    report.append("       TOTAL SHIPMENTS REPORT\n");
	    report.append("=========================================\n\n");

	    //Daily Stats (Using assignment StartDate as for shipment date)
	    report.append("--- Daily Shipments ---\n");
	    String dailySql = "SELECT startDate, COUNT(*) as cnt FROM assignment GROUP BY startDate"; //counts all the shipments for specific dates
	    ResultSet rs = stmt.executeQuery(dailySql);
	    
	    report.append(String.format("%-15s | %-10s%n", "Date", "Count")); //String format used to format the headings and add to the string
	    report.append("----------------------------\n");
	    while(rs.next()) {
	    	//String format used to format the data and add to the string
	    	report.append(String.format("%-15s | %-10s%n", rs.getString("startDate"), rs.getInt("cnt")));
	    }
	    report.append("\n");

	    //Weekly Stats
	    report.append("--- Weekly Shipments ---\n");
	    String weeklySql = "SELECT YEARWEEK(startDate) as wk, COUNT(*) as cnt FROM assignment GROUP BY wk"; //counts all the shipments for specific weeks
	    rs = stmt.executeQuery(weeklySql);
	    report.append(String.format("%-15s | %-10s%n", "Year-Week", "Count"));
	    report.append("----------------------------\n");
	    while(rs.next()) {
	    	report.append(String.format("%-15s | %-10s%n", rs.getString("wk"), rs.getInt("cnt")));
	    }
	    report.append("\n");

	    //Monthly Stats
	    report.append("--- Monthly Shipments ---\n");
	    String monthlySql = "SELECT DATE_FORMAT(startDate, '%Y-%M') as mth, COUNT(*) as cnt FROM assignment GROUP BY mth"; //counts all the shipments for specific months
	    rs = stmt.executeQuery(monthlySql);
	    report.append(String.format("%-20s | %-10s%n", "Month", "Count"));
	    report.append("---------------------------------\n");
	    while(rs.next()) {
	    	report.append(String.format("%-20s | %-10s%n", rs.getString("mth"), rs.getInt("cnt")));
	    }
	    
	    display.setText(report.toString()); //Report added to text area
	}
	
	//The text area is manipulated to store the revenue report
	//throws the SQL exception caught in the constructor
	private void revenueReport(JTextArea display) throws SQLException {
	    Connection conn = getDatabaseConn();
	    Statement stmt = conn.createStatement();
	    StringBuilder report = new StringBuilder();

	    report.append("=========================================\n");
	    report.append("           REVENUE REPORT\n");
	    report.append("=========================================\n\n");
	    
	    String sql = "SELECT invoiceID, cost, balance, status FROM invoice";
	    ResultSet rs = stmt.executeQuery(sql);
	    
	    report.append(String.format("%-10s | %-12s | %-12s | %-10s%n", "Inv ID", "Cost ($)", "Bal ($)", "Status"));
	    report.append("------------------------------------------------------\n");
	    
	    double totalCost = 0;
	    double totalBalance = 0;
	    
	    //Each invoice data is added to the report and the total cost and balance are kept track of
	    while(rs.next()) {
	    	double cost = rs.getDouble("cost");
	    	double bal = rs.getDouble("balance");
	    	totalCost += cost;
	    	totalBalance += bal;
	    	
	    	report.append(String.format("%-10d | %-12.2f | %-12.2f | %-10s%n", 
	    			rs.getInt("invoiceID"), cost, bal, rs.getString("status")));
	    }
	    
	    report.append("------------------------------------------------------\n");
	    report.append(String.format("%-10s | %-12.2f | %-12.2f | %-10s%n", "TOTALS", totalCost, totalBalance, ""));
	    report.append("\n");
	    report.append("Total Earned (Cost - Bal): $" + String.format("%.2f", (totalCost - totalBalance))); //total earned is printed

	    display.setText(report.toString()); //Report added to text area
	}
	
	//The text area is manipulated to store the performance report
	//throws the SQL exception caught in the constructor
	private void performanceReport(JTextArea display) throws SQLException {
	    Connection conn = getDatabaseConn();
	    Statement stmt = conn.createStatement();
	    StringBuilder report = new StringBuilder();

	    report.append("=========================================\n");
	    report.append("        DELIVERY PERFORMANCE\n");
	    report.append("=========================================\n\n");

	    //List all shipments and their status
	    String sql = "SELECT s.trackingNumber, s.recipientName, a.startDate, a.endDate, "
	    		+ "DATEDIFF(a.endDate, a.startDate) as daysTaken "
	    		+ "FROM assignment a "
	    		+ "JOIN shipment s ON s.trackingNumber = a.trackingNumber";
	    
	    ResultSet rs = stmt.executeQuery(sql);
	    
	    report.append(String.format("%-10s | %-15s | %-10s | %-10s%n", "Track #", "Recipient", "Days", "Status"));
	    report.append("------------------------------------------------------\n");
	    
	    int onTimeCount = 0;
	    int delayedCount = 0;
	    
	    
	    while(rs.next()) {
	    	int days = rs.getInt("daysTaken");
	    	String status = "On Time";
	    	
	    	//7 days is delayed. If endDate is null (0 days), it would be in transit
	    	if (days > 7) {
	    		status = "DELAYED";
	    		delayedCount++; //number of delayed incremented
	    	} else {
	    		onTimeCount++; //number of ontime incremented
	    	}
	    	
	    	//Shipment information is added along with their Delay status
	    	report.append(String.format("%-10d | %-15s | %-10d | %-10s%n", 
	    			rs.getInt("trackingNumber"), 
	    			truncate(rs.getString("recipientName"), 15), 
	    			days, status));
	    }
	    
	    report.append("\n");
	    report.append("--- Summary ---\n");
	    report.append("On Time Shipments: " + onTimeCount + "\n");
	    report.append("Delayed Shipments: " + delayedCount + "\n");
	    
	    display.setText(report.toString()); //Report added to textArea
	}
	
	//The text area is manipulated to store the vehicle utilisation report
	//throws the SQL exception caught in the constructor
	private void vehicleUtilization(JTextArea display) throws SQLException {
		Connection conn = getDatabaseConn();
	    Statement stmt = conn.createStatement();
	    StringBuilder report = new StringBuilder();

	    report.append("=========================================\n");
	    report.append("        VEHICLE UTILIZATION\n");
	    report.append("=========================================\n\n");
	    
	    String sql = "SELECT plateNumber, weightCapacity, availableWeight, quantityCapacity, availableQuantity FROM vehicle";
	    ResultSet rs = stmt.executeQuery(sql);
	    
	    report.append(String.format("%-10s | %-8s | %-8s | %-8s | %-8s | %-8s%n", 
	    		"Plate", "W.Cap", "W.Avail", "Q.Cap", "Q.Avail", "Status"));
	    report.append("------------------------------------------------------------------\n");
	    
	    int fullCount = 0;
	    int totalVehicles = 0;
	    
	    //number of full vehicles are added based on the available and capacity values
	    while(rs.next()) {
	    	totalVehicles++;
	    	float wCap = rs.getFloat("weightCapacity");
	    	float wAvail = rs.getFloat("availableWeight");
	    	float qCap = rs.getFloat("quantityCapacity");
	    	float qAvail = rs.getFloat("availableQuantity");
	    	
	    	//Determine status
	    	String status = "Available";
	    	if (wAvail <= 0 || qAvail <= 0) {
	    		status = "FULL";
	    		fullCount++;
	    	} else if (wAvail < wCap || qAvail < qCap) {
	    		status = "Partial";
	    	}
	    	
	    	report.append(String.format("%-10s | %-8.0f | %-8.0f | %-8.0f | %-8.0f | %-8s%n", 
	    			rs.getString("plateNumber"), wCap, wAvail, qCap, qAvail, status));
	    }
	    
	    report.append("\n");
	    /*If there is at least one vehicle, calculate the utilization percentage.
	    Otherwise, set utilization to 0.*/
	    float utilPercent = (totalVehicles > 0) ? ((float)fullCount / totalVehicles * 100) : 0;
	    report.append("Full Capacity Vehicles: " + fullCount + "/" + totalVehicles + "\n");
	    report.append(String.format("Fleet Utilization (Full): %.2f%%%n", utilPercent));

	    display.setText(report.toString());
	}
	
	private void exportToPDF(String content, String filePath) {
	    try {
	        //Create PDF document
	        com.lowagie.text.Document document = new com.lowagie.text.Document();
	        //give the file path of the document created
	        com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));

	        //open document
	        document.open();

	        //sent the font of the document
	        com.lowagie.text.Font monoFont =
	                new com.lowagie.text.Font(com.lowagie.text.Font.COURIER, 10);

	        //Add the contents of the textArea passed by string to the document
	        document.add(new com.lowagie.text.Paragraph(content, monoFont));

	        document.close(); //close document

	        JOptionPane.showMessageDialog(this,
	                "PDF Exported Successfully to:\n" + filePath);

	    } catch (Exception e) {
	    	logger.error("Failed to export PDF file.", e);
	        JOptionPane.showMessageDialog(this,
	                "Error Exporting PDF: " + e.getMessage());
	    }
	}
	
	private String truncate(String str, int max) {
		if (str == null) return ""; //if the string is null and empty string is returned
		return str.length() > max ? str.substring(0, max-2) + ".." : str; //if the string is longer that the max string length, Take a substring from index 0 up to (max - 2)
																			//then append 2 dots
	}
}