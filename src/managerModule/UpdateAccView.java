//Writen by : Sean Groves #2303829
//			Advanced Programming Mon@11am
package managerModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import system.Manager;

public class UpdateAccView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel header;
	private JButton customerAcc;
	private JButton staffAcc;
	private JButton backBtn;

	/**
	 * Create the frame.
	 */
	public UpdateAccView(Manager man) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 562, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		header = new JLabel("What type of account are we Updating");
		header.setBounds(170, 10, 244, 13);
		contentPane.setLayout(null);
		contentPane.add(header);
		
		//customer account creation
		customerAcc = new JButton("Customer");
		customerAcc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				UpdateCust next = new UpdateCust(man);
				next.setVisible(true);
								
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
				UpdateStaff next = new UpdateStaff(man);
				next.setVisible(true);
								
				dispose();
				
			}
			
		});
		staffAcc.setBounds(127, 33, 119, 43);
		contentPane.add(staffAcc);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(220, 216, 85, 21);
		contentPane.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ManagerFrame next = new ManagerFrame(man);
				next.setVisible(true);
								
				dispose();
			}
		});
		
	}
}


