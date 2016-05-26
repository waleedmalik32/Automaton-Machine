import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OpenFile extends JFrame {
	static JFrame frame;
	private static JTextField filename = new JTextField();
	private static JTextField dir = new JTextField();
	private JButton open = new JButton("Open"), save = new JButton("Done");

	static String filenamestr = new String();

	public OpenFile() {
		JPanel p = new JPanel();
		open.addActionListener(new OpenL());
		p.add(open);
		save.addActionListener(new SaveL());
		p.add(save);
		Container cp = getContentPane();
		cp.add(p, BorderLayout.SOUTH);
		dir.setEditable(false);
		filename.setEditable(false);
		p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		p.add(filename);
		p.add(dir);
		cp.add(p, BorderLayout.NORTH);
	}

	class OpenL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(OpenFile.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(c.getSelectedFile().getName());
				dir.setText(c.getCurrentDirectory().toString());
				filenamestr = dir.getText() + "\\" + filename.getText();
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("");
				dir.setText("");
				filenamestr = "";
			}
		}
	}

	class SaveL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MainClass.path = OpenFile.filenamestr;
			frame.dispose();
		}
	}

	public static String main(String[] args) {

		run(250, 110);
		String path = filenamestr;
		return path;
	}

	public static void run(int width, int height) {
		frame = new OpenFile();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
} // /:~

