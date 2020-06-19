package care.finance;

import java.awt.Dimension;

import javax.swing.JFrame;

public class PAN {
	JFrame frame;

	PAN() {
		frame = new JFrame("PAN");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(500, 500));
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
