import java.awt.Color;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.border.*;
import java.awt.event.*;
import java.sql.*;

public class Registration_Form {

	private JFrame frame;
	private JTextField textField_p_1, textField_p_2, textField_p_3, textField_p_4, textField_p_6;
	private final String URL = "jdbc:mysql://localhost:3306/gym_management";
	private final String USER = "root";
	private final String PASSWORD = "";

	public Registration_Form() {
		initialize();
	}

	public void showWindow() {
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 100, 328, 366);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Registration", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(6, 10, 316, 323);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_p_1 = new JLabel("Name");
		lblNewLabel_p_1.setBounds(6, 21, 120, 16);
		panel.add(lblNewLabel_p_1);
		textField_p_1 = new JTextField();
		textField_p_1.setBounds(138, 21, 170, 23);
		panel.add(textField_p_1);

		JLabel lblNewLabel_p_2 = new JLabel("Phone");
		lblNewLabel_p_2.setBounds(6, 49, 120, 16);
		panel.add(lblNewLabel_p_2);
		textField_p_2 = new JTextField();
		textField_p_2.setBounds(138, 49, 170, 23);
		panel.add(textField_p_2);

		JLabel lblNewLabel_p_3 = new JLabel("Email");
		lblNewLabel_p_3.setBounds(6, 77, 120, 16);
		panel.add(lblNewLabel_p_3);
		textField_p_3 = new JTextField();
		textField_p_3.setBounds(138, 77, 170, 23);
		panel.add(textField_p_3);

		JLabel lblNewLabel_p_4 = new JLabel("Date of Birth");
		lblNewLabel_p_4.setBounds(6, 103, 120, 16);
		panel.add(lblNewLabel_p_4);
		JDateChooser dateChooser_p_1 = new JDateChooser();
		dateChooser_p_1.setBounds(138, 103, 170, 23);
		dateChooser_p_1.setDateFormatString("yyyy-MM-dd");
		panel.add(dateChooser_p_1);

		JLabel lblNewLabel_p_5 = new JLabel("Gender");
		lblNewLabel_p_5.setBounds(6, 131, 120, 16);
		panel.add(lblNewLabel_p_5);
		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(138, 127, 65, 23);
		panel.add(rdbtnMale);
		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(205, 127, 85, 23);
		panel.add(rdbtnFemale);
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(rdbtnMale);
		genderGroup.add(rdbtnFemale);

		JLabel lblNewLabel_p_6 = new JLabel("Address");
		lblNewLabel_p_6.setBounds(6, 159, 120, 16);
		panel.add(lblNewLabel_p_6);
		textField_p_4 = new JTextField();
		textField_p_4.setBounds(138, 159, 170, 23);
		panel.add(textField_p_4);

		JLabel lblNewLabel_p_7 = new JLabel("Membership-Type");
		lblNewLabel_p_7.setBounds(6, 187, 120, 16);
		panel.add(lblNewLabel_p_7);
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setBounds(138, 183, 127, 27);
		comboBox.addItem("Regular");
		comboBox.addItem("Premium");
		comboBox.addItem("VIP");
		panel.add(comboBox);

		JLabel lblNewLabel_p_8 = new JLabel("Create-Date");
		lblNewLabel_p_8.setBounds(6, 215, 120, 16);
		panel.add(lblNewLabel_p_8);
		JDateChooser dateChooser_p_2 = new JDateChooser();
		dateChooser_p_2.setBounds(138, 215, 170, 23);
		dateChooser_p_2.setDateFormatString("yyyy-MM-dd");
		panel.add(dateChooser_p_2);

		JLabel lblNewLabel_p_10 = new JLabel("Card ID");
		lblNewLabel_p_10.setBounds(6, 243, 120, 16);
		panel.add(lblNewLabel_p_10);
		textField_p_6 = new JTextField();
		textField_p_6.setBounds(138, 243, 170, 23);
		panel.add(textField_p_6);

		JButton btnNewButton_p_1 = new JButton("Add");
		btnNewButton_p_1.setBounds(191, 278, 117, 29);
		panel.add(btnNewButton_p_1);

		btnNewButton_p_1.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to add this data?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				String name = textField_p_1.getText();
				String phone = textField_p_2.getText();
				String email = textField_p_3.getText();
				String date_of_birth = ((JTextField) dateChooser_p_1.getDateEditor().getUiComponent()).getText();
				String gender = rdbtnMale.isSelected() ? "Male" : (rdbtnFemale.isSelected() ? "Female" : "Not Selected");
				String address = textField_p_4.getText();
				String membership_type = comboBox.getSelectedItem().toString();
				String create_date = ((JTextField) dateChooser_p_2.getDateEditor().getUiComponent()).getText();
				String card_id = textField_p_6.getText();

				addData(name, phone, email, date_of_birth, gender, address, membership_type, create_date, card_id);
			}
		});
	}

	private void addData(String name, String phone, String email, String date_of_birth, String gender, String address, String membership_type, String create_date, String card_id) {
		String query = "INSERT INTO members (name, phone, email, date_of_birth, gender, address, membership_type, create_date, card_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			 PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.setString(2, phone);
			stmt.setString(3, email);
			stmt.setString(4, date_of_birth);
			stmt.setString(5, gender);
			stmt.setString(6, address);
			stmt.setString(7, membership_type);
			stmt.setString(8, create_date);
			stmt.setString(9, card_id);
			int rowsInserted = stmt.executeUpdate();
			if (rowsInserted > 0) {
				JOptionPane.showMessageDialog(frame, "Data inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
