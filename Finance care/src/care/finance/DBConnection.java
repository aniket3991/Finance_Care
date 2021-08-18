package care.finance;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.GridLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLSyntaxErrorException;

public class DBConnection {

	static String an = "aniket";
	static String username = "";
	static String password = "";
	static JTextField usernametext;
	static JPasswordField passwordtext;
	static String DBName;
	static String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/";

	public static void createDatabase() throws Exception {
		if (username.isBlank() && password.isBlank())
			setup();
		Class.forName(jdbcDriver);
		Connection connection = DriverManager.getConnection(url, username, password);
		PreparedStatement sql = connection.prepareStatement("CREATE DATABASE FINANCECARE");
		sql.executeUpdate();
		connection.close();
	}

	@SuppressWarnings("deprecation")
	public static void setup() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
		panel.setPreferredSize(new Dimension(200, 100));
		JLabel usernamelabel = new JLabel("Username");
		JLabel passwordlabel = new JLabel("Password");
		usernametext = new JTextField();
		passwordtext = new JPasswordField();
		panel.add(usernamelabel);
		panel.add(usernametext);
		panel.add(passwordlabel);
		panel.add(passwordtext);
		int option = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.CANCEL_OPTION)
			System.exit(0);
		username = usernametext.getText();
		password = passwordtext.getText();
	}

	public static Connection setupConnection() {
		Connection connection = null;
		try {
			if (username.isBlank() && password.isBlank())
				setup();
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url + "FINANCECARE", username, password);
		} catch (SQLSyntaxErrorException e) {
			try {
				JOptionPane.showMessageDialog(null, "Database not found\nDatabase Created ;)\nPress OK and try again");
				createDatabase();
				SQLQuery.createTableClientEntry();
				SQLQuery.createTableIncomeTax();
				connection = DriverManager.getConnection(url + "FINANCECARE", username, password);
			} catch (Exception database) {
				database.printStackTrace();
			}
		} catch (Exception notfound) {

			// notfound.printStackTrace();
			wrongPassword();
		}
		return connection;
	}

	// If password of username wrong
	public static void wrongPassword() {
		JOptionPane.showMessageDialog(null, "Please remember correct Username and Password");
		username = "";
		password = "";
	}
}
