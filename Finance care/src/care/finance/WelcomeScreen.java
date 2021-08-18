package care.finance;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class WelcomeScreen extends JWindow {
	
	
	private static final long serialVersionUID = 1L;
	Font font = new Font(Font.MONOSPACED, Font.PLAIN, 30);
	Font smallfont = new Font(Font.MONOSPACED, Font.PLAIN, 15);
	Color green = new Color(0x00ff1a);
	JLabel welcome = new JLabel("Welcome to the Finance Care");
	JLabel version = new JLabel("Version : v1.0");
	JLabel author = new JLabel("Written by : ANIKET KUMAR MISHRA");

	WelcomeScreen() throws Exception {
		setSize(700, 250);
		setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(0x000000));
		welcome.setFont(font);
		welcome.setBounds(100, 20, 500, 50);
		welcome.setForeground(new Color(0x00ff1a));
		version.setFont(smallfont);
		version.setForeground(green);
		version.setBounds(400, 200, 200, 20);
		author.setFont(smallfont);
		author.setBounds(400, 230, 350, 20);
		author.setForeground(green);
		panel.add(welcome);
		panel.add(version);
		panel.add(author);
		add(panel);
		setVisible(true);
		Thread.sleep(3000);
		dispose();
	}

	 //public static void main(String[] args) throws Exception
	 //{
	 //new WelcomeScreen();
	 //}
}