/*
 * Class Author: Diwani Walters (2303848)
 * */
package customerModule;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import gui.MainMenu;

public class CustomerMenu1 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public CustomerMenu1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,500);
		setLocationRelativeTo(null);		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		JLabel welLabel = new JLabel("WELCOME CUSTOMER!!");
		welLabel.setForeground(Color.BLACK);
		welLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
		topPanel.add(welLabel);
		
		JPanel midPanel = new JPanel();
		contentPane.add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(new GridLayout(1, 0, 5, 5));
		
		JButton createAccButt = new JButton("<html>CREATE<br>ACCOUNT</html>");
		createAccButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Navigate to the creat account page
				CreateAccount createAcc = new CreateAccount();
				createAcc.setVisible(true);
		
				dispose(); 
			}
		});
		createAccButt.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(createAccButt);
		
		JButton btnNewButton_1 = new JButton("LOG IN");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//navigate to the login page
				Login log = new Login();
				log.setVisible(true);
		
				dispose(); 
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 43));
		midPanel.add(btnNewButton_1);
		
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton backButt = new JButton("BACK");
		backButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Navigate back to the main menu
				MainMenu prevMenu = new MainMenu();
				prevMenu.setVisible(true);
		
				dispose(); 
			}
		});
		backButt.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomPanel.add(backButt);

	}

}
