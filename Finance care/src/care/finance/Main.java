package care.finance;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Main implements ActionListener {
	String[] details = { "Name :", "Contact No. :", "Address :", "PAN Card No. :", "DOB(dd-mm-yyyy)", "Aadhar No. :",
			"GSTIN :", "GST ID :", "GST Password :", "E-mail :", "Password :" };
	String[] sidebuttons = { "Income Tax" };
	String[] searchitems = { "Select", "NAME", "PANCARD", "DOB", "AADHAR" };
	String oldpancard;
	static JTextField[] textfield = new JTextField[9];
	static JPasswordField[] passwordfield = new JPasswordField[2];
	JFrame frame;

	JTextField searchfield;

	JComboBox<String> searchby;

	JButton searchbutton;
	static JButton updatebutton;
	static JButton savebutton;
	static JButton deletebutton;

	JCheckBox showgstpassword;
	JCheckBox showemailpassword;

	Main()

	{
		frame = new JFrame("Finance Care");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(1050, 700));

		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(null);
		mainpanel.setBackground(new Color(0xffffff));

		JLabel welcome = new JLabel("Welcome to the Finance Care");
		welcome.setForeground(new Color(0x053700));
		Font font = new Font("The Roman Times", Font.ITALIC, 40);
		welcome.setFont(font);
		welcome.setBounds(380, 0, 750, 70);
		JPanel customerdetails = AddFields.addEntryFields(details, textfield, passwordfield);
		customerdetails.setBackground(new Color(0xffffff));
		customerdetails.setBounds(50, 100, 600, 500);

		// sidebuttons for side button in Frame and frame for closing current frame

		JPanel addservices = Services.addServices(sidebuttons, frame);
		addservices.setBackground(new Color(0xcffffff));
		addservices.setBounds(750, 150, 300, 400);

		showgstpassword = new JCheckBox();
		showgstpassword.setBounds(650, 475, 20, 20);
		showgstpassword.addActionListener(ae -> {
			if (showgstpassword.isSelected())
				passwordfield[0].setEchoChar((char) 0);
			else
				passwordfield[0].setEchoChar('*');
		});

		showemailpassword = new JCheckBox("");
		showemailpassword.setBounds(650, 560, 20, 20);
		showemailpassword.addActionListener(ae -> {
			if (showemailpassword.isSelected())
				passwordfield[1].setEchoChar((char) 0);
			else
				passwordfield[1].setEchoChar('*');
		});

		savebutton = new JButton("Save");
		savebutton.setBounds(543, 620, 100, 40);
		savebutton.setFont(Services.font);
		savebutton.addActionListener(this);

		searchby = new JComboBox<String>(searchitems);
		searchby.setBounds(900, 55, 130, 30);
		searchby.addItemListener(e -> {
			if (searchby.getSelectedItem().equals("Select")) {
				savebutton.setEnabled(true);
				updatebutton.setEnabled(false);
				deletebutton.setEnabled(false);
				setAsRestart();
			}
		});

		searchfield = new JTextField();
		searchfield.setFont(Services.font);
		searchfield.setBounds(1040, 55, 200, 30);

		searchbutton = new JButton("Search");
		searchbutton.setFont(Services.font);
		searchbutton.setBounds(1250, 55, 100, 30);
		searchbutton.addActionListener(this);

		updatebutton = new JButton("Update");
		updatebutton.setFont(Services.font);
		updatebutton.setBounds(60, 620, 100, 40);
		updatebutton.addActionListener(this);
		updatebutton.setEnabled(false);

		deletebutton = new JButton("Delete");
		deletebutton.setFont(Services.font);
		deletebutton.setBounds(1200, 620, 100, 40);
		deletebutton.addActionListener(this);
		deletebutton.setEnabled(false);

		mainpanel.add(welcome);
		mainpanel.add(customerdetails);
		mainpanel.add(showgstpassword);
		mainpanel.add(showemailpassword);
		mainpanel.add(addservices);
		mainpanel.add(searchby);
		mainpanel.add(searchfield);
		mainpanel.add(searchbutton);
		mainpanel.add(updatebutton);
		mainpanel.add(savebutton);
		mainpanel.add(deletebutton);

		frame.add(mainpanel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws Exception {
		new WelcomeScreen();
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(searchbutton)) {
			setAsRestart();
			savebutton.setEnabled(false);
			updatebutton.setEnabled(true);
			deletebutton.setEnabled(true);
			SQLQuery.searchForClient(searchby, searchfield, textfield, passwordfield);
			oldpancard = textfield[3].getText();
		}

		if (e.getSource().equals(savebutton)) {

			SQLQuery.insertIntoClientDetailTable(textfield, passwordfield);
		}

		if (e.getSource().equals(updatebutton)) {
			SQLQuery.updateQueryForClientEntryTable(oldpancard, textfield, passwordfield);
		}

		if (e.getSource().equals(deletebutton)) {
			String pancard = textfield[3].getText();
			int option = JOptionPane.showConfirmDialog(null, "Are you sure ", "Warning", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION)
				{
				SQLQuery.deleteClient(pancard);
				JOptionPane.showMessageDialog(null, "Deleted!!!");
				}
		}
	}

	public static void setAsRestart() {
		for (int i = 0; i < textfield.length; i++)
			textfield[i].setText("");
		for (int i = 0; i < passwordfield.length; i++)
			passwordfield[i].setText("");
	}
}