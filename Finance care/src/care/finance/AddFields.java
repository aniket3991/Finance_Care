package care.finance;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
 // class
// 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
public class AddFields {

	// Functions for Client Entry frames
	// ***********************************************************************

	static Font font = new Font(Font.DIALOG, Font.PLAIN, 20);
	static Color color = new Color(0x00ff1a);

	// Client entry panel
	public static JPanel addEntryFields(String[] fields, JTextField textfield[], JPasswordField[] passwordfield) {

		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		int textcounter = 0;
		int passwordcounter = 0;
		for (int i = 0; i < fields.length; i++) {
			JLabel label = new JLabel(fields[i]);
			label.setFont(font);
			if (fields[i].contentEquals("GST Password :") || fields[i].contentEquals("Password :")) {
				passwordfield[passwordcounter] = new JPasswordField(20);
				passwordfield[passwordcounter].setFont(font);
				passwordfield[passwordcounter].setEchoChar('*');

				panel.add(label);
				panel.add(passwordfield[passwordcounter]);
				passwordcounter++;
			} else {
				textfield[textcounter] = new JTextField(20);
				textfield[textcounter].setFont(font);
				label.setLabelFor(textfield[textcounter]);
				if (fields[i].equals("Contact No. :") || fields[i].equals("Aadhar No. :"))
					setValidationForNumber(textfield[textcounter]);
				if (fields[i].equals("DOB(dd-mm-yyyy)"))
					setValidDate(textfield[textcounter]);
				panel.add(label);
				panel.add(textfield[textcounter]);
				textcounter++;
			}

		}

		panel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Customer Details"));
		return panel;
	}

	// Text field validation only for numbers

	public static void setValidationForNumber(JTextField numbertext) {
		numbertext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyChar() == '\b')
					numbertext.setEditable(true);

				else
					numbertext.setEditable(false);
			}
		});
	}

	// Date field

	public static void setValidDate(JTextField datetext) {
		datetext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyChar() == '\b' || e.getKeyChar() == '-')
					datetext.setEditable(true);

				else
					datetext.setEditable(false);
			}
		});
	}

	// *****************************************************************************

	// Methods for Income Tax frame

	// *****************************************************************************

	public static JPanel addIncomeTaxField(String[] fields, JComboBox<String>[] done_due, String[] msg, JFrame frame) {
		JPanel panel = new JPanel(new GridLayout(fields.length, 3, 5, 5));
		JLabel label;
		JButton[] notebutton = new JButton[2];
		String[] paymentbox = { "Select", "Done", "Due", "Process", "Other" };
		JTextArea[] messagebox = new JTextArea[2];

		for (int i = 0; i < fields.length; i++) {
			label = new JLabel(fields[i]);
			label.setFont(font);
			done_due[i] = new JComboBox<>(paymentbox);
			done_due[i].setFont(font);
			notebutton[i] = new JButton("Any MSG");
			notebutton[i].setFont(font);
			messagebox[i] = new JTextArea();
			msg[i] = "";

			int k = i;

			notebutton[i].addActionListener(e -> {
				messagebox[k].setText(msg[k]);
				messagebox[k].setPreferredSize(new Dimension(300, 100));
				int option = JOptionPane.showConfirmDialog(null, messagebox[k], "Message",
						JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					msg[k] = messagebox[k].getText();
				}
			});

			panel.add(label);
			panel.add(done_due[i]);
			panel.add(notebutton[i]);
		}

		return panel;
	}

	// field for payments

	public static JPanel addPaymentField(String[] fields, JTextField[][] paymenttextfield) {
		JPanel panel = new JPanel(new GridLayout(fields.length + 1, 3, 5, 5));

		JLabel blank = new JLabel("");
		JLabel total = new JLabel("Total");
		total.setFont(font);
		JLabel paid = new JLabel("Paid");
		paid.setFont(font);
		JLabel due = new JLabel("Due");
		due.setFont(font);

		JLabel label;

		panel.add(blank);
		panel.add(total);
		panel.add(paid);
		panel.add(due);

		for (int i = 0; i < fields.length; i++) {
			label = new JLabel(fields[i]);
			label.setFont(font);
			panel.add(label);

			for (int k = 0; k < 2; k++) {
				paymenttextfield[i][k] = new JTextField("0", 10);
				paymenttextfield[i][k].setFont(font);
			}
			paymenttextfield[i][2] = new JTextField("0", 10);
			paymenttextfield[i][2].setFont(font);
			paymenttextfield[i][2].setEditable(false);
			calculateDue(paymenttextfield[i][0], paymenttextfield[i][1], paymenttextfield[i][2], paymenttextfield);
			panel.add(paymenttextfield[i][0]);
			panel.add(paymenttextfield[i][1]);
			panel.add(paymenttextfield[i][2]);
		}
		paymenttextfield[4][0].setEditable(false);
		paymenttextfield[4][1].setEditable(false);

		panel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), "Payment Field"));

		return panel;
	}

	public static void setAmountField(JTextField field) {
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyChar() == '\b' || e.getKeyChar() == '.'
						|| e.getKeyChar() == '\n')
					field.setEditable(true);

				else
					field.setEditable(false);
			}
		});
	}

	// Auto Calculation for payment field
	public static void calculateDue(JTextField total, JTextField paid, JTextField due,
			JTextField[][] paymenttextfield) {
		setAmountField(total);
		setAmountField(paid);

		// Auto calculate due amount

		total.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {

					String totalamount = total.getText();
					String paidamount = paid.getText();
					String dueamount = "0";
					if (totalamount.isBlank())
						totalamount = "0";

					if (paidamount.isBlank())
						paidamount = "0";
					try {
						Double d = Double.parseDouble(totalamount) - Double.parseDouble(paidamount);
						dueamount = String.format("%.2f", d);
					} catch (NumberFormatException numberformat) {
						JOptionPane.showMessageDialog(null, "Something is wrong in amounts");
					}
					due.setText(dueamount);
					calculateTotal(paymenttextfield);
				}

			}

		});

		paid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					String totalamount = total.getText();
					String paidamount = paid.getText();
					String dueamount = "0";
					if (totalamount.isBlank())
						totalamount = "0";

					if (paidamount.isBlank())
						paidamount = "0";
					try {
						Double d = Double.parseDouble(totalamount) - Double.parseDouble(paidamount);
						dueamount = String.format("%.2f", d);
					} catch (NumberFormatException numberformat) {
						JOptionPane.showMessageDialog(null, "Something is wrong in amounts");
					}
					due.setText(dueamount);
					calculateTotal(paymenttextfield);
				}

			}

		});

	}

	// Calculate Total fields for all tatal
	public static void calculateTotal(JTextField[][] paymentfields) {
		Double total = Double.valueOf(0);
		Double paid = Double.valueOf(0);
		Double due = Double.valueOf(0);
		try {
			for (int i = 0; i < paymentfields.length - 1; i++) {
				for (int k = 0; k < 3; k++) {
					if (paymentfields[i][k].getText().isBlank())
						paymentfields[i][k].setText("0");
				}
				total += Double.parseDouble(paymentfields[i][0].getText());
				paid += Double.parseDouble(paymentfields[i][1].getText());
				due += Double.parseDouble(paymentfields[i][2].getText());
			}
			paymentfields[4][0].setText(String.format("%.2f", total));
			paymentfields[4][1].setText(String.format("%.2f", paid));
			paymentfields[4][2].setText(String.format("%.2f", due));
		} catch (NumberFormatException wrongnumber) {
			//
		}
	}

}
