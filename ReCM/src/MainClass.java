import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainClass {
	private JFrame frame;
	static String path;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClass window = new MainClass();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainClass(){
		frame = new JFrame("RECM");
		frame.setBounds(100, 100, 609, 398);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel lblRecmRegularExpression = new JLabel("RecM- Regular Expression Combo Machine");
		lblRecmRegularExpression.setFont(new Font("Serif", Font.PLAIN, 24));
		lblRecmRegularExpression.setForeground(Color.RED);
		lblRecmRegularExpression.setBounds(101, 21, 492, 54);
		frame.getContentPane().add(lblRecmRegularExpression);
		
		JButton btnNewButton = new JButton("Select Input File");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				OpenFile.main(null);
			}
		});

		btnNewButton.setBounds(199, 230, 204, 79);
		frame.getContentPane().add(btnNewButton);
		
		JButton button = new JButton("Initiate Machine");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ConverterFrame.main(null);
			}
		});

		button.setBounds(199, 103, 204, 79);
		frame.getContentPane().add(button);
		
		JLabel lblProjectDevelopers = new JLabel("<html><center><b><u>Project Developers</u> : <font color=BLUE>Ibraheem Ajmal, Faizan Zafar, Armaan Javed and Waleed Malik</font></b>");
		lblProjectDevelopers.setBounds(68, 344, 503, 14);
		frame.getContentPane().add(lblProjectDevelopers);
		
	}
}
