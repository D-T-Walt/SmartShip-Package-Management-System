/*
 * Class Author: Diwani Walters (2303848)
 * */
package customerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.Customer;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomerFunctionalities extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	public CustomerFunctionalities(Customer cus) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel welLabel = new JLabel("WELCOME "+ cus.getFirstName().toUpperCase()+"!!");
		welLabel.setForeground(Color.BLACK);
		welLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(welLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(1, 0, 5, 5));
		
		JButton shipmentButt = new JButton("<html>REQUEST<br>SHIPMENT</html>");
		shipmentButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Navigate to the request shipment page
				RequestShipment request = new RequestShipment(cus);
				request.setVisible(true);
		
				dispose(); 
			}
		});
		shipmentButt.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(shipmentButt);
		
		JButton trackButt = new JButton("<html>TRACK<br>PACKAGE</html>");
		trackButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//navigate to the shipemt tracking page
				PackageTracking tracking = new PackageTracking(cus);
				tracking.setVisible(true);
		
				dispose(); 
			}
		});
		trackButt.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(trackButt);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//navigate to the previous menu
				CustomerMenu1 prevMenu = new CustomerMenu1();
				prevMenu.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);
		
	}

}
