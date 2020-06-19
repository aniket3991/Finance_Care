package care.finance;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IncomeTax implements ActionListener {
	JFrame frame;

	String[] itrandaudit = { "ITR", "Audit" };
	String[] searchitem = { "Select", "NAME", "PANCARD", "DOB", "AADHAR" };
	String[] buttons = { "Client Entry", "GST", "PAN", "Other Services", "View Details" };
	static String[] paymentfields = { "ITR", "Audit", "Tax", "Others", "All Total" };
	static String[] msg = new String[2];
	static String selectedyear;

	JTextField searchfield;
	JTextField pancard;
	static JTextField newfyentryfield;
	static JTextField[][] paymenttextfields = new JTextField[paymentfields.length][3];

	static JButton savebutton;
	JButton searchbutton;
	JButton updatebutton;
	JButton deletebutton;

	JLabel pancardlabel;
	JLabel fy;
	JLabel newentry;

	@SuppressWarnings("rawtypes")
	static JComboBox[] done_due = new JComboBox[2];
	public static JComboBox<String> searchby;
	static JComboBox<String> financialyear = new JComboBox<String>();
	DefaultComboBoxModel<String> yearbox = new DefaultComboBoxModel<String>();

	Font font = new Font("The Times Romans", Font.BOLD, 16);

	IncomeTax() {
		frame = new JFrame("Income Tax");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(1110, 655));

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(0xffffff));

		// Different Search Options

		searchby = new JComboBox<>(searchitem);
		searchby.setFont(font);
		searchby.setBounds(10, 20, 130, 30);

		searchfield = new JTextField(20);
		searchfield.setFont(font);
		searchfield.setBounds(150, 20, 300, 30);

		searchbutton = new JButton("Search");
		searchbutton.setBounds(470, 20, 130, 30);
		searchbutton.addActionListener(this);

		pancardlabel = new JLabel("PANCARD");
		pancardlabel.setFont(font);
		pancardlabel.setBounds(750, 20, 130, 30);

		pancard = new JTextField(15);
		pancard.setBounds(850, 20, 200, 30);
		pancard.setFont(font);
		pancard.setEditable(false);

		newentry = new JLabel("New Entry");
		newentry.setFont(font);
		newentry.setBounds(1060, 20, 100, 30);

		newfyentryfield = new JTextField(10);
		AddFields.setValidDate(newfyentryfield);
		newfyentryfield.setFont(font);
		newfyentryfield.setBounds(1160, 20, 170, 30);

		fy = new JLabel("F.Y");
		fy.setFont(font);
		fy.setBounds(1110, 70, 50, 30);

		financialyear.setBounds(1160, 70, 170, 30);
		financialyear.setFont(font);

		// Update all fields according to selected year
		financialyear.addItemListener(e -> {
			if (financialyear.getSelectedItem().equals("Select")) {
				savebutton.setEnabled(true);
				updatebutton.setEnabled(false);
				deletebutton.setEnabled(false);
				setAsRestart();
			} else {
				savebutton.setEnabled(false);
				updatebutton.setEnabled(true);
				deletebutton.setEnabled(true);
				selectedyear = financialyear.getSelectedItem().toString();
				String pan = pancard.getText();
				SQLQuery.searchForSelectedYear(pan, selectedyear, done_due, msg, paymenttextfields);
				fillSumFields();

			}
		});

		@SuppressWarnings("unchecked")
		JPanel itrauditpanel = AddFields.addIncomeTaxField(itrandaudit, done_due, msg, frame);
		itrauditpanel.setBounds(20, 150, 700, 80);
		itrauditpanel.setBackground(new Color(0xffffff));

		JPanel paymentpanel = AddFields.addPaymentField(paymentfields, paymenttextfields);
		paymentpanel.setBounds(20, 280, 700, 270);
		paymentpanel.setBackground(new Color(0xffffff));

		JPanel addServices = Services.addServices(buttons, frame);
		addServices.setBounds(800, 150, 300, 400);
		addServices.setBackground(new Color(0xffffff));

		updatebutton = new JButton("Update");
		updatebutton.setFont(font);
		updatebutton.setEnabled(false);
		updatebutton.setBounds(20, 570, 100, 40);
		updatebutton.addActionListener(this);

		savebutton = new JButton("Save");
		savebutton.setFont(font);
		savebutton.setBounds(610, 570, 100, 40);
		savebutton.addActionListener(this);
		
		deletebutton = new JButton("Delete");
		deletebutton.setFont(font);
		deletebutton.setEnabled(false);
		deletebutton.setBounds(1200, 570, 100, 40);
		deletebutton.addActionListener(this);

		panel.add(searchby);
		panel.add(searchfield);
		panel.add(searchbutton);
		panel.add(pancardlabel);
		panel.add(pancard);
		panel.add(fy);
		panel.add(newfyentryfield);
		panel.add(newentry);
		panel.add(financialyear);
		panel.add(itrauditpanel);
		panel.add(paymentpanel);
		panel.add(addServices);
		panel.add(updatebutton);
		panel.add(savebutton);
		panel.add(deletebutton);

		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String searchtype = "PANCARD";
		String searchfor = searchfield.getText();
		if (e.getSource().equals(searchbutton)) {
			setAsRestart();
			updatebutton.setEnabled(false);
			searchtype = searchby.getSelectedItem().toString();
			yearbox = SQLQuery.searchClient(searchtype, searchfor, pancard);
			financialyear.setModel(yearbox);

		}

		if (e.getSource().equals(savebutton)) {
			SQLQuery.insertIntoIncomeTable(pancard, newfyentryfield, done_due, msg, paymenttextfields);
		}

		if (e.getSource().equals(updatebutton)) {
			String pan = pancard.getText();
			SQLQuery.updateQueryForIncome(pan, selectedyear, done_due, msg, paymenttextfields);
			JOptionPane.showMessageDialog(null, "Successfully Update!");
		}

		if (e.getSource().equals(deletebutton)) {
			String panyear = pancard.getText() + selectedyear;

			int option = JOptionPane.showConfirmDialog(null, "Are you sure ", "Warning", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				{
					SQLQuery.deleteFinancialYear(panyear);
					setAsRestart();
				}
				JOptionPane.showMessageDialog(null, "Deleted!!!");
			}
		}
	}

	// Set All Fields for new entry
	public static void setAsRestart() {
		done_due[0].setSelectedItem("Select");
		done_due[1].setSelectedItem("Select");
		msg[0] = "";
		msg[1] = "";
		newfyentryfield.setText("");
		for (int i = 0; i < paymentfields.length; i++) {
			for (int k = 0; k < 3; k++)
				paymenttextfields[i][k].setText("0");
		}
		savebutton.setEnabled(true);
	}

	// Auto fill calculate fields after year selected
	public static void fillSumFields() {
		for (int i = 0; i < 4; i++) {
			String totalamount = paymenttextfields[i][0].getText();
			String paidamount = paymenttextfields[i][1].getText();
			String dueamount = "0";
			Double d = Double.parseDouble(totalamount) - Double.parseDouble(paidamount);
			dueamount = String.format("%.2f", d);

			paymenttextfields[i][2].setText(dueamount);
		}
		AddFields.calculateTotal(paymenttextfields);
	}

	public static void main(String[] args) {
		new IncomeTax();
	}

}