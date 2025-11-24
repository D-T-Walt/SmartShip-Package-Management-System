//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import system.Manager;
import system.Vehicle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JButton;

public class AddVehicle extends JFrame {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(AddVehicle.class);
	private JFrame frame;
	private JPanel contentPane;
	private JTextField plateField;
	private JTextField modelField;
	private JTextField quantityField;
	private JTextField weightField;

	private static Connection myConn = null;
	private Statement stmt = null;

	/**
	 * Create the frame.
	 */
	public AddVehicle(Manager man) {
		getDatabaseConn();
		
		frame = new JFrame("New Vehicle Form");
		frame.setSize(450,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel header = new JLabel("Add Vehicle");
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		header.setBounds(10, 10, 293, 60);
		contentPane.add(header);
		
		plateField = new JTextField();
		plateField.setBounds(94, 70, 96, 19);
		contentPane.add(plateField);
		plateField.setColumns(10);
		
		JLabel plateLabel = new JLabel("License Plate:");
		plateLabel.setBounds(10, 73, 96, 13);
		contentPane.add(plateLabel);
		
		JLabel modelLabel = new JLabel("Model:");
		modelLabel.setBounds(243, 73, 45, 13);
		contentPane.add(modelLabel);
		
		modelField = new JTextField();
		modelField.setBounds(312, 70, 96, 19);
		contentPane.add(modelField);
		modelField.setColumns(10);
		
		JLabel quantityLabel = new JLabel("Quantity Cap. : ");
		quantityLabel.setBounds(10, 117, 111, 13);
		contentPane.add(quantityLabel);
		
		quantityField = new JTextField();
		quantityField.setBounds(94, 114, 96, 19);
		contentPane.add(quantityField);
		quantityField.setColumns(10);
		
		JLabel weightLabel = new JLabel("Weight Cap. :");
		weightLabel.setBounds(229, 117, 100, 13);
		contentPane.add(weightLabel);
		
		weightField = new JTextField();
		weightField.setBounds(312, 114, 96, 19);
		contentPane.add(weightField);
		weightField.setColumns(10);
		
		JButton addBtn = new JButton("Add");
		addBtn.setBounds(258, 216, 85, 21);
		contentPane.add(addBtn);
		addBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String plate = plateField.getText().trim();
				String model = modelField.getText().trim();
				String weightCapTxt= weightField.getText().trim();
				String quantityCapTxt = quantityField.getText().trim();
				
				//checking if all fields are filled before proceeding
				if (plate.isEmpty() || model.isEmpty() 
						|| weightCapTxt.isEmpty() ||  quantityCapTxt.isEmpty()){
					JOptionPane.showMessageDialog(
							frame, 
							"Data missing. Fill in all fields", 
							"Create Status", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				float quantityCap = 0;
				float weightCap= 0;
				
				try {
					weightCap = Float.parseFloat(weightCapTxt);
					quantityCap = Float.parseFloat(quantityCapTxt);
					
					if(weightCap <= 0 || quantityCap <= 0) {
						throw new NumberFormatException("Negative or Zero value");
					}
				} catch (NumberFormatException ex) {
					logger.error("Invalid number format for vehicle weight or quantity capacity: " + ex.getMessage());
					JOptionPane.showMessageDialog(
							frame, 
							"Invalid Weight or Quantity.", 
							"Input Error", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Vehicle v = new Vehicle(plate,model,weightCap,quantityCap);
				
				boolean vehicleAdded = create(v);
				if (vehicleAdded) {
					JOptionPane.showMessageDialog(
							frame, 
							"Vehicle added", 
							"Create Status", 
							JOptionPane.INFORMATION_MESSAGE);
					
					ManagerFrame prev = new ManagerFrame(man);
					prev.setVisible(true);
									
					dispose();
				}
			}
		});
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(105, 216, 85, 21);
		contentPane.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			
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
	
	public boolean create(Vehicle v) {
		
		String sql = "INSERT INTO smartship.vehicle "
		        + "(plateNumber, model, weightCapacity, quantityCapacity, availableWeight, availableQuantity) "
		        + "VALUES ('" + v.getPlateNum() + "', '" + v.getModel() + "', " + v.getWeightCapacity() + ", "
		        + v.getQuantityCapacity() + ", " + v.getWeightCapacity() + ", " + v.getQuantityCapacity() + ")";

		try {
	        stmt = myConn.createStatement();
	        int rows= stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	        
	        if (rows > 0) {
	        	logger.info("Vehicle added with plate number " + v.getPlateNum());
	            return true; //success
	        }
	    } catch (SQLIntegrityConstraintViolationException e) {
	    	//Handles duplicate plateNumber
			logger.error("Duplicate Plate Number entered: " + e.getMessage());
			JOptionPane.showMessageDialog(null, 
					"License Plate Number already exists", 
					"Duplicate Plate Number", 
					JOptionPane.ERROR_MESSAGE);
			
		} catch (SQLException e) {
	    	logger.error("Databse Error adding vehicle: " + e.getMessage());
			JOptionPane.showMessageDialog(null, 
					"An error occurred while adding the vehicle.", 
					"Vehicle Database Error", 
					JOptionPane.ERROR_MESSAGE);
	    }
		
		return false;
	}
}
