package care.finance;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;

public class Services {
	JPanel panel;
	public static Font font = new Font("The Times Roman", Font.BOLD, 16);

	String[] service = { "Client Entry", "Income Tax", "GST", "PAN", "Other Services", "View Details" };

	Services() {
		JFrame frame = new JFrame();
		frame.setBounds(365, 133, 600, 500);

		panel = new JPanel();
		panel = addServices(service, frame);

		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws Exception {
		new WelcomeScreen();
		new Services();
	}

	// fuction to set set buttons and frame for closing current frame

	public static JPanel addServices(String[] buttons, JFrame frame) {
		int i = 0;
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		for (i = 0; i < buttons.length; i++) {
			gbc.gridy = i;
			gbc.insets = new Insets(5, 0, 5, 0);
			JButton button = new JButton(buttons[i]);

			button.setFont(font);
			button.setPreferredSize(new Dimension(280, 60));

			button.requestFocus();
			button.addActionListener(e -> {
				if (button.getText() == "Client Entry") {
					new Main();
					frame.dispose();
				}

				else if (button.getText() == "Income Tax") {
					new IncomeTax();
					frame.dispose();
				} else if (button.getText() == "GST") {
					new GST();
					frame.dispose();

				} else if (button.getText() == "PAN") {
					new PAN();
					frame.dispose();

				} else if (button.getText() == "Other Services") {
					new OtherServices();
					frame.dispose();

				} else if (button.getText() == "View Details") {
					new ViewDetails();
					frame.dispose();
				} else {
					// JOptionPane op = new JOptionPane();

				}
			});
			panel.add(button, gbc);
		}

		return panel;
	}

}
