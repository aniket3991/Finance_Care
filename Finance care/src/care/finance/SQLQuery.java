package care.finance;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

public class SQLQuery {

//*********************************************************************************************
	// Methods for CLIENTDETAILS Table
//*********************************************************************************************

	// Table for Client entry
	public static void createTableClientEntry() {
		try {
			Connection connection = DBConnection.setupConnection();
			Statement statement = connection.createStatement();
			String cliententry = "CREATE TABLE CLIENTDETAILS (NAME VARCHAR(50) NOT NULL, "
					+ "CONTACT_NO VARCHAR(10), ADDRESS VARCHAR(300), PANCARD VARCHAR(10) PRIMARY KEY NOT NULL, DOB VARCHAR(10) NOT NULL, "
					+ "AADHAR VARCHAR(20), GSTIN VARCHAR(20), GST_ID VARCHAR(30), "
					+ "GST_PASS VARCHAR(30), EMAIL VARCHAR(50), PASSWORD VARCHAR(30))";
			statement.executeUpdate(cliententry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Table for Income Tax field
	public static void createTableIncomeTax() {
		try {
			Connection connection = DBConnection.setupConnection();
			Statement statement = connection.createStatement();
			String cliententry = "CREATE TABLE INCOME (PANCARD VARCHAR(10) NOT NULL, "
					+ "FINANCIAL_YEAR VARCHAR(9) NOT NULL, ITR_PROCESS VARCHAR(7), AUDIT_PROCESS VARCHAR(7),"
					+ "ITR_MSG VARCHAR(200) , AUDIT_MSG VARCHAR(200) , ITR_TOTAL DECIMAL(10,2) ,ITR_PAID DECIMAL(10,2), "
					+ "AUDIT_TOTAL DECIMAL(10,2) , AUDIT_PAID DECIMAL(10,2), TAX_TOTAL DECIMAL(10,2) ,"
					+ "TAX_PAID DECIMAL(10,2),OTHER_TOTAL DECIMAL(10,2) ,OTHER_PAID DECIMAL(10,2), PAN_YEAR VARCHAR(25) PRIMARY KEY)";
			statement.executeUpdate(cliententry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Query to insert values into CLIENTDETAILS table
	// @SuppressWarnings("deprecation")
	public static void insertIntoClientDetailTable(JTextField[] textfield, JPasswordField[] passwordfield) {
		String[] textstring = new String[textfield.length];
		String[] passwordstring = new String[passwordfield.length];
		for (int i = 0; i < textfield.length; i++) {
			textstring[i] = textfield[i].getText();
			if (textstring[i].isBlank())
				textstring[i] = null;
		}
		for (int i = 0; i < passwordfield.length; i++) {
			passwordstring[i] = String.copyValueOf(passwordfield[i].getPassword());
		}

		try {
			Connection connection = DBConnection.setupConnection();
			String cliententry = "INSERT INTO CLIENTDETAILS VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(cliententry);
			ps.setString(1, textstring[0]);
			ps.setString(2, textstring[1]);
			ps.setString(3, textstring[2]);
			ps.setString(4, textstring[3]);
			ps.setString(5, textstring[4]);
			ps.setString(6, textstring[5]);
			ps.setString(7, textstring[6]);
			ps.setString(8, textstring[7]);
			ps.setString(9, passwordstring[0]);
			ps.setString(10, textstring[8]);
			ps.setString(11, passwordstring[1]);
			ps.executeUpdate();
			connection.close();
			JOptionPane.showMessageDialog(null, "Saved Successfully");
			Main.setAsRestart();

		} catch (SQLIntegrityConstraintViolationException e) {
			if (textstring[0] == null || textstring[3] == null || textstring[4] == null || textstring[5] == null) {
				JOptionPane.showMessageDialog(null, "Name,PAN,DOB,Aadhar must be filled");
			} else {
				JOptionPane.showMessageDialog(null, "This PAN Card already exists");
			}
		} catch (MysqlDataTruncation ex) {
			JOptionPane.showMessageDialog(null, "Check Data Carefully");
		} catch (Exception e) {
			// DBConnection.wrongPassword();
			e.printStackTrace();
		}
	}

	public static void searchForClient(JComboBox<String> searchby, JTextField searchfield, JTextField[] textfield,
			JPasswordField[] passwordfield) {
		Connection connection = DBConnection.setupConnection();
		String searchquery = "SELECT * FROM CLIENTDETAILS WHERE " + searchby.getSelectedItem().toString() + "=\'"
				+ searchfield.getText() + "\'";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(searchquery);
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "Not Found!");
				searchfield.setText("");
				searchby.setSelectedItem("Select");
			} else {
				do {
					int k = 0;
					int j = 0;
					for (int i = 0; i < textfield.length + passwordfield.length; i++) {
						if (i == 8 || i == 10) {
							passwordfield[k].setText(rs.getString(i + 1));
							k++;
						} else {
							textfield[j].setText(rs.getString(i + 1));
							j++;
						}
					}
				} while (rs.next());
			}
		} catch (SQLSyntaxErrorException searchtype) {
			JOptionPane.showMessageDialog(null, "Please select search type");
			Main.savebutton.setEnabled(true);
			Main.updatebutton.setEnabled(false);
			Main.deletebutton.setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateQueryForClientEntryTable(String oldpancard, JTextField textfield[],
			JPasswordField[] passwordfield) {
		Connection connection = DBConnection.setupConnection();
		String cliententryupdate = "UPDATE CLIENTDETAILS SET NAME = ?, CONTACT_NO = ?,"
				+ "ADDRESS = ?, PANCARD = ?, DOB = ?, AADHAR = ?, GSTIN = ?, GST_ID = ?,"
				+ "GST_PASS = ?, EMAIL = ?, PASSWORD = ? WHERE PANCARD = ?";
		String incomeupdate = "UPDATE INCOME SET PANCARD = ?,PAN_YEAR = CONCAT(PANCARD,FINANCIAL_YEAR) WHERE PANCARD = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(cliententryupdate);

			int k = 0;
			int j = 0;
			for (int i = 0; i < textfield.length + passwordfield.length; i++) {
				if (i == 8 || i == 10) {
					ps.setString(i + 1, String.copyValueOf(passwordfield[k].getPassword()));
					k++;
				} else {
					if (i == 0 || i == 3 || i == 4 || i == 5) {
						if (textfield[i].getText().isBlank())
							throw new SQLIntegrityConstraintViolationException();
					}
					ps.setString(i + 1, textfield[j].getText());
					j++;
				}
			}
			ps.setString(12, oldpancard);
			ps.executeUpdate();

			String newpancard = textfield[3].getText();
			if (!newpancard.equals(oldpancard)) {
				ps = connection.prepareStatement(incomeupdate);
				ps.setString(1, textfield[3].getText());
				ps.setString(2, oldpancard);
				ps.executeUpdate();
			}

			connection.close();
			JOptionPane.showMessageDialog(null, "Successfully Updated!");
		} catch (MysqlDataTruncation ex) {
			JOptionPane.showMessageDialog(null, "Check Data Carefully");
		} catch (SQLIntegrityConstraintViolationException ex) {
			JOptionPane.showMessageDialog(null, "Name,PAN,DOB,Aadhar must be filled");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteClient(String pancard) {
		Connection connection = DBConnection.setupConnection();
		String deletefromclientdetails = "DELETE FROM CLIENTDETAILS WHERE PANCARD = \'" + pancard + "\'";
		String deletefromincometax = "DELETE FROM INCOME WHERE PANCARD = \'" + pancard + "\'";
		try {
			PreparedStatement ps = connection.prepareStatement(deletefromclientdetails);
			ps.executeUpdate();
			ps = connection.prepareStatement(deletefromincometax);
			ps.executeUpdate();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//*********************************************************************************************

//*********************************************************************************************
	// Methods for INCOME Table
//*********************************************************************************************

	// Method to insert values into INCOME table
	@SuppressWarnings("rawtypes")
	public static void insertIntoIncomeTable(JTextField pancard, JTextField fy, JComboBox[] done_due, String[] msg,
			JTextField[][] paymenttextfields) {
		String panyear = pancard.getText() + fy.getText();
		String pancardtext = pancard.getText();
		if (pancardtext.isBlank())
			pancardtext = null;
		String fytext = fy.getText();
		if (fytext.isBlank())
			fytext = null;
		String itrwork = done_due[0].getSelectedItem().toString();
		String auditwork = done_due[1].getSelectedItem().toString();
		String itrmsg = msg[0];
		String auditmsg = msg[1];
		Double[][] paymentfields = new Double[4][2];
		try {
			for (int i = 0; i < 4; i++) {
				paymentfields[i][0] = Double.parseDouble(paymenttextfields[i][0].getText());
				paymentfields[i][1] = Double.parseDouble(paymenttextfields[i][1].getText());
			}
		} catch (NumberFormatException wrongamount) {
			JOptionPane.showMessageDialog(null, "Something is wrong in amounts");
		
		}

		try {
			Connection connection = DBConnection.setupConnection();
			String incomequery = "INSERT INTO INCOME VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(incomequery);
			ps.setString(1, pancardtext);
			ps.setString(2, fytext);
			ps.setString(3, itrwork);
			ps.setString(4, auditwork);
			ps.setString(5, itrmsg);
			ps.setString(6, auditmsg);
			ps.setDouble(7, paymentfields[0][0]);
			ps.setDouble(8, paymentfields[0][1]);
			ps.setDouble(9, paymentfields[1][0]);
			ps.setDouble(10, paymentfields[1][1]);
			ps.setDouble(11, paymentfields[2][0]);
			ps.setDouble(12, paymentfields[2][1]);
			ps.setDouble(13, paymentfields[3][0]);
			ps.setDouble(14, paymentfields[3][1]);
			ps.setString(15, panyear);
			ps.executeUpdate();
			connection.close();
			JOptionPane.showMessageDialog(null, "Saved Successfully");
			IncomeTax.setAsRestart();
		} catch (SQLIntegrityConstraintViolationException sameyear) {
			if (fytext == null)
				JOptionPane.showMessageDialog(null, "New Entry for Financial Year must be filled");
			else if (pancardtext == null)
				JOptionPane.showMessageDialog(null, "Must select a client");
			else
				JOptionPane.showMessageDialog(null, "This year of entry is available for this client");
		} catch (NumberFormatException wrongamount) {
			JOptionPane.showMessageDialog(null, "Something is wrong in amounts");
		} catch (MysqlDataTruncation ex) {
			JOptionPane.showMessageDialog(null, "Check Data Carefully");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Search query for Income tax field
	public static DefaultComboBoxModel<String> searchClient(String searchby, String search, JTextField pancard) {
		String pan;
		DefaultComboBoxModel<String> financialyear = new DefaultComboBoxModel<String>();
		try {
			Connection connection = DBConnection.setupConnection();
			String searchquery = "SELECT PANCARD FROM CLIENTDETAILS WHERE " + searchby + "=\'" + search + "\'";
			Statement searchstatement = connection.createStatement();
			ResultSet result = searchstatement.executeQuery(searchquery);
			if (result.next() == false) {
				JOptionPane.showMessageDialog(null, "Not Found!");
				pancard.setText("");
			} else {
				do {
					pan = result.getString(1);
					pancard.setText(pan);
				} while (result.next());
				financialyear = searchingForYears(pan);
			}
			connection.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return financialyear;
	}

	// Searching of year field in Income tax used in searchClient method
	public static DefaultComboBoxModel<String> searchingForYears(String pancard) {
		DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>();
		cm.addElement("Select");
		try {
			Connection connection = DBConnection.setupConnection();
			String searchquery = "SELECT FINANCIAL_YEAR FROM INCOME WHERE PANCARD =\'" + pancard + "\'";
			Statement searchstatement = connection.createStatement();
			ResultSet result = searchstatement.executeQuery(searchquery);
			if (result.next() == false) {
				JOptionPane.showMessageDialog(null, "No previous Data Found!");
			} else {
				do {
					String rs = result.getString(1);
					cm.addElement(rs);
				} while (result.next());
			}
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cm;
	}

	@SuppressWarnings("rawtypes")
	public static void searchForSelectedYear(String pancard, String selectedyear, JComboBox[] done_due, String[] msg,
			JTextField[][] paymentfields) {
		try {
			Connection connection = DBConnection.setupConnection();
			String searchquery = "SELECT ITR_PROCESS, AUDIT_PROCESS, ITR_MSG, AUDIT_MSG,"
					+ " ITR_TOTAL, ITR_PAID, AUDIT_TOTAL, AUDIT_PAID, TAX_TOTAL, TAX_PAID,"
					+ " OTHER_TOTAL, OTHER_PAID FROM INCOME WHERE PANCARD = \'" + pancard + "\' AND FINANCIAL_YEAR = \'"
					+ selectedyear + "\'";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(searchquery);
			while (rs.next()) {
				done_due[0].setSelectedItem(rs.getString(1));
				done_due[1].setSelectedItem(rs.getString(2));
				String itrmsg = rs.getString(3);
				msg[0] = itrmsg;
				String auditmsg = rs.getString(4);
				msg[1] = auditmsg;
				int k = 5;
				for (int i = 0; i < 4; i++) {
					paymentfields[i][0].setText(Double.toString(rs.getDouble(k++)));
					paymentfields[i][1].setText(Double.toString(rs.getDouble(k++)));
				}
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void updateQueryForIncome(String pancard, String selectedyear, JComboBox[] done_due, String[] msg,
			JTextField[][] paymentfields) {
		try {
			Connection connection = DBConnection.setupConnection();
			String updatequery = "UPDATE INCOME SET ITR_PROCESS = ?, AUDIT_PROCESS = ?, ITR_MSG = ?, "
					+ "AUDIT_MSG = ?, ITR_TOTAL = ?, ITR_PAID = ?, AUDIT_TOTAL = ?, AUDIT_PAID = ?,"
					+ "TAX_TOTAL = ?, TAX_PAID = ?, OTHER_TOTAL = ?, OTHER_PAID = ? WHERE PANCARD = ?"
					+ " AND FINANCIAL_YEAR = ?";
			PreparedStatement ps = connection.prepareStatement(updatequery);
			ps.setString(1, done_due[0].getSelectedItem().toString());
			ps.setString(2, done_due[1].getSelectedItem().toString());
			ps.setString(3, msg[0]);
			ps.setString(4, msg[1]);
			ps.setDouble(5, Double.parseDouble(paymentfields[0][0].getText()));
			ps.setDouble(6, Double.parseDouble(paymentfields[0][1].getText()));
			ps.setDouble(7, Double.parseDouble(paymentfields[1][0].getText()));
			ps.setDouble(8, Double.parseDouble(paymentfields[1][1].getText()));
			ps.setDouble(9, Double.parseDouble(paymentfields[2][0].getText()));
			ps.setDouble(10, Double.parseDouble(paymentfields[2][1].getText()));
			ps.setDouble(11, Double.parseDouble(paymentfields[3][0].getText()));
			ps.setDouble(12, Double.parseDouble(paymentfields[3][1].getText()));
			ps.setString(13, pancard);
			ps.setString(14, selectedyear);
			ps.executeUpdate();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteFinancialYear(String panyear)
	{
		Connection connection = DBConnection.setupConnection();
		String deletefromincometax = "DELETE FROM INCOME WHERE PAN_YEAR = \'" + panyear + "\'";
		try {
			PreparedStatement ps = connection.prepareStatement(deletefromincometax);
			ps.executeUpdate();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//*********************************************************************************************

	//public static void main(String[] agrs) {
		// JTextField j = new JTextField();
		// JComboBox<String> cm = searchingForYears("1122");
	//}
}
