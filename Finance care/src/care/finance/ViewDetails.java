package care.finance;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ViewDetails {
	JFrame frame;

	ViewDetails() {
		frame = new JFrame("View Details");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(500, 500));

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
