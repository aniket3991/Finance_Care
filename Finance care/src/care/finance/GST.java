package care.finance;

import javax.swing.*;
import java.awt.*;

public class GST {
	JFrame frame;

	String[] searchitem = { "Name", "PAN", "DOB", "Aadhar" };

	GST() {
		frame = new JFrame("GST");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(500, 500));
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
